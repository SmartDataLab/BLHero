package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.tool.RandomUtil;

import com.xiugou.x1.design.module.autogen.NameRandomAbstractCache;

@org.springframework.stereotype.Component
public class NameRandomCache extends NameRandomAbstractCache<NameRandomAbstractCache.NameRandomCfg> {
	
	/**
	 * 生成随机名字
	 * 
	 * @param sex
	 *            性别参数 1或2
	 */
	public String generationRandomName(String language, int sex) {
		List<NameRandomCfg> firstNameList = this.getInSexTypeCollector(sex, 1); // type=1是姓
		NameRandomCfg firstName = firstNameList.get(RandomUtil.closeOpen(0, firstNameList.size()));
		List<NameRandomCfg> secondNameList = this.getInSexTypeCollector(sex, 2); // type=2是名
		NameRandomCfg secondName = secondNameList.get(RandomUtil.closeOpen(0, secondNameList.size()));
		if("en".equals(language)) {
			return firstName.getEn() + secondName.getEn();
		} else {
			return firstName.getName() + secondName.getName();
		}
	}
}