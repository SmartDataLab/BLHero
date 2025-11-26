/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.gaming.tool.GsonUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.RandomUtil;
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
import com.xiugou.x1.backstage.module.hotfix.model.FixDesign;
import com.xiugou.x1.backstage.module.hotfix.model.FixDesignResult;
import com.xiugou.x1.backstage.module.hotfix.service.FixDesignResultService;
import com.xiugou.x1.backstage.module.hotfix.service.FixDesignService;
import com.xiugou.x1.backstage.module.hotfix.service.HotfixService;
import com.xiugou.x1.backstage.module.hotfix.struct.FixDesignTask;

/**
 * @author YY
 *
 */
@Controller
public class FixDesignController {
	
	private static Logger logger = LoggerFactory.getLogger(FixDesignController.class);
	
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private FixDesignService fixDesignService;
	@Autowired
	private FixDesignResultService fixDesignResultService;
	@Autowired
	private HotfixService hotfixService;
	
	@ApiDocument("请求热更配置记录")
	@RequestMapping(value = "/fixDesign/data.auth")
	@ResponseBody
	public PageData<FixDesign> data(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.orderBy("order by id desc");
		querySet.formWhere();
		
		return fixDesignService.query(querySet);
	}
	
	@ApiDocument("上传热更配置")
	@PostMapping(value = "/fixDesign/upload.auth")
	@ResponseBody
	public FixDesign upload(MultipartFile[] files, UploadServerForm form) {
		System.out.println("MultipartFile " + files.length + " " + GsonUtil.toJson(form));
		RoleContext roleContext = userService.getCurrUser();
		
		Set<Integer> uidSet = new HashSet<>(form.getServerUids());
		
		List<GameServer> servers = gameChannelService.getServers();
		Map<Integer, GameServer> serverMap = ListMapUtil.listToMap(servers, GameServer::getId);
		for(int uid : uidSet) {
			Asserts.isTrue(serverMap.containsKey(uid), TipsCode.FIX_DESIGN_SERVER_WRONG);
		}
		
		String time = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDDHHMMSS, DateTimeUtil.currMillis());
		
		FixDesign fixDesign = new FixDesign();
		fixDesign.setUserId(roleContext.getId());
		fixDesign.setUserName(roleContext.getName());
		fixDesign.getServerUids().addAll(form.getServerUids());
		fixDesign.setFixTime(time + "_" + RandomUtil.closeOpen(1, 9999));
		
		//热更文件相对路径
		String relativePath = "FD" + time + File.separator;
		String filePath = hotfixService.getHotfixDesignFolder() + relativePath;
		File fileDir = new File(filePath);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
		for(MultipartFile file : files) {
			File targetFile = new File(filePath + file.getOriginalFilename() + "." + time);
			String relativeFile = relativePath + file.getOriginalFilename() + "." + time;
			try {
				//保存原文件
				file.transferTo(targetFile);
				fixDesign.getFileNames().add(relativeFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fixDesignService.insert(fixDesign);
		
		List<FixDesignResult> results = new ArrayList<>();
		for(int serverUid : uidSet) {
			FixDesignResult result = new FixDesignResult();
			result.setFixId(fixDesign.getId());
			
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
		fixDesignResultService.insertAll(results);
		
		logger.info("热更配置已就绪，添加热更配置任务{}，热更批号{}", fixDesign.getId(), fixDesign.getFixTime());
		
		FixDesignTask task = new FixDesignTask();
		task.setFixDesign(fixDesign);
		task.setResults(results);
		fixDesignService.addTask(task);
		
		return fixDesign;
	}
	
	@ApiDocument("重发热更配置")
	@PostMapping(value = "/fixDesign/resend.auth")
	@ResponseBody
	public void resend(@RequestParam("id") long id) {
		FixDesign fixDesign = fixDesignService.getById(id);
		Asserts.isTrue(fixDesign != null, TipsCode.FIX_DESIGN_MISS, id);
		
		List<FixDesignResult> results = fixDesignResultService.queryByFixId(fixDesign.getId());
		
		FixDesignTask task = new FixDesignTask();
		task.setFixDesign(fixDesign);
		task.setResults(results);
		fixDesignService.addTask(task);
	}
	
	@ApiDocument("请求热更配置结果数据")
	@RequestMapping(value = "/fixDesignResult/data.auth")
	@ResponseBody
	public PageData<FixDesignResult> data(@RequestParam("id") long id) {
		List<FixDesignResult> result = fixDesignResultService.queryByFixId(id);
		
		SortUtil.sortInt(result, FixDesignResult::getServerUid);
		
		PageData<FixDesignResult> pageData = new PageData<>();
		pageData.setCount(result.size());
		pageData.setData(result);
		return pageData;
	}
}
