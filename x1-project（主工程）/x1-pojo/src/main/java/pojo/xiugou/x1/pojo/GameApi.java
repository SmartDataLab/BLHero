/**
 * 
 */
package pojo.xiugou.x1.pojo;

/**
 * @author YY
 *
 */
public interface GameApi {
	
	//玩家上报数据到后台
	static String playerReport = "/api/playerReport";
	//是否白名单
	static String isWhiteList = "/api/isWhiteList";
	//是否可以使用礼包码
	static String canUseCode = "/api/canUseCode";
	//游戏服务器心跳
	static String heartBeat = "/api/heartBeat";
	
	static String dbs = "/api/dbs";
	
	static String sendMailSystem = "/mailApi/sendMailSystem";
	static String deleteMailSystem = "/mailApi/deleteMailSystem";
	static String deletePlayerMail = "/mailApi/deletePlayerMail";
	static String queryPlayerMails = "/mailApi/queryPlayerMails";
	
	static String rechargeCallback = "/rechargeApi/rechargeCallback";
	static String virtualRecharge = "/rechargeApi/virtualRecharge";
	
	//玩家英雄数据
	static String playerDetails = "/playerApi/info";
	//玩家英雄数据
	static String playerDetailsHeroes = "/playerApi/heroes";
	//玩家背包数据
	static String playerDetailsBag = "/playerApi/bag";
	//在线玩家
	static String playerOnlines = "/playerApi/onlines";
	//玩家封号
	static String playerForbid = "/playerApi/forbid";
	//同步账号封禁
	static String playerForbidList = "/playerApi/forbidList";
	//强制踢某个玩家下线
	static String playerForcedOffline = "/playerApi/forcedOffline";
	//玩家强制改名
	static String playerForceChangeName = "/playerApi/forceChangeName";
	
	//立刻开服
	static String serverOpenNow = "/serverApi/openNow";
	//设置预期开服时间
	static String serverSetOpenTime = "/serverApi/setOpenTime";
	//通知服务器维护
	static String serverMaintain = "/serverApi/maintain";
	//测试服务器是否运行中
	static String serverTestRunning = "/serverApi/testRunning";
	//执行命令
	static String serverRunCommand = "/serverApi/runCommand";
	
	//发放金手指奖励
	static String godFingerSendGift = "/godFingerApi/sendGift";
	
	//热更配置
	static String fixDesignUpload = "/fixDesignApi/upload";
	//热更代码
	static String fixCodeUpload = "/fixCodeApi/upload";
	//执行补丁
	static String fixCodePatch = "/fixCodeApi/patch";
	
	//更新流水事件
	static String gameCauseUpdate = "/gameCauseApi/update";
	//更新道具配置数据
	static String itemCfgUpdate = "/itemCfgApi/update";
	//更新充值商品配置数据
	static String rechargeProductCfgUpdate = "/rechargeProductCfgApi/update";
	
	//更新兑换码礼包
	static String giftCodeUpdate = "/giftCodeApi/update";
	
	
	static String sanQiGift = "/sanqihuyuApi/gift";
	static String sanQiWords = "/sanqihuyuApi/getWords";
	
	
	static String loginDot = "/api/loginDot";
	static String loginDotMain = "/api/loginDotMain";
	//创号打点数据上报到后台
	static String loginDotReport = "/api/loginDotReport";
}
