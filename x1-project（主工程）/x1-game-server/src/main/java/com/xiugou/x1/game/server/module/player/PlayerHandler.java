/**
 *
 */
package com.xiugou.x1.game.server.module.player;

import java.time.LocalDateTime;
import java.util.TimeZone;

import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.util.SensitiveUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.NameRandomCache;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.player.LogoutInternalMessage;
import com.xiugou.x1.game.server.foundation.player.LogoutType;
import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler.InfoPriority;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannelManager;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannelTask;
import com.xiugou.x1.game.server.module.player.event.PlayerCreateEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerLoginEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerLogoutEvent;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerDailyResetMessage;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerForbidListMessage;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerForbidMessage;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerLoginMessage;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.ForbidListService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

import pb.xiugou.x1.protobuf.player.Player.ChangeNameRequest;
import pb.xiugou.x1.protobuf.player.Player.ChangeNameResponse;
import pb.xiugou.x1.protobuf.player.Player.EasyLoginRequest;
import pb.xiugou.x1.protobuf.player.Player.HeartBeatRequest;
import pb.xiugou.x1.protobuf.player.Player.HeartBeatResponse;
import pb.xiugou.x1.protobuf.player.Player.LoginRequest;
import pb.xiugou.x1.protobuf.player.Player.LoginResponse;
import pb.xiugou.x1.protobuf.player.Player.LoginStatus;
import pb.xiugou.x1.protobuf.player.Player.LogoutMessage;
import pb.xiugou.x1.protobuf.player.Player.LogoutRequest;
import pb.xiugou.x1.protobuf.player.Player.PlayerModuleInfoRequest;
import pb.xiugou.x1.protobuf.player.Player.PlayerModuleInfoResponse;
import pb.xiugou.x1.protobuf.player.Player.PlayerMoreInfoEndMessage;
import pb.xiugou.x1.protobuf.player.Player.PlayerMoreInfoRequest;
import pb.xiugou.x1.protobuf.player.Player.PlayerMoreInfoResponse;
import pb.xiugou.x1.protobuf.player.Player.PlayerPushEndMessage;
import pb.xiugou.x1.protobuf.player.Player.PlayerWiretapRequest;
import pb.xiugou.x1.protobuf.player.Player.PlayerWiretapResponse;

/**
 * @author YY
 */
@Controller
public class PlayerHandler {

    private static Logger logger = LoggerFactory.getLogger(PlayerHandler.class);

    @Autowired
    private PlayerService playerService;
    @Autowired
    private ApplicationSettings applicationSettings;
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private TimeSetting timeSetting;
    @Autowired
    private ForbidListService forbidListService;
    @Autowired
    private DifChannelManager difChannelManager;
    @Autowired
	private NameRandomCache nameRandomCache;

