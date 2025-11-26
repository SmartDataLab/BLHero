/**
 * 
 */
package com.xiugou.x1.backstage.module.system.service;

import org.gaming.backstage.service.AbstractService;
import org.gaming.tool.DateTimeUtil;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.system.model.SystemCounter;

/**
 * @author YY
 *
 */
@Service
public class SystemCounterService extends AbstractService<SystemCounter> {

	private SystemCounter instance;
	public SystemCounter instance() {
		if(instance == null) {
			synchronized (this) {
				instance = this.getById(1L);
				if(instance == null) {
					instance = new SystemCounter();
					instance.setId(1L);
					instance.setRemainTime(DateTimeUtil.currMillis());
					this.insert(instance);
				}
			}
		}
		return instance;
	}
}
