/**
 * 
 */
package com.xiugou.x1.game.server.module.recruit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.recruit.constant.RefreshType;
import com.xiugou.x1.game.server.module.recruit.model.Recruit;
import com.xiugou.x1.game.server.module.recruit.service.RecruitService;
import com.xiugou.x1.game.server.module.recruit.struct.RecruitData;

/**
 * @author YY
 *
 */
@Controller
public class RecruitGmHandler {

	@Autowired
	private RecruitService recruitService;
	
	@PlayerGmCmd(command = "RECRUIT_RESET")
	public void gmRecharge(PlayerContext playerContext, String[] params) {
		Recruit recruit = recruitService.getEntity(playerContext.getId());
		for(int i = 0; i < 10000; i++) {
			List<RecruitData> recruitDatas = recruitService.getNewRefreshHeroList(recruit, RefreshType.USE_DIAMOND);
			Set<Integer> set = new HashSet<>();
			for(RecruitData recruitData : recruitDatas) {
				set.add(recruitData.getIdentity());
			}
			if(set.contains(1040004) || set.contains(1040005) || set.contains(1040006)) {
				System.out.println(GsonUtil.toJson(recruitDatas));
			}
		}
		recruit.getDrawNums().put(1040004, 181);
		recruit.getDrawNums().put(1040005, 244);
		recruit.getDrawNums().put(1040006, 52);
		System.out.println(GsonUtil.toJson(recruit.getDrawNums()));
	}
	
	@PlayerGmCmd(command = "RECRUIT_REFRESH")
	public void testRefresh(PlayerContext playerContext, String[] params) {
		Recruit recruit = recruitService.getEntity(playerContext.getId());
		List<RecruitData> recruitDatas = recruitService.getNewRefreshHeroList(recruit, RefreshType.USE_DIAMOND);
		System.out.println(GsonUtil.toJson(recruitDatas));
	}
}
