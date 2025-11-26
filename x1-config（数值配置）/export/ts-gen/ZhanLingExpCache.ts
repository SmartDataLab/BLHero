
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
export default class ZhanLingExpCache extends DesignCache<CFG_ZhanLingExp> {
	private static instance: ZhanLingExpCache = null;
	public static get Instance(): ZhanLingExpCache {
		if(ZhanLingExpCache.instance === null) {
			ZhanLingExpCache.instance = new ZhanLingExpCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ZhanLingExpCache.instance.fileName);
			ZhanLingExpCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ZhanLingExpCache.instance;
	}
	public get fileName(): string {
		return "P1001经验类战令奖励_ZhanLingExp";
	}
	protected createInstance(): CFG_ZhanLingExp {
		return new CFG_ZhanLingExp();
	}

	protected activityIdLevelIndex: Map<number, Map<number, CFG_ZhanLingExp>> = null;

	protected activityIdCollector: Map<number, CFG_ZhanLingExp[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引activityIdLevelIndex
		let activityIdLevelIndex: Map<number, Map<number, CFG_ZhanLingExp>> = new Map<number, Map<number, CFG_ZhanLingExp>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhanLingExp = this.all()[i];
			let layer1Map: Map<number, CFG_ZhanLingExp> = activityIdLevelIndex.get(data.getActivityId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_ZhanLingExp>();
				activityIdLevelIndex.set(data.getActivityId(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.activityIdLevelIndex = activityIdLevelIndex;
		//构建收集器activityIdCollector
		let activityIdCollector: Map<number, CFG_ZhanLingExp[]> = new Map<number, CFG_ZhanLingExp[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhanLingExp = this.all()[i];
			let collector: CFG_ZhanLingExp[] = activityIdCollector.get(data.getActivityId());
			if(collector === undefined) {
				collector = [];
				activityIdCollector.set(data.getActivityId(), collector);
			}
			collector.push(data);
		}
		this.activityIdCollector = activityIdCollector;
	}

	public getInActivityIdLevelIndex(activityId: number, level: number): CFG_ZhanLingExp {
		let layer1Map: Map<number, CFG_ZhanLingExp> = this.activityIdLevelIndex.get(activityId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("ZhanLingExpCache.getInActivityIdLevelIndex", activityId, level);
		}
		let t: CFG_ZhanLingExp = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("ZhanLingExpCache.getInActivityIdLevelIndex", activityId, level);
		}
		return t;
	}

	public findInActivityIdLevelIndex(activityId: number, level: number): CFG_ZhanLingExp {
		let layer1Map: Map<number, CFG_ZhanLingExp> = this.activityIdLevelIndex.get(activityId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_ZhanLingExp = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInActivityIdCollector(activityId: number) : CFG_ZhanLingExp[] {
		let ts: CFG_ZhanLingExp[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ZhanLingExpCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public findInActivityIdCollector(activityId: number) : CFG_ZhanLingExp[] {
		let ts: CFG_ZhanLingExp[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ZhanLingExp implements IDesignData {
	//序号
	protected id: number = 0;
	//活动类型ID
	protected activityId: number = 0;
	//等级
	protected level: number = 0;
	//升至本级所需经验
	protected needExp: number = 0;
	//免费奖励
	protected freeReward: Reward[] = [];
	//高级奖励
	protected premiumReward: Reward[] = [];
	//购买本等级消耗
	protected buyLevelCost: Cost = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getActivityId(): number {
		return this.activityId;
	}
	public getLevel(): number {
		return this.level;
	}
	public getNeedExp(): number {
		return this.needExp;
	}
	public getFreeReward(): Reward[] {
		return this.freeReward;
	}
	public getPremiumReward(): Reward[] {
		return this.premiumReward;
	}
	public getBuyLevelCost(): Cost {
		return this.buyLevelCost;
	}
	private formatFreeReward(): Reward {
		return new Reward();
	}
	private formatPremiumReward(): Reward {
		return new Reward();
	}
	private formatBuyLevelCost(): Cost {
		return new Cost();
	}
}