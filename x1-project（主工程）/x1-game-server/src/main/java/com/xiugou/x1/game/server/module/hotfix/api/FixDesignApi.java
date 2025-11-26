/**
 * 
 */
package com.xiugou.x1.game.server.module.hotfix.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.gaming.design.export.CsvReader;
import org.gaming.design.loader.DesignCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xiugou.x1.design.X1SeparatorDesignParser;
import com.xiugou.x1.game.server.module.hotfix.service.HotfixService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class FixDesignApi {

	private static Logger logger = LoggerFactory.getLogger(FixDesignApi.class);

	@Autowired
	private HotfixService hotfixService;

	@PostMapping(value = GameApi.fixDesignUpload)
	@ResponseBody
	public String upload(MultipartFile[] files, @RequestParam("fixTime") String fixTime) {
		String relativePath = "FD" + fixTime + File.separator;
		String filePath = hotfixService.getHotfixDesignFolder() + relativePath;

		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		logger.info("加载{}中的热更配置开始", filePath);

		List<File> fixFiles = new ArrayList<>();
		for (MultipartFile file : files) {
			try {
				File targetFile = new File(filePath + file.getOriginalFilename());
				// 保存原文件
				file.transferTo(targetFile);
				fixFiles.add(targetFile);
				logger.info("加载{}中的热更配置，文件{}备份中", filePath, file.getOriginalFilename());
			} catch (Exception e) {
				ServerResponse serverResponse = new ServerResponse(ServerResponseCode.FAIL);
				serverResponse.setMessage(e.getMessage());
				return serverResponse.result();
			}
		}

		DesignCacheManager.loadSome(filePath, new CsvReader(), new X1SeparatorDesignParser());
		logger.info("加载{}中的热更配置完成", relativePath);
		for (File file : fixFiles) {
			file.renameTo(new File(file.getPath() + ".bak"));
		}
		return ServerResponse.SUCCESES.result();
	}
}
