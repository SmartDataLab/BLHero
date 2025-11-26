
import App from "../../App";
import DesignCache from "../../config/base/DesignCache";
import DesignDataNotFoundError from "../../config/base/DesignDataNotFoundError";
import { X1SeparatorDesignParser } from "../../config/X1SeparatorDesignParser";
import { Reward } from "../../config/struct/Reward";
import { Keyv } from "../../config/struct/Keyv";
import { Cost } from "../../config/struct/Cost";
import { RandomItem } from "../../config/struct/RandomItem";
import { BattAttr } from "../../config/struct/BattAttr";
import { BattBuff } from "../../config/struct/BattBuff";
//当前类代码由导表工具生成，请勿修改
export default class HomeProducePoolCache extends DesignCache<CFG_HomeProducePool> {
	private static instance: HomeProducePoolCache = null;
	public static get Instance(): HomeProducePoolCache {
		if(HomeProducePoolCache.instance === null) {
			HomeProducePoolCache.instance = new HomeProducePoolCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HomeProducePoolCache.instance.fileName);
			HomeProducePoolCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HomeProducePoolCache.instance;
	}
	public get fileName(): string {
		return "J家园产出池等级表_HomeProducePool";
	}
	protected createInstance(): CFG_HomeProducePool {
		return new CFG_HomeProducePool();
	}

	protected buildingIdLevelIndex: Map<number, Map<number, CFG_HomeProducePool>> = null;


	protected loadAutoGenerate(): void {
		//构建索引buildingIdLevelIndex
		let buildingIdLevelIndex: Map<number, Map<number, CFG_HomeProducePool>> = new Map<number, Map<number, CFG_HomeProducePool>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HomeProducePool = this.all()[i];
			let layer1Map: Map<number, CFG_HomeProducePool> = buildingIdLevelIndex.get(data.getBuildingId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_HomeProducePool>();
				buildingIdLevelIndex.set(data.getBuildingId(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.buildingIdLevelIndex = buildingIdLevelIndex;
	}

	public getInBuildingIdLevelIndex(buildingId: number, level: number): CFG_HomeProducePool {
		let layer1Map: Map<number, CFG_HomeProducePool> = this.buildingIdLevelIndex.get(buildingId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("HomeProducePoolCache.getInBuildingIdLevelIndex", buildingId, level);
		}
		let t: CFG_HomeProducePool = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("HomeProducePoolCache.getInBuildingIdLevelIndex", buildingId, level);
		}
		return t;
	}

	public findInBuildingIdLevelIndex(buildingId: number, level: number): CFG_HomeProducePool {
		let layer1Map: Map<number, CFG_HomeProducePool> = this.buildingIdLevelIndex.get(buildingId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_HomeProducePool = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_HomeProducePool implements IDesignData {
	//序号
	protected id: number = 0;
	//建筑物ID
	protected buildingId: number = 0;
	//资源池等级
	protected level: number = 0;
	//升级消耗
	protected upCost: Cost = null;
	//最大容量
	protected maxStore: number = 0;
	//产出间隔秒
	protected produceGap: number = 0;
	//产出的道具ID
	protected produceType: number = 0;
	//每个时间间隔产出的数量
	protected produce: number = 0;
	//界面文本
	protected uiDescLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getBuildingId(): number {
		return this.buildingId;
	}
	public getLevel(): number {
		return this.level;
	}
	public getUpCost(): Cost {
		return this.upCost;
	}
	public getMaxStore(): number {
		return this.maxStore;
	}
	public getProduceGap(): number {
		return this.produceGap;
	}
	public getProduceType(): number {
		return this.produceType;
	}
	public getProduce(): number {
		return this.produce;
	}
	public getUiDescLang(): string {
		return this.uiDescLang;
	}
	private formatUpCost(): Cost {
		return new Cost();
	}
}