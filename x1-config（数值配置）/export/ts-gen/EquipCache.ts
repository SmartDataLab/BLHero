
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
export default class EquipCache extends DesignCache<CFG_Equip> {
	private static instance: EquipCache = null;
	public static get Instance(): EquipCache {
		if(EquipCache.instance === null) {
			EquipCache.instance = new EquipCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EquipCache.instance.fileName);
			EquipCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EquipCache.instance;
	}
	public get fileName(): string {
		return "Z装备表_Equip";
	}
	protected createInstance(): CFG_Equip {
		return new CFG_Equip();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Equip implements IDesignData {
	//装备ID
	protected equipId: number = 0;
	//装备大类
	protected bigType: number = 0;
	//装备小类
	protected type: number = 0;
	//装备名称
	protected nameLang: string = "";
	//备注
	protected beizhu: string = "";
	//装备图标
	protected icon: string = "";
	//装备品质
	protected quality: number = 0;
	//等级限制
	protected level: number = 0;
	//装备基础属性
	protected attrs: BattAttr[] = [];
	//附加属性库
	protected attrRep: number = 0;
	//是否需要鉴定
	protected appraise: number = 0;
	//基础评分
	protected score: number = 0;
	//分解获得
	protected smeltReward: Reward[] = [];
	//套装ID
	protected suitId: number = 0;
	public Id(): number {
		return this.equipId;
	}
	public getEquipId(): number {
		return this.equipId;
	}
	public getBigType(): number {
		return this.bigType;
	}
	public getType(): number {
		return this.type;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getBeizhu(): string {
		return this.beizhu;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getLevel(): number {
		return this.level;
	}
	public getAttrs(): BattAttr[] {
		return this.attrs;
	}
	public getAttrRep(): number {
		return this.attrRep;
	}
	public getAppraise(): number {
		return this.appraise;
	}
	public getScore(): number {
		return this.score;
	}
	public getSmeltReward(): Reward[] {
		return this.smeltReward;
	}
	public getSuitId(): number {
		return this.suitId;
	}
	private formatAttrs(): BattAttr {
		return new BattAttr();
	}
	private formatSmeltReward(): Reward {
		return new Reward();
	}
}