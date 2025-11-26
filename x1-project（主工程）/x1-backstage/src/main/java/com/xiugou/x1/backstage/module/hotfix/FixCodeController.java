/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.hotfix.form.UploadServerForm;
import com.xiugou.x1.backstage.module.hotfix.model.FixCode;
import com.xiugou.x1.backstage.module.hotfix.model.FixCodeResult;
import com.xiugou.x1.backstage.module.hotfix.service.FixCodeResultService;
import com.xiugou.x1.backstage.module.hotfix.service.FixCodeService;
import com.xiugou.x1.backstage.module.hotfix.service.HotfixService;
import com.xiugou.x1.backstage.module.hotfix.struct.FixCodeTask;
import com.xiugou.x1.backstage.module.hotfix.struct.ServerPatch;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class FixCodeController {
	
	private static Logger logger = LoggerFactory.getLogger(FixCodeController.class);
	
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private FixCodeService fixCodeService;
	@Autowired
	private FixCodeResultService fixCodeResultService;
	@Autowired
	private HotfixService hotfixService;
	
	@ApiDocument("请求热更代码记录")
	@RequestMapping(value = "/fixCode/data.auth")
	@ResponseBody
	public PageData<FixCode> data(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.orderBy("order by id desc");
		querySet.formWhere();
		
		return fixCodeService.query(querySet);
	}
	
	@ApiDocument("上传热更代码")
	@PostMapping(value = "/fixCode/upload.auth")
	@ResponseBody
	public FixCode upload(MultipartFile[] files, UploadServerForm form) {
		RoleContext roleContext = userService.getCurrUser();
		Set<Integer> uidSet = new HashSet<>(form.getServerUids());
		
		List<GameServer> servers = gameChannelService.getServers();
		Map<Integer, GameServer> serverMap = ListMapUtil.listToMap(servers, GameServer::getId);
		for(int uid : uidSet) {
			Asserts.isTrue(serverMap.containsKey(uid), TipsCode.FIX_DESIGN_SERVER_WRONG);
		}
		
		String time = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDDHHMMSS, DateTimeUtil.currMillis());
		
		FixCode fixCode = new FixCode();
		fixCode.setUserId(roleContext.getId());
		fixCode.setUserName(roleContext.getName());
		fixCode.getServerUids().addAll(form.getServerUids());
		fixCode.setFixTime(time);
		
		//热更文件相对路径
		String relativePath = "FC" + time + File.separator;
		String filePath = hotfixService.getHotfixCodeFolder() + relativePath;
		File fileDir = new File(filePath);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
		for(MultipartFile file : files) {
			File targetFile = new File(filePath + file.getOriginalFilename());
			String relativeFile = relativePath + file.getOriginalFilename();
			//保存原文件
			try {
				file.transferTo(targetFile);
				fixCode.getFileNames().add(relativeFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fixCodeService.insert(fixCode);
		
		List<FixCodeResult> results = new ArrayList<>();
		for(int serverUid : uidSet) {
			FixCodeResult result = new FixCodeResult();
			result.setFixId(fixCode.getId());
			
			GameServer gameServer = gameServerService.getEntity(serverUid);
			if(gameServer == null) {
				result.setStatus(2);
				result.setMessage("根据唯一ID" + serverUid + "未找到服务器");
			} else {
				result.setServerUid(gameServer.getId());
				result.setServerId(gameServer.getServerId());
				result.setName(gameServer.getName());
			}
			results.add(result);
		}
		fixCodeResultService.insertAll(results);
		
		logger.info("热更代码已就绪，添加热更代码任务{}，热更批号{}", fixCode.getId(), fixCode.getFixTime());
		
		FixCodeTask task = new FixCodeTask();
		task.setFixCode(fixCode);
		task.setResults(results);
		fixCodeService.addTask(task);
		return fixCode;
	}
	
	@ApiDocument("重发热更代码")
	@PostMapping(value = "/fixCode/resend.auth")
	@ResponseBody
	public void resend(@RequestParam("id") long id) {
		FixCode fixCode = fixCodeService.getById(id);
		Asserts.isTrue(fixCode != null, TipsCode.FIX_DESIGN_MISS, id);
		
		List<FixCodeResult> results = fixCodeResultService.queryByFixId(fixCode.getId());
		
		FixCodeTask task = new FixCodeTask();
		task.setFixCode(fixCode);
		task.setResults(results);
		fixCodeService.addTask(task);
	}
	
	@ApiDocument("请求热更代码结果数据")
	@RequestMapping(value = "/fixCodeResult/data.auth")
	@ResponseBody
	public PageData<FixCodeResult> data(@RequestParam("id") long id) {
		List<FixCodeResult> result = fixCodeResultService.queryByFixId(id);
		
		SortUtil.sortInt(result, FixCodeResult::getServerUid);
		
		PageData<FixCodeResult> pageData = new PageData<>();
		pageData.setCount(result.size());
		pageData.setData(result);
		return pageData;
	}
	
	@ApiDocument("执行补丁")
	@RequestMapping(value = "/fixCodeResult/patch.auth")
	@ResponseBody
	public void patch(ServerPatch patch) {
		for(Integer serverUid : patch.getServerUids()) {
			GameServer gameServer = gameServerService.getEntity(serverUid);
			ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, patch.getParam(), Collections.emptyMap());
			logger.info("游戏服{}-{}-{}执行补丁{}，响应码：{}，响应消息：{}", gameServer.getId(), gameServer.getServerId(),
					gameServer.getName(), patch.getParam(), serverResponse.getCode(), serverResponse.getMessage());
		}
	}
}
