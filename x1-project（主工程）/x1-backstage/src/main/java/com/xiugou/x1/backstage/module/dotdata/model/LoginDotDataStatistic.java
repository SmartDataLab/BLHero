/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "dot_data_login_statistic", comment = "登录打点数据统计", dbAlias = "backstage", indexs = {
		@Index(name = "channelid_datastr", columns = { "channel_id", "date_str" }, type = IndexType.UNIQUE) })
public class LoginDotDataStatistic extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "date_str", comment = "日期字符串，yyyyMMdd", readonly = true)
	private String dateStr;

	@Column(name = "req_res_url_begin", comment = "根据版本号请求资源路径")
	private int reqResUrlBegin;
	@Column(name = "req_res_url_sucess", comment = "根据版本号请求资源路径成功")
	private int reqResUrlSucess;
	@Column(name = "req_res_url_fail", comment = "根据版本号请求资源路径成功")
	private int reqResUrlFail;
	@Column(name = "sdk_login_begin", comment = "SDK开始登陆")
	private int sdkLoginBegin;
	@Column(name = "sdk_login_sucess", comment = "SDK的登录成功")
	private int sdkLoginSucess;
	@Column(name = "sdk_login_fail", comment = "SDK的登录失败")
	private int sdkLoginFail;
	@Column(name = "req_server_verify_begin", comment = "请求服务器账号验证")
	private int reqServerVerifyBegin;
	@Column(name = "req_server_verify_sucess", comment = "请求服务器账号验证成功")
	private int reqServerVerifySucess;
	@Column(name = "req_server_verify_fail", comment = "请求服务器账号验证失败")
	private int reqServerVerifyFail;
	@Column(name = "req_server_list_begin", comment = "请求服务器列表")
	private int reqServerListBegin;
	@Column(name = "req_server_list_sucess", comment = "请求服务器列表成功")
	private int reqServerListSucess;
	@Column(name = "req_server_list_fail", comment = "请求服务器列表失败")
	private int reqServerListFail;
	@Column(name = "start_game", comment = "点击开始游戏")
	private int startGame;
	@Column(name = "req_socket_connect_sucess", comment = "向服务器socket连接成功")
	private int reqSocketConnectSucess;
	@Column(name = "req_socket_login_begin", comment = "向服务器发送登陆请求协议")
	private int reqSocketLoginBegin;
	@Column(name = "req_socket_login_sucess", comment = "向服务器发送登陆请求协议成功")
	private int reqSocketLoginSucess;
	@Column(name = "req_socket_login_fail", comment = "向服务器发送登陆请求协议失败")
	private int reqSocketLoginFail;
	@Column(name = "create_succ", comment = "创号成功")
	private int createSucc;
	@Column(name = "req_server_push_data_end", comment = "服务器推送数据结束")
	private int reqServerPushDataEnd;
	@Column(name = "in_game", comment = "进入游戏")
	private int inGame;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public int getReqResUrlBegin() {
		return reqResUrlBegin;
	}
	public void setReqResUrlBegin(int reqResUrlBegin) {
		this.reqResUrlBegin = reqResUrlBegin;
	}
	public int getReqResUrlSucess() {
		return reqResUrlSucess;
	}
	public void setReqResUrlSucess(int reqResUrlSucess) {
		this.reqResUrlSucess = reqResUrlSucess;
	}
	public int getReqResUrlFail() {
		return reqResUrlFail;
	}
	public void setReqResUrlFail(int reqResUrlFail) {
		this.reqResUrlFail = reqResUrlFail;
	}
	public int getSdkLoginBegin() {
		return sdkLoginBegin;
	}
	public void setSdkLoginBegin(int sdkLoginBegin) {
		this.sdkLoginBegin = sdkLoginBegin;
	}
	public int getSdkLoginSucess() {
		return sdkLoginSucess;
	}
	public void setSdkLoginSucess(int sdkLoginSucess) {
		this.sdkLoginSucess = sdkLoginSucess;
	}
	public int getSdkLoginFail() {
		return sdkLoginFail;
	}
	public void setSdkLoginFail(int sdkLoginFail) {
		this.sdkLoginFail = sdkLoginFail;
	}
	public int getReqServerVerifyBegin() {
		return reqServerVerifyBegin;
	}
	public void setReqServerVerifyBegin(int reqServerVerifyBegin) {
		this.reqServerVerifyBegin = reqServerVerifyBegin;
	}
	public int getReqServerVerifySucess() {
		return reqServerVerifySucess;
	}
	public void setReqServerVerifySucess(int reqServerVerifySucess) {
		this.reqServerVerifySucess = reqServerVerifySucess;
	}
	public int getReqServerVerifyFail() {
		return reqServerVerifyFail;
	}
	public void setReqServerVerifyFail(int reqServerVerifyFail) {
		this.reqServerVerifyFail = reqServerVerifyFail;
	}
	public int getReqServerListBegin() {
		return reqServerListBegin;
	}
	public void setReqServerListBegin(int reqServerListBegin) {
		this.reqServerListBegin = reqServerListBegin;
	}
	public int getReqServerListSucess() {
		return reqServerListSucess;
	}
	public void setReqServerListSucess(int reqServerListSucess) {
		this.reqServerListSucess = reqServerListSucess;
	}
	public int getReqServerListFail() {
		return reqServerListFail;
	}
	public void setReqServerListFail(int reqServerListFail) {
		this.reqServerListFail = reqServerListFail;
	}
	public int getStartGame() {
		return startGame;
	}
	public void setStartGame(int startGame) {
		this.startGame = startGame;
	}
	public int getReqSocketConnectSucess() {
		return reqSocketConnectSucess;
	}
	public void setReqSocketConnectSucess(int reqSocketConnectSucess) {
		this.reqSocketConnectSucess = reqSocketConnectSucess;
	}
	public int getReqSocketLoginBegin() {
		return reqSocketLoginBegin;
	}
	public void setReqSocketLoginBegin(int reqSocketLoginBegin) {
		this.reqSocketLoginBegin = reqSocketLoginBegin;
	}
	public int getReqSocketLoginSucess() {
		return reqSocketLoginSucess;
	}
	public void setReqSocketLoginSucess(int reqSocketLoginSucess) {
		this.reqSocketLoginSucess = reqSocketLoginSucess;
	}
	public int getReqSocketLoginFail() {
		return reqSocketLoginFail;
	}
	public void setReqSocketLoginFail(int reqSocketLoginFail) {
		this.reqSocketLoginFail = reqSocketLoginFail;
	}
	public int getCreateSucc() {
		return createSucc;
	}
	public void setCreateSucc(int createSucc) {
		this.createSucc = createSucc;
	}
	public int getReqServerPushDataEnd() {
		return reqServerPushDataEnd;
	}
	public void setReqServerPushDataEnd(int reqServerPushDataEnd) {
		this.reqServerPushDataEnd = reqServerPushDataEnd;
	}
	public int getInGame() {
		return inGame;
	}
	public void setInGame(int inGame) {
		this.inGame = inGame;
	}
}
