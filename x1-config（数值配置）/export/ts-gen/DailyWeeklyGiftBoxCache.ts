
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
export default class DailyWeeklyGiftBoxCache extends DesignCache<CFG_DailyWeeklyGiftBox> {
	private static instance: DailyWeeklyGiftBoxCache = null;
	public static get Instance(): DailyWeeklyGiftBoxCache {
		if(DailyWeeklyGiftBoxCache.instance === null) {
			DailyWeeklyGiftBoxCache.instance = new DailyWeeklyGiftBoxCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DailyWeeklyGiftBoxCache.instance.fileName);
			DailyWeeklyGiftBoxCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DailyWeeklyGiftBoxCache.instance;
	}
	public get fileName(): string {
		return "R日常周常活跃宝箱_DailyWeeklyGiftBox";
	}
	protected createInstance(): CFG_DailyWeeklyGiftBox {
		return new CFG_DailyWeeklyGiftBox();
	}

	protected typeRewardNumIndex: Map<number, Map<number, CFG_DailyWeeklyGiftBox>> = null;

	protected typeCollector: Map<number, CFG_DailyWeeklyGiftBox[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引typeRewardNumIndex
		let typeRewardNumIndex: Map<number, Map<number, CFG_DailyWeeklyGiftBox>> = new Map<number, Map<number, CFG_DailyWeeklyGiftBox>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DailyWeeklyGiftBox = this.all()[i];
			let layer1Map: Map<number, CFG_DailyWeeklyGiftBox> = typeRewardNumIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_DailyWeeklyGiftBox>();
				typeRewardNumIndex.set(data.getType(), layer1Map);
			}
			layer1Map.set(data.getRewardNum(), data);
		}
		this.typeRewardNumIndex = typeRewardNumIndex;
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_DailyWeeklyGiftBox[]> = new Map<number, CFG_DailyWeeklyGiftBox[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DailyWeeklyGiftBox = this.all()[i];
			let collector: CFG_DailyWeeklyGiftBox[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}

	public getInTypeRewardNumIndex(type: number, rewardNum: number): CFG_DailyWeeklyGiftBox {
		let layer1Map: Map<number, CFG_DailyWeeklyGiftBox> = this.typeRewardNumIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("DailyWeeklyGiftBoxCache.getInTypeRewardNumIndex", type, rewardNum);
		}
		let t: CFG_DailyWeeklyGiftBox = layer1Map.get(rewardNum);
		if(t === undefined) {
			throw new DesignDataNotFoundError("DailyWeeklyGiftBoxCache.getInTypeRewardNumIndex", type, rewardNum);
		}
		return t;
	}

	public findInTypeRewardNumIndex(type: number, rewardNum: number): CFG_DailyWeeklyGiftBox {
		let layer1Map: Map<number, CFG_DailyWeeklyGiftBox> = this.typeRewardNumIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_DailyWeeklyGiftBox = layer1Map.get(rewardNum);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInTypeCollector(type: number) : CFG_DailyWeeklyGiftBox[] {
		let ts: CFG_DailyWeeklyGiftBox[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("DailyWeeklyGiftBoxCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_DailyWeeklyGiftBox[] {
		let ts: CFG_DailyWeeklyGiftBox[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_DailyWeeklyGiftBox implements IDesignData {
	//编号
	protected id: number = 0;
	//任务类型
	protected type: number = 0;
	//奖励序号
	protected rewardNum: number = 0;
	//活跃度
	protected points: number = 0;
	//任务奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getRewardNum(): number {
		return this.rewardNum;
	}
	public getPoints(): number {
		return this.points;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}