    @PlayerCmd(needLogin = false)
    public LoginResponse login(PlayerContext playerContext, LoginRequest request) {
        Asserts.isTrue(applicationSettings.getGameServerId() == request.getServerId(), TipsCode.SERVER_NOT_MATCH);
        Asserts.isTrue(request.getOpenId() != null && !"".equals(request.getOpenId().trim()), TipsCode.OPENID_NOT_EMPTY);

        logger.info("玩家{}请求登录", request.getOpenId());
        
        //正在维护或者服务器还没有对外开放的时候，需要看看是不是白名单账号
        if (serverInfoService.isMaintain() || !serverInfoService.isOpen()) {
            //TODO 请求后台查看是不是白名单
            if (!playerService.isWhite(request.getChannelId(), request.getOpenId())) {
            	logger.info("服务器维护中");
                LoginResponse.Builder response = LoginResponse.newBuilder();
                response.setStatus(LoginStatus.MAINTAIN);
                return response.build();
            }
        }
        String openId = request.getOpenId().trim();
        Asserts.isTrue(!forbidListService.isForbid(openId), TipsCode.OPENID_FORBID);
        
        //验证各渠道的登录
        difChannelManager.checkLogin(openId, request.getChannelData());
        
        Player player = playerService.getByOpenIdServerId(openId, request.getServerId());
        boolean isCreate = false;
        if (player == null) {
            player = new Player();
            player.setOpenId(openId);
            player.setServerId(request.getServerId());
            
            String nick = null;
            for(int i = 0; i < 10; i++) {
            	nick = nameRandomCache.generationRandomName(applicationSettings.getGameLanguage(), Math.random() < 0.5 ? 1 : 2);
            	if(!playerService.hasNick(nick)) {
            		break;
            	} else {
            		nick = nick + RandomUtil.closeClose(10000, 99999);
            		if(!playerService.hasNick(nick)) {
                		break;
            		}
            	}
            }
            player.setNick(nick);
            player.setHead("");
            player.setLevel(1);
            player.setSex(0);
            player.setCreateIp(playerContext.getRemoteAddress());
            player.setCreateDeviceType(request.getDeviceType());
            player.setCreateDeviceId(request.getDeviceId());
            player.setCreateChannel(request.getChannelId());

            player.setLoginIp(player.getCreateIp());
            player.setLoginDeviceType(request.getDeviceType() == null ? "" : request.getDeviceType().toUpperCase());
            player.setLoginDeviceId(player.getCreateDeviceId());
            
            if(!difChannelManager.checkName(player, nick, request.getChannelData())) {
            	logger.info("名字验证违规");
            	LoginResponse.Builder response = LoginResponse.newBuilder();
                response.setStatus(LoginStatus.VIOLATION);
                return response.build();
            }
            playerService.insert(player);
            playerService.addToCreatedPlayers(player);
            playerService.addToFreshPlayer(player);
            
            PlayerOneToOneService.createPlayerDatas(player.getId());
            
            isCreate = true;
            logger.info("玩家{}创号登录，ID：{}", request.getOpenId(), player.getId());
        } else {
        	if(request.getReConnect()) {
        		logger.info("玩家{}重连登录，ID：{}", request.getOpenId(), player.getId());
        	} else {
        		logger.info("玩家{}普通登录，ID：{}", request.getOpenId(), player.getId());
        	}
        }

        if (!isCreate) {
            // 玩家被封号
            if (LocalDateTimeUtil.now().isBefore(player.getForbidEndTime())) {
                LoginResponse.Builder response = LoginResponse.newBuilder();
                response.setStatus(LoginStatus.FORBID);
                response.setForbindEndTime(LocalDateTimeUtil.toEpochMilli(player.getForbidEndTime()));
                return response.build();
            }
            if(!request.getReConnect()) {
        		logger.info("创建推送任务");
        		difChannelManager.addTask(new DifChannelTask(player, request.getChannelData(), false));
            } else {
        		logger.info("不创建推送任务");
        	}
        } else {
        	difChannelManager.addTask(new DifChannelTask(player, request.getChannelData(), true));
        }

        playerContext.setId(player.getId());
        playerContext.setServerId(player.getServerId());
        playerContext.setOpenId(player.getOpenId());
        playerContext.setActor(PlayerActorPool.getActor(player.getId()));
        playerContext.setActorReady(true);
        playerContext.setNewComer(isCreate);
        playerContext.setChannelData(request.getChannelData());
        playerContext.setDeviceType(request.getDeviceType() != null ? request.getDeviceType().toUpperCase() : "");
        playerContext.setDeviceId(request.getDeviceId());

        playerContext.tell(new PlayerLoginMessage(playerContext, isCreate));

        LoginResponse.Builder response = LoginResponse.newBuilder();
        response.setStatus(LoginStatus.NORMAL);
        response.setPid(player.getId());
        response.setServerTime(DateTimeUtil.currMillis());
        response.setOffsetUtc(TimeZone.getDefault().getOffset(DateTimeUtil.currMillis()));
        response.setNick(player.getNick());
        response.setHead(player.getHead());
        response.setExp(player.getExp());
        response.setLevel(player.getLevel());
        response.setChangeNameTimes(player.getChangeNameNum());
        response.setVipLevel(player.getVipLevel());
        response.setOpenDay(serverInfoService.getOpenedDay());
        response.setOpenGm(applicationSettings.isGameDebugMode());
        response.setIsCreate(isCreate);
        response.setCreateTime(LocalDateTimeUtil.toEpochMilli(player.getInsertTime()));
        response.setVipExp(player.getVipExp());
        return response.build();
    }
    
