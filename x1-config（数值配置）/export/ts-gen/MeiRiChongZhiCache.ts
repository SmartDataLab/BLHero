
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
export default class MeiRiChongZhiCache extends DesignCache<CFG_MeiRiChongZhi> {
	private static instance: MeiRiChongZhiCache = null;
	public static get Instance(): MeiRiChongZhiCache {
		if(MeiRiChongZhiCache.instance === null) {
			MeiRiChongZhiCache.instance = new MeiRiChongZhiCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MeiRiChongZhiCache.instance.fileName);
			MeiRiChongZhiCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MeiRiChongZhiCache.instance;
	}
	public get fileName(): string {
		return "P1011每日充值_MeiRiChongZhi";
	}
	protected createInstance(): CFG_MeiRiChongZhi {
		return new CFG_MeiRiChongZhi();
	}

	protected activityIdRewardIdIndex: Map<number, Map<number, CFG_MeiRiChongZhi>> = null;

	protected activityIdCollector: Map<number, CFG_MeiRiChongZhi[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引activityIdRewardIdIndex
		let activityIdRewardIdIndex: Map<number, Map<number, CFG_MeiRiChongZhi>> = new Map<number, Map<number, CFG_MeiRiChongZhi>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_MeiRiChongZhi = this.all()[i];
			let layer1Map: Map<number, CFG_MeiRiChongZhi> = activityIdRewardIdIndex.get(data.getActivityId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_MeiRiChongZhi>();
				activityIdRewardIdIndex.set(data.getActivityId(), layer1Map);
			}
			layer1Map.set(data.getRewardId(), data);
		}
		this.activityIdRewardIdIndex = activityIdRewardIdIndex;
		//构建收集器activityIdCollector
		let activityIdCollector: Map<number, CFG_MeiRiChongZhi[]> = new Map<number, CFG_MeiRiChongZhi[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_MeiRiChongZhi = this.all()[i];
			let collector: CFG_MeiRiChongZhi[] = activityIdCollector.get(data.getActivityId());
			if(collector === undefined) {
				collector = [];
				activityIdCollector.set(data.getActivityId(), collector);
			}
			collector.push(data);
		}
		this.activityIdCollector = activityIdCollector;
	}

	public getInActivityIdRewardIdIndex(activityId: number, rewardId: number): CFG_MeiRiChongZhi {
		let layer1Map: Map<number, CFG_MeiRiChongZhi> = this.activityIdRewardIdIndex.get(activityId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("MeiRiChongZhiCache.getInActivityIdRewardIdIndex", activityId, rewardId);
		}
		let t: CFG_MeiRiChongZhi = layer1Map.get(rewardId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("MeiRiChongZhiCache.getInActivityIdRewardIdIndex", activityId, rewardId);
		}
		return t;
	}

	public findInActivityIdRewardIdIndex(activityId: number, rewardId: number): CFG_MeiRiChongZhi {
		let layer1Map: Map<number, CFG_MeiRiChongZhi> = this.activityIdRewardIdIndex.get(activityId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_MeiRiChongZhi = layer1Map.get(rewardId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInActivityIdCollector(activityId: number) : CFG_MeiRiChongZhi[] {
		let ts: CFG_MeiRiChongZhi[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("MeiRiChongZhiCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public findInActivityIdCollector(activityId: number) : CFG_MeiRiChongZhi[] {
		let ts: CFG_MeiRiChongZhi[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_MeiRiChongZhi implements IDesignData {
	//序号
	protected id: number = 0;
	//活动ID
	protected activityId: number = 0;
	//奖励ID
	protected rewardId: number = 0;
	//充值数量
	protected targetNum: number = 0;
	//自选奖励
	protected selectRewards: Reward[] = [];
	//固定奖励
	protected rewards: Reward[] = [];
	//描述
	protected descLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getActivityId(): number {
		return this.activityId;
	}
	public getRewardId(): number {
		return this.rewardId;
	}
	public getTargetNum(): number {
		return this.targetNum;
	}
	public getSelectRewards(): Reward[] {
		return this.selectRewards;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	public getDescLang(): string {
		return this.descLang;
	}
	private formatSelectRewards(): Reward {
		return new Reward();
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}