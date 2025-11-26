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
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.dotdata.vo.TaskDotDataResultVo;
import com.xiugou.x1.backstage.module.dotdata.vo.TaskDotDataVo;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;

import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskDot;
import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskTiming;

/**
 * @author YY
 *
 */
@Controller
public class TaskDotDataController {

	@Autowired
	private DataBaseManager dataBaseManager;
	
	@ApiDocument("任务打点数据")
	@RequestMapping(value = "/taskDotData/data.auth")
	@ResponseBody
	public PageData<TaskDotDataResultVo> data(@RequestParam("serverUid") int serverUid) {
		DataBase dataBase = dataBaseManager.getLogDb(serverUid);
		
		Table table = MainlineTaskDot.class.getAnnotation(Table.class);
		String querySql = "SELECT task_id as taskId, max(task_name) as taskName, timing, count(1) as num FROM `" + table.name() + "` group by task_id, timing";
		List<TaskDotDataVo> groupedVos = ReadOnlyDao.queryAliasObjects(dataBase, TaskDotDataVo.class, querySql);
		
		Map<Integer, TaskDotDataResultVo> taskMap = new HashMap<>();
		for(TaskDotDataVo vo : groupedVos) {
			TaskDotDataResultVo resultVo = taskMap.get(vo.getTaskId());
			if(resultVo == null) {
				resultVo = new TaskDotDataResultVo();
				resultVo.setTaskId(vo.getTaskId());
				resultVo.setTaskName(vo.getTaskName());
				taskMap.put(resultVo.getTaskId(), resultVo);
			}
			if(vo.getTiming() == MainlineTaskTiming.START.getValue()) {
				resultVo.setStartNum(resultVo.getStartNum() + vo.getNum());
			} else if(vo.getTiming() == MainlineTaskTiming.FINISH.getValue()) {
				resultVo.setFinishNum(resultVo.getFinishNum() + vo.getNum());
			}
		}
		
		List<TaskDotDataResultVo> resultList = new ArrayList<>(taskMap.values());
		SortUtil.sortInt(resultList, TaskDotDataResultVo::getTaskId);
		
		for(TaskDotDataResultVo resultVo : resultList) {
			if(resultVo.getStartNum() <= 0) {
				continue;
			}
			resultVo.setFinishRate(resultVo.getFinishNum() * 1.0f / resultVo.getStartNum());
		}
		
		PageData<TaskDotDataResultVo> pageData = new PageData<>();
		pageData.setCount(resultList.size());
		pageData.setData(resultList);
		return pageData;
	}
}
