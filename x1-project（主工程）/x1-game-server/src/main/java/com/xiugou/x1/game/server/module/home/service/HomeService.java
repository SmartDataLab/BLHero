/**
 *
 */
package com.xiugou.x1.game.server.module.home.service;

import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.HomeProducePoolCache;
import com.xiugou.x1.design.module.autogen.HomeProducePoolAbstractCache.HomeProducePoolCfg;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.home.model.Home;
import com.xiugou.x1.game.server.module.home.struct.BuildingData;
import com.xiugou.x1.game.server.module.home.struct.HomeProducer;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;

import pb.xiugou.x1.protobuf.bag.Home.PbHomeBuilding;
import pb.xiugou.x1.protobuf.bag.Home.PbHomeProducer;
/**
 * @author YY
 */
@Service
public class HomeService extends PlayerOneToOneService<Home> {

	@Autowired
    private HomeProducePoolCache homeProducePoolCache;
	
    @Override
    protected Home createWhenNull(long entityId) {
        Home home = new Home();
        home.setPid(entityId);
        return home;
    }

    public void calculateOutput(HomeProducer homeProducer) {
    	HomeProducePoolCfg homeProducePoolCfg = homeProducePoolCache.getInBuildingIdLevelIndex(homeProducer.getId(), homeProducer.getLevel());
    	if(homeProducer.getProduce() >= homeProducePoolCfg.getMaxStore()) {
    		return;
    	}
    	long nowTime = DateTimeUtil.currMillis();
        long produceGap = homeProducePoolCfg.getProduceGap() * DateTimeUtil.ONE_SECOND_MILLIS;
        //产出份数
        long startTime = homeProducer.getStartTime();
        long outputTimes = (nowTime - startTime) / produceGap;
        long total = homeProducer.getProduce() + outputTimes * homeProducePoolCfg.getProduce();
        //取不超过最大存储量的数量
        homeProducer.setProduce(Math.min(homeProducePoolCfg.getMaxStore(), total));
        
        if(homeProducer.getProduce() >= homeProducePoolCfg.getMaxStore()) {
        	homeProducer.setStartTime(nowTime);
        } else {
        	homeProducer.setStartTime(startTime + outputTimes * produceGap);
        }
    }

    public PbHomeBuilding build(BuildingData buildingData) {
        PbHomeBuilding.Builder builder = PbHomeBuilding.newBuilder();
        builder.setId(buildingData.getId());
        builder.setActive(buildingData.isActive());
        builder.setOpening(PbHelper.build(buildingData.getOpening()));
        return builder.build();
    }
    
    public PbHomeProducer build(HomeProducer producer) {
        PbHomeProducer.Builder builder = PbHomeProducer.newBuilder();
        builder.setId(producer.getId());
        builder.setLevel(producer.getLevel());
        builder.setStartTime(producer.getStartTime());
        builder.setProduce(producer.getProduce());
        return builder.build();
    }
}
