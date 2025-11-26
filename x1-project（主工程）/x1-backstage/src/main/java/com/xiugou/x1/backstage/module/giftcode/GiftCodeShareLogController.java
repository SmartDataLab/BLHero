/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeShareLog;
import com.xiugou.x1.backstage.module.giftcode.service.GiftCodeShareLogService;
import com.xiugou.x1.backstage.module.giftcode.struct.GiftCodeShareLogQuery;

/**
 * @author YY
 *
 */
@Controller
public class GiftCodeShareLogController {

	@Autowired
	private GiftCodeShareLogService giftCodeShareLogService;
	
	@ApiDocument("请求通用礼包码使用日志数据")
	@RequestMapping(value = "/giftCodeShareLog/data.auth")
	@ResponseBody
	public PageData<GiftCodeShareLog> data(GiftCodeShareLogQuery query) {
		QuerySet querySet = new QuerySet();
		if(query.getCode() != null && !"".equals(query.getCode())) {
			querySet.addCondition("code = ?", query.getCode());
		}
		if(query.getPlayerId() != 0) {
			querySet.addCondition("player_id = ?", query.getPlayerId());
		}
		if(query.getPlayerName() != null && !"".equals(query.getPlayerName())) {
			querySet.addCondition("player_name like ?", "%" + query.getPlayerName() + "%");
		}
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		return giftCodeShareLogService.query(querySet);
	}
}