    @PlayerCmd(needLogin = false)
    public LoginResponse easyLogin(PlayerContext playerContext, EasyLoginRequest request) {
    	Asserts.isTrue(applicationSettings.getGameServerId() == request.getServerId(), TipsCode.SERVER_NOT_MATCH);
        Asserts.isTrue(request.getOpenId() != null && !"".equals(request.getOpenId().trim()), TipsCode.OPENID_NOT_EMPTY);
        
        String openId = request.getOpenId().trim();
        Player player = playerService.getByOpenIdServerId(openId, request.getServerId());
        Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, openId);
        
        playerContext.setId(player.getId());
        playerContext.setServerId(player.getServerId());
        playerContext.setOpenId(player.getOpenId());
        playerContext.setActor(PlayerActorPool.getActor(player.getId()));
        playerContext.setActorReady(true);
        playerContext.setNewComer(false);
        playerContext.setDeviceType(request.getDeviceType() != null ? request.getDeviceType().toUpperCase() : "");
        playerContext.setDeviceId("");

        playerContext.tell(new PlayerLoginMessage(playerContext, false));

        LoginResponse.Builder response = LoginResponse.newBuilder();
        response.setStatus(LoginStatus.NORMAL);
        response.setPid(player.getId());
        response.setServerTime(DateTimeUtil.currMillis());
        response.setOffsetUtc(TimeZone.getDefault().getOffset(DateTimeUtil.currMillis()));
        response.setNick(player.getNick());
        response.setHead(player.getHead());
        response.setExp(player.getExp());
        response.setLevel(player.getLevel());
        response.setChangeNameTimes(player.getChangeNameNum());
        response.setVipLevel(player.getVipLevel());
        response.setOpenDay(serverInfoService.getOpenedDay());
        response.setOpenGm(applicationSettings.isGameDebugMode());
        response.setIsCreate(false);
        response.setCreateTime(LocalDateTimeUtil.toEpochMilli(player.getInsertTime()));
        response.setVipExp(player.getVipExp());
        return response.build();
    }

    @InternalCmd
    public void login(PlayerLoginMessage message) {
        PlayerContext playerContext = message.getPlayerContext();

        Asserts.isTrue(playerContext.isActorReady(), TipsCode.PLAYER_NOT_READY);

        Player player = playerService.getEntity(playerContext.getId());

        PlayerContext oldContext = playerContextManager.getContext(playerContext.getId());
        if (oldContext != null && playerContext != oldContext) {
            logger.info("玩家{}-{}异地登录，注销旧连接ID：{}", player.getId(), player.getOpenId(), oldContext.getChannel().id());
            LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
            logoutMessage.playerContext = oldContext;
            logoutMessage.logoutType = LogoutType.ANOTHER_LOGIN;
            logoutMessage.lastLoginTime = player.getLastLoginTime();
            oldContext.tell(logoutMessage);
        }

        LocalDateTime localDateTime = timeSetting.theDayOTime(player.getLastLoginTime());
        boolean sameDay = LocalDateTimeUtil.isSameDay(localDateTime, LocalDateTimeUtil.now());
        player.setOnline(true);
        player.setLastLoginTime(LocalDateTimeUtil.now());
        player.setLoginIp(playerContext.getRemoteAddress());
        player.setLoginDeviceType(playerContext.getDeviceType());
        player.setLoginDeviceId(playerContext.getDeviceId());
        playerService.update(player);

        playerContext.setLogin(true);
        playerContextManager.addToOnline(playerContext);
        playerService.addToReport(player);

        playerService.onlineCount.put(player.getId(), 1);

        if (message.isCreate()) {
            EventBus.post(PlayerCreateEvent.of(player));
        } else  {
            EventBus.post(PlayerLoginEvent.of(player, !sameDay));
        }
        //推送玩家数据
        AbstractModuleHandler.pushInfo(playerContext, InfoPriority.BASE);
        playerContextManager.push(player.getId(), PlayerPushEndMessage.Proto.ID, PlayerPushEndMessage.getDefaultInstance());
    }

    @PlayerCmd
    public void logout(PlayerContext playerContext, LogoutRequest request) {
        LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
        logoutMessage.playerContext = playerContext;
        logoutMessage.logoutType = LogoutType.PLAYER_LOGOUT;
        playerContext.tell(logoutMessage);
    }

    @InternalCmd
    public void logout(LogoutInternalMessage message) {
        PlayerContext playerContext = message.playerContext;
        if(playerContext.getId() <= 0) {
        	playerContext.closeChannel();
    		return;
    	}
        if (playerContext.getLogoutType() != null) {
            logger.info("玩家{}连接已经断开，登出方式：{}，连接ID：{}", playerContext.getId(), playerContext.getLogoutType().getDesc(), playerContext.getChannel().id());
            return;
        }

        Player player = playerService.getEntity(playerContext.getId());
        if (message.logoutType != LogoutType.ANOTHER_LOGIN && playerContext.getId() > 0) {
            player.setOnline(false);
        }
        player.setLastLogoutTime(LocalDateTimeUtil.now());
        if (message.lastLoginTime != null) {
            player.setDailyOnline(player.getDailyOnline() + (DateTimeUtil.currSecond() - LocalDateTimeUtil.toEpochSecond(message.lastLoginTime)));
        } else {
            player.setDailyOnline(player.getDailyOnline() + (DateTimeUtil.currSecond() - LocalDateTimeUtil.toEpochSecond(player.getLastLoginTime())));
        }
        playerService.update(player);


        boolean login = playerContext.isLogin();
        playerContext.setActorReady(false);
        playerContext.setLogin(false);
        playerContext.setLogoutType(message.logoutType);
        playerContextManager.removeFromOnline(playerContext);
        playerService.addToReport(player);

        LogoutMessage.Builder builder = LogoutMessage.newBuilder();
        builder.setType(message.logoutType.getCode());
        if (message.forbidEndTime != null) {
            builder.setForbindEndTime(LocalDateTimeUtil.toEpochMilli(message.forbidEndTime));
        }
        playerContext.write(LogoutMessage.Proto.ID, builder.build());

        playerContext.closeChannel();

        if (login) {
            EventBus.post(new PlayerLogoutEvent(playerContext.getId()));
        }
        logger.info("玩家{}登出游戏，登出方式：{}，连接ID：{}", playerContext.getId(), playerContext.getLogoutType().getDesc(), playerContext.getChannel().id());
    }

    @PlayerCmd
    public HeartBeatResponse heartBeat(PlayerContext playerContext, HeartBeatRequest request) {
        HeartBeatResponse.Builder builder = HeartBeatResponse.newBuilder();
        builder.setServerTime(DateTimeUtil.currMillis());
        return builder.build();
    }


    @PlayerCmd
    public ChangeNameResponse changeName(PlayerContext playerContext, ChangeNameRequest request) {
        String newName = request.getNewName();
        Asserts.isTrue(!"".equals(newName), TipsCode.STRING_EMPTY);
        Asserts.isTrue(!playerService.hasNick(newName), TipsCode.REPEATED_NICK);
        
        Player player = playerService.getEntity(playerContext.getId());
        
        boolean checked = difChannelManager.checkName(player, newName, playerContext.getChannelData());
        Asserts.isTrue(checked, TipsCode.PLAYER_NAME_HAS_SENSITIVE);
        
        String firstSensitiveWord = SensitiveUtil.hasSensitive(newName);
        Asserts.isTrue(firstSensitiveWord == null, TipsCode.PLAYER_NAME_HAS_SENSITIVE);
        
        if (player.getChangeNameNum() > 0) {
            thingService.cost(playerContext.getId(), battleConstCache.getPlayer_renick_cost(), GameCause.PLAYER_CHANGE_NAME);
        }
        String oldNick = player.getNick();
        player.setNick(newName);
        player.setChangeNameNum(player.getChangeNameNum() + 1);
        playerService.update(player);
        playerService.changeNick(newName, oldNick);
        
        difChannelManager.changeName(player);

        ChangeNameResponse.Builder response = ChangeNameResponse.newBuilder();
        response.setNewName(newName);
        response.setChangeNameTimes(player.getChangeNameNum());
        return response.build();
    }

    @InternalCmd
    public void forbid(PlayerForbidMessage message) {
        Player player = playerService.getEntity(message.getPlayerId());

        player.setForbidEndTime(LocalDateTimeUtil.ofEpochMilli(message.getForbidEndTime()));
        playerService.update(player);

        if (LocalDateTimeUtil.now().isBefore(player.getForbidEndTime())) {
            PlayerContext playerContext = playerContextManager.getContext(player.getId());
            if (playerContext != null) {
                LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
                logoutMessage.playerContext = playerContext;
                logoutMessage.logoutType = LogoutType.FORBID;
                logoutMessage.forbidEndTime = player.getForbidEndTime();
                playerContext.tell(logoutMessage);
            }
        } else {
            //解封时玩家必定不在线，这里不需要做消息通知
        }
    }
    
    @InternalCmd
    public void forbidList(PlayerForbidListMessage message) {
    	PlayerContext playerContext = playerContextManager.getContext(message.getPlayerId());
        if (playerContext != null) {
            LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
            logoutMessage.playerContext = playerContext;
            logoutMessage.logoutType = LogoutType.FORBID;
            logoutMessage.forbidEndTime = LocalDateTime.of(2099, 12, 31, 23, 59);
            playerContext.tell(logoutMessage);
        }
    }
    

    @PlayerCmd
    public PlayerModuleInfoResponse info(PlayerContext playerContext, PlayerModuleInfoRequest request) {
        AbstractModuleHandler handler = AbstractModuleHandler.getHandler(request.getModule());
        handler.pushInfo(playerContext);

        return PlayerModuleInfoResponse.getDefaultInstance();
    }
    
    @InternalCmd
    public void dailyReset(PlayerDailyResetMessage message) {
    	playerService.getEntity(message.getPlayerId());
    	
    	//玩家每日数据重置
    	AbstractModuleHandler.dailyReset(message.getPlayerId());
    	//推送玩家数据
        AbstractModuleHandler.dailyPush(message.getPlayerContext());
        logger.info("跨天处理推送玩家{}消息", message.getPlayerId());
        //
//        playerContextManager.push(message.getPid(), PlayerPushEndMessage.Proto.ID, PlayerPushEndMessage.getDefaultInstance());
    }
    
    @PlayerCmd
    public PlayerMoreInfoResponse moreInfo(PlayerContext playerContext, PlayerMoreInfoRequest request) {
    	//推送玩家数据
        AbstractModuleHandler.pushInfo(playerContext, InfoPriority.DETAIL);
        playerContextManager.push(playerContext.getId(), PlayerMoreInfoEndMessage.Proto.ID, PlayerMoreInfoEndMessage.getDefaultInstance());
        
        return PlayerMoreInfoResponse.getDefaultInstance();
    }
    
    @PlayerCmd
    public PlayerWiretapResponse wiretap(PlayerContext playerContext, PlayerWiretapRequest request) {
    	PlayerContext otherContext = playerContextManager.getContext(request.getPlayerId());
    	if(otherContext != null) {
    		otherContext.setWiretap(playerContext);
    	}
    	return PlayerWiretapResponse.getDefaultInstance();
    }
}
