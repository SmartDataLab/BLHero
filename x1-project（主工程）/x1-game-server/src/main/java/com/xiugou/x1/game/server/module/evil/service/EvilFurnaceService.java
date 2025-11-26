package com.xiugou.x1.game.server.module.evil.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.evil.model.EvilFurnace;
import com.xiugou.x1.game.server.module.evil.struct.RefineEvilData;

import pb.xiugou.x1.protobuf.refineevil.RefineEvil.PbRefineEvilDetail;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Service
public class EvilFurnaceService extends PlayerOneToOneService<EvilFurnace> {

	@Override
	protected EvilFurnace createWhenNull(long entityId) {
		EvilFurnace evilFurnace = new EvilFurnace();
		evilFurnace.setPid(entityId);
		return evilFurnace;
	}

	public PbRefineEvilDetail build(RefineEvilData refineEvilData, int position) {
		PbRefineEvilDetail.Builder builder = PbRefineEvilDetail.newBuilder();
		builder.setPosition(position);
		builder.setEvilSoulId(refineEvilData.getIdentity());
		builder.setStartTime(refineEvilData.getStartTime());
		builder.setEndTime(refineEvilData.getEndTime());
		return builder.build();
	}
}
