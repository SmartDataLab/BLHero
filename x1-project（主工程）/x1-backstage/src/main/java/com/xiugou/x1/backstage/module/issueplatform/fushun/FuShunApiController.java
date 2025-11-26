/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.fushun;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gaming.backstage.advice.ResponseResult;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.issueplatform.fushun.model.FuShunSetting;
import com.xiugou.x1.backstage.module.issueplatform.fushun.service.FuShunSettingService;
import com.xiugou.x1.backstage.module.issueplatform.fushun.struct.FuShunRechargeData;
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
 * 
 */
@Controller
public class FuShunApiController {

	private static Logger logger = LoggerFactory.getLogger(FuShunApiController.class);

	@Autowired
	private FuShunSettingService fuShunSettingService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RechargeCallbackService rechargeCallbackService;
	@Autowired
	private RechargeProductCfgService rechargeProductCfgService;

	/**
	 * 测试：http://127.0.0.1:9001/api/fushun/login?token=AAAA
	 * 
	 * @param uid
	 * @param token
	 * @return
	 */
	@ApiDocument("提供给游戏客户端验证fushun登录数据的接口")
	@RequestMapping(value = "/api/fushun/login")
	@ResponseBody
	public ResponseResult login(@RequestParam("code") String code) {
		FuShunSetting setting = fuShunSettingService.getSetting();

		Map<String, Object> map = new HashMap<>();
		map.put("game_id", setting.getGameId());
		map.put("code", code);
		String signSource = ApiUtil.jointValueToSource(map, setting.getGameKey());
		String sign = ApiUtil.buildSign(signSource);
		map.put("sign", sign);

		logger.info("fushun签名原文：{}", signSource);
		logger.info("fushun登录签名：{}", sign);
		String response = HttpUtil.formPost(setting.getLoginUrl(), map);
		logger.info("fushun登录验证响应：{}", response);

		FuShunLoginResponse loginResponse = GsonUtil.parseJson(response, FuShunLoginResponse.class);

		ResponseResult result = new ResponseResult();

		if (loginResponse.state != 1) {
			// 失败
			result.setCode(-1);
			result.setMessage("FAIL");
			return result;
		}
		result.setCode(0);
		result.setMessage("SUCCESS");
		result.setData(loginResponse);
		return result;
	}

	public static class FuShunLoginResponse {
		public int state;
		public int uid;
		public String token;
		public String msg;
	}

	public static void main(String[] args) {
		String signSource = "cp_order_num=R2024073118531757437828&ext=productId=15001#orderId=R2024073118531757437828#money=100#openId=124947#playerId=335879998537734#sign=c8f831a5f8672bdd8f6e4185dcd45d5a#level=1&game_id=30128&pt_order_num=TU202407311853189118&role_id=335879998537734&server_id=4&time=1722432457&total_fee=100&uid=124947da371d609efdf1bf9c7e9d8fff364af9";
		String localSign = ApiUtil.buildSign(signSource);
		System.out.println(localSign);
		System.out.println("d11ac6881966b3890a45ee2f04ced7a7");
	}
	
