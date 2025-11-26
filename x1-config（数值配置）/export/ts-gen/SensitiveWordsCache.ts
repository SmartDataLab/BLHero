
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
export default class SensitiveWordsCache extends DesignCache<CFG_SensitiveWords> {
	private static instance: SensitiveWordsCache = null;
	public static get Instance(): SensitiveWordsCache {
		if(SensitiveWordsCache.instance === null) {
			SensitiveWordsCache.instance = new SensitiveWordsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SensitiveWordsCache.instance.fileName);
			SensitiveWordsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SensitiveWordsCache.instance;
	}
	public get fileName(): string {
		return "M敏感词库表统计_SensitiveWords";
	}
	protected createInstance(): CFG_SensitiveWords {
		return new CFG_SensitiveWords();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_SensitiveWords implements IDesignData {
	//ID
	protected id: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
}