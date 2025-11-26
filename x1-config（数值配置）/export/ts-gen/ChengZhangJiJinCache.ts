
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
export default class ChengZhangJiJinCache extends DesignCache<CFG_ChengZhangJiJin> {
	private static instance: ChengZhangJiJinCache = null;
	public static get Instance(): ChengZhangJiJinCache {
		if(ChengZhangJiJinCache.instance === null) {
			ChengZhangJiJinCache.instance = new ChengZhangJiJinCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ChengZhangJiJinCache.instance.fileName);
			ChengZhangJiJinCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ChengZhangJiJinCache.instance;
	}
	public get fileName(): string {
		return "P1006成长基金_ChengZhangJiJin";
	}
	protected createInstance(): CFG_ChengZhangJiJin {
		return new CFG_ChengZhangJiJin();
	}

	protected roundRewardIdIndex: Map<number, Map<number, CFG_ChengZhangJiJin>> = null;

	protected roundCollector: Map<number, CFG_ChengZhangJiJin[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引roundRewardIdIndex
		let roundRewardIdIndex: Map<number, Map<number, CFG_ChengZhangJiJin>> = new Map<number, Map<number, CFG_ChengZhangJiJin>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ChengZhangJiJin = this.all()[i];
			let layer1Map: Map<number, CFG_ChengZhangJiJin> = roundRewardIdIndex.get(data.getRound());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_ChengZhangJiJin>();
				roundRewardIdIndex.set(data.getRound(), layer1Map);
			}
			layer1Map.set(data.getRewardId(), data);
		}
		this.roundRewardIdIndex = roundRewardIdIndex;
		//构建收集器roundCollector
		let roundCollector: Map<number, CFG_ChengZhangJiJin[]> = new Map<number, CFG_ChengZhangJiJin[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ChengZhangJiJin = this.all()[i];
			let collector: CFG_ChengZhangJiJin[] = roundCollector.get(data.getRound());
			if(collector === undefined) {
				collector = [];
				roundCollector.set(data.getRound(), collector);
			}
			collector.push(data);
		}
		this.roundCollector = roundCollector;
	}

	public getInRoundRewardIdIndex(round: number, rewardId: number): CFG_ChengZhangJiJin {
		let layer1Map: Map<number, CFG_ChengZhangJiJin> = this.roundRewardIdIndex.get(round);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("ChengZhangJiJinCache.getInRoundRewardIdIndex", round, rewardId);
		}
		let t: CFG_ChengZhangJiJin = layer1Map.get(rewardId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("ChengZhangJiJinCache.getInRoundRewardIdIndex", round, rewardId);
		}
		return t;
	}

	public findInRoundRewardIdIndex(round: number, rewardId: number): CFG_ChengZhangJiJin {
		let layer1Map: Map<number, CFG_ChengZhangJiJin> = this.roundRewardIdIndex.get(round);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_ChengZhangJiJin = layer1Map.get(rewardId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInRoundCollector(round: number) : CFG_ChengZhangJiJin[] {
		let ts: CFG_ChengZhangJiJin[] = this.roundCollector.get(round);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ChengZhangJiJinCache.getInRoundCollector", round);
		}
		return ts;
	}

	public findInRoundCollector(round: number) : CFG_ChengZhangJiJin[] {
		let ts: CFG_ChengZhangJiJin[] = this.roundCollector.get(round);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ChengZhangJiJin implements IDesignData {
	//序号
	protected id: number = 0;
	//奖励期数
	protected round: number = 0;
	//奖励ID
	protected rewardId: number = 0;
	//等级要求
	protected level: number = 0;
	//奖励
	protected reward: Reward = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRound(): number {
		return this.round;
	}
	public getRewardId(): number {
		return this.rewardId;
	}
	public getLevel(): number {
		return this.level;
	}
	public getReward(): Reward {
		return this.reward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}