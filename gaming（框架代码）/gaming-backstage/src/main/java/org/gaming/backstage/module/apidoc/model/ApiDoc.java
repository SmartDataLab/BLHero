/**
 * 
 */
package org.gaming.backstage.module.apidoc.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "api_doc", comment = "接口说明", dbAlias = "backstage")
public class ApiDoc extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "href_url", comment = "链接地址")
	private String hrefUrl;
	@Column(comment = "接口说明")
	private String comment;
	@Column(name = "param_form", comment = "参数格式", length = 2000)
	private String paramForm;
	@Column(name = "return_form", comment = "返回格式", length = 2000)
	private String returnForm;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHrefUrl() {
		return hrefUrl;
	}
	public void setHrefUrl(String hrefUrl) {
		this.hrefUrl = hrefUrl;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getParamForm() {
		return paramForm;
	}
	public void setParamForm(String paramForm) {
		this.paramForm = paramForm;
	}
	public String getReturnForm() {
		return returnForm;
	}
	public void setReturnForm(String returnForm) {
		this.returnForm = returnForm;
	}
}
