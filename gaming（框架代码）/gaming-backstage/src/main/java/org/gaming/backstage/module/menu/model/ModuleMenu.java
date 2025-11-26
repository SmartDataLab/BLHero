/**
 * 
 */
package org.gaming.backstage.module.menu.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 * 模块菜单，导航区的分类菜单
 */
@Repository
@Table(name = "menu2_module", comment = "模块菜单，导航区的分类菜单", dbAlias = "backstage")
public class ModuleMenu extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "system_id", comment = "所属的系统菜单ID")
	private long systemId;
	@Column(comment = "模块名字")
	private String title;
	@Column(comment = "排序位置")
	private long sort;
	@Column(comment = "图标")
	private String icon;
	@Column(name = "route_name", comment = "路由名字")
	private String routeName;
	@Column(name = "route_path", comment = "路由路径")
	private String routePath;
	@Column(comment = "组件")
	private String component;
	
	public boolean sameWith(ModuleMenu other) {
		if(this.systemId != other.systemId) {
			return false;
		}
		if(!this.title.equals(other.title)) {
			return false;
		}
		if(!this.routeName.equals(other.routeName)) {
			return false;
		}
		if(!this.routePath.equals(other.routePath)) {
			return false;
		}
		if(!this.component.equals(other.component)) {
			return false;
		}
		return true;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSort() {
		return sort;
	}
	public void setSort(long sort) {
		this.sort = sort;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public long getSystemId() {
		return systemId;
	}
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getRoutePath() {
		return routePath;
	}

	public void setRoutePath(String routePath) {
		this.routePath = routePath;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	@Override
	public Long redisOwnerKey() {
		return 0L;
	}

	@Override
	public Long redisHashKey() {
		return id;
	}
}
