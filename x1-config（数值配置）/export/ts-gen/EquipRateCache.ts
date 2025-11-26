
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
export default class EquipRateCache extends DesignCache<CFG_EquipRate> {
	private static instance: EquipRateCache = null;
	public static get Instance(): EquipRateCache {
		if(EquipRateCache.instance === null) {
			EquipRateCache.instance = new EquipRateCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EquipRateCache.instance.fileName);
			EquipRateCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EquipRateCache.instance;
	}
	public get fileName(): string {
		return "Z装备品质概率表_EquipRate";
	}
	protected createInstance(): CFG_EquipRate {
		return new CFG_EquipRate();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_EquipRate implements IDesignData {
	//序号
	protected id: number = 0;
	//属性下限
	protected attrDown: number = 0;
	//属性上限
	protected attrUp: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getAttrDown(): number {
		return this.attrDown;
	}
	public getAttrUp(): number {
		return this.attrUp;
	}
}