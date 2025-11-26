/**
 * 
 */
package com.xiugou.x1.backstage.module.system.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "system_counter", comment = "系统数据统计时间表", dbAlias = "backstage")
public class SystemCounter extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "remain_time", comment = "留存统计时间")
	private long remainTime;
	@Column(name = "pay_remain_time", comment = "付费留存统计时间")
	private long payRemainTime;
	@Column(name = "ltv_time", comment = "LTV统计时间")
	private long ltvTime;
	@Column(name = "god_finger_time", comment = "金手指发放时间")
	private long godFingerTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}
	public long getLtvTime() {
		return ltvTime;
	}
	public void setLtvTime(long ltvTime) {
		this.ltvTime = ltvTime;
	}
	public long getPayRemainTime() {
		return payRemainTime;
	}
	public void setPayRemainTime(long payRemainTime) {
		this.payRemainTime = payRemainTime;
	}
	public long getGodFingerTime() {
		return godFingerTime;
	}
	public void setGodFingerTime(long godFingerTime) {
		this.godFingerTime = godFingerTime;
	}
}
