package com.xiugou.x1.design.module;


import com.xiugou.x1.design.module.autogen.LanguageAutoAbstractCache;

@org.springframework.stereotype.Component
public class LanguageAutoCache extends LanguageAutoAbstractCache<LanguageAutoAbstractCache.LanguageAutoCfg> {
	
	public String getLang(String langType, String langKey) {
		LanguageAutoCfg languageAutoCfg = this.findInLangIdIndex(langKey);
		if(languageAutoCfg == null) {
			return "";
		}
		if("en".equals(langType)) {
			return this.getInLangIdIndex(langKey).getEn();
		} else {
			return this.getInLangIdIndex(langKey).getCn();
		}
	}
}