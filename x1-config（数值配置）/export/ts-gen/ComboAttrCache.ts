
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
export default class ComboAttrCache extends DesignCache<CFG_ComboAttr> {
	private static instance: ComboAttrCache = null;
	public static get Instance(): ComboAttrCache {
		if(ComboAttrCache.instance === null) {
			ComboAttrCache.instance = new ComboAttrCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ComboAttrCache.instance.fileName);
			ComboAttrCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ComboAttrCache.instance;
	}
	public get fileName(): string {
		return "L连击Buff_ComboAttr";
	}
	protected createInstance(): CFG_ComboAttr {
		return new CFG_ComboAttr();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ComboAttr implements IDesignData {
	//连击次数
	protected num: number = 0;
	//buffId
	protected buffId: number = 0;
	//断连时间/毫秒
	protected disconnect: number = 0;
	public Id(): number {
		return this.num;
	}
	public getNum(): number {
		return this.num;
	}
	public getBuffId(): number {
		return this.buffId;
	}
	public getDisconnect(): number {
		return this.disconnect;
	}
}