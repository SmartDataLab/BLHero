package com.xiugou.x1.design.module;


import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.autogen.LanguageTipsAbstractCache;

@Component
public class LanguageTipsCache extends LanguageTipsAbstractCache<LanguageTipsAbstractCache.LanguageTipsCfg> {
	
	private static LanguageTipsCache INS = null;
	
	public static final LanguageTipsCache ins() {
		if(INS == null) {
			INS = org.gaming.ruler.spring.Spring.getBean(LanguageTipsCache.class);
		}
		return INS;
	}
	
	public String getText(String language, String langId) {
		LanguageTipsCfg tipsCfg = this.findInLangIdIndex(langId);
		if(tipsCfg == null) {
			return langId;
		}
		if(language.equals("en")) {
			return tipsCfg.getEn();
		}
		return tipsCfg.getCn();
	}
}