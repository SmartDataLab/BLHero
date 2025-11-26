/**
 * 
 */
package org.gaming.backstage.module.menu.service;

import org.gaming.backstage.module.menu.model.SystemMenu;
import org.gaming.backstage.service.SystemOneToManyService;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class SystemMenuService extends SystemOneToManyService<SystemMenu> {

	public SystemMenu getMenu(long menuId) {
		return this.getEntity(menuId);
	}
}
