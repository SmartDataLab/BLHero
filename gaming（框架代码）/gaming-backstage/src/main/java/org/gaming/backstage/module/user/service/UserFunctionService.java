/**
 * 
 */
package org.gaming.backstage.module.user.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.user.model.UserFunction;
import org.gaming.backstage.service.OneToManyRedisService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class UserFunctionService extends OneToManyRedisService<UserFunction> {
	
	public boolean hasAuth(long userId, long functionId) {
		return this.getEntity(userId, functionId) != null;
	}
	
	public boolean hasAuthw(long userId, long functionId) {
		UserFunction userFunction = this.getEntity(userId, functionId);
		if(userFunction == null) {
			return false;
		}
		return userFunction.isWritee();
	}
	
	
	public PageData<UserFunction> queryUserFunction(QuerySet querySet) {
		List<UserFunction> userFunctions = repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		long count = repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams());
		PageData<UserFunction> pageData = new PageData<>(count, userFunctions);
		return pageData;
	}

	@Override
	protected String cacheContext() {
		return "user";
	}

	@Override
	protected QueryOptions queryForOwner(long ownerId) {
		QueryOptions options = new QueryOptions();
		options.put("userId", ownerId);
		return options;
	}
}
