/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class BuffCureResult implements ICureResult {
	private Sprite docter;
	private Sprite patient;
	private long cure;
	public Sprite getDocter() {
		return docter;
	}
	public void setDocter(Sprite docter) {
		this.docter = docter;
	}
	public Sprite getPatient() {
		return patient;
	}
	public void setPatient(Sprite patient) {
		this.patient = patient;
	}
	public long getCure() {
		return cure;
	}
	public void setCure(long cure) {
		this.cure = cure;
	}
}
