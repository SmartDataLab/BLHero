
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
export default class TrainingGeneralCampCache extends DesignCache<CFG_TrainingGeneralCamp> {
	private static instance: TrainingGeneralCampCache = null;
	public static get Instance(): TrainingGeneralCampCache {
		if(TrainingGeneralCampCache.instance === null) {
			TrainingGeneralCampCache.instance = new TrainingGeneralCampCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TrainingGeneralCampCache.instance.fileName);
			TrainingGeneralCampCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TrainingGeneralCampCache.instance;
	}
	public get fileName(): string {
		return "X训练营普通属性加成表_TrainingGeneralCamp";
	}
	protected createInstance(): CFG_TrainingGeneralCamp {
		return new CFG_TrainingGeneralCamp();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_TrainingGeneralCamp implements IDesignData {
	//训练等级
	protected id: number = 0;
	//角色等级
	protected level: number = 0;
	//属性字段名
	protected attr: BattAttr = null;
	//消耗道具
	protected costThing: Cost = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLevel(): number {
		return this.level;
	}
	public getAttr(): BattAttr {
		return this.attr;
	}
	public getCostThing(): Cost {
		return this.costThing;
	}
	private formatAttr(): BattAttr {
		return new BattAttr();
	}
	private formatCostThing(): Cost {
		return new Cost();
	}
}