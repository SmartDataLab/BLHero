
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
export default class GiftCodeCache extends DesignCache<CFG_GiftCode> {
	private static instance: GiftCodeCache = null;
	public static get Instance(): GiftCodeCache {
		if(GiftCodeCache.instance === null) {
			GiftCodeCache.instance = new GiftCodeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GiftCodeCache.instance.fileName);
			GiftCodeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GiftCodeCache.instance;
	}
	public get fileName(): string {
		return "D兑换码礼包表_GiftCode";
	}
	protected createInstance(): CFG_GiftCode {
		return new CFG_GiftCode();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_GiftCode implements IDesignData {
	//兑换码礼包ID
	protected id: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
}