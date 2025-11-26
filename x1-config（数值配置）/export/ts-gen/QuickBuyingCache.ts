
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
export default class QuickBuyingCache extends DesignCache<CFG_QuickBuying> {
	private static instance: QuickBuyingCache = null;
	public static get Instance(): QuickBuyingCache {
		if(QuickBuyingCache.instance === null) {
			QuickBuyingCache.instance = new QuickBuyingCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + QuickBuyingCache.instance.fileName);
			QuickBuyingCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return QuickBuyingCache.instance;
	}
	public get fileName(): string {
		return "K快捷购买表_QuickBuying";
	}
	protected createInstance(): CFG_QuickBuying {
		return new CFG_QuickBuying();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_QuickBuying implements IDesignData {
	//道具ID
	protected id: number = 0;
	//道具名字
	protected nameLang: string = "";
	//每份数量
	protected num: number = 0;
	//购买消耗
	protected cost: Cost = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getNum(): number {
		return this.num;
	}
	public getCost(): Cost {
		return this.cost;
	}
	private formatCost(): Cost {
		return new Cost();
	}
}