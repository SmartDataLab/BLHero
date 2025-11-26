
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
export default class RankRewardCache extends DesignCache<CFG_RankReward> {
	private static instance: RankRewardCache = null;
	public static get Instance(): RankRewardCache {
		if(RankRewardCache.instance === null) {
			RankRewardCache.instance = new RankRewardCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + RankRewardCache.instance.fileName);
			RankRewardCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return RankRewardCache.instance;
	}
	public get fileName(): string {
		return "P排行榜奖励表_RankReward";
	}
	protected createInstance(): CFG_RankReward {
		return new CFG_RankReward();
	}

	protected typeRewardIdIndex: Map<number, Map<number, CFG_RankReward>> = null;

	protected typeCollector: Map<number, CFG_RankReward[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引typeRewardIdIndex
		let typeRewardIdIndex: Map<number, Map<number, CFG_RankReward>> = new Map<number, Map<number, CFG_RankReward>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_RankReward = this.all()[i];
			let layer1Map: Map<number, CFG_RankReward> = typeRewardIdIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_RankReward>();
				typeRewardIdIndex.set(data.getType(), layer1Map);
			}
			layer1Map.set(data.getRewardId(), data);
		}
		this.typeRewardIdIndex = typeRewardIdIndex;
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_RankReward[]> = new Map<number, CFG_RankReward[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_RankReward = this.all()[i];
			let collector: CFG_RankReward[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}

	public getInTypeRewardIdIndex(type: number, rewardId: number): CFG_RankReward {
		let layer1Map: Map<number, CFG_RankReward> = this.typeRewardIdIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("RankRewardCache.getInTypeRewardIdIndex", type, rewardId);
		}
		let t: CFG_RankReward = layer1Map.get(rewardId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("RankRewardCache.getInTypeRewardIdIndex", type, rewardId);
		}
		return t;
	}

	public findInTypeRewardIdIndex(type: number, rewardId: number): CFG_RankReward {
		let layer1Map: Map<number, CFG_RankReward> = this.typeRewardIdIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_RankReward = layer1Map.get(rewardId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInTypeCollector(type: number) : CFG_RankReward[] {
		let ts: CFG_RankReward[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("RankRewardCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_RankReward[] {
		let ts: CFG_RankReward[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_RankReward implements IDesignData {
	//序号
	protected id: number = 0;
	//类型
	protected type: number = 0;
	//奖励ID
	protected rewardId: number = 0;
	//达到条件
	protected condition: number = 0;
	//奖励
	protected reward: Reward[] = [];
	//条件描述
	protected tex: string = "";
	//条件描述
	protected texLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getRewardId(): number {
		return this.rewardId;
	}
	public getCondition(): number {
		return this.condition;
	}
	public getReward(): Reward[] {
		return this.reward;
	}
	public getTex(): string {
		return this.tex;
	}
	public getTexLang(): string {
		return this.texLang;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}