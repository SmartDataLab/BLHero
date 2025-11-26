/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.struct;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.backstage.module.mail.model.MailSystem;

/**
 * @author YY
 *
 */
public class MailCheck {
	private List<MailSystem> mails = new ArrayList<>();

	public List<MailSystem> getMails() {
		return mails;
	}

	public void setMails(List<MailSystem> mails) {
		this.mails = mails;
	}
}
