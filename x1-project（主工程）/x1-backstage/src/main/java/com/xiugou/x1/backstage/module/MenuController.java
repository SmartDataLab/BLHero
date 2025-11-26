/**
 * 
 */
package com.xiugou.x1.backstage.module;

import org.gaming.backstage.module.menu.annotation.FunctionPage;
import org.gaming.backstage.module.menu.annotation.ModulePage;
import org.gaming.backstage.module.menu.annotation.SystemPage;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.backstage.module.clientlog.ClientLogController;
import com.xiugou.x1.backstage.module.clientversion.ClientVersionController;
import com.xiugou.x1.backstage.module.dotdata.BossDotDataController;
import com.xiugou.x1.backstage.module.dotdata.FogDotDataController;
import com.xiugou.x1.backstage.module.dotdata.GuideDotDataController;
import com.xiugou.x1.backstage.module.dotdata.LoginDotDataController;
import com.xiugou.x1.backstage.module.dotdata.RecruitDotDataController;
import com.xiugou.x1.backstage.module.dotdata.RecruitRefreshDotDataController;
import com.xiugou.x1.backstage.module.dotdata.TaskDotDataController;
import com.xiugou.x1.backstage.module.gamecause.GameCauseController;
import com.xiugou.x1.backstage.module.gamelog.GameLogController;
import com.xiugou.x1.backstage.module.gameserver.BulletinController;
import com.xiugou.x1.backstage.module.gameserver.GameChannelController;
import com.xiugou.x1.backstage.module.gameserver.GameChannelServerController;
import com.xiugou.x1.backstage.module.gameserver.GamePlatformController;
import com.xiugou.x1.backstage.module.gameserver.GameRegionController;
import com.xiugou.x1.backstage.module.gameserver.GameServerController;
import com.xiugou.x1.backstage.module.gameserver.GameServerMaintainController;
import com.xiugou.x1.backstage.module.gameserver.ServerOpenController;
import com.xiugou.x1.backstage.module.gameserver.UserChannelController;
import com.xiugou.x1.backstage.module.giftcode.GiftCodeCfgController;
import com.xiugou.x1.backstage.module.giftcode.GiftCodeController;
import com.xiugou.x1.backstage.module.giftcode.GiftCodeShareLogController;
import com.xiugou.x1.backstage.module.godfinger.GodFingerController;
import com.xiugou.x1.backstage.module.hotfix.FixCodeController;
import com.xiugou.x1.backstage.module.hotfix.FixDesignController;
import com.xiugou.x1.backstage.module.item.ItemCfgController;
import com.xiugou.x1.backstage.module.mail.MailSystemController;
import com.xiugou.x1.backstage.module.mail.PlayerMailController;
import com.xiugou.x1.backstage.module.player.ConsumeController;
import com.xiugou.x1.backstage.module.player.ForbidListController;
import com.xiugou.x1.backstage.module.player.PlayerController;
import com.xiugou.x1.backstage.module.player.PlayerLTVController;
import com.xiugou.x1.backstage.module.player.PlayerOnlineLogController;
import com.xiugou.x1.backstage.module.player.PlayerPayRemainController;
import com.xiugou.x1.backstage.module.player.PlayerRemainController;
import com.xiugou.x1.backstage.module.player.PlayerScatterLogController;
import com.xiugou.x1.backstage.module.player.PlayerTimeLogController;
import com.xiugou.x1.backstage.module.player.ServerResumeController;
import com.xiugou.x1.backstage.module.player.WhiteListController;
import com.xiugou.x1.backstage.module.recharge.RechargeCallbackController;
import com.xiugou.x1.backstage.module.recharge.RechargeProductCountController;
import com.xiugou.x1.backstage.module.recharge.RechargeRankController;
import com.xiugou.x1.backstage.module.recharge.RechargeVirtualController;
import com.xiugou.x1.backstage.module.rechargeproduct.RechargeProductCfgController;

/**
 * @author YY
 *
 */
