/**
 * 
 */
package pojo.xiugou.x1.pojo.log.server;

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
@Table(name = "server_resume_log", comment = "游戏汇总日志表", dbAlias = "log", indexs = {
		@Index(name = "date_str", columns = { "date_str" }, type = IndexType.UNIQUE) })
public class ServerResumeLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "日志ID")
	private long id;
	@Column(name = "date_str", comment = "日期")
	private String dateStr;
	@Column(name = "player_num", comment = "总玩家数")
	private long playerNum;
	@Column(name = "new_player_num", comment = "新玩家数")
	private long newPlayerNum;
	@Column(name = "login_player_num", comment = "登录玩家数")
	private long loginPlayerNum;
	@Column(name = "login_num", comment = "登录次数")
	private long loginNum;
	@Column(name = "old_login_num", comment = "去新登录玩家数")
	private long oldLoginNum;
	@Column(name = "recharge_num", comment = "充值金额")
	private long rechargeNum;
	@Column(name = "recharge_player_num", comment = "充值人数")
	private long rechargePlayerNum;
	@Column(name = "recharge_for_diamond", comment = "直购钻石金额")
	private long rechargeForDiamond;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public long getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(long playerNum) {
		this.playerNum = playerNum;
	}
	public long getNewPlayerNum() {
		return newPlayerNum;
	}
	public void setNewPlayerNum(long newPlayerNum) {
		this.newPlayerNum = newPlayerNum;
	}
	public long getLoginPlayerNum() {
		return loginPlayerNum;
	}
	public void setLoginPlayerNum(long loginPlayerNum) {
		this.loginPlayerNum = loginPlayerNum;
	}
	public long getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(long loginNum) {
		this.loginNum = loginNum;
	}
	public long getOldLoginNum() {
		return oldLoginNum;
	}
	public void setOldLoginNum(long oldLoginNum) {
		this.oldLoginNum = oldLoginNum;
	}
	public long getRechargeNum() {
		return rechargeNum;
	}
	public void setRechargeNum(long rechargeNum) {
		this.rechargeNum = rechargeNum;
	}
	public long getRechargePlayerNum() {
		return rechargePlayerNum;
	}
	public void setRechargePlayerNum(long rechargePlayerNum) {
		this.rechargePlayerNum = rechargePlayerNum;
	}
	public long getRechargeForDiamond() {
		return rechargeForDiamond;
	}
	public void setRechargeForDiamond(long rechargeForDiamond) {
		this.rechargeForDiamond = rechargeForDiamond;
	}
}
