/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.dotdata.vo.BossDotDataResultVo;
import com.xiugou.x1.backstage.module.dotdata.vo.BossDotDataVo;
import com.xiugou.x1.backstage.module.dotdata.vo.PlayerLevelNumVo;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;

import pojo.xiugou.x1.pojo.log.mainline.MainlineBossTiming;

/**
 * @author YY
 *
 */
@Controller
public class BossDotDataController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	
	//用于缓存查询结果
	private PageData<BossDotDataResultVo> resultData;
	private long refreshTime;
	
	@ApiDocument("引导打点数据")
	@RequestMapping(value = "/bossDotData/data.auth")
	@ResponseBody
	public PageData<BossDotDataResultVo> data(@RequestParam("serverUid") int serverUid) {
		DataBase dataBase = dataBaseManager.getLogDb(serverUid);
		GameServer gameServer = gameServerService.getEntity(serverUid);
		
		String querySql = "SELECT boss_id as bossId,max(boss_name) as bossName,`level`,count(1) as challengeNum,"
				+ "count(DISTINCT player_id) as playerNum,MIN(fighting) as minFighting,timing "
				+ "FROM `mainline_boss_dot` GROUP BY boss_id,`level`,timing";
		List<BossDotDataVo> groupedVos = ReadOnlyDao.queryAliasObjects(dataBase, BossDotDataVo.class, querySql);
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("platform_id = ?", gameServer.getPlatformId());
		querySet.addCondition("server_id = ?", gameServer.getServerId());
		querySet.groupBy("GROUP BY `level`");
		querySet.formWhere();
		String playerSql = "SELECT `level`,count(1) as num FROM `player` " + querySet.getWhere();
		List<PlayerLevelNumVo> levelVos = ReadOnlyDao.queryAliasObjects(dataBase, PlayerLevelNumVo.class, playerSql, querySet.getParams());
		
		Map<Integer, BossDotDataResultVo> resultMap = new HashMap<>();
		for(BossDotDataVo bossVo : groupedVos) {
			int canNum = 0;
			if(bossVo.getTiming() == MainlineBossTiming.CHALLENGE.getValue()) {
				for(PlayerLevelNumVo playerNum : levelVos) {
					if(playerNum.getLevel() >= bossVo.getLevel()) {
						canNum += playerNum.getNum();
					}
				}
			}
			
			BossDotDataResultVo resultVo = resultMap.get(bossVo.getBossId());
			if(resultVo == null) {
				resultVo = new BossDotDataResultVo();
				resultVo.setBossId(bossVo.getBossId());
				resultVo.setBossName(bossVo.getBossName());
				resultMap.put(resultVo.getBossId(), resultVo);
			}
			if(bossVo.getTiming() == MainlineBossTiming.CHALLENGE.getValue()) {
				resultVo.setCanChallengeNum(canNum);
				resultVo.setChallengeNum(bossVo.getChallengeNum());
				resultVo.setPlayerNum(bossVo.getPlayerNum());
			} else if(bossVo.getTiming() == MainlineBossTiming.KILL.getValue()) {
				resultVo.setKillNum(bossVo.getChallengeNum());
				resultVo.setMinFighting(bossVo.getMinFighting());
			}
		}
		List<BossDotDataResultVo> resultList = new ArrayList<>(resultMap.values());
		SortUtil.sortInt(resultList, BossDotDataResultVo::getBossId);
		
		PageData<BossDotDataResultVo> pageData = new PageData<>();
		pageData.setData(resultList);
		pageData.setCount(resultList.size());
		return pageData;
	}
}