	/**
	 * http://127.0.0.1:9001/api/fushun/recharge
	 */
	@ApiDocument("提供给fushun的充值回调接口")
	@RequestMapping(value = "/api/fushun/recharge")
	@ResponseBody
	public String recharge(HttpServletRequest request,
			@RequestParam(name = "game_id") String game_id, @RequestParam(name = "server_id") String server_id,
			@RequestParam(name = "role_id") String role_id, @RequestParam(name = "cp_order_num") String cp_order_num,
			@RequestParam(name = "pt_order_num") String pt_order_num, @RequestParam(name = "total_fee") String total_fee,
			@RequestParam(name = "uid") String uid, @RequestParam(name = "time") String time,
			@RequestParam(name = "ext") String ext, @RequestParam(name = "sign") String sign) {
		
		FuShunRechargeData rechargeData = new FuShunRechargeData();
		rechargeData.game_id = game_id;
		rechargeData.server_id = server_id;
		rechargeData.role_id = role_id;
		rechargeData.cp_order_num = cp_order_num;
		rechargeData.pt_order_num = pt_order_num;
		rechargeData.total_fee = total_fee;
		rechargeData.uid = uid;
		rechargeData.time = time;
		rechargeData.ext = ext;
		rechargeData.sign = sign;
		logger.info("fushun充值回调请求: {}", GsonUtil.toJson(rechargeData));
		
		FuShunSetting setting = fuShunSettingService.getSetting();
		
//		fushun充值回调请求: {"game_id":"30128","server_id":"4","role_id":"335879998537734","cp_order_num":"R2024073118531757437828","pt_order_num":"TU202407311853189118","total_fee":"100","uid":"124947","time":"1722432457","ext":"productId\u003d15001#orderId\u003dR2024073118531757437828#money\u003d100#openId\u003d124947#playerId\u003d335879998537734#sign\u003dc8f831a5f8672bdd8f6e4185dcd45d5a#level\u003d1","sign":"d11ac6881966b3890a45ee2f04ced7a7"}
//		fushun充值回调签名原文：cp_order_num=R2024073118531757437828&ext=productId=15001#orderId=R2024073118531757437828#money=100#openId=124947#playerId=335879998537734#sign=c8f831a5f8672bdd8f6e4185dcd45d5a#level=1&game_id=30128&pt_order_num=TU202407311853189118&role_id=335879998537734&server_id=4&time=1722432457&total_fee=100&uid=124947&da371d609efdf1bf9c7e9d8fff364af9
//		fushun充值回调签名：d11ac6881966b3890a45ee2f04ced7a7
//		fushun充值回调本地签名：fd10171860ee35589e47544b43166dbc
		

		Map<String, Object> map = new HashMap<>();
		try {
			map.put("game_id", URLEncoder.encode(rechargeData.game_id, "UTF-8"));
			map.put("server_id", URLEncoder.encode(rechargeData.server_id, "UTF-8"));
			map.put("role_id", URLEncoder.encode(rechargeData.role_id, "UTF-8"));
			map.put("cp_order_num", URLEncoder.encode(rechargeData.cp_order_num, "UTF-8"));
			map.put("pt_order_num", URLEncoder.encode(rechargeData.pt_order_num, "UTF-8"));
			map.put("total_fee", URLEncoder.encode(rechargeData.total_fee, "UTF-8"));
			map.put("uid", URLEncoder.encode(rechargeData.uid, "UTF-8"));
			map.put("time", URLEncoder.encode(rechargeData.time, "UTF-8"));
			map.put("ext", URLEncoder.encode(rechargeData.ext, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String signSource = ApiUtil.jointKeyValueToSource(map, "", "=", "&");
		signSource = signSource.substring(0, signSource.length() - 1) + setting.getGameKey();
		String localSign = ApiUtil.buildSign(signSource);
		logger.info("fushun充值回调签名原文：{}", signSource);
		logger.info("fushun充值回调签名：{}", sign);
		logger.info("fushun充值回调本地签名：{}", localSign);

		RechargeCallback recharge = new RechargeCallback();
		try {
			recharge.setCallbackData(GsonUtil.toJson(rechargeData));

			BigDecimal bigPayMoney = new BigDecimal(rechargeData.total_fee);
			long payMoney = bigPayMoney.longValue();

			String sdkOrderId = rechargeData.pt_order_num;
			String gameOrderId = rechargeData.cp_order_num;

			String decodeExt = rechargeData.ext;
			try {
				decodeExt = URLDecoder.decode(rechargeData.ext, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			// 验签扩展参数
			RechargeExtra rechargeExtra = RechargeExtra.decode(decodeExt);
			String localExtraSign = rechargeExtra.buildSign(applicationSettings.getBackstageKey());

			// 验签数据
			recharge.setSign(sign);
			recharge.setLocalSign(localSign);
			recharge.setExtraSign(rechargeExtra.getSign());
			recharge.setLocalExtraSign(localExtraSign);
			recharge.setAllCheck(true);

			if (localSign.equals(sign)) {
				recharge.setRemark("验签通过");
			} else {
				recharge.setRemark("验签不通过");
				recharge.setAllCheck(false);
			}
			if (localExtraSign.equals(rechargeExtra.getSign())) {
				recharge.setRemark("扩展验签通过");
			} else {
				recharge.setRemark("扩展验签不通过");
				recharge.setAllCheck(false);
			}
			if (payMoney == rechargeExtra.getMoney()) {
				recharge.setRemark("金额一致");
			} else {
				recharge.setRemark("金额不一致");
				recharge.setAllCheck(false);
			}

			// 获取请求IP
			String ip = HttpUtil.getRemoteIp(request);
			recharge.setRemoteIp(ip);
			recharge.setSdkOrderId(sdkOrderId);
			recharge.setGameOrderId(gameOrderId);

			Player player = playerService.getById(rechargeExtra.getPlayerId());
			recharge.setPlayerId(rechargeExtra.getPlayerId());
			if (player != null) {
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
			if (productCfg != null) {
				recharge.setProductName(productCfg.getName());
			}
		} catch (Exception e) {
			logger.error("", e);
			RechargeResponse response = new RechargeResponse();
			response.state = TipsCode.RECHARGE_FAIL.getCode();
			response.msg = TipsCode.RECHARGE_FAIL.getMessage();
			return GsonUtil.toJson(response);
		} finally {
			rechargeCallbackService.insert(recharge);
		}
		RechargeStatus rechargeStatus = rechargeCallbackService.handleCallback(recharge);
		if (rechargeStatus == RechargeStatus.SUCCESS || rechargeStatus == RechargeStatus.REPEATED) {
			RechargeResponse response = new RechargeResponse();
			response.state = 1;
			response.msg = "成功";
			return GsonUtil.toJson(response);
		} else {
			RechargeResponse response = new RechargeResponse();
			response.state = TipsCode.RECHARGE_FAIL.getCode();
			response.msg = TipsCode.RECHARGE_FAIL.getMessage();
			return GsonUtil.toJson(response);
		}
	}
	
	public static class RechargeResponse {
		public int state;
		public String msg;
	}
}
