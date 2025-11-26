/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.service.SystemOneToManyService;
import com.xiugou.x1.cross.server.module.server.model.CrossGroup;

/**
 * @author YY
 *
 */
@Service
public class CrossGroupService extends SystemOneToManyService<CrossGroup> {

	@Override
	public List<CrossGroup> getEntities() {
		return this.repository().getAllInCache();
	}

	public CrossGroup getEntity(int crossGroupId) {
		return this.repository().getByMainKey(crossGroupId);
	}
}
