/**
 * 
 */
package pojo.xiugou.x1.pojo.module.giftcode;

/**
 * @author YY
 *
 */
public class GiftCodeResponse {
	//礼包配置ID
	private int giftConfigId;
	//1兑换码无效，2兑换码已被使用，3已使用过同类兑换码
	private int tipsCode;
	public int getGiftConfigId() {
		return giftConfigId;
	}
	public void setGiftConfigId(int giftConfigId) {
		this.giftConfigId = giftConfigId;
	}
	public int getTipsCode() {
		return tipsCode;
	}
	public void setTipsCode(int tipsCode) {
		this.tipsCode = tipsCode;
	}
}
