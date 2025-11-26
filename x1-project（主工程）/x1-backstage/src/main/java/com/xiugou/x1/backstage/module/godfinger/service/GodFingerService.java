/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.godfinger.model.GodFinger;
import com.xiugou.x1.backstage.module.system.model.SystemCounter;
import com.xiugou.x1.backstage.module.system.service.SystemCounterService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.player.form.GodFingerTable;
import pojo.xiugou.x1.pojo.module.player.form.GodFingerTable.GodFingerData;

/**
 * @author YY
 *
 */
@Service
public class GodFingerService extends AbstractService<GodFinger> {
	
	@Autowired
	private SystemCounterService systemCounterService;
	@Autowired
	private GameServerService gameServerService;
	
	public PageData<GodFinger> query(QuerySet querySet) {
		List<GodFinger> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<GodFinger> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void runInSchedule() {
		SystemCounter systemCounter = systemCounterService.instance();
		if(DateTimeUtil.currMillis() < systemCounter.getGodFingerTime()) {
			return;
		}
		systemCounter.setGodFingerTime(DateTimeUtil.tomorrowZeroMillis());
		systemCounterService.update(systemCounter);
		
		List<GodFinger> list = this.repository().getAllInDb();
		Map<Integer, List<GodFinger>> serverMap = ListMapUtil.fillListMap(list, GodFinger::getServerUid);
		
		for(Entry<Integer, List<GodFinger>> entry : serverMap.entrySet()) {
			GameServer gameServer = gameServerService.getEntity(entry.getKey());
			GodFingerTable table = new GodFingerTable();
			for(GodFinger godFinger : entry.getValue()) {
				GodFingerData data = new GodFingerData();
				data.setPlayerId(godFinger.getPlayerId());
				data.setMoney(godFinger.getMoney());
				table.getDatas().add(data);
			}
			logger.info("发放服务器{}-{}-{}的金手指奖励", gameServer.getId(), gameServer.getServerId(), gameServer.getName());
			X1HttpUtil.jsonPost(gameServer, GameApi.godFingerSendGift, table);
		}
	}
}
