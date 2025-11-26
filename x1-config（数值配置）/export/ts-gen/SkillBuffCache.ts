
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
export default class SkillBuffCache extends DesignCache<CFG_SkillBuff> {
	private static instance: SkillBuffCache = null;
	public static get Instance(): SkillBuffCache {
		if(SkillBuffCache.instance === null) {
			SkillBuffCache.instance = new SkillBuffCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SkillBuffCache.instance.fileName);
			SkillBuffCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SkillBuffCache.instance;
	}
	public get fileName(): string {
		return "Buff表_SkillBuff";
	}
	protected createInstance(): CFG_SkillBuff {
		return new CFG_SkillBuff();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_SkillBuff implements IDesignData {
	//buffID
	protected id: number = 0;
	//buff名称
	protected name: string = "";
	//buff名称
	protected nameLang: string = "";
	//buff描述
	protected describeLang: string = "";
	//buff功能ID：是标识时不同的功能，

	protected buffEnum: number = 0;
	//属性参数
	protected attrParams: BattAttr[] = [];
	//状态系列：
不同类型的buff统一都是共同作用
	protected series: number = 0;
	//buff执行优先级
	protected priority: number = 0;
	//叠加方式
1、覆盖：同类型buff，优先级高的覆盖优先级低的
2、叠加：同类型buff，共存叠加层数
3、共存，buff共同各自作用

	protected stackWay: number = 0;
	//最大叠加层数：
	protected maxStack: number = 0;
	//层数叠满触发的buff
	protected maxStackTrigger: number[] = [];
	//BUFF机制参数
	protected intParams: number[] = [];
	//作用范围
	protected rangeWay: string = "";
	//作用对象
	protected targetType: string = "";
	//目标数量上限
	protected targetLimit: string = "";
	//持续时间毫秒
	protected lastTime: number = 0;
	//buff图标id
	protected icon: string = "";
	//buff特效id
	protected animEffect: string = "";
	//受击特效
	protected hitModel: string = "";
	//buff放置的节点类型
	protected animNodeType: number = 0;
	//特效位置
	protected animPosition: number = 0;
	//特效是否循环播放
	protected animLoop: string = "";
	//增减益
	protected gain_or_loss: number = 0;
	//驱散方式
	protected remove_type: number = 0;
	//羁绊英雄列表
	protected buffHeroList: string = "";
	//羁绊主英雄
	protected buffMainHero: string = "";
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
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getBuffEnum(): number {
		return this.buffEnum;
	}
	public getAttrParams(): BattAttr[] {
		return this.attrParams;
	}
	public getSeries(): number {
		return this.series;
	}
	public getPriority(): number {
		return this.priority;
	}
	public getStackWay(): number {
		return this.stackWay;
	}
	public getMaxStack(): number {
		return this.maxStack;
	}
	public getMaxStackTrigger(): number[] {
		return this.maxStackTrigger;
	}
	public getIntParams(): number[] {
		return this.intParams;
	}
	public getRangeWay(): string {
		return this.rangeWay;
	}
	public getTargetType(): string {
		return this.targetType;
	}
	public getTargetLimit(): string {
		return this.targetLimit;
	}
	public getLastTime(): number {
		return this.lastTime;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getAnimEffect(): string {
		return this.animEffect;
	}
	public getHitModel(): string {
		return this.hitModel;
	}
	public getAnimNodeType(): number {
		return this.animNodeType;
	}
	public getAnimPosition(): number {
		return this.animPosition;
	}
	public getAnimLoop(): string {
		return this.animLoop;
	}
	public getGain_or_loss(): number {
		return this.gain_or_loss;
	}
	public getRemove_type(): number {
		return this.remove_type;
	}
	public getBuffHeroList(): string {
		return this.buffHeroList;
	}
	public getBuffMainHero(): string {
		return this.buffMainHero;
	}
	private formatAttrParams(): BattAttr {
		return new BattAttr();
	}
	private formatMaxStackTrigger(): number {
		return 0;
	}
	private formatIntParams(): number {
		return 0;
	}
}