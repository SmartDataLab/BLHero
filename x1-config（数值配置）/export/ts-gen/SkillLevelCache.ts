
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
export default class SkillLevelCache extends DesignCache<CFG_SkillLevel> {
	private static instance: SkillLevelCache = null;
	public static get Instance(): SkillLevelCache {
		if(SkillLevelCache.instance === null) {
			SkillLevelCache.instance = new SkillLevelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SkillLevelCache.instance.fileName);
			SkillLevelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SkillLevelCache.instance;
	}
	public get fileName(): string {
		return "2技能表_SkillLevel";
	}
	protected createInstance(): CFG_SkillLevel {
		return new CFG_SkillLevel();
	}

	protected skillIdLevelIndex: Map<number, Map<number, CFG_SkillLevel>> = null;


	protected loadAutoGenerate(): void {
		//构建索引skillIdLevelIndex
		let skillIdLevelIndex: Map<number, Map<number, CFG_SkillLevel>> = new Map<number, Map<number, CFG_SkillLevel>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SkillLevel = this.all()[i];
			let layer1Map: Map<number, CFG_SkillLevel> = skillIdLevelIndex.get(data.getSkillId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SkillLevel>();
				skillIdLevelIndex.set(data.getSkillId(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.skillIdLevelIndex = skillIdLevelIndex;
	}

	public getInSkillIdLevelIndex(skillId: number, level: number): CFG_SkillLevel {
		let layer1Map: Map<number, CFG_SkillLevel> = this.skillIdLevelIndex.get(skillId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SkillLevelCache.getInSkillIdLevelIndex", skillId, level);
		}
		let t: CFG_SkillLevel = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SkillLevelCache.getInSkillIdLevelIndex", skillId, level);
		}
		return t;
	}

	public findInSkillIdLevelIndex(skillId: number, level: number): CFG_SkillLevel {
		let layer1Map: Map<number, CFG_SkillLevel> = this.skillIdLevelIndex.get(skillId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SkillLevel = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SkillLevel implements IDesignData {
	//技能ID
4000000+伙伴ID*1000+技能顺序ID+技能等级
	protected id: number = 0;
	//技能组ID
	protected skillId: number = 0;
	//名称
	protected name: string = "";
	//名称
	protected nameLang: string = "";
	//技能图标
	protected icon: string = "";
	//技能标签
	protected skillType: number = 0;
	//技能等级
	protected level: number = 0;
	//技能描述：
技能面板调用
支持富文本&跳转链接功能
	protected describe: string = "";
	//技能描述：
技能面板调用
支持富文本&跳转链接功能
	protected describeLang: string = "";
	//效果类型
1、近战伤害
2、技能伤害
4、附加buff
	protected dmgType: number = 0;
	//效果参数
3、治疗：系数类型（1施法者攻击百分比；2目标生命上限百分比）
- 治疗系数在伤害比例字段配置
5、复活：系统类型（1施法者攻击百分比；2目标生命上限百分比）
- 治疗系数在伤害比例字段配置
6、弹射：弹射次数#弹射范围#伤害系数（暂未实现）
7、子弹伤害：子弹ID#子弹类型（1矩形；2扇形）#子弹数量#射击方式（1平射；2抛物线）#范围参数（1间隔距离；2夹角）
	protected specialEffect: string = "";
	//作用对象
1自己
2己方
3敌方
4己方除自己
99.无(不调用相应技能指令；处理技能1替代普攻类伙伴)
	protected targetType: number = 0;
	//攻击距离
技能攻击距离/像素
距离算施法者和目标的中心点距离
	protected atkRange: number = 0;
	//动作名称
0：无动作
1：普攻
2：技能1
3：技能2
	protected actionId: number = 0;
	//目标选择
1：距离最近的目标
2：以自身为中心
3：以目标为中心
4：以受击目标为中心
5：生命最低的单位（百分比）
6：生命最少的单位（生命值）
7：已死亡己方（复活技能使用）
	protected selectType: number = 0;
	//作用范围
格式：范围类型#类型参数
0：读攻击距离
1：圆形，配置格式：1#半径
2：矩形，配置格式：2#宽度#长度
3：扇形，配置格式：3#夹角度数#半径
	protected rangeWay: string = "";
	//目标数量上限：


	protected targetLimit: number = 0;
	//技能效果ID-单
	protected skillEffectId: number = 0;
	//技能效果ID-多：
不配置则不读，配置则只取此字段，单效果字段不生效

单效果:第1个攻击帧触发伤害&攻击特效，其他帧只触发伤害

多效果:每个攻击帧都触发伤害&攻击特效

配置格式:技能效果ID#特效/作用范围缩放比例#特效间距
缩放比例单位:百分比

	protected skillEffectId1: string = "";
	//常驻被动属性
	protected nailAttr: BattAttr[] = [];
	//战斗中被动属性
	protected passiveAttr: BattAttr[] = [];
	//伤害固定值
	protected fixValue: number = 0;
	//伤害比例
	protected skillFactor: number = 0;
	//起手时间：
起手时间内，打断后技能不进入CD 攻击帧时间减100

	protected raiseHandTime: number = 0;
	//攻击帧时间：
出伤害/攻击特效时间/ms
多段伤害时第1个攻击帧出特效，其他的攻击帧只出伤害

	protected atkFrame: string = "";
	//动作时间：
动作持续时间，与伤害无关；施法动画的长度

	protected actionTime: number = 0;
	//技能时间毫秒：
技能时间不能小于动作时间，否则会导致技能一致持续释放

如果特效播放时间过短，就去看下特效持续时间是否正确
用攻击帧时间+特效时间


	protected skillTime: number = 0;
	//技能冷却
	protected cdTime: number = 0;
	//额外触发条件
	protected extraCd: Keyv = null;
	//移动是否停止施法
	protected cease: number = 0;
	//附加buff：格式：buff#概率#是否每个帧都附加Buff#目标类型#目标数量|....

目标类型：0与技能目标相同；1自己；2己方；3敌方；4己方除自己
附加buff时间：-1每个攻击帧都会附近，大于0则指定攻击帧触发
目标数量，默认为0不用配置，与目标数量不同时配置


	protected buffs: BattBuff[] = [];
	//额外特殊触发条件
条件类型#参数
1、携带指定buff类型时；
2、指定战斗表现时触发
---1闪避时；2暴击时

	protected specialCondition: string = "";
	//额外特殊触发效果：
1、造成额外伤害#伤害比列
2、立即释放指定技能#技能ID#间隔时间
3、增加额外目标人数#目标数量
	protected specialEffect1: string = "";
	//位移类型：
Administrator:
1、拉怪
2、冲锋（人物碰到怪物后，与怪物同时冲到最大攻击距离处）
3、突进（人物碰到目标怪物后停止）
4、穿刺（怪物位置不动，人物穿过去）
5、瞬移
6、平移

	protected moveType: number = 0;
	//位移类型参数：
1、拉怪：类型#距离#初速度#加速度
--类型1：身前X像素（只能配置圆形和扇形）
--类型2：中轴线X像素（只能配置矩形）
2、冲锋：初速度#加速度
3、突进：初速度#加速度
4、穿刺：初速度#加速度
5、瞬移：类型
--类型1：受击目标所在位置；
--类型2：最近友方所在位置；
--类型3：最远友方所在位置
6、平移#0（无参数）

	protected moveVariate: string = "";
	//战力
	protected fighting: number = 0;
	//是否显示技能预警
	protected skillWarning: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSkillId(): number {
		return this.skillId;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getSkillType(): number {
		return this.skillType;
	}
	public getLevel(): number {
		return this.level;
	}
	public getDescribe(): string {
		return this.describe;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getDmgType(): number {
		return this.dmgType;
	}
	public getSpecialEffect(): string {
		return this.specialEffect;
	}
	public getTargetType(): number {
		return this.targetType;
	}
	public getAtkRange(): number {
		return this.atkRange;
	}
	public getActionId(): number {
		return this.actionId;
	}
	public getSelectType(): number {
		return this.selectType;
	}
	public getRangeWay(): string {
		return this.rangeWay;
	}
	public getTargetLimit(): number {
		return this.targetLimit;
	}
	public getSkillEffectId(): number {
		return this.skillEffectId;
	}
	public getSkillEffectId1(): string {
		return this.skillEffectId1;
	}
	public getNailAttr(): BattAttr[] {
		return this.nailAttr;
	}
	public getPassiveAttr(): BattAttr[] {
		return this.passiveAttr;
	}
	public getFixValue(): number {
		return this.fixValue;
	}
	public getSkillFactor(): number {
		return this.skillFactor;
	}
	public getRaiseHandTime(): number {
		return this.raiseHandTime;
	}
	public getAtkFrame(): string {
		return this.atkFrame;
	}
	public getActionTime(): number {
		return this.actionTime;
	}
	public getSkillTime(): number {
		return this.skillTime;
	}
	public getCdTime(): number {
		return this.cdTime;
	}
	public getExtraCd(): Keyv {
		return this.extraCd;
	}
	public getCease(): number {
		return this.cease;
	}
	public getBuffs(): BattBuff[] {
		return this.buffs;
	}
	public getSpecialCondition(): string {
		return this.specialCondition;
	}
	public getSpecialEffect1(): string {
		return this.specialEffect1;
	}
	public getMoveType(): number {
		return this.moveType;
	}
	public getMoveVariate(): string {
		return this.moveVariate;
	}
	public getFighting(): number {
		return this.fighting;
	}
	public getSkillWarning(): number {
		return this.skillWarning;
	}
	private formatNailAttr(): BattAttr {
		return new BattAttr();
	}
	private formatPassiveAttr(): BattAttr {
		return new BattAttr();
	}
	private formatExtraCd(): Keyv {
		return new Keyv();
	}
	private formatBuffs(): BattBuff {
		return new BattBuff();
	}
}