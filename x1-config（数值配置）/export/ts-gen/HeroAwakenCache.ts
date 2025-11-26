
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
export default class HeroAwakenCache extends DesignCache<CFG_HeroAwaken> {
	private static instance: HeroAwakenCache = null;
	public static get Instance(): HeroAwakenCache {
		if(HeroAwakenCache.instance === null) {
			HeroAwakenCache.instance = new HeroAwakenCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroAwakenCache.instance.fileName);
			HeroAwakenCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroAwakenCache.instance;
	}
	public get fileName(): string {
		return "Y英雄觉醒表_HeroAwaken";
	}
	protected createInstance(): CFG_HeroAwaken {
		return new CFG_HeroAwaken();
	}

	protected qualityLevelIndex: Map<number, Map<number, CFG_HeroAwaken>> = null;


	protected loadAutoGenerate(): void {
		//构建索引qualityLevelIndex
		let qualityLevelIndex: Map<number, Map<number, CFG_HeroAwaken>> = new Map<number, Map<number, CFG_HeroAwaken>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HeroAwaken = this.all()[i];
			let layer1Map: Map<number, CFG_HeroAwaken> = qualityLevelIndex.get(data.getQuality());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_HeroAwaken>();
				qualityLevelIndex.set(data.getQuality(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.qualityLevelIndex = qualityLevelIndex;
	}

	public getInQualityLevelIndex(quality: number, level: number): CFG_HeroAwaken {
		let layer1Map: Map<number, CFG_HeroAwaken> = this.qualityLevelIndex.get(quality);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("HeroAwakenCache.getInQualityLevelIndex", quality, level);
		}
		let t: CFG_HeroAwaken = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("HeroAwakenCache.getInQualityLevelIndex", quality, level);
		}
		return t;
	}

	public findInQualityLevelIndex(quality: number, level: number): CFG_HeroAwaken {
		let layer1Map: Map<number, CFG_HeroAwaken> = this.qualityLevelIndex.get(quality);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_HeroAwaken = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroAwaken implements IDesignData {
	//序号
	protected id: number = 0;
	//英雄品质
	protected quality: number = 0;
	//觉醒等级
	protected level: number = 0;
	//消耗材料
	protected cost: Cost[] = [];
	//消耗本体数量
	protected needBody: number = 0;
	//指定英雄碎片品质
	protected otherQuality: number = 0;
	//数量
	protected otherBody: number = 0;
	//属性加成
	protected attr: BattAttr[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getLevel(): number {
		return this.level;
	}
	public getCost(): Cost[] {
		return this.cost;
	}
	public getNeedBody(): number {
		return this.needBody;
	}
	public getOtherQuality(): number {
		return this.otherQuality;
	}
	public getOtherBody(): number {
		return this.otherBody;
	}
	public getAttr(): BattAttr[] {
		return this.attr;
	}
	private formatCost(): Cost {
		return new Cost();
	}
	private formatAttr(): BattAttr {
		return new BattAttr();
	}
}