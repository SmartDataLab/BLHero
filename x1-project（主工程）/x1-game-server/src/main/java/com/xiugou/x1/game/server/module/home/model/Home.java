/**
 *
 */
package com.xiugou.x1.game.server.module.home.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.home.constant.BuildingType;
import com.xiugou.x1.game.server.module.home.struct.BuildingData;
import com.xiugou.x1.game.server.module.home.struct.HomeProducer;

/**
 * @author YY
 * 家园
 */
@Repository
@JvmCache
@Table(name = "home", comment = "玩家场景数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Home extends AbstractEntity {
    @Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "store_lv", comment = "仓库等级，从0开始")
    private int storeLv;
    @Column(comment = "肉")
    private long meat;
    @Column(comment = "木")
    private long wood;
    @Column(comment = "矿")
    private long mine;
    @Column(comment = "资源产出池（产出池类型，数据）", length = 300)
    private Map<Integer, HomeProducer> producers = new HashMap<>();
    @Column(name = "building_opening", comment = "建筑激活交付数据", extra = "text")
    private Map<Integer, BuildingData> buildingOpening = new HashMap<>();

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getStoreLv() {
        return storeLv;
    }

    public void setStoreLv(int storeLv) {
        this.storeLv = storeLv;
    }

    public Map<Integer, HomeProducer> getProducers() {
        return producers;
    }

    public void setProducers(Map<Integer, HomeProducer> producers) {
        this.producers = producers;
    }

    public long getMeat() {
        return meat;
    }

    public void setMeat(long meat) {
        this.meat = meat;
    }

    public long getWood() {
        return wood;
    }

    public void setWood(long wood) {
        this.wood = wood;
    }

    public long getMine() {
        return mine;
    }

    public void setMine(long mine) {
        this.mine = mine;
    }

    public Map<Integer, BuildingData> getBuildingOpening() {
        return buildingOpening;
    }

    public void setBuildingOpening(Map<Integer, BuildingData> buildingOpening) {
        this.buildingOpening = buildingOpening;
    }

    public boolean isBuildingOpen(BuildingType buildingType) {
    	BuildingData building = buildingOpening.get(buildingType.getBuildingId());
    	if(building == null) {
    		return false;
    	}
    	return building.isActive();
    }
}