@SystemPage(id = 2, name = "游戏管理", sort = 2, routeName = "gameSettings", routeComponent = "Layout")
@ModulePage(id = 10001, name = "服务器管理", systemId = 2, routeName = "serverSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10001, name = "平台管理", routeName = "gamePlatform", sort = 1, authClass = GamePlatformController.class)
	@FunctionPage(moduleId = 10001, name = "渠道管理", routeName = "gameChannel", sort = 2, authClass = GameChannelController.class)
	@FunctionPage(moduleId = 10001, name = "服务器管理", routeName = "gameServer", sort = 3, authClass = GameServerController.class)
	@FunctionPage(moduleId = 10001, name = "渠道与服务器关系", routeName = "gameChannelServer", sort = 4, authClass = GameChannelServerController.class)
	@FunctionPage(moduleId = 10001, name = "渠道大区管理", routeName = "gameRegion", sort = 5, authClass = GameRegionController.class)
	@FunctionPage(moduleId = 10001, name = "开服管理", routeName = "serverOpen", sort = 6, authClass = ServerOpenController.class)
	@FunctionPage(moduleId = 10001, name = "渠道权限管理", routeName = "userChannel", sort = 7, authClass = UserChannelController.class)
	@FunctionPage(moduleId = 10001, name = "游戏公告管理", routeName = "bulletin", sort = 8, authClass = BulletinController.class)
	@FunctionPage(moduleId = 10001, name = "服务器维护管理", routeName = "gameServerMaintain", sort = 9, authClass = GameServerMaintainController.class)
	@FunctionPage(moduleId = 10001, name = "客户端版本管理", routeName = "clientVersion", sort = 10, authClass = ClientVersionController.class)
@ModulePage(id = 10002, name = "玩家管理", systemId = 2, routeName = "loginSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10002, name = "玩家信息管理", routeName = "player", sort = 1, authClass = PlayerController.class)
	@FunctionPage(moduleId = 10002, name = "白名单管理", routeName = "whiteList", sort = 2, authClass = WhiteListController.class)
	@FunctionPage(moduleId = 10002, name = "金手指名单", routeName = "godFinger", sort = 3, authClass = GodFingerController.class)
	@FunctionPage(moduleId = 10002, name = "封禁账号", routeName = "forbidList", sort = 4, authClass = ForbidListController.class)
@ModulePage(id = 10003, name = "游戏统计数据", systemId = 2, routeName = "censusSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10003, name = "注册分时", routeName = "playerTimeLog", sort = 1, authClass = PlayerTimeLogController.class)
	@FunctionPage(moduleId = 10003, name = "在线人数", routeName = "playerOnlineLog", sort = 2, authClass = PlayerOnlineLogController.class)
	@FunctionPage(moduleId = 10003, name = "在线时长占比", routeName = "playerScatterLog", sort = 3, authClass = PlayerScatterLogController.class)
	@FunctionPage(moduleId = 10003, name = "玩家留存", routeName = "playerRemain", sort = 4, authClass = PlayerRemainController.class)
	@FunctionPage(moduleId = 10003, name = "付费留存", routeName = "playerPayRemain", sort = 5, authClass = PlayerPayRemainController.class)
	@FunctionPage(moduleId = 10003, name = "付费LTV", routeName = "playerLTV", sort = 6, authClass = PlayerLTVController.class)
	@FunctionPage(moduleId = 10003, name = "游戏汇总", routeName = "serverResume", sort = 7, authClass = ServerResumeController.class)
	@FunctionPage(moduleId = 10003, name = "货币消费统计", routeName = "consume", sort = 8, authClass = ConsumeController.class)
	@FunctionPage(moduleId = 10003, name = "登录数据打点", routeName = "loginDotData", sort = 9, authClass = LoginDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "任务数据打点", routeName = "taskDotData", sort = 10, authClass = TaskDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "引导数据打点", routeName = "guideDotData", sort = 11, authClass = GuideDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "招募数据打点", routeName = "recruitDotData", sort = 11, authClass = RecruitDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "招募刷新数据打点", routeName = "recruitRefreshDotData", sort = 12, authClass = RecruitRefreshDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "BOSS击杀数据打点", routeName = "bossDotData", sort = 13, authClass = BossDotDataController.class)
	@FunctionPage(moduleId = 10003, name = "场景区域解锁数据打点", routeName = "fogDotData", sort = 14, authClass = FogDotDataController.class)
@ModulePage(id = 10004, name = "邮件相关", systemId = 2, routeName = "mailSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10004, name = "系统邮件", routeName = "mailSystem", sort = 1, authClass = MailSystemController.class)
	@FunctionPage(moduleId = 10004, name = "玩家邮件", routeName = "playerMail", sort = 2, authClass = PlayerMailController.class)
@ModulePage(id = 10005, name = "玩家流水查询", systemId = 2, routeName = "logSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10005, name = "钻石日志", routeName = "diamondLogs", sort = 1, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "金币日志", routeName = "goldLogs", sort = 2, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "道具日志", routeName = "itemLogs", sort = 3, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "肉日志", routeName = "meatLogs", sort = 4, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "木日志", routeName = "woodLogs", sort = 5, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "矿日志", routeName = "mineLogs", sort = 6, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "英雄日志", routeName = "heroLogs", sort = 7, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "装备日志", routeName = "equipLogs", sort = 8, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "角色经验日志", routeName = "playerExpLogs", sort = 9, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "英雄战力日志", routeName = "heroFightingLogs", sort = 10, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "已删邮件日志", routeName = "mailLogs", sort = 11, authClass = GameLogController.class)
	@FunctionPage(moduleId = 10005, name = "玩家战力日志", routeName = "playerFightingLogs", sort = 12, authClass = GameLogController.class)
@ModulePage(id = 10006, name = "特殊操作", systemId = 2, routeName = "fixSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10006, name = "热更配置", routeName = "fixDesign", sort = 1, authClass = FixDesignController.class)
	@FunctionPage(moduleId = 10006, name = "热更代码", routeName = "fixCode", sort = 2, authClass = FixCodeController.class)
	@FunctionPage(moduleId = 10006, name = "流水事件", routeName = "gameCause", sort = 3, authClass = GameCauseController.class)
	@FunctionPage(moduleId = 10006, name = "道具配置", routeName = "itemCfg", sort = 4, authClass = ItemCfgController.class)
	@FunctionPage(moduleId = 10006, name = "兑换码礼包配置", routeName = "giftCodeCfg", sort = 5, authClass = GiftCodeCfgController.class)
	@FunctionPage(moduleId = 10006, name = "兑换码生成", routeName = "giftCode", sort = 6, authClass = GiftCodeController.class)
	@FunctionPage(moduleId = 10006, name = "通用兑换码日志", routeName = "giftCodeShareLog", sort = 7, authClass = GiftCodeShareLogController.class)
	@FunctionPage(moduleId = 10006, name = "客户端日志", routeName = "clientLog", sort = 8, authClass = ClientLogController.class)
	@FunctionPage(moduleId = 10006, name = "充值商品配置", routeName = "rechargeProductCfg", sort = 9, authClass = RechargeProductCfgController.class)
@ModulePage(id = 10007, name = "充值相关", systemId = 2, routeName = "rechargeSettings", routeComponent = "layout/secondaryLayout")
	@FunctionPage(moduleId = 10007, name = "充值回调查询", routeName = "recharge", sort = 1, authClass = RechargeCallbackController.class)
	@FunctionPage(moduleId = 10007, name = "充值排名", routeName = "rechargeRank", sort = 2, authClass = RechargeRankController.class)
	@FunctionPage(moduleId = 10007, name = "充值项目统计", routeName = "rechargeProductCount", sort = 3, authClass = RechargeProductCountController.class)
	@FunctionPage(moduleId = 10007, name = "虚拟充值（内部充值）", routeName = "rechargeVirtual", sort = 4, authClass = RechargeVirtualController.class)
//@ModulePage(id = 10008, name = "37互娱相关", systemId = 2, routeName = "sanQiHuYu", routeComponent = "layout/secondaryLayout")
//	@FunctionPage(moduleId = 10008, name = "37互娱渠道参数设置", routeName = "sanQiHuYuSetting", sort = 1, authClass = SanQiHuYuSettingController.class)
//	@FunctionPage(moduleId = 10008, name = "37互娱IP白名单", routeName = "sanQiHuYuWhiteIp", sort = 2, authClass = SanQiHuYuWhiteIpController.class)
//	@FunctionPage(moduleId = 10008, name = "37互娱发放礼包配置", routeName = "sanQiHuYuGiftCfg", sort = 3, authClass = SanQiHuYuGiftCfgController.class)

@Controller
public class MenuController {
	//5.1	角色查询			OK
	//5.2	玩家封禁			OK
	//5.3	注册分时			OK		后OK
	//5.4	在线人数			OK		后OK
	//5.5	在线玩家列表		OK		后OK
	//5.6	在线时长占比		OK		后OK
	//5.7	玩家流水查询		OK		后OK
	//5.8	登陆流失			暂时先不做
	//5.9	用户留存			OK
	//5.10	付费用户留存统计	OK
	//5.11	付费LTV			OK
	//5.12	游戏汇总			OK		后OK
	//5.13	充值排名			OK		后OK
	//5.14	充值记录			OK
	//5.15	充值项目统计		OK		后OK
	//5.16	货币消耗统计		OK		后OK
	//5.17	渠道配置			OK
	//5.18	服务器配置			OK
	//5.19	开服设置			OK
	//5.20	单服邮件			OK		后OK
	//5.21	单服公告			OK
	//5.22	维护白名单			OK		后OK
	//5.23	绿色通道			暂时先不做
	//5.24	金手指名单			OK
	//5.25	账号权限管理		OK
	//5.xx	热更配置			OK		后OK
	//5.xx	热更代码			OK		后OK
	//5.xx	用户与渠道			OK		后OK
	//5.xx	流水事件			OK
	//5.xx	道具配置			OK
	//5.xx	兑换码配置					后OK
	//5.xx	兑换码生成					后OK
	//5.xx	通用兑换码日志				后OK
}
