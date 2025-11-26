/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.yile.service;

import org.gaming.backstage.service.SystemOneToManyService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.issueplatform.yile.model.YiLeSetting;

/**
 * @author YY
 *
 */
@Service
public class YiLeSettingService extends SystemOneToManyService<YiLeSetting> implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public void start() throws Exception {
		if(!applicationSettings.isServerBackstageMain()) {
			return;
		}
		YiLeSetting yiLeSetting = this.getById(1L);
		if(yiLeSetting != null) {
			return;
		}
		yiLeSetting = new YiLeSetting();
		yiLeSetting.setId(1L);
		this.insert(yiLeSetting);
	}
	
	public YiLeSetting getSetting() {
		return this.getEntity(1L);
	}
}
