
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
export default class TongTianTaTeQuanRewardCache extends DesignCache<CFG_TongTianTaTeQuanReward> {
	private static instance: TongTianTaTeQuanRewardCache = null;
	public static get Instance(): TongTianTaTeQuanRewardCache {
		if(TongTianTaTeQuanRewardCache.instance === null) {
			TongTianTaTeQuanRewardCache.instance = new TongTianTaTeQuanRewardCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TongTianTaTeQuanRewardCache.instance.fileName);
			TongTianTaTeQuanRewardCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TongTianTaTeQuanRewardCache.instance;
	}
	public get fileName(): string {
		return "P1004特权系统通天塔层数奖励_TongTianTaTeQuanReward";
	}
	protected createInstance(): CFG_TongTianTaTeQuanReward {
		return new CFG_TongTianTaTeQuanReward();
	}


	protected towerTypeRoundCollector: Map<number, Map<number, CFG_TongTianTaTeQuanReward[]>> = null;

	protected loadAutoGenerate(): void {
		//构建收集器towerTypeRoundCollector
		let towerTypeRoundCollector: Map<number, Map<number, CFG_TongTianTaTeQuanReward[]>> = new Map<number, Map<number, CFG_TongTianTaTeQuanReward[]>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_TongTianTaTeQuanReward = this.all()[i];
			let layer1Map: Map<number, CFG_TongTianTaTeQuanReward[]> = towerTypeRoundCollector.get(data.getTowerType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_TongTianTaTeQuanReward[]>();
				towerTypeRoundCollector.set(data.getTowerType(), layer1Map);
			}
			let collector: CFG_TongTianTaTeQuanReward[] = layer1Map.get(data.getRound());
			if(collector === undefined) {
				collector = [];
				layer1Map.set(data.getRound(), collector);
			}
			collector.push(data);
		}
		this.towerTypeRoundCollector = towerTypeRoundCollector;
	}



	public getInTowerTypeRoundCollector(towerType: number, round: number) : CFG_TongTianTaTeQuanReward[] {
		let layer1Map: Map<number, CFG_TongTianTaTeQuanReward[]> = this.towerTypeRoundCollector.get(towerType);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("TongTianTaTeQuanRewardCache.getInTowerTypeRoundCollector", towerType, round);
		}
		let ts: CFG_TongTianTaTeQuanReward[] = layer1Map.get(round);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("TongTianTaTeQuanRewardCache.getInTowerTypeRoundCollector", towerType, round);
		}
		return ts;
	}

	public findInTowerTypeRoundCollector(towerType: number, round: number) : CFG_TongTianTaTeQuanReward[] {
		let layer1Map: Map<number, CFG_TongTianTaTeQuanReward[]> = this.towerTypeRoundCollector.get(towerType);
		if(layer1Map === undefined) {
			return null;
		}
		let ts: CFG_TongTianTaTeQuanReward[] = layer1Map.get(round);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_TongTianTaTeQuanReward implements IDesignData {
	//序号
	protected id: number = 0;
	//塔类型
	protected towerType: number = 0;
	//奖励期数
	protected round: number = 0;
	//奖励等级
	protected rewardLv: number = 0;
	//所需达到层数
	protected layer: number = 0;
	//普通挑战奖励
	protected normalReward: Reward = null;
	//高级挑战奖励
	protected highReward: Reward = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getTowerType(): number {
		return this.towerType;
	}
	public getRound(): number {
		return this.round;
	}
	public getRewardLv(): number {
		return this.rewardLv;
	}
	public getLayer(): number {
		return this.layer;
	}
	public getNormalReward(): Reward {
		return this.normalReward;
	}
	public getHighReward(): Reward {
		return this.highReward;
	}
	private formatNormalReward(): Reward {
		return new Reward();
	}
	private formatHighReward(): Reward {
		return new Reward();
	}
}