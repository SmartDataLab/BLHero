/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.service;

import java.util.List;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCode;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeCfg;

/**
 * @author YY
 *
 */
@Service
public class GiftCodeService extends AbstractService<GiftCode> implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private GiftCodeCfgService giftCodeCfgService;
	
	public boolean updateUnuse(GiftCode giftCode) {
		return this.repository().updateWhen(giftCode, "player_id = 0");
	}

	@Override
	public void start() throws Exception {
		if(!applicationSettings.isServerBackstageMain()) {
			return;
		}
		//修复礼包码的名称
		QuerySet querySet = new QuerySet();
		querySet.addCondition("config_name = ''");
		querySet.formWhere();
		List<GiftCode> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		GiftCodeCfg giftCodeCfg = null;
		for(GiftCode giftCode : list) {
			if(giftCodeCfg != null) {
				if(giftCodeCfg.getId() != giftCode.getConfigId()) {
					giftCodeCfg = giftCodeCfgService.getById(giftCode.getConfigId());
				}
			} else {
				giftCodeCfg = giftCodeCfgService.getById(giftCode.getConfigId());
			}
			giftCode.setConfigName(giftCodeCfg == null ? "" : giftCodeCfg.getName());
		}
		this.updateAll(list);
	}
}
