package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.mail.IMailTemplateDesign;

import com.xiugou.x1.design.module.autogen.MailAbstractCache;

@org.springframework.stereotype.Component
public class MailCache extends MailAbstractCache<MailCache.MailConfig> {

	public static class MailConfig extends MailAbstractCache.MailCfg implements IMailTemplateDesign {

		@Override
		public int getTemplateId() {
			return this.getId();
		}

		@Override
		public List<String> getTitleFormatRules() {
			return this.getServerTitleFormat();
		}

		@Override
		public List<String> getContentFormatRules() {
			return this.getServerContentFormat();
		}

		@Override
		public int getExpireDay() {
			return this.ExpirDay;
		}
	}
}