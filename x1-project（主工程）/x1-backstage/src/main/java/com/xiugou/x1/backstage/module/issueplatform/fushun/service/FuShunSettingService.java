/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.fushun.service;

import org.gaming.backstage.service.SystemOneToManyService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.issueplatform.fushun.model.FuShunSetting;

/**
 * @author YY
 *
 */
@Service
public class FuShunSettingService extends SystemOneToManyService<FuShunSetting> implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public void start() throws Exception {
		if(!applicationSettings.isServerBackstageMain()) {
			return;
		}
		FuShunSetting setting = this.getById(1L);
		if(setting != null) {
			return;
		}
		setting = new FuShunSetting();
		setting.setId(1L);
		this.insert(setting);
	}
	
	public FuShunSetting getSetting() {
		return this.getEntity(1L);
	}
}
