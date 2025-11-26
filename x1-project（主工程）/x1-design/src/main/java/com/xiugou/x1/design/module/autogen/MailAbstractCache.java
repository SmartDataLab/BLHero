package com.xiugou.x1.design.module.autogen;


public abstract class MailAbstractCache<T extends MailAbstractCache.MailCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y邮件表_Mail";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class MailCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 邮件模板ID
		 */
		protected int id;
		/**
		 * 邮件应用场景
		 */
		protected String comment;
		/**
		 * 邮件过期天数
		 */
		protected int ExpirDay;
		/**
		 * 标题后端参数格式
		 */
		protected java.util.List<String> serverTitleFormat;
		/**
		 * 内容后端参数格式
		 */
		protected java.util.List<String> serverContentFormat;
		/**
		 * 标题
		 */
		protected String title;
		/**
		 * 内容
		 */
		protected String content;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getComment() {
			return comment;
		}
		public int getExpirDay() {
			return ExpirDay;
		}
		public java.util.List<String> getServerTitleFormat() {
			return serverTitleFormat;
		}
		public java.util.List<String> getServerContentFormat() {
			return serverContentFormat;
		}
		public String getTitle() {
			return title;
		}
		public String getContent() {
			return content;
		}
	}

}