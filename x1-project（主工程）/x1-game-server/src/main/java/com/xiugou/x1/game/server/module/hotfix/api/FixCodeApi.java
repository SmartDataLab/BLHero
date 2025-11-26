/**
 * 
 */
package com.xiugou.x1.game.server.module.hotfix.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.gaming.ruler.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xiugou.x1.game.server.module.hotfix.service.HotfixService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class FixCodeApi {

	private static Logger logger = LoggerFactory.getLogger(FixCodeApi.class);
	
	@Autowired
	private FixCodeTestA fixCodeTestA;
	@Autowired
	private HotfixService hotfixService;
	
	//进程ID
	private String processId;
	
	@PostConstruct
	public void init() {
//		processId = Agent.getProcessId();
		logger.info("热更代码准备，服务器启动设定进程ID={}", processId);
	}
	
	@PostMapping(value = GameApi.fixCodeUpload)
	@ResponseBody
	public String upload(MultipartFile[] files, @RequestParam("fixTime") String fixTime) {
		String relativePath = "FC" + fixTime + File.separator;
		String patchPath = hotfixService.getHotfixCodeFolder() + relativePath;
		
		File fileDir = new File(patchPath);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
		logger.info("加载{}中的热更代码开始", patchPath);
		//1、将文件备份到指定目录中
		List<File> fixFiles = new ArrayList<>();
		for(MultipartFile file : files) {
			try {
				File targetFile = new File(patchPath + file.getOriginalFilename());
				//保存原文件
				file.transferTo(targetFile);
				fixFiles.add(targetFile);
				logger.info("加载{}中的热更代码，文件{}备份中", patchPath, file.getOriginalFilename());
			} catch (Exception e) {
				ServerResponse serverResponse = new ServerResponse(ServerResponseCode.FAIL);
				serverResponse.setMessage(e.getMessage());
				return serverResponse.result();
			}
		}
		//2、执行热更处理
//		Agent.hotfix(processId, patchPath);
		logger.info("加载{}中的热更代码完成", patchPath);
		//3、重命名用于热更的文件
		for (File file : fixFiles) {
			file.renameTo(new File(file.getPath() + ".bak"));
		}
		
		return ServerResponse.SUCCESES.result();
	}
	
	@RequestMapping(value = "/fixCodeApi/patch")
	@ResponseBody
	public void patch(@RequestParam(value = "param", required = false) String param) {
		System.out.println("热更前");
		fixCodeTestA.test();
		System.out.println("热更后");
		fixCodeTestA.test();
	}
	
	public static void main(String[] args) {
		String res = HttpUtil.formPost("http://120.79.34.46:20001/fixCodeApi/patch", Collections.emptyMap());
		System.out.println(res);
	}
}
