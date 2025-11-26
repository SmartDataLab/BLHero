/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.dotdata.vo.FogDotDataResultVo;
import com.xiugou.x1.backstage.module.dotdata.vo.FogDotDataVo;
import com.xiugou.x1.backstage.module.dotdata.vo.PlayerLevelNumVo;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;

import pojo.xiugou.x1.pojo.log.mainline.MainlineFogDot;

/**
 * @author YY
 *
 */
@Controller
public class FogDotDataController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	
	public static void main(String[] args) {
		String json = "[10016,10017,10018,10019,10020,10001,10002,10003,10004,10005,10006,10007,10008,10009,10010,10011,10012,10013,10014,10015,10021,10022,10023,10024,10025,10026,10027,10028,10029,10030,10031,10032,10033,10034,10035,10036,10037,10038,10039,10040,10041,10042,10043,10044,10045,10046,10047,10048,10049,10050,10051,10052,10053,10054,10055,10056,10057,10058,10059,10060,10061,10062,10063,10064,10065,10066,10067,10068,10069,10070,10071,10072,10073,10074,10075,10076,10077,10078,10079,10080,10081,10082,10083,10084,10085,10086,10087,10088,10089,10090,10091,10092,10093,10094,10095,10096,10097,10098,10099,10100,10101,10102,10103,10104,10105,10106,10107,10108,10109,10110,10111,10112,10113,10114,10115,10116,10117,10118,10119,10120,10121,10122,10123,10124,10125,10126,10127,10128,10129,10130,10131,10132,10133,10134,10135,10136,10137,10138,10139,10140,10141,10142,10143,10144,10145,10146,10147,10148,10149,10150,10151,10152,10153,10154,10155,10156,10157,10158,10159,10160,10161,10162,10163,10164,10165,10166,10167,10168,10169,10170,10171,10172,10173,10174,10175,10176,10177,10178,10179,10180,10181,10182,10183,10184,10185,10186,10187,10188,10189,10190,10191,10192,10193,10194,10195,10196,10197,10198,10199,10200,10201]";
		List<Integer> list = GsonUtil.getList(json, Integer.class);
		Collections.sort(list);
		for(int value : list) {
			System.out.println(value);
		}
	}
	
	//用于缓存查询结果
	private PageData<FogDotDataResultVo> resultData;
	private long refreshTime;
	
	@ApiDocument("引导打点数据")
	@RequestMapping(value = "/fogDotData/data.auth")
	@ResponseBody
	public PageData<FogDotDataResultVo> data(@RequestParam("serverUid") int serverUid) {
		DataBase dataBase = dataBaseManager.getLogDb(serverUid);
		GameServer gameServer = gameServerService.getEntity(serverUid);
		
		Table table = MainlineFogDot.class.getAnnotation(Table.class);
		String querySql = "SELECT fog_id as fogId,count(1) as num,MIN(fighting) as minFighting,`level` FROM `" + table.name()
				+ "` GROUP BY fog_id,`level`";
		List<FogDotDataVo> groupedVos = ReadOnlyDao.queryAliasObjects(dataBase, FogDotDataVo.class, querySql);

		QuerySet querySet = new QuerySet();
		querySet.addCondition("platform_id = ?", gameServer.getPlatformId());
		querySet.addCondition("server_id = ?", gameServer.getServerId());
		querySet.groupBy("GROUP BY `level`");
		querySet.formWhere();
		String playerSql = "SELECT `level`,count(1) as num FROM `player` " + querySet.getWhere();
		List<PlayerLevelNumVo> levelVos = ReadOnlyDao.queryAliasObjects(dataBase, PlayerLevelNumVo.class, playerSql, querySet.getParams());
		
		List<FogDotDataResultVo> resultList = new ArrayList<>();
		for(FogDotDataVo fogVo : groupedVos) {
			int canNum = 0;
			for(PlayerLevelNumVo playerNum : levelVos) {
				if(playerNum.getLevel() >= fogVo.getLevel()) {
					canNum += playerNum.getNum();
				}
			}
			FogDotDataResultVo resultVo = new FogDotDataResultVo();
			resultVo.setFogId(fogVo.getFogId());
			resultVo.setCanNum(canNum);
			resultVo.setNum(fogVo.getNum());
			resultVo.setMinFighting(fogVo.getMinFighting());
			if(canNum > 0) {
				BigDecimal rate = new BigDecimal(fogVo.getNum() * 1.0f / canNum * 100);
				resultVo.setRate(rate.setScale(2).toString() + "%");
			}
			resultList.add(resultVo);
		}
		
		SortUtil.sortInt(resultList, FogDotDataResultVo::getFogId);
		
		PageData<FogDotDataResultVo> pageData = new PageData<>();
		pageData.setData(resultList);
		pageData.setCount(resultList.size());
		return pageData;
	}
}
