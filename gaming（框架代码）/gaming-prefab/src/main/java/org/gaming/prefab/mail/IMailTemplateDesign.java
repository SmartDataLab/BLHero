/**
 * 
 */
package org.gaming.prefab.mail;

import java.util.List;

/**
 * @author YY
 *
 */
public interface IMailTemplateDesign {

	int getTemplateId();
	
	List<String> getTitleFormatRules();
	List<String> getContentFormatRules();
	
	int getExpireDay();
}
