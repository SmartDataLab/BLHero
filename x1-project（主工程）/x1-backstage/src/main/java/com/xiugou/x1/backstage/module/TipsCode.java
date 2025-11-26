/**
 * 
 */
package com.xiugou.x1.backstage.module;

import org.gaming.backstage.advice.IResultTips;

/**
 * @author YY
 *
 */
public enum TipsCode implements IResultTips {

	POST_NO_RESPONSE(100001, "通过%s请求无法得到游戏服的响应"),
	POST_FAIL(100002, "请求%s未能返回成功结果"),
	ERROR_PARAM(100003, "参数错误"),
	WHAT_DO_YOU_WANT_TO_DO(100004, "(╯‵□′)╯︵┻━┻"),
	
	GAME_SERVER_MISS(200001, "未找到UID为%s的服务器信息"),
	SERVER_ID_NOT_ZERO(200002, "服务器ID不能为0"),
	CHANNEL_MISS(200003, "未找到ID为%s的渠道信息"),
	REGION_MISS(200004, "未找到渠道%s下的大区%s信息"),
	CHANNEL_SERVER_MISS(200005, "渠道%s服务器%s关系未找到"),
	CHANNEL_NOT_CHOOSE(200006, "还没有选择渠道"),
	SERVER_OPENED(200007, "该服务器已经开服，不能再修改预期开服时间"),
	SERVER_OPENTIME_NEED_FUTURE(200008, "开服时间必须是未来的时间"),
	REGION_EXIST(200009, "渠道%s下的大区%s信息已经存在，不能重复添加"),
	CHANNEL_GAMESERVER_MISS(200010, "渠道%s下未找到服务器UID%s"),
	BULLETIN_MISS(200011, "未找到ID为%s的公告信息"),
	BULLETIN_USING_IN_CHANNEL(200012, "该公告还在渠道[%s]下使用，请先修改引用关系"),
	PLATFORM_MISS(200013, "未找到ID为%s的平台信息"),
	PLATFORM_SERVER_REPEAT(200014, "该平台下已经有ID为%s的服务器"),
	CLIENT_VERSION_MISS(200015, "未找到ID为%s的客户端版本信息数据"),
	CLIENT_VERSION_DELETE_CURR(200016, "不能删除正在对外使用的版本信息"),
	CLIENT_VERSION_TYPE_ERROR(200017, "版本类型参数错误"),
	CLIENT_VERSION_CHANNEL_MISS(200018, "未找到渠道%s下版本编号为%s的版本信息"),
	SERVER_REGION_TYPE_UNMATCH(200019, "服务器与大区的类型不一致"),
	
	MAIL_MISS(300001, "未找到ID为%s的系统邮件"),
	MAIL_HAS_CHECK(300002, "系统邮件%s已经审核，无法修改"),
	MAIL_EDIT_NEED_CREATOR(300003, "系统邮件只能由创建人修改"),
	MAIL_NOT_CHECK_PASS(300004, "该系统邮件还没有审核通过"),
	MAIL_RESEND_NEED_CHECKER(300005, "只有审核员才能对该系统邮件进行重发"),
	MAIL_DELETE_PLAYER_ERROR(300006, "删除玩家邮件出现异常"),
	MAIL_ITEM_MISS(300007, "邮件附件未找到道具%s，请更新道具配置"),
	MAIL_DELETE_AFTER_PASS(300008, "审核通过的系统邮件才有必要通知删除"),
	MAIL_SERVER_EMPTY(300009, "服务器列表不能为空"),
	
	PLAYER_NOT_EXIST(400001, "未找到玩家%s"),
	PLAYER_NOT_EXIST_IN_CHANNEL(400002, "未找到渠道%s下的玩家%s"),
	PLAYER_FORBID_FAIL(400003, "玩家封号失败"),
	PLAYER_FORCED_OFFLINE_FAIL(400004, "玩家强制下线失败"),
	PLAYER_NOT_IN_CHANNEL(400005, "该玩家不在你的授权渠道中"),
	RECHARGE_FAIL(400006, "充值回调处理失败"),
	
	GOD_FINGER_PLAYER_WRONG(500001, "修改金手指玩家数据未匹配"),
	GOD_FINGER_BIGGER_ZERO(500002, "金手指金额必须大于0"),
	GOD_FINGER_NOT_EXIST(500003, "未找到ID为%s的金手指"),
	
	FIX_DESIGN_MISS(600001, "未找到ID为%s的热更配制记录"),
	FIX_DESIGN_SERVER_WRONG(600002, "选中的服务器不在当前渠道下"),
	
	CONSUME_TYPE_NOT_FOUND(700001, "未找到对应的资源日志"),
	;
	private final int code;
	private final String message;
	private TipsCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
