/**
 * 
 */
package org.gaming.backstage.module.apidoc.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.apidoc.model.ApiDoc;
import org.gaming.backstage.service.AbstractService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.spring.Spring;
import org.gaming.tool.ListMapUtil;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author YY
 *
 */
@Service
public class ApiDocService extends AbstractService<ApiDoc> implements Lifecycle {
	
	private Gson gson;
	
	public ApiDocService() {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		this.gson = builder.create();
	}
	
	public List<ApiDoc> getAll() {
		return this.repository().getAllInDb();
	}
	
	@Override
	public void start() throws Exception {
		List<ApiDoc> list = this.repository().getAllInDb();
		Map<String, ApiDoc> docMap = ListMapUtil.listToMap(list, ApiDoc::getHrefUrl);
		
		Collection<Object> beans1 = Spring.getBeansWithAnnotation(Controller.class);
		Collection<Object> beans2 = Spring.getBeansWithAnnotation(RestController.class);

		Set<Object> beans = new HashSet<>();
		beans.addAll(beans1);
		beans.addAll(beans2);

		List<ApiDoc> insertList = new ArrayList<>();
		List<ApiDoc> updateList = new ArrayList<>();
		for (Object bean : beans) {
			for(Method method : bean.getClass().getDeclaredMethods()) {
				ApiDocument apiDocument = method.getAnnotation(ApiDocument.class);
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				if(apiDocument == null || requestMapping == null) {
					continue;
				}
				ApiDoc apiDoc = docMap.get(requestMapping.value()[0]);
				if(apiDoc == null) {
					apiDoc = new ApiDoc();
					apiDoc.setHrefUrl(requestMapping.value()[0]);
					apiDocChange(apiDoc, apiDocument, method);
					insertList.add(apiDoc);
				} else {
					if(apiDocChange(apiDoc, apiDocument, method)) {
						updateList.add(apiDoc);
					}
				}
			}
		}
		this.repository().insertAll(insertList);
		this.repository().updateAll(updateList);
	}
	
	private boolean apiDocChange(ApiDoc apiDoc, ApiDocument apiDocument, Method method) {
		boolean change = false;
		if(!apiDocument.value().equals(apiDoc.getComment())) {
			apiDoc.setComment(apiDocument.value());
			change = true;
		}
		StringBuilder builder = new StringBuilder();
		for(Parameter parameter : method.getParameters()) {
			if(parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class) {
				continue;
			}
			if (parameter.getType() == int.class || parameter.getType() == Integer.class
					|| parameter.getType() == long.class || parameter.getType() == Long.class
					|| parameter.getType() == String.class) {
				RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
				if(requestParam != null) {
					builder.append(requestParam.value() + ":" + parameter.getType().getSimpleName()).append(";\n");
				} else {
					builder.append(parameter.getName() + ":" + parameter.getType().getSimpleName()).append(";\n");
				}
			} else if(parameter.getType() == java.util.List.class) {
				ParameterizedType typeImpl = (ParameterizedType)parameter.getParameterizedType();
				RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
				if (requestParam != null) {
					if (typeImpl.getActualTypeArguments()[0] == Long.class) {
						builder.append(requestParam.value() + ":" + "long[]").append(";\n");
					} else if (typeImpl.getActualTypeArguments()[0] == Integer.class) {
						builder.append(requestParam.value() + ":" + "int[]").append(";\n");
					} else if (typeImpl.getActualTypeArguments()[0] == String.class) {
						builder.append(requestParam.value() + ":" + "String[]").append(";\n");
					} else {
						builder.append(
								requestParam.value() + ":" + typeImpl.getActualTypeArguments()[0].getTypeName() + "[]")
								.append(";\n");
					}
				} else {
					builder.append(
							parameter.getName() + ":" + typeImpl.getActualTypeArguments()[0].getTypeName() + "[]")
							.append(";\n");
				}
			} else {
				Class<?> clazz = parameter.getType();
				while(clazz != null) {
					for(Field field : clazz.getDeclaredFields()) {
						builder.append(field.getName() + ":" + field.getType().getSimpleName()).append(";\n");
					}
					clazz = clazz.getSuperclass();
				}
			}
		}
		if(!builder.toString().equals(apiDoc.getParamForm())) {
			apiDoc.setParamForm(builder.toString());
			change = true;
		}
		try {
			String returnForm = "";
			if(method.getReturnType() == void.class) {
				
			} else if(List.class.isAssignableFrom(method.getReturnType())) {
				List<Object> returnList = new ArrayList<>();
				Class<?> clazz = (Class<?>)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
				returnList.add(clazz.newInstance());
				returnForm = gson.toJson(returnList);
			} else {
				Object returnObj = method.getReturnType().newInstance();
				if(method.getGenericReturnType() instanceof ParameterizedType) {
					
					Class<?> clazz = (Class<?>)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
					
					for(Field field : method.getReturnType().getDeclaredFields()) {
						if(List.class.isAssignableFrom(field.getType())) {
							List<Object> list = new ArrayList<>();
							list.add(clazz.newInstance());
							field.setAccessible(true);
							field.set(returnObj, list);
						}
					}
				} else {
					for(Field field : method.getReturnType().getDeclaredFields()) {
						if(List.class.isAssignableFrom(field.getType())) {
							List<Object> list = new ArrayList<>();
							Class<?> clazz = (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
							list.add(clazz.newInstance());
							field.setAccessible(true);
							field.set(returnObj, list);
						}
					}
				}
				returnForm = gson.toJson(returnObj);
			}
			if(!returnForm.equals(apiDoc.getReturnForm())) {
				apiDoc.setReturnForm(returnForm);
				change = true;
			}
		} catch (Exception e) {
			logger.error(apiDocument.value() + " " + method.getName() + "异常", e);
		}
		return change;
	}
}
