/**
 * 
 */
package org.gaming.backstage.module.user.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.backstage.PageData;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.user.model.User;
import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class UserService {
	
	private ThreadLocal<RoleContext> userThread = new ThreadLocal<>();
	
	private ConcurrentMap<Long, String> userIdTokenMap = new ConcurrentHashMap<>();
	private ConcurrentMap<String, RoleContext> tokenToContextMap = new ConcurrentHashMap<>();
	
	private BaseRepository<User> repository() {
		return SlimDao.getRepository(User.class);
	}
	
	public void insert(User user) {
		repository().insert(user);
	}
	
	public void update(User user) {
		repository().update(user);
	}
	
	public void delete(User user) {
		repository().delete(user);
	}
	
	public User getByName(String username) {
		return repository().getBaseDao().queryOneWhere("WHERE name = ?", username);
	}
	
	public User getById(long userId) {
		return repository().getByMainKey(userId);
	}
	
	public PageData<User> queryUser(int level, int page, int pageSize) {
		//只查比自己低级的用户
		QuerySet querySet = new QuerySet();
		querySet.addCondition("level >= ?", level);
		querySet.limit(page, pageSize);
		querySet.formWhere();
		List<User> result = repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		long count = repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams());
		PageData<User> queryPage = new PageData<>(count, result);
		return queryPage;
	}
	
	
	public RoleContext getCurrUser() {
		return userThread.get();
	}
	
	public void setCurrUser(RoleContext userContext) {
		userThread.set(userContext);
	}
	
	public void cleanCurrUser() {
		userThread.remove();
	}
	
	public void addRoleContext(RoleContext roleContext) {
		String oldToken = userIdTokenMap.get(roleContext.getId());
		if(oldToken != null) {
			tokenToContextMap.remove(oldToken);
		}
		userIdTokenMap.put(roleContext.getId(), roleContext.getToken());
		tokenToContextMap.put(roleContext.getToken(), roleContext);
	}
	
	public RoleContext getContext(String token) {
		return tokenToContextMap.get(token);
	}
	
	public boolean isTokenValid(RoleContext roleContext, String token) {
		String currToken = userIdTokenMap.get(roleContext.getId());
		if(currToken == null || roleContext.getToken() == null || token == null) {
			return false;
		}
		if(!currToken.equals(roleContext.getToken())) {
			return false;
		}
		if(!currToken.equals(token)) {
			return false;
		}
		return true;
	}
}
