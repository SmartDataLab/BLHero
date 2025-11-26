package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao;

import java.time.LocalDateTime;
import java.util.List;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model.XianShiLiBao;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.service.XianShiLiBaoService;

import pb.xiugou.x1.protobuf.xianshilibao.P1005XianShiLiBao.XianShiLiBaoInfoResponse;

/**
 * @author yh
 * @date 2023/9/25
 * @apiNote
 */
@Controller
public class XianShiLiBaoHandler extends AbstractModuleHandler {
    @Autowired
    private XianShiLiBaoService xianShiLiBaoService;
    @Autowired
    private ApplicationSettings applicationSettings;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        List<XianShiLiBao> list = xianShiLiBaoService.getEntities(playerId);
        
        XianShiLiBaoInfoResponse.Builder response = XianShiLiBaoInfoResponse.newBuilder();
        if(applicationSettings.isGameArraignType() && "IOS".equals(playerContext.getDeviceType())) {
			//IOS提审服不返回
		} else {
			LocalDateTime now = LocalDateTimeUtil.now();
			for (XianShiLiBao xianShiLiBao : list) {
	        	if(now.isAfter(xianShiLiBao.getEndTime()) || xianShiLiBao.isBuy()) {
	        		continue;
	        	}
	        	response.addGiftDetail(xianShiLiBaoService.build(xianShiLiBao));
	        }
		}
        playerContextManager.push(playerId, XianShiLiBaoInfoResponse.Proto.ID, response.build());
    }
}
