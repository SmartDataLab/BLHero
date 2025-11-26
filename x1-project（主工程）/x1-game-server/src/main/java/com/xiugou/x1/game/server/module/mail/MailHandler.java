package com.xiugou.x1.game.server.module.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.mail.message.SystemMailMessage;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.mail.service.MailSystemService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.mail.Mail.MailDeleteBatchRequest;
import pb.xiugou.x1.protobuf.mail.Mail.MailDeleteBatchResponse;
import pb.xiugou.x1.protobuf.mail.Mail.MailDeleteOneRequest;
import pb.xiugou.x1.protobuf.mail.Mail.MailDeleteOneResponse;
import pb.xiugou.x1.protobuf.mail.Mail.MailInfoResponse;
import pb.xiugou.x1.protobuf.mail.Mail.MailReadRequest;
import pb.xiugou.x1.protobuf.mail.Mail.MailReadResponse;
import pb.xiugou.x1.protobuf.mail.Mail.MailReceiveBatchRequest;
import pb.xiugou.x1.protobuf.mail.Mail.MailReceiveBatchResponse;
import pb.xiugou.x1.protobuf.mail.Mail.MailReceiveOneRequest;
import pb.xiugou.x1.protobuf.mail.Mail.MailReceiveOneResponse;

/**
 * @author yh
 * @date 2023/6/1
 * @apiNote
 */
@Controller
public class MailHandler extends AbstractModuleHandler {

    @Autowired
    private MailService mailService;
    @Autowired
    private ThingService thingService;
    @Autowired
    private MailSystemService mailSystemService;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
	public boolean needDailyPush() {
		return false;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        MailInfoResponse.Builder response = MailInfoResponse.newBuilder();
        List<Mail> mails = mailService.mails(playerId);
        for (Mail mail : mails) {
            response.addMails(mailService.build(mail));
        }
        playerContextManager.push(playerId, MailInfoResponse.Proto.ID, response.build());
    }

    /**
     * 领取附件奖励
     */
    @PlayerCmd
    public MailReceiveOneResponse receiveOne(PlayerContext playerContext, MailReceiveOneRequest request) {
        Mail mail = mailService.getMail(playerContext.getId(), request.getId());
        Asserts.isTrue(mail != null, TipsCode.MAIL_NOT_EXIST, request.getId());
        Asserts.isTrue(!"".equals(mail.getAttachment()), TipsCode.MAIL_NO_ATTACHMENT);
        Asserts.isTrue(!mail.isReceive(), TipsCode.MAIL_RECEIVES);
        List<RewardThing> rewards = GsonUtil.getList(mail.getAttachment(), RewardThing.class);
        
        boolean flag = thingService.tryAdd(playerContext.getId(), rewards, GameCause.MAIL_TAKE_REWARD);
        Asserts.isTrue(flag, TipsCode.MAIL_CAPACITY_LIMIT);
        
        thingService.add(playerContext.getId(), rewards, GameCause.MAIL_TAKE_REWARD, NoticeType.TIPS,
                mail.getGameCause() + ":" + mail.getGameCauseText());
        
        mail.markRead();
        mail.markReceive();
        mailService.update(mail);
        
        MailReceiveOneResponse.Builder response = MailReceiveOneResponse.newBuilder();
        response.setId(mail.getId());
        response.setReceive(mail.isReceive());
        return response.build();
    }
    
    @PlayerCmd
    public MailReceiveBatchResponse receiveBatch(PlayerContext playerContext, MailReceiveBatchRequest request) {
        List<Mail> mails = mailService.mails(playerContext.getId());
        List<Mail> collect = mails.stream().filter(m -> !"".equals(m.getAttachment())).filter(m -> !m.isReceive()).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return MailReceiveBatchResponse.getDefaultInstance();
        }
        
        RewardReceipt rewardReceipt = new RewardReceipt();
        
        boolean hasFull = false;
        MailReceiveBatchResponse.Builder response = MailReceiveBatchResponse.newBuilder();
        for (Mail mail : collect) {
            List<RewardThing> list = GsonUtil.getList(mail.getAttachment(), RewardThing.class);
            if (!thingService.tryAdd(playerContext.getId(), list, GameCause.MAIL_TAKE_REWARD)) {
            	hasFull = true;
            	System.out.println("hasFull " + hasFull);
                continue;
            }
            mail.setRead(true);
            mail.setReceive(true);
            mailService.update(mail);
            response.addIds(mail.getId());
            
            if(!list.isEmpty()) {
            	RewardReceipt oneReceipt = thingService.add(playerContext.getId(), list, GameCause.MAIL_TAKE_REWARD, NoticeType.SLIENT);
            	rewardReceipt.merge(oneReceipt);
            }
        }
        for (RewardDetail reward : rewardReceipt.getDetails()) {
        	response.addThings(PbHelper.build(reward));
		}
        response.setHasFull(hasFull);
        return response.build();
    }

    /**
     * 读取邮件信息
     */
    @PlayerCmd
    public MailReadResponse read(PlayerContext playerContext, MailReadRequest request) {
        Mail mail = mailService.getMail(playerContext.getId(), request.getId());
        Asserts.isTrue(mail != null, TipsCode.MAIL_NOT_EXIST, request.getId());

        if (!mail.isRead()) {
            mail.markRead();
            mailService.update(mail);
        }

        MailReadResponse.Builder response = MailReadResponse.newBuilder();
        response.setId(mail.getId());
        response.setRead(mail.isRead());
        return response.build();
    }

    @PlayerCmd
    public MailDeleteOneResponse deleteOne(PlayerContext playerContext, MailDeleteOneRequest request) {
        Mail mail = mailService.getMail(playerContext.getId(), request.getId());
        Asserts.isTrue(mail.isRead(), TipsCode.MAIL_UNREAD);
        Asserts.isTrue("".equals(mail.getAttachment()) || mail.isReceive(), TipsCode.MAIL_NO_RECEIVES);
        
        mailService.delete(Collections.singletonList(mail));
        
        MailDeleteOneResponse.Builder response = MailDeleteOneResponse.newBuilder();
        response.setId(mail.getId());
        return response.build();
    }
    
    /**
     * 批量删除邮件
     */
    @PlayerCmd
    public MailDeleteBatchResponse deleteBatch(PlayerContext context, MailDeleteBatchRequest request) {
        List<Mail> mails = mailService.mails(context.getId());
        List<Mail> deteleMails = new ArrayList<>();
        for (Mail mail : mails) {
            if (!mail.isRead()) {
                continue;
            }
            if (!"".equals(mail.getAttachment()) && !mail.isReceive()) {
                continue;
            }
            deteleMails.add(mail);
        }
        mailService.delete(deteleMails);

        MailDeleteBatchResponse.Builder response = MailDeleteBatchResponse.newBuilder();
        for (Mail mail : deteleMails) {
            response.addIds(mail.getId());
        }
        return response.build();
    }
    
    @InternalCmd
    public void checkMail(SystemMailMessage message) {
    	List<Mail> newMails = mailSystemService.pullMailFromSystem(message.getPlayerId());
    	if(newMails.isEmpty()) {
    		return;
    	}
    	mailService.sendMail(newMails, NoticeType.NORMAL);
    }
}