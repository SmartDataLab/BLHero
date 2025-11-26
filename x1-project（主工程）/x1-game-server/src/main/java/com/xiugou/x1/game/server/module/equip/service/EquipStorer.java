package com.xiugou.x1.game.server.module.equip.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.InstanceThingStorer;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.equip.EquipLog;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Component
public class EquipStorer extends InstanceThingStorer<EquipLog, EquipReceipt> {

    @Autowired
    private EquipService equipService;
    @Autowired
    private MailService mailService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BattleConstCache battleConstCache;

    @Override
    protected BaseRepository<EquipLog> initRepository() {
        return SlimDao.getRepository(EquipLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.EQUIP;
    }

    @Override
    protected EquipReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        List<Equip> equipList = equipService.getEntities(entityId);

        List<Equip> insertList = new ArrayList<>();
        List<RewardThing> mailRewards = new ArrayList<>();

        int addNum = 0;
        for (IRewardThing reward : rewardThings) {
            for (int i = 0; i < reward.getNum(); i++) {
                if (equipList.size() + addNum >= 200) {
                    mailRewards.add(RewardThing.of(reward.getThingId(), 1));
                } else {
                    addNum += 1;
                    insertList.add(equipService.create(entityId, reward.getThingId()));
                }
            }
        }
        equipService.insert(insertList);

        if (!mailRewards.isEmpty()) {
            //超上限的装备发邮件
            mailRewards = ThingUtil.mergeReward(mailRewards);
            Mail mail = mailService.newMail(entityId, MailTemplate.EQUIP_LIMIT, mailRewards, GameCause.EQUIP_BAG_LIMIT);
            mailService.sendMail(mail, NoticeType.NORMAL);
        }

        EquipReceipt receipt = new EquipReceipt(insertList);
        for (Equip equip : insertList) {
            receipt.append(new RewardDetail(equip.getIdentity(), 1, equipService.build(equip).toByteString()));
        }
        return receipt;
    }

    @Override
    protected void afterAdd(long entityId, EquipReceipt receipt, IGameCause cause, String remark) {
        List<EquipLog> equipLogs = builEquipLog(entityId, receipt.getEquips(), cause, remark);
        this.repository().insertAll(equipLogs);
    }

    private List<EquipLog> builEquipLog(long entityId, List<Equip> equips, IGameCause cause, String remark) {
    	String ownerName = this.getOwnerName(entityId);
    	List<EquipLog> logs = new ArrayList<>();
        for (Equip equip : equips) {
            EquipLog log = new EquipLog();
            log.setOwnerId(entityId);
            log.setOwnerName(ownerName);
            log.setCurr(1);
            log.setDelta(1);
            log.setGameCause(cause);
            log.setThingId(equip.getIdentity());
            log.setThingName(equip.getName());
            log.setRemark(remark);
            log.setInstanceId(equip.getId());
            log.setInstanceData(GsonUtil.toJson(equip));
            logs.add(log);
        }
        return logs;
    }

    @Override
    protected boolean tryAdd(long entityId, List<? extends IRewardThing> rewardThings) {
        List<Equip> equipList = equipService.getEntities(entityId);
        int sum = 0;
        for (IRewardThing rewardThing : rewardThings) {
            sum += rewardThing.getNum();
        }
        // 200常量
        return equipList.size() + sum <= battleConstCache.getEquip_capacity_limit();
    }
    
    @Override
	protected String getOwnerName(long entityId) {
		return playerService.getEntity(entityId).getNick();
	}
}
