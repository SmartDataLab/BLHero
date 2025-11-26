/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.yile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gaming.backstage.advice.ResponseResult;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.issueplatform.yile.model.YiLeSetting;
import com.xiugou.x1.backstage.module.issueplatform.yile.service.YiLeSettingService;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.module.recharge.model.RechargeCallback;
import com.xiugou.x1.backstage.module.recharge.service.RechargeCallbackService;
import com.xiugou.x1.backstage.module.recharge.service.RechargeStatus;
import com.xiugou.x1.backstage.module.rechargeproduct.model.RechargeProductCfg;
import com.xiugou.x1.backstage.module.rechargeproduct.service.RechargeProductCfgService;

import pojo.xiugou.x1.pojo.apiutil.ApiUtil;
import pojo.xiugou.x1.pojo.module.recharge.RechargeExtra;

/**
 * @author YY
 *
 */
@Controller
public class YiLeApiController {

	private static Logger logger = LoggerFactory.getLogger(YiLeApiController.class);
	
	@Autowired
	private YiLeSettingService yiLeSettingService;
	@Autowired
	private RechargeCallbackService rechargeCallbackService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RechargeProductCfgService rechargeProductCfgService;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	
	@ApiDocument("提供给益乐的充值回调接口")
	@RequestMapping(value = "/api/yile/recharge")
	@ResponseBody
	public String recharge(HttpServletRequest request, @RequestParam("game_id") String game_id,
			@RequestParam("uid") String uid, @RequestParam("amount") String amount,
			@RequestParam("order_id") String order_id, @RequestParam("cp_order_id") String cp_order_id,
			@RequestParam("role_id") String role_id, @RequestParam("server_id") String server_id,
			@RequestParam("ext") String ext, @RequestParam("time") String time, @RequestParam("sign") String sign) {
		
		YiLeSetting yiLeSetting = yiLeSettingService.getSetting();
		
		Map<String, Object> map = new HashMap<>();
		map.put("game_id", game_id);
		map.put("uid", uid);
		map.put("amount", amount);
		map.put("order_id", order_id);
		map.put("cp_order_id", cp_order_id);
		map.put("role_id", role_id);
		map.put("server_id", server_id);
		map.put("ext", ext);
		map.put("time", time);
		String signSource = ApiUtil.jointKeyValueToSource(map, yiLeSetting.getChargeKey(), "=", "&");
		String localSign = ApiUtil.buildSign(signSource);
		logger.info("益乐充值回调签名原文：{}", signSource);
		logger.info("益乐充值回调签名：{}", sign);
		logger.info("益乐充值回调本地签名：{}", localSign);
		
		RechargeCallback recharge = new RechargeCallback();
		try {
			recharge.setCallbackData(GsonUtil.toJson(map));
			
			BigDecimal bigPayMoney = new BigDecimal(amount).multiply(new BigDecimal("100"));
			long payMoney = bigPayMoney.longValue();
			
			String sdkOrderId = order_id;
			String gameOrderId = cp_order_id;
			
			String decodeExt = ext;
			try {
				decodeExt = URLDecoder.decode(ext, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			//验签扩展参数
			RechargeExtra rechargeExtra = RechargeExtra.decode(decodeExt);
			String localExtraSign = rechargeExtra.buildSign(applicationSettings.getBackstageKey());
			
	        //验签数据
	        recharge.setSign(sign);
	        recharge.setLocalSign(localSign);
	        recharge.setExtraSign(rechargeExtra.getSign());
	        recharge.setLocalExtraSign(localExtraSign);
	        recharge.setAllCheck(true);
	        
	        if(localSign.equals(sign)) {
	        	recharge.setRemark("验签通过");
	        } else {
	        	recharge.setRemark("验签不通过");
	        	recharge.setAllCheck(false);
	        }
	        //益乐的扩展参数只支持128个字符，所以扩展参数的验签，只匹配开头就好，问题不大
	        if(localExtraSign.startsWith(rechargeExtra.getSign())) {
	        	recharge.setRemark("扩展验签通过");
	        } else {
	        	recharge.setRemark("扩展验签不通过");
	        	recharge.setAllCheck(false);
	        }
	        if(payMoney == rechargeExtra.getMoney()) {
	        	recharge.setRemark("金额一致");
	        } else {
	        	recharge.setRemark("金额不一致");
	        	recharge.setAllCheck(false);
	        }
	        
	        //获取请求IP
			String ip = HttpUtil.getRemoteIp(request);
			recharge.setRemoteIp(ip);
			recharge.setSdkOrderId(sdkOrderId);
			recharge.setGameOrderId(gameOrderId);
			
			Player player = playerService.getById(rechargeExtra.getPlayerId());
			recharge.setPlayerId(rechargeExtra.getPlayerId());
			if(player != null) {
				recharge.setNick(player.getNick());
				recharge.setPlatformId(player.getPlatformId());
				recharge.setChannelId(player.getChannelId());
				recharge.setServerId(player.getServerId());
			}
			recharge.setOpenId(rechargeExtra.getOpenId());
			recharge.setLevel(rechargeExtra.getLevel());
			
			recharge.setMoney(payMoney);
			recharge.setProductId(rechargeExtra.getProductId());
			RechargeProductCfg productCfg = rechargeProductCfgService.getEntity(rechargeExtra.getProductId());
			if(productCfg != null) {
				recharge.setProductName(productCfg.getName());
			}
		} catch (Exception e) {
			logger.error("", e);
			return "fail";
		} finally {
			rechargeCallbackService.insert(recharge);
		}
		RechargeStatus rechargeStatus = rechargeCallbackService.handleCallback(recharge);
		if(rechargeStatus == RechargeStatus.SUCCESS || rechargeStatus == RechargeStatus.REPEATED) {
			return "success";
		} else {
			return "fail";
		}
	}
	
	//?uid=?&token=?&loginTime=?
	@ApiDocument("提供给游戏客户端验证益乐登录数据的接口")
	@RequestMapping(value = "/api/yile/checkLogin")
	@ResponseBody
	public ResponseResult checkLogin(@RequestParam("uid") String uid, @RequestParam("token") String token,
			@RequestParam("loginTime") String loginTime) {
		
		YiLeSetting yiLeSetting = yiLeSettingService.getSetting();

		Map<String, Object> map = new HashMap<>();
		map.put("game_id", yiLeSetting.getGameId());
		map.put("uid", uid);
		map.put("token", token);
		map.put("login_time", loginTime);
		map.put("check_time", DateTimeUtil.currSecond() + "");
		String signSource = ApiUtil.jointKeyValueToSource(map, yiLeSetting.getProductKey(), "=", "&");
		String sign = ApiUtil.buildSign(signSource);
		map.put("sign", sign);
		
		logger.info("益乐签名原文：{}", signSource);
		logger.info("益乐登录签名：{}", sign);
		String response = HttpUtil.formPost(yiLeSetting.getLoginUrl(), map);
		logger.info("益乐登录验证相应：{}", response);
		
		YiLeLoginResponse loginResponse = GsonUtil.parseJson(response, YiLeLoginResponse.class);
		
		if(loginResponse.code == 20000) {
			ResponseResult result = new ResponseResult();
			result.setCode(0);
			result.setMessage("SUCCESS");
			return result;
		} else {
			ResponseResult result = new ResponseResult();
			result.setCode(-1);
			result.setMessage("FAIL");
			return result;
		}
	}
	
	public static class YiLeLoginResponse {
		private int code;
	}
	
	
	public static void main(String[] args) {
		
		String ext = "productId%3D4002%23orderId%3DR2024032911342966058870%23money%3D100%23openId%3D45279647%23playerId%3D336215442194433%23sign%3De29b43af1fce0174ce4f23330";
		String decodeExt = ext;
		try {
			decodeExt = URLDecoder.decode(ext, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		RechargeExtra rechargeExtra = RechargeExtra.decode(decodeExt);
		String localExtraSign = rechargeExtra.buildSign("xiugouyouxi2023");
		System.out.println(localExtraSign);
		
//		Map<String, String> map = new HashMap<>();
//		map.put("game_id", "10000");
//		map.put("uid", "10230");
//		map.put("amount", "6");
//		map.put("order_id", "20191122112389692");
//		map.put("cp_order_id", "15734597671058");
//		map.put("role_id", "1");
//		map.put("server_id", "1");
//		map.put("ext", "a=b&c=c");
//		map.put("time", "1574393034");
//		String signSource = ApiUtil.jointKeyValueToSource(map, "yl123456", "=", "&");
//		String localSign = ApiUtil.buildSign(signSource);
//		System.out.println(localSign);
//		System.out.println("ff77a4aad260e92e9e67105e6db12282");
//		map.put("sign", localSign);
//		String response = HttpUtil.formPost("http://127.0.0.1:9001/api/yile/recharge", map);
//		System.out.println(response);
		
//		amount=1&cp_order_id=R2024032816301477735433&ext=productId%3D4002%23orderId%3DR2024032816301477735433%23money%3D100%23openId%3D45279647%23playerId%3D336215442194433%23sign%3D0f9be2f5dd896c9015e7d5a08&game_id=10910&order_id=202403281630140452950&role_id=336215442194433&server_id=1&time=1711614640&uid=45279647&e58leea614f472e294cea85d75a2eb62
		
//		String str;
//		try {
//			str = URLDecoder.decode("productId%3D4002%23orderId%3DR2024032816301477735433%23money%3D100%23openId%3D45279647%23playerId%3D336215442194433%23sign%3D0f9be2f5dd896c9015e7d5a08", "UTF-8");
//			System.out.println(str);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String signSource = "amount=1&cp_order_id=R2024032816301477735433&ext=productId%3D4002%23orderId%3DR2024032816301477735433%23money%3D100%23openId%3D45279647%23playerId%3D336215442194433%23sign%3D0f9be2f5dd896c9015e7d5a08&game_id=10910&order_id=202403281630140452950&role_id=336215442194433&server_id=1&time=1711614640&uid=45279647&e581eea614f472e294cea85d75a2eb62";
//		String sign = MD5Util.getMD5(signSource);
//		System.out.println(sign);
//		益乐充值回调签名：a3732d90a84cbb23e0e8db6442092ebf
//		益乐充值回调本地签名：d5516c470a87d5181d9c0a2cef54bef2
		
		
//		amount=6&cp_order_id=15734597671058&ext=a=b&c=c&game_id=10000&order_id=20191122112
//				389692&role_id=1&server_id=1&time=1574393034&uid=10230&yl123456
	}
}
