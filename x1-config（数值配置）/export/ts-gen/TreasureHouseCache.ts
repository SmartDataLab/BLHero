
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
export default class TreasureHouseCache extends DesignCache<CFG_TreasureHouse> {
	private static instance: TreasureHouseCache = null;
	public static get Instance(): TreasureHouseCache {
		if(TreasureHouseCache.instance === null) {
			TreasureHouseCache.instance = new TreasureHouseCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TreasureHouseCache.instance.fileName);
			TreasureHouseCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TreasureHouseCache.instance;
	}
	public get fileName(): string {
		return "Z珍宝阁_TreasureHouse";
	}
	protected createInstance(): CFG_TreasureHouse {
		return new CFG_TreasureHouse();
	}

	protected typePeriodRechargeIdIndex: Map<number, Map<number, Map<number, CFG_TreasureHouse>>> = null;
	protected typePeriodRewardIdIndex: Map<number, Map<number, Map<number, CFG_TreasureHouse>>> = null;

	protected typePeriodCollector: Map<number, Map<number, CFG_TreasureHouse[]>> = null;

	protected loadAutoGenerate(): void {
		//构建索引typePeriodRechargeIdIndex
		let typePeriodRechargeIdIndex: Map<number, Map<number, Map<number, CFG_TreasureHouse>>> = new Map<number, Map<number, Map<number, CFG_TreasureHouse>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_TreasureHouse = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = typePeriodRechargeIdIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_TreasureHouse>>();
				typePeriodRechargeIdIndex.set(data.getType(), layer1Map);
			}
			let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(data.getPeriod());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_TreasureHouse>();
				layer1Map.set(data.getPeriod(), layer2Map);
			}
			layer2Map.set(data.getRechargeId(), data);
		}
		this.typePeriodRechargeIdIndex = typePeriodRechargeIdIndex;
		//构建索引typePeriodRewardIdIndex
		let typePeriodRewardIdIndex: Map<number, Map<number, Map<number, CFG_TreasureHouse>>> = new Map<number, Map<number, Map<number, CFG_TreasureHouse>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_TreasureHouse = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = typePeriodRewardIdIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_TreasureHouse>>();
				typePeriodRewardIdIndex.set(data.getType(), layer1Map);
			}
			let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(data.getPeriod());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_TreasureHouse>();
				layer1Map.set(data.getPeriod(), layer2Map);
			}
			layer2Map.set(data.getRewardId(), data);
		}
		this.typePeriodRewardIdIndex = typePeriodRewardIdIndex;
		//构建收集器typePeriodCollector
		let typePeriodCollector: Map<number, Map<number, CFG_TreasureHouse[]>> = new Map<number, Map<number, CFG_TreasureHouse[]>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_TreasureHouse = this.all()[i];
			let layer1Map: Map<number, CFG_TreasureHouse[]> = typePeriodCollector.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_TreasureHouse[]>();
				typePeriodCollector.set(data.getType(), layer1Map);
			}
			let collector: CFG_TreasureHouse[] = layer1Map.get(data.getPeriod());
			if(collector === undefined) {
				collector = [];
				layer1Map.set(data.getPeriod(), collector);
			}
			collector.push(data);
		}
		this.typePeriodCollector = typePeriodCollector;
	}

	public getInTypePeriodRechargeIdIndex(type: number, period: number, rechargeId: number): CFG_TreasureHouse {
		let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = this.typePeriodRechargeIdIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(period);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		let t: CFG_TreasureHouse = layer2Map.get(rechargeId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		return t;
	}
	public getInTypePeriodRewardIdIndex(type: number, period: number, rewardId: number): CFG_TreasureHouse {
		let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = this.typePeriodRewardIdIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(period);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		let t: CFG_TreasureHouse = layer2Map.get(rewardId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		return t;
	}

	public findInTypePeriodRechargeIdIndex(type: number, period: number, rechargeId: number): CFG_TreasureHouse {
		let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = this.typePeriodRechargeIdIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(period);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_TreasureHouse = layer2Map.get(rechargeId);
		if(t === undefined) {
			return null;
		}
		return t;
	}
	public findInTypePeriodRewardIdIndex(type: number, period: number, rewardId: number): CFG_TreasureHouse {
		let layer1Map: Map<number, Map<number, CFG_TreasureHouse>> = this.typePeriodRewardIdIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_TreasureHouse> = layer1Map.get(period);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_TreasureHouse = layer2Map.get(rewardId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInTypePeriodCollector(type: number, period: number) : CFG_TreasureHouse[] {
		let layer1Map: Map<number, CFG_TreasureHouse[]> = this.typePeriodCollector.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodCollector", type, period);
		}
		let ts: CFG_TreasureHouse[] = layer1Map.get(period);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("TreasureHouseCache.getInTypePeriodCollector", type, period);
		}
		return ts;
	}

	public findInTypePeriodCollector(type: number, period: number) : CFG_TreasureHouse[] {
		let layer1Map: Map<number, CFG_TreasureHouse[]> = this.typePeriodCollector.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let ts: CFG_TreasureHouse[] = layer1Map.get(period);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_TreasureHouse implements IDesignData {
	//序号
	protected id: number = 0;
	//类型
	protected type: number = 0;
	//期数
	protected period: number = 0;
	//充值商品表ID
	protected rechargeId: number = 0;
	//是否为免费商品
	protected rewardId: number = 0;
	//限制条件
	protected condition: Keyv[] = [];
	//限购类型
	protected limitType: number = 0;
	//限购数量
	protected limitNum: number = 0;
	//商品内容
	protected reward: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getPeriod(): number {
		return this.period;
	}
	public getRechargeId(): number {
		return this.rechargeId;
	}
	public getRewardId(): number {
		return this.rewardId;
	}
	public getCondition(): Keyv[] {
		return this.condition;
	}
	public getLimitType(): number {
		return this.limitType;
	}
	public getLimitNum(): number {
		return this.limitNum;
	}
	public getReward(): Reward[] {
		return this.reward;
	}
	private formatCondition(): Keyv {
		return new Keyv();
	}
	private formatReward(): Reward {
		return new Reward();
	}
}