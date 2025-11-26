/**
 * 
 */
package com.xiugou.x1.game.server.module.handbook.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.HandbookCatalogCache;
import com.xiugou.x1.design.module.autogen.HandbookCatalogAbstractCache.HandbookCatalogCfg;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.handbook.model.Handbook;

/**
 * @author YY
 *
 */
@Service
public class HandbookService extends OneToManyService<Handbook> {

	@Autowired
	private HandbookCatalogCache handbookCatalogCache;
	
	@Override
	public List<Handbook> getEntities(long ownerId) {
		List<Handbook> books = super.getEntities(ownerId);
		Set<Integer> bookTypes = handbookCatalogCache.getTypes();
		if(books.size() != bookTypes.size()) {
			Map<Integer, Handbook> bookMap = ListMapUtil.listToMap(books, Handbook::getIdentity);
			for(int bookType : bookTypes) {
				if(bookMap.containsKey(bookType)) {
					continue;
				}
				HandbookCatalogCfg cfg = handbookCatalogCache.getInTypeCollector(bookType).get(0);
				Handbook handbook = new Handbook();
				handbook.setPid(ownerId);
				handbook.setIdentity(cfg.getType());
				handbook.setName(cfg.getGroupName());
				this.insert(handbook);
				books.add(handbook);
			}
		}
		return books;
	}
	
	public Handbook getEntity(long playerId, int identity) {
        return repository().getByKeys(playerId, identity);
    }
}
