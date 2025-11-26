/**
 * 
 */
package com.xiugou.x1.backstage.module.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.item.model.ItemCfg;
import com.xiugou.x1.backstage.module.item.service.ItemCfgService;
import com.xiugou.x1.backstage.module.mail.form.MailCheckForm;
import com.xiugou.x1.backstage.module.mail.form.MailSystemForm;
import com.xiugou.x1.backstage.module.mail.model.MailSystem;
import com.xiugou.x1.backstage.module.mail.model.MailSystemResult;
import com.xiugou.x1.backstage.module.mail.service.MailSystemResultService;
import com.xiugou.x1.backstage.module.mail.service.MailSystemService;
import com.xiugou.x1.backstage.module.mail.struct.MailCheck;
import com.xiugou.x1.backstage.module.mail.vo.MailSystemVo;

import pojo.xiugou.x1.pojo.module.mail.MailAttachment;

/**
 * @author YY
 *
 */
@Controller
public class MailSystemController {
	
	@Autowired
	private MailSystemService mailSystemService;
	@Autowired
	private MailSystemResultService mailSystemResultService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemCfgService itemCfgService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求系统邮件数据")
	@RequestMapping(value = "/mail/data.auth")
	@ResponseBody
	public PageData<MailSystemVo> mailDatas(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		PageData<MailSystem> oriPageData = mailSystemService.query(querySet);
		
		PageData<MailSystemVo> pageData = new PageData<>();
		pageData.setCount(oriPageData.getCount());
		for(MailSystem mailSystem : oriPageData.getData()) {
			pageData.getData().add(buildVo(mailSystem));
		}
		return pageData;
	}
	
	public MailSystemVo buildVo(MailSystem mail) {
		MailSystemVo vo = new MailSystemVo();
		vo.setId(mail.getId());
		vo.setUserId(mail.getUserId());
		vo.setUserName(mail.getUserName());
		vo.setTitle(mail.getTitle());
		vo.setContent(mail.getContent());
		vo.setRewards(mail.getRewards());
		vo.setPids(mail.getPids());
		vo.setType(mail.getType());
		vo.setPlayerLevel(mail.getPlayerLevel());
		vo.setStatus(mail.getStatus());
		vo.setCheckUserId(mail.getCheckUserId());
		vo.setCheckUserName(mail.getCheckUserName());
		vo.setServerUids(new ArrayList<>(mail.getServerUids()));
		return vo;
	}
	
	@ApiDocument("保存系统邮件数据")
	@RequestMapping(value = "/mail/save.authw")
	@ResponseBody
	public MailSystem saveMail(MailSystemForm form) {
		System.out.println(GsonUtil.toJson(form));
		RoleContext roleContext = userService.getCurrUser();
		MailSystem mail = null;
		
		Asserts.isTrue(!form.getServerUids().isEmpty(), TipsCode.MAIL_SERVER_EMPTY);
		if(form.getId() <= 0) {
			mail = new MailSystem();
			mail.setUserId(roleContext.getId());
			mail.setUserName(roleContext.getName());
			
			formatMail(mail, form);
			mailSystemService.insert(mail);
		} else {
			mail = mailSystemService.getById(form.getId());
			Asserts.isTrue(mail != null, TipsCode.MAIL_MISS, form.getId());
			Asserts.isTrue(mail.getStatus() == 0, TipsCode.MAIL_HAS_CHECK, form.getId());
			Asserts.isTrue(roleContext.getId() == mail.getUserId(), TipsCode.MAIL_EDIT_NEED_CREATOR);
			
			formatMail(mail, form);
			mailSystemService.update(mail);
		}
		return mail;
	}
	
	private void formatMail(MailSystem mail, MailSystemForm form) {
		long currChannel = gameChannelService.currChannel();
		Map<Integer, GameServer> gameServerMap = ListMapUtil.listToMap(gameChannelService.getServers(), GameServer::getId);
		
		mail.setTitle(form.getTitle());
		mail.setContent(form.getContent());
		if(form.getRewards() != null) {
			List<ItemCfg> itemCfgs = itemCfgService.getAll();
			Map<Integer, ItemCfg> itemMap = ListMapUtil.listToMap(itemCfgs, ItemCfg::getId);
			
			mail.getRewards().clear();
			List<MailAttachment> list = GsonUtil.getList(form.getRewards(), MailAttachment.class);
			for(MailAttachment attachment : list) {
				ItemCfg itemCfg = itemMap.get(attachment.getItem());
				Asserts.isTrue(itemCfg != null, TipsCode.MAIL_ITEM_MISS, attachment.getItem());
				attachment.setName(itemCfg.getName());
			}
			mail.getRewards().addAll(list);
		}
		mail.setType(form.getType());
		if(form.getServerUids() != null) {
			mail.getServerUids().clear();
			for(int serverUid : form.getServerUids()) {
				GameServer gameServer = gameServerMap.get(serverUid);
				Asserts.isTrue(gameServer != null, TipsCode.CHANNEL_GAMESERVER_MISS, currChannel, serverUid);
				mail.getServerUids().add(serverUid);
			}
		}
		if(form.getPids() != null) {
			mail.setPids(form.getPids());
		}
		mail.setPlayerLevel(form.getPlayerLevel());
	}
	
