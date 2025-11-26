
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
export default class HeroLevelCache extends DesignCache<CFG_HeroLevel> {
	private static instance: HeroLevelCache = null;
	public static get Instance(): HeroLevelCache {
		if(HeroLevelCache.instance === null) {
			HeroLevelCache.instance = new HeroLevelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroLevelCache.instance.fileName);
			HeroLevelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroLevelCache.instance;
	}
	public get fileName(): string {
		return "Y英雄等级表_HeroLevel";
	}
	protected createInstance(): CFG_HeroLevel {
		return new CFG_HeroLevel();
	}

	protected heroIdentityLevelIndex: Map<number, Map<number, CFG_HeroLevel>> = null;

	protected heroIdentityCollector: Map<number, CFG_HeroLevel[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引heroIdentityLevelIndex
		let heroIdentityLevelIndex: Map<number, Map<number, CFG_HeroLevel>> = new Map<number, Map<number, CFG_HeroLevel>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HeroLevel = this.all()[i];
			let layer1Map: Map<number, CFG_HeroLevel> = heroIdentityLevelIndex.get(data.getHeroIdentity());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_HeroLevel>();
				heroIdentityLevelIndex.set(data.getHeroIdentity(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.heroIdentityLevelIndex = heroIdentityLevelIndex;
		//构建收集器heroIdentityCollector
		let heroIdentityCollector: Map<number, CFG_HeroLevel[]> = new Map<number, CFG_HeroLevel[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HeroLevel = this.all()[i];
			let collector: CFG_HeroLevel[] = heroIdentityCollector.get(data.getHeroIdentity());
			if(collector === undefined) {
				collector = [];
				heroIdentityCollector.set(data.getHeroIdentity(), collector);
			}
			collector.push(data);
		}
		this.heroIdentityCollector = heroIdentityCollector;
	}

	public getInHeroIdentityLevelIndex(heroIdentity: number, level: number): CFG_HeroLevel {
		let layer1Map: Map<number, CFG_HeroLevel> = this.heroIdentityLevelIndex.get(heroIdentity);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("HeroLevelCache.getInHeroIdentityLevelIndex", heroIdentity, level);
		}
		let t: CFG_HeroLevel = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("HeroLevelCache.getInHeroIdentityLevelIndex", heroIdentity, level);
		}
		return t;
	}

	public findInHeroIdentityLevelIndex(heroIdentity: number, level: number): CFG_HeroLevel {
		let layer1Map: Map<number, CFG_HeroLevel> = this.heroIdentityLevelIndex.get(heroIdentity);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_HeroLevel = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInHeroIdentityCollector(heroIdentity: number) : CFG_HeroLevel[] {
		let ts: CFG_HeroLevel[] = this.heroIdentityCollector.get(heroIdentity);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("HeroLevelCache.getInHeroIdentityCollector", heroIdentity);
		}
		return ts;
	}

	public findInHeroIdentityCollector(heroIdentity: number) : CFG_HeroLevel[] {
		let ts: CFG_HeroLevel[] = this.heroIdentityCollector.get(heroIdentity);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroLevel implements IDesignData {
	//序号
	protected id: number = 0;
	//英雄标识
	protected heroIdentity: number = 0;
	//等级
	protected level: number = 0;
	//下一级序号
	protected nextId: number = 0;
	//消耗本体数量
	protected costBody: number = 0;
	//除本体外的消耗
	protected costMaterial: Cost[] = [];
	//是不是突破升级类型
	protected breakLevel: number = 0;
	//技能
	protected skills: Keyv[] = [];
	//全局属性
	protected globalAttribute: BattAttr[] = [];
	//升级属性
	protected levelAttribute: BattAttr[] = [];
	//阶段描述
	protected phaseDesc: string = "";
	//阶段描述
	protected phaseDescLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getHeroIdentity(): number {
		return this.heroIdentity;
	}
	public getLevel(): number {
		return this.level;
	}
	public getNextId(): number {
		return this.nextId;
	}
	public getCostBody(): number {
		return this.costBody;
	}
	public getCostMaterial(): Cost[] {
		return this.costMaterial;
	}
	public getBreakLevel(): number {
		return this.breakLevel;
	}
	public getSkills(): Keyv[] {
		return this.skills;
	}
	public getGlobalAttribute(): BattAttr[] {
		return this.globalAttribute;
	}
	public getLevelAttribute(): BattAttr[] {
		return this.levelAttribute;
	}
	public getPhaseDesc(): string {
		return this.phaseDesc;
	}
	public getPhaseDescLang(): string {
		return this.phaseDescLang;
	}
	private formatCostMaterial(): Cost {
		return new Cost();
	}
	private formatSkills(): Keyv {
		return new Keyv();
	}
	private formatGlobalAttribute(): BattAttr {
		return new BattAttr();
	}
	private formatLevelAttribute(): BattAttr {
		return new BattAttr();
	}
}