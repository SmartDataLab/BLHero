/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.hotfix.model.FixDesign;
import com.xiugou.x1.backstage.module.hotfix.model.FixDesignResult;
import com.xiugou.x1.backstage.module.hotfix.struct.FixDesignTask;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Service
public class FixDesignService extends AbstractService<FixDesign> {

	private static Logger logger = LoggerFactory.getLogger(FixDesignService.class);
	
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private FixDesignResultService fixDesignResultService;
	@Autowired
	private HotfixService hotfixService;
	
	private Queue<FixDesignTask> taskQueue = new ConcurrentLinkedQueue<>();
	
	public PageData<FixDesign> query(QuerySet querySet) {
		List<FixDesign> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<FixDesign> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void addTask(FixDesignTask task) {
		taskQueue.add(task);
	}
	
	public void runInSchedule() {
		FixDesignTask task = taskQueue.poll();
		if(task == null) {
			return;
		}
		
		List<File> files = new ArrayList<>();
		for(String filePath : task.getFixDesign().getFileNames()) {
			File file = new File(hotfixService.getHotfixDesignFolder() + filePath);
			files.add(file);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("fixTime", task.getFixDesign().getFixTime());
		
		List<FixDesignResult> updateList = new ArrayList<>();
		for(FixDesignResult result : task.getResults()) {
			if(result.getStatus() == 1) {
				continue;
			}
			GameServer gameServer = gameServerService.getEntity(result.getServerUid());
			
			logger.info("正在向服务器{}-{}-{}发送热更配置请求", gameServer.getId(), gameServer.getServerId(), gameServer.getName());
			ServerResponse serverResponse = X1HttpUtil.postParamAndFile(gameServer, GameApi.fixDesignUpload, params, files);
			
			if(serverResponse == null) {
				result.setStatus(2);
				result.setStatus(-1);
				result.setMessage(String.format("服务器%s-%s-%s没有响应", gameServer.getId(), gameServer.getServerId(), gameServer.getName()));
			} else {
				if(serverResponse.getCode() == 0) {
					result.setStatus(1);
					result.setCode(serverResponse.getCode());
					result.setMessage(serverResponse.getMessage());
				} else {
					result.setStatus(2);
					result.setCode(serverResponse.getCode());
					result.setMessage(serverResponse.getMessage());
				}
				logger.info("收到向服务器{}-{}-{}发送热更配置的响应，响应码：{}，响应信息：{}", gameServer.getId(), gameServer.getServerId(),
						gameServer.getName(), serverResponse.getCode(), serverResponse.getMessage());
			}
			updateList.add(result);
		}
		fixDesignResultService.updateAll(updateList);
	}
}
