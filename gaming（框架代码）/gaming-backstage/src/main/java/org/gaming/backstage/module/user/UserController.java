/**
 * 
 */
package org.gaming.backstage.module.user;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.form.UserForm;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserFunctionService;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YY
 *
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserFunctionService userFunctionService;
	
	@ApiDocument("请求用户列表数据")
	@RequestMapping(value = "/user/data.auth")
	@ResponseBody
	public PageData<User> userJson(PageQuery query) {
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		PageData<User> pageData = userService.queryUser(currUser.getLevel(), query.getPage(), query.getLimit());
		
		PageData<User> result = new PageData<>();
		result.setCount(pageData.getCount());
		for(User user : pageData.getData()) {
			User vo = new User();
			vo.setId(user.getId());
			vo.setName(user.getName());
			vo.setPassword("******");
			vo.setLevel(user.getLevel());
			vo.setSuperUser(user.isSuperUser());
			vo.setUpUserId(user.getUpUserId());
			vo.setPhone(user.getPhone());
			vo.setMailAddress(user.getMailAddress());
			vo.setUsable(user.isUsable());
			vo.setCanReadSensitive(user.isCanReadSensitive());
			result.getData().add(vo);
		}
		return result;
	}
	
	@ApiDocument("保存用户数据，数据ID小于等于0时新增，大于0时更新")
	@RequestMapping(value = "/user/save.authw")
	@ResponseBody
	public User save(UserForm form) {
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		
		User user = null;
		if(form.getId() <= 0) {
			user = new User();
			user.setName(form.getName());
			user.setPassword(MD5Util.getMD5(form.getPassword()));
			user.setLevel(currUser.getLevel() + 1);
			user.setUpUserId(currUser.getId());
			user.setPhone(form.getPhone());
			user.setMailAddress(form.getMailAddress());
			if(currUser.isSuperUser()) {
				user.setSuperUser(form.isSuperUser());
			} else {
				user.setSuperUser(false);
			}
			user.setUsable(form.isUsable());
			user.setCanReadSensitive(form.isCanReadSensitive());
			userService.insert(user);
		} else {
			user = userService.getById(form.getId());
			Asserts.isTrue(user != null, WebCode.USER_NOT_FOUND);
			Asserts.isTrue(currUser.getLevel() < user.getLevel(), WebCode.USER_LEVEL_LACK);
			
			user.setName(form.getName());
			user.setPhone(form.getPhone());
			user.setMailAddress(form.getMailAddress());
			if(currUser.isSuperUser()) {
				user.setSuperUser(form.isSuperUser());
			} else {
				user.setSuperUser(false);
			}
			user.setUsable(form.isUsable());
			user.setCanReadSensitive(form.isCanReadSensitive());
			userService.update(user);
		}
		return user;
	}
	
	@ApiDocument("删除用户数据")
	@RequestMapping(value = "/user/delete.authw")
	@ResponseBody
	public void delete(UserForm form) {
		User targetUser = userService.getById(form.getId());
		Asserts.isTrue(targetUser != null, WebCode.USER_NOT_FOUND);
		
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		Asserts.isTrue(currUser.getLevel() < targetUser.getLevel(), WebCode.USER_LEVEL_LACK);
		
		userService.delete(targetUser);
		//删除用户关联的菜单功能
		userFunctionService.deleteAllInOwner(targetUser.getId());
	}
}
