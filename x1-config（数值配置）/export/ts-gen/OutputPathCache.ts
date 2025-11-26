
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
export default class OutputPathCache extends DesignCache<CFG_OutputPath> {
	private static instance: OutputPathCache = null;
	public static get Instance(): OutputPathCache {
		if(OutputPathCache.instance === null) {
			OutputPathCache.instance = new OutputPathCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + OutputPathCache.instance.fileName);
			OutputPathCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return OutputPathCache.instance;
	}
	public get fileName(): string {
		return "J奖励产出途径表_OutputPath";
	}
	protected createInstance(): CFG_OutputPath {
		return new CFG_OutputPath();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_OutputPath implements IDesignData {
	//id
	protected id: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
}