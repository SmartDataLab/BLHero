
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
export default class PurgatoryRefineCache extends DesignCache<CFG_PurgatoryRefine> {
	private static instance: PurgatoryRefineCache = null;
	public static get Instance(): PurgatoryRefineCache {
		if(PurgatoryRefineCache.instance === null) {
			PurgatoryRefineCache.instance = new PurgatoryRefineCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatoryRefineCache.instance.fileName);
			PurgatoryRefineCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatoryRefineCache.instance;
	}
	public get fileName(): string {
		return "L炼狱洗练消耗_PurgatoryRefine";
	}
	protected createInstance(): CFG_PurgatoryRefine {
		return new CFG_PurgatoryRefine();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PurgatoryRefine implements IDesignData {
	//洗练次数
	protected times: number = 0;
	//通关消耗
	protected passCost: Cost = null;
	//消耗
	protected cost: Cost = null;
	public Id(): number {
		return this.times;
	}
	public getTimes(): number {
		return this.times;
	}
	public getPassCost(): Cost {
		return this.passCost;
	}
	public getCost(): Cost {
		return this.cost;
	}
	private formatPassCost(): Cost {
		return new Cost();
	}
	private formatCost(): Cost {
		return new Cost();
	}
}