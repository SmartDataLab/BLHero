/**
 * 
 */
package com.xiugou.x1.backstage.module.gamecause.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gamecause.model.GameCause;

/**
 * @author YY
 *
 */
@Service
public class GameCauseService extends AbstractService<GameCause> {

	public PageData<GameCause> query(QuerySet querySet) {
		List<GameCause> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<GameCause> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void insertUpdate(List<GameCause> list) {
		this.repository().getBaseDao().insertUpdate(list);
	}
	
	public List<GameCause> getAll() {
		return this.repository().getAllInDb();
	}
}
