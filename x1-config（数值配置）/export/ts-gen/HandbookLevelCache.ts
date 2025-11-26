
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
export default class HandbookLevelCache extends DesignCache<CFG_HandbookLevel> {
	private static instance: HandbookLevelCache = null;
	public static get Instance(): HandbookLevelCache {
		if(HandbookLevelCache.instance === null) {
			HandbookLevelCache.instance = new HandbookLevelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HandbookLevelCache.instance.fileName);
			HandbookLevelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HandbookLevelCache.instance;
	}
	public get fileName(): string {
		return "T图鉴等级表_HandbookLevel";
	}
	protected createInstance(): CFG_HandbookLevel {
		return new CFG_HandbookLevel();
	}

	protected qualityUpTypeLevelIndex: Map<number, Map<number, Map<number, CFG_HandbookLevel>>> = null;


	protected loadAutoGenerate(): void {
		//构建索引qualityUpTypeLevelIndex
		let qualityUpTypeLevelIndex: Map<number, Map<number, Map<number, CFG_HandbookLevel>>> = new Map<number, Map<number, Map<number, CFG_HandbookLevel>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HandbookLevel = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_HandbookLevel>> = qualityUpTypeLevelIndex.get(data.getQuality());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_HandbookLevel>>();
				qualityUpTypeLevelIndex.set(data.getQuality(), layer1Map);
			}
			let layer2Map: Map<number, CFG_HandbookLevel> = layer1Map.get(data.getUpType());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_HandbookLevel>();
				layer1Map.set(data.getUpType(), layer2Map);
			}
			layer2Map.set(data.getLevel(), data);
		}
		this.qualityUpTypeLevelIndex = qualityUpTypeLevelIndex;
	}

	public getInQualityUpTypeLevelIndex(quality: number, upType: number, level: number): CFG_HandbookLevel {
		let layer1Map: Map<number, Map<number, CFG_HandbookLevel>> = this.qualityUpTypeLevelIndex.get(quality);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		let layer2Map: Map<number, CFG_HandbookLevel> = layer1Map.get(upType);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		let t: CFG_HandbookLevel = layer2Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		return t;
	}

	public findInQualityUpTypeLevelIndex(quality: number, upType: number, level: number): CFG_HandbookLevel {
		let layer1Map: Map<number, Map<number, CFG_HandbookLevel>> = this.qualityUpTypeLevelIndex.get(quality);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_HandbookLevel> = layer1Map.get(upType);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_HandbookLevel = layer2Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_HandbookLevel implements IDesignData {
	//序号
	protected id: number = 0;
	//图鉴品质
	protected quality: number = 0;
	//提升方式
	protected upType: number = 0;
	//图鉴等级
	protected level: number = 0;
	//需要等级
	protected needLevel: number = 0;
	//消耗道具数量
	protected needCost: Cost = null;
	//增长积分
	protected point: number = 0;
	//提升属性
	protected attrs: BattAttr[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getUpType(): number {
		return this.upType;
	}
	public getLevel(): number {
		return this.level;
	}
	public getNeedLevel(): number {
		return this.needLevel;
	}
	public getNeedCost(): Cost {
		return this.needCost;
	}
	public getPoint(): number {
		return this.point;
	}
	public getAttrs(): BattAttr[] {
		return this.attrs;
	}
	private formatNeedCost(): Cost {
		return new Cost();
	}
	private formatAttrs(): BattAttr {
		return new BattAttr();
	}
}