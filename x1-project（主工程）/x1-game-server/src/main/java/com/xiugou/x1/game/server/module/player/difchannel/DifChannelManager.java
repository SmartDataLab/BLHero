/**
 * 
 */
package com.xiugou.x1.game.server.module.player.difchannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.util.SensitiveUtil.SensitiveBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderResponse;

/**
 * @author YY
 *
 */
@Service
public class DifChannelManager implements Lifecycle {
	
	private static Logger logger = LoggerFactory.getLogger(DifChannelManager.class);
	
	@Autowired
	private ApplicationSettings applicationSettings;
	
	private static Map<String, DifChannel> channelMap = new HashMap<>();
	
	public static void register(DifChannel difChannel) {
		channelMap.put(difChannel.identity(), difChannel);
	}
	
	public static DifChannel get(String channelIdentity) {
		return channelMap.get(channelIdentity);
	}
	
	//渠道对接任务
	private final Queue<DifChannelTask> taskQueue = new ConcurrentLinkedQueue<>();
	
	public void runInSchedule() {
		DifChannelTask task = taskQueue.poll();
		while(task != null) {
			DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
			if(difChannel != null) {
				if(task.isCreate()) {
					difChannel.onCreate(task.getPlayer(), task.getChannelData());
				} else {
					difChannel.onLogin(task.getPlayer(), task.getChannelData());
				}
			}
			task = taskQueue.poll();
		}
	}
	
	public void addTask(DifChannelTask task) {
		taskQueue.add(task);
	}
	
	public void onRecharge(Recharge recharge, ByteString channelData, RechargeOrderResponse.Builder response) {
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		if(difChannel == null) {
			return;
		}
		difChannel.onRecharge(recharge, channelData, response);
	}
	
	public boolean checkName(Player player, String newNick, ByteString channelData) {
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		if(difChannel == null) {
			return true;
		}
		return difChannel.checkName(player, newNick, channelData);
	}
	
	public void changeName(Player player) {
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		if(difChannel == null) {
			return;
		}
		difChannel.changeName(player);
	}
	
	public void loadSensitiveInChannel(SensitiveBuilder builder) {
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		if(difChannel == null) {
			return;
		}
		difChannel.loadSensitive(builder);
	}
	
	public void checkLogin(String openId, ByteString channelData) {
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		if(difChannel == null) {
			return;
		}
		difChannel.checkLogin(openId, channelData);
	}
	
	@Override
	public void start() throws Exception {
		logger.info("当前渠道标识：{}", applicationSettings.getGameChannelIdentity());
		DifChannel difChannel = channelMap.get(applicationSettings.getGameChannelIdentity());
		logger.info("当前渠道处理类：{}", (difChannel == null ? "无" : difChannel.getClass().getSimpleName()));
	}
}
