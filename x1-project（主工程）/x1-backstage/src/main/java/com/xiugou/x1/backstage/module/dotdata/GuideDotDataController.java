/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.dotdata.vo.GuideDotDataResultVo;
import com.xiugou.x1.backstage.module.dotdata.vo.GuideDotDataVo;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;

import pojo.xiugou.x1.pojo.log.function.GuideDot;

/**
 * @author YY
 *
 */
@Controller
public class GuideDotDataController {

	@Autowired
	private DataBaseManager dataBaseManager;
	
	
	@ApiDocument("引导打点数据")
	@RequestMapping(value = "/guideDotData/data.auth")
	@ResponseBody
	public PageData<GuideDotDataResultVo> data(@RequestParam("serverUid") int serverUid) {
		DataBase dataBase = dataBaseManager.getLogDb(serverUid);
		
		Table table = GuideDot.class.getAnnotation(Table.class);
		
		String querySql = "SELECT guide_id as guideId, max(guide_name) as guideName, step, count(1) as num FROM `" + table.name()
				+ "` group by guide_id, step";
		List<GuideDotDataVo> groupedVos = ReadOnlyDao.queryAliasObjects(dataBase, GuideDotDataVo.class, querySql);
		
		Map<String, GuideDotDataResultVo> resultMap = new HashMap<>();
		for(GuideDotDataVo vo : groupedVos) {
			String key = vo.getGuideId() + "_" + vo.getStep();
			GuideDotDataResultVo resultVo = resultMap.get(key);
			if(resultVo == null) {
				resultVo = new GuideDotDataResultVo();
				resultVo.setGuideId(vo.getGuideId());
				resultVo.setGuideName(vo.getGuideName());
				resultVo.setStep(vo.getStep());
				resultMap.put(key, resultVo);
			}
			resultVo.setNum(vo.getNum());
		}
		
		List<GuideDotDataResultVo> resultList = new ArrayList<>(resultMap.values());
		SortUtil.sortInt(resultList, GuideDotDataResultVo::getGuideId, GuideDotDataResultVo::getStep);
		
		int totalLose = 0;
		for(int i = 0; i < resultList.size(); i++) {
			GuideDotDataResultVo resultVo = resultList.get(i);
			int loseNum = 0;
			if(i > 0) {
				GuideDotDataResultVo preResultVo = resultList.get(i - 1);
				loseNum = preResultVo.getNum() - resultVo.getNum();
				resultVo.setLoseNum(loseNum);
				totalLose += loseNum;
			} else {
				resultVo.setLoseNum(0);
			}
			
			if(resultVo.getNum() > 0) {
				BigDecimal rate = new BigDecimal(loseNum * 1.0f / resultVo.getNum() * 100);
				resultVo.setLoseRate(rate.setScale(2, RoundingMode.HALF_DOWN).toString() + "%");
			} else {
				resultVo.setLoseRate("");
			}
		}
		int finishNum = 0;
		if(!resultList.isEmpty()) {
			finishNum = resultList.get(resultList.size() - 1).getNum();
		}
		
		GuideDotDataResultVo totalVo = new GuideDotDataResultVo();
		totalVo.setGuideName("流失汇总");
		totalVo.setLoseNum(totalLose);
		if(finishNum + totalLose > 0) {
			BigDecimal rate = new BigDecimal(totalLose * 1.0f / (finishNum + totalLose) * 100);
			totalVo.setLoseRate(rate.setScale(2, RoundingMode.HALF_DOWN).toString() + "%");
		} else {
			totalVo.setLoseRate("");
		}
		resultList.add(totalVo);
		
		PageData<GuideDotDataResultVo> pageData = new PageData<>();
		pageData.setCount(resultList.size());
		pageData.setData(resultList);
		return pageData;
	}
	
}
