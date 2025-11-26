/**
 * 
 */
package org.gaming.backstage.module.menu.service;

import org.gaming.backstage.module.menu.model.ModuleMenu;
import org.gaming.backstage.service.SystemOneToManyService;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class ModuleMenuService extends SystemOneToManyService<ModuleMenu> {

	public ModuleMenu getMenu(long menuId) {
		return this.getEntity(menuId);
	}
}
