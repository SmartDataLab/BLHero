/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class GuideDotDataResultVo {
	private int guideId;
	private String guideName;
	private int step;
	private int num;
	private int loseNum;
	private String loseRate;
	public int getGuideId() {
		return guideId;
	}
	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getLoseRate() {
		return loseRate;
	}
	public void setLoseRate(String loseRate) {
		this.loseRate = loseRate;
	}
	public String getGuideName() {
		return guideName;
	}
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	public int getLoseNum() {
		return loseNum;
	}
	public void setLoseNum(int loseNum) {
		this.loseNum = loseNum;
	}
}
