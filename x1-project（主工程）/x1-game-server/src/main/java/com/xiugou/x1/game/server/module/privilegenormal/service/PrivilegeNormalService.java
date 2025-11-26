/**
 * 
 */
package com.xiugou.x1.game.server.module.privilegenormal.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.privilegenormal.model.PrivilegeNormal;
import com.xiugou.x1.game.server.module.privilegenormal.struct.PrivilegeNormalData;

/**
 * @author YY
 *
 */
@Service
public class PrivilegeNormalService extends PlayerOneToOneResetableService<PrivilegeNormal> {

	@Override
	protected PrivilegeNormal createWhenNull(long entityId) {
		PrivilegeNormal entity = new PrivilegeNormal();
		entity.setPid(entityId);
		return entity;
	}

	@Override
	protected void doDailyReset(PrivilegeNormal entity) {
		for(PrivilegeNormalData data : entity.getDatas().values()) {
			data.setRewarded(false);
		}
	}
}