	@ApiDocument("系统邮件审核通过")
	@RequestMapping(value = "/mail/checkPass.authw")
	@ResponseBody
	public MailCheck checkPass(MailCheckForm form) {
		RoleContext roleContext = userService.getCurrUser();
		
		List<MailSystem> mails = new ArrayList<>();
		for(long id : form.getIds()) {
			MailSystem mail = mailSystemService.getById(id);
			Asserts.isTrue(mail != null, TipsCode.MAIL_MISS, id);
			Asserts.isTrue(mail.getStatus() == 0, TipsCode.MAIL_HAS_CHECK, id);
			mails.add(mail);
		}
		for(MailSystem mail : mails) {
			mail.setStatus(1);
			mail.setCheckUserId(roleContext.getId());
			mail.setCheckUserName(roleContext.getName());
			//发送邮件到服务器
			mailSystemService.addToSending(mail);
		}
		mailSystemService.updateAll(mails);
		
		MailCheck mailCheck = new MailCheck();
		mailCheck.setMails(mails);
		return mailCheck;
	}
	
	@ApiDocument("系统邮件审核不通过")
	@RequestMapping(value = "/mail/checkNotPass.authw")
	@ResponseBody
	public MailCheck checkNotPass(MailCheckForm form) {
		RoleContext roleContext = userService.getCurrUser();
		
		List<MailSystem> mails = new ArrayList<>();
		for(long id : form.getIds()) {
			MailSystem mail = mailSystemService.getById(id);
			Asserts.isTrue(mail != null, TipsCode.MAIL_MISS, id);
			Asserts.isTrue(mail.getStatus() == 0, TipsCode.MAIL_HAS_CHECK, id);
			mails.add(mail);
		}
		for(MailSystem mail : mails) {
			mail.setStatus(2);
			mail.setCheckUserId(roleContext.getId());
			mail.setCheckUserName(roleContext.getName());
		}
		mailSystemService.updateAll(mails);
		
		MailCheck mailCheck = new MailCheck();
		mailCheck.setMails(mails);
		return mailCheck;
	}
	
	@ApiDocument("重新发送系统邮件")
	@RequestMapping(value = "/mail/resend.auth")
	@ResponseBody
	public void resend(@RequestParam("id") long id) {
		RoleContext roleContext = userService.getCurrUser();
		
		MailSystem mail = mailSystemService.getById(id);
		Asserts.isTrue(mail != null, TipsCode.MAIL_MISS, id);
		Asserts.isTrue(mail.getStatus() == 1, TipsCode.MAIL_NOT_CHECK_PASS, id);
		Asserts.isTrue(mail.getCheckUserId() == roleContext.getId(), TipsCode.MAIL_RESEND_NEED_CHECKER);
		
		//发送邮件到服务器
		mailSystemService.addToSending(mail);
	}
	
	@ApiDocument("通知游戏服删除系统邮件")
	@RequestMapping(value = "/mail/delete.authw")
	@ResponseBody
	public void deleteMail(@RequestParam("id") long id) {
		RoleContext roleContext = userService.getCurrUser();
		
		MailSystem mail = mailSystemService.getById(id);
		Asserts.isTrue(mail != null, TipsCode.MAIL_MISS, id);
		Asserts.isTrue(mail.getCheckUserId() == roleContext.getId(), TipsCode.MAIL_RESEND_NEED_CHECKER);
		Asserts.isTrue(mail.getStatus() == 1, TipsCode.MAIL_DELETE_AFTER_PASS);
		
		mail.setStatus(3);
		mailSystemService.update(mail);
		mailSystemService.addToSending(mail);
	}
	
	@ApiDocument("请求系统邮件发送结果数据")
	@RequestMapping(value = "/mail/mailResult.auth")
	@ResponseBody
	public PageData<MailSystemResult> mailResult(@RequestParam("id") long id) {
		if(id == 0) {
			return new PageData<>();
		}
		QuerySet querySet = new QuerySet();
		querySet.addCondition("mail_id = ?", id);
		querySet.orderBy("order by server_id");
		querySet.formWhere();
		return mailSystemResultService.query(querySet);
	}
}
