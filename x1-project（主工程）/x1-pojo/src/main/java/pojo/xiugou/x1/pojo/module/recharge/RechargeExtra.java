/**
 * 
 */
package pojo.xiugou.x1.pojo.module.recharge;

import java.util.HashMap;
import java.util.Map;

import pojo.xiugou.x1.pojo.apiutil.ApiUtil;

/**
 * @author YY
 *
 */
public class RechargeExtra {
	private int productId;
	private String orderId;
	private long money;
	private String openId;
	private long playerId;
	private String sign;
	private int level;
	
	public static RechargeExtra decode(String extraStr) {
		RechargeExtra extra = new RechargeExtra();
		
		String[] parts = extraStr.split("#");
		for(String part : parts) {
			if("".equals(part)) {
				continue;
			}
			String[] subParts = part.split("=");
			String fieldName = subParts[0];
			String fieldValue = subParts[1];
			if("productId".equals(fieldName)) {
				extra.productId = Integer.parseInt(fieldValue);
				
			} else if("orderId".equals(fieldName)) {
				extra.orderId = fieldValue;
				
			} else if("money".equals(fieldName)) {
				extra.money = Long.parseLong(fieldValue);
				
			} else if("openId".equals(fieldName)) {
				extra.openId = fieldValue;
				
			} else if("playerId".equals(fieldName)) {
				extra.playerId = Long.parseLong(fieldValue);
				
			} else if("sign".equals(fieldName)) {
				extra.sign = fieldValue;
				
			} else if("level".equals(fieldName)) {
				extra.level = Integer.parseInt(fieldValue);
			}
		}
		return extra;
	}
	
	//37那边的透传参数不能超过225长度，真尼玛服了
	public String encode() {
		String str = "";
		str += "productId=" + productId + "#";
		str += "orderId=" + orderId + "#";
		str += "money=" + money + "#";
		str += "openId=" + openId + "#";
		str += "playerId=" + playerId + "#";
		str += "sign=" + sign + "#";
		str += "level=" + level;
		return str;
	}
	
	public String buildSign(String signKey) {
		Map<String, Object> params = new HashMap<>();
		params.put("productId", String.valueOf(this.productId));
		params.put("orderId", String.valueOf(this.orderId));
		params.put("money", String.valueOf(this.money));
		params.put("openId", String.valueOf(this.openId));
		params.put("playerId", String.valueOf(this.playerId));
        String source = ApiUtil.jointValueToSource(params, signKey);
        return ApiUtil.buildSign(source);
	}
	
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
