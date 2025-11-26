/**
 * 
 */
package org.gaming.backstage.module.menu.service;

import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.service.SystemOneToManyService;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class FunctionMenuService extends SystemOneToManyService<FunctionMenu> {

	public FunctionMenu getMenu(long menuId) {
		return this.getEntity(menuId);
	}
	
	public FunctionMenu getByAuthClazz(String authClazz) {
		for(FunctionMenu menu : this.getEntities()) {
			if(authClazz.equals(menu.getAuthClazz())) {
				return menu;
			}
		}
		return null;
	}
}
