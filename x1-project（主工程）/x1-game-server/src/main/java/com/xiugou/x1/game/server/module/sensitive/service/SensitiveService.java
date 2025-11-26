package com.xiugou.x1.game.server.module.sensitive.service;

import java.util.List;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.util.SensitiveUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.SensitiveWordsCache;
import com.xiugou.x1.design.module.autogen.SensitiveWordsAbstractCache.SensitiveWordsCfg;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannelManager;

/**
 * @author yh
 * @date 2023/7/21
 * @apiNote
 */
@Service
public class SensitiveService implements Lifecycle {
	
	private static Logger logger = LoggerFactory.getLogger(SensitiveService.class);
	
	@Autowired
	private SensitiveWordsCache sensitiveWordsCache;
	@Autowired
	private DifChannelManager difChannelManager;

	@Override
	public void start() throws Exception {
		SensitiveUtil.SensitiveBuilder builder = SensitiveUtil.newBuilder();
		difChannelManager.loadSensitiveInChannel(builder);
		List<SensitiveWordsCfg> sensitiveWordsCfg = sensitiveWordsCache.all();
		for (SensitiveWordsCfg sensitive : sensitiveWordsCfg) {
			builder.addSensitive(sensitive.getWord());
		}
		logger.info("加载本地敏感词库{}", sensitiveWordsCfg.size());
		builder.setUp();
	}

	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MIN);
	}
	
	
}
