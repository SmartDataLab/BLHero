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
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.dotdata.vo.RecruitDotDataQuery;
import com.xiugou.x1.backstage.module.dotdata.vo.RecruitDotDataResultVo;
import com.xiugou.x1.backstage.module.dotdata.vo.RecruitDotDataVo;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServerRuntime;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerRuntimeService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;

import pojo.xiugou.x1.pojo.log.recruit.RecruitPrizeDrawLog;

/**
 * @author YY
 *
 */
@Controller
public class RecruitDotDataController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private GameServerRuntimeService gameServerRuntimeService;
	
	@ApiDocument("招募打点数据")
	@RequestMapping(value = "/recruitDotData/data.auth")
	@ResponseBody
	public PageData<RecruitDotDataResultVo> data(RecruitDotDataQuery query) {
		DataBase dataBase = dataBaseManager.getLogDb(query.getServerUid());
		GameServer gameServer = gameServerService.getEntity(query.getServerUid());
		GameServerRuntime gameServerRuntime = gameServerRuntimeService.getEntity(gameServer.getId());

		Table table = RecruitPrizeDrawLog.class.getAnnotation(Table.class);
		
		QuerySet querySet = new QuerySet();
		long startTime = query.getStartTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.currMillis();
		}
		String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
		querySet.addCondition("date_str >= ?", startStr);
		
		long endTime = query.getEndTime() * 1000L;
		if(endTime <= 0) {
			endTime = DateTimeUtil.currMillis();
		}
		String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
		querySet.addCondition("date_str <= ?", endStr);
		
		querySet.groupBy("group by pid, date_str");
		querySet.formWhere();
		
		String tableMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, startTime);
		String querySql = "SELECT pid, date_str as dateStr, count(1) as num FROM " + table.name() + "_" + tableMonth + " " + querySet.getWhere();
		
		List<RecruitDotDataVo> groupedVos = ReadOnlyDao.queryAliasObjects(dataBase, RecruitDotDataVo.class, querySql, querySet.getParams());
		
		Map<String, RecruitDotDataResultVo> resultMap = new HashMap<>();
		for(RecruitDotDataVo vo : groupedVos) {
			RecruitDotDataResultVo resultVo = resultMap.get(vo.getDateStr());
			if(resultVo == null) {
				resultVo = new RecruitDotDataResultVo();
				resultVo.setDateStr(vo.getDateStr());
				resultMap.put(resultVo.getDateStr(), resultVo);
			}
			if(vo.getNum() <= 10) {
				resultVo.setNum0to10(resultVo.getNum0to10() + 1);
			} else if(vo.getNum() <= 20) {
				resultVo.setNum11to20(resultVo.getNum11to20() + 1);
			} else {
				resultVo.setNum21toMax(resultVo.getNum21toMax() + 1);
			}
		}
		List<RecruitDotDataResultVo> resultList = new ArrayList<>(resultMap.values());
		SortUtil.sortStr(resultList, RecruitDotDataResultVo::getDateStr);
		for(RecruitDotDataResultVo resultVo : resultList) {
			resultVo.setJoinNum(resultVo.getNum0to10() + resultVo.getNum11to20() + resultVo.getNum11to20());
			if(gameServerRuntime != null) {
				resultVo.setCreateNum(gameServerRuntime.getCreateNum());
			}
		}
		
		PageData<RecruitDotDataResultVo> pageData = new PageData<>();
		pageData.setCount(resultList.size());
		pageData.setData(resultList);
		return pageData;
	}
}
