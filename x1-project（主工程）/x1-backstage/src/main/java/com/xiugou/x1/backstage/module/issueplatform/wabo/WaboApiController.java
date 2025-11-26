/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.wabo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaming.backstage.advice.ResponseResult;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.issueplatform.wabo.model.WaboSetting;
import com.xiugou.x1.backstage.module.issueplatform.wabo.service.WaboSettingService;
import com.xiugou.x1.backstage.module.issueplatform.wabo.struct.WaboRechargeData;
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
public class WaboApiController {

	private static Logger logger = LoggerFactory.getLogger(WaboApiController.class);
	
	@Autowired
	private WaboSettingService waboSettingService;
	@Autowired
	private RechargeCallbackService rechargeCallbackService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RechargeProductCfgService rechargeProductCfgService;
	
	/**
	 * http://127.0.0.1:9001/api/wabo/recharge?gameId=?&timestamp=?&sign=?
	 * @param request
	 * @param response
	 * @param gameId
	 * @param timestamp
	 * @param sign
	 * @param rechargeData
	 * @return
	 */
	@ApiDocument("提供给wabo的充值回调接口")
	@RequestMapping(value = "/api/wabo/recharge")
	@ResponseBody
	public String recharge(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "gameId") String gameId, @RequestParam(name = "timestamp") String timestamp,
			@RequestParam(name = "sign") String sign, @RequestBody WaboRechargeData rechargeData) {
		WaboSetting waboSetting = waboSettingService.getSetting();
		
		logger.info("recharge.gameId: {}", gameId);
		logger.info("recharge.timestamp: {}", timestamp);
		logger.info("recharge.sign: {}", sign);
		logger.info("recharge.rechargeData: {}", GsonUtil.toJson(rechargeData));
		
		Map<String, Object> map = new HashMap<>();
		map.put("gameId", gameId);
		map.put("timestamp", timestamp);
		String signSource = ApiUtil.jointKeyValueToSource(map, waboSetting.getGameKey(), "=", "&");
		String localSign = ApiUtil.buildSign(signSource);
		logger.info("wabo充值回调签名原文：{}", signSource);
		logger.info("wabo充值回调签名：{}", sign);
		logger.info("wabo充值回调本地签名：{}", localSign);
		
		RechargeCallback recharge = new RechargeCallback();
		try {
			rechargeData.setTimestamp(timestamp);
			recharge.setCallbackData(GsonUtil.toJson(rechargeData));
			
			BigDecimal bigPayMoney = new BigDecimal(rechargeData.getItemDoublePrice()).multiply(new BigDecimal("100"));
			long payMoney = bigPayMoney.longValue();
			
			String sdkOrderId = rechargeData.getGameOrderId();
			String gameOrderId = rechargeData.getCpOrderId();
			
			String decodeExt = rechargeData.getExtraInfo();
			try {
				decodeExt = URLDecoder.decode(rechargeData.getExtraInfo(), "UTF-8");
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
	        if(localExtraSign.equals(rechargeExtra.getSign())) {
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
			response.setStatus(TipsCode.RECHARGE_FAIL.getCode());
			return "fail";
		} finally {
			rechargeCallbackService.insert(recharge);
		}
		RechargeStatus rechargeStatus = rechargeCallbackService.handleCallback(recharge);
		if(rechargeStatus == RechargeStatus.SUCCESS || rechargeStatus == RechargeStatus.REPEATED) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "success";
		} else {
			response.setStatus(TipsCode.RECHARGE_FAIL.getCode());
			return "fail";
		}
	}
	
	/**
	 * 测试：http://127.0.0.1:9001/api/wabo/checkLogin?token=AAAA
	 * @param uid
	 * @param token
	 * @return
	 */
	@ApiDocument("提供给游戏客户端验证益乐登录数据的接口")
	@RequestMapping(value = "/api/wabo/checkLogin")
	@ResponseBody
	public ResponseResult checkLogin(@RequestParam("token") String token) {
		WaboSetting waboSetting = waboSettingService.getSetting();

		Map<String, Object> map = new HashMap<>();
		map.put("gameId", waboSetting.getGameId());
		map.put("token", token);
		map.put("timestamp", DateTimeUtil.currMillis() + "");
		String signSource = ApiUtil.jointKeyValueToSource(map, waboSetting.getGameKey(), "=", "&");
		String sign = ApiUtil.buildSign(signSource);
		map.put("sign", sign);
		
		logger.info("wabo签名原文：{}", signSource);
		logger.info("wabo登录签名：{}", sign);
		String response = HttpUtil.get(waboSetting.getLoginUrl(), map);
		logger.info("wabo登录验证相应：{}", response);
		
		WaboLoginResponse loginResponse = GsonUtil.parseJson(response, WaboLoginResponse.class);
		
		if(loginResponse.state.code == 0) {
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
	
	public static class WaboLoginResponse {
		public WaboLoginState state;
		public WaboLoginData data;
	}
	
	public static class WaboLoginState {
		public int code;
		public String msg;
	}
	
	public static class WaboLoginData {
		public String gameId;
		public String gameAccountId;
		public String system;
		public String pn;
		public String deviceId;
		public String loginType;
		public String uniqe;//这是什么？
		public String loginTime;
		public String expiredTime;
	}
	
	public static void main(String[] args) {
		Map<String, Object> parameter1 = new HashMap<>();
		parameter1.put("token", "AAAAA");
		System.out.println(HttpUtil.get("http://127.0.0.1:9001/api/wabo/checkLogin", parameter1));
		
		Map<String, Object> parameter2 = new HashMap<>();
		parameter2.put("ev", "C1");
		parameter2.put("cpOrderId", "D1");
		parameter2.put("itemDoublePrice", "9.66");
		parameter2.put("extraInfo", "{}");
		
		String rechargeUrl = "http://127.0.0.1:9001/api/wabo/recharge?gameId=A1&timestamp=178915134&sign=B1";
		String response = HttpUtil.formPost(rechargeUrl, parameter2);
		System.out.println(response);
	}
}
