
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
export default class MonsterCache extends DesignCache<CFG_Monster> {
	private static instance: MonsterCache = null;
	public static get Instance(): MonsterCache {
		if(MonsterCache.instance === null) {
			MonsterCache.instance = new MonsterCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MonsterCache.instance.fileName);
			MonsterCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MonsterCache.instance;
	}
	public get fileName(): string {
		return "G怪物表_Monster";
	}
	protected createInstance(): CFG_Monster {
		return new CFG_Monster();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Monster implements IDesignData {
	//怪物ID
	protected id: number = 0;
	//怪物名称(程序字)
	protected name: string = "";
	//怪物名称(程序字)
	protected nameLang: string = "";
	//怪物模型
	protected identity: number = 0;
	//战斗类型
	protected hateType: number = 0;
	//怪物头像
	protected icon: string = "";
	//怪物名称(艺术字)
	protected monsterName: string = "";
	//放大系数
	protected scale: number = 0;
	//体积半径
	protected volume: number = 0;
	//等级
	protected level: number = 0;
	//类型
	protected type: number = 0;
	//怪物属性
	protected attribute: BattAttr[] = [];
	//血条数
	protected hpPoints: number = 0;
	//最高扣血
	protected maxHurt: number = 0;
	//刷新时间秒
	protected refreshTime: number = 0;
	//寻敌范围
	protected huntingRange: number = 0;
	//追击范围
	protected chaseRange: number = 0;
	//脱战范围
	protected outOfRange: number = 0;
	//脱战回血效率
	protected revitalize: string = "";
	//击杀固定产出
	protected produce: Reward = null;
	//怪物技能
	protected skills: Keyv[] = [];
	//霸体状态
	protected superArmor: string = "";
	//怪物战力
	protected fighting: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getIdentity(): number {
		return this.identity;
	}
	public getHateType(): number {
		return this.hateType;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getMonsterName(): string {
		return this.monsterName;
	}
	public getScale(): number {
		return this.scale;
	}
	public getVolume(): number {
		return this.volume;
	}
	public getLevel(): number {
		return this.level;
	}
	public getType(): number {
		return this.type;
	}
	public getAttribute(): BattAttr[] {
		return this.attribute;
	}
	public getHpPoints(): number {
		return this.hpPoints;
	}
	public getMaxHurt(): number {
		return this.maxHurt;
	}
	public getRefreshTime(): number {
		return this.refreshTime;
	}
	public getHuntingRange(): number {
		return this.huntingRange;
	}
	public getChaseRange(): number {
		return this.chaseRange;
	}
	public getOutOfRange(): number {
		return this.outOfRange;
	}
	public getRevitalize(): string {
		return this.revitalize;
	}
	public getProduce(): Reward {
		return this.produce;
	}
	public getSkills(): Keyv[] {
		return this.skills;
	}
	public getSuperArmor(): string {
		return this.superArmor;
	}
	public getFighting(): number {
		return this.fighting;
	}
	private formatAttribute(): BattAttr {
		return new BattAttr();
	}
	private formatProduce(): Reward {
		return new Reward();
	}
	private formatSkills(): Keyv {
		return new Keyv();
	}
}