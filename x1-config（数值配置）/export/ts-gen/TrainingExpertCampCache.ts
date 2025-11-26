
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
export default class TrainingExpertCampCache extends DesignCache<CFG_TrainingExpertCamp> {
	private static instance: TrainingExpertCampCache = null;
	public static get Instance(): TrainingExpertCampCache {
		if(TrainingExpertCampCache.instance === null) {
			TrainingExpertCampCache.instance = new TrainingExpertCampCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TrainingExpertCampCache.instance.fileName);
			TrainingExpertCampCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TrainingExpertCampCache.instance;
	}
	public get fileName(): string {
		return "X训练营高级属性加成表_TrainingExpertCamp";
	}
	protected createInstance(): CFG_TrainingExpertCamp {
		return new CFG_TrainingExpertCamp();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_TrainingExpertCamp implements IDesignData {
	//高级训练营等级
	protected id: number = 0;
	//需要角色等级
	protected level: number = 0;
	//加成类型
	protected type: number = 0;
	//加成增量
	protected num: number = 0;
	//加成详情
	protected buff: number = 0;
	//消耗道具
	protected costThing: Cost = null;
	//属性名称
	protected attrNameLang: string = "";
	//属性描述
	protected attrDescLang: string = "";
	//图标
	protected icon: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLevel(): number {
		return this.level;
	}
	public getType(): number {
		return this.type;
	}
	public getNum(): number {
		return this.num;
	}
	public getBuff(): number {
		return this.buff;
	}
	public getCostThing(): Cost {
		return this.costThing;
	}
	public getAttrNameLang(): string {
		return this.attrNameLang;
	}
	public getAttrDescLang(): string {
		return this.attrDescLang;
	}
	public getIcon(): string {
		return this.icon;
	}
	private formatCostThing(): Cost {
		return new Cost();
	}
}