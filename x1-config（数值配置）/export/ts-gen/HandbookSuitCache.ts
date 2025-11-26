
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
export default class HandbookSuitCache extends DesignCache<CFG_HandbookSuit> {
	private static instance: HandbookSuitCache = null;
	public static get Instance(): HandbookSuitCache {
		if(HandbookSuitCache.instance === null) {
			HandbookSuitCache.instance = new HandbookSuitCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HandbookSuitCache.instance.fileName);
			HandbookSuitCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HandbookSuitCache.instance;
	}
	public get fileName(): string {
		return "T图鉴套装属性表_HandbookSuit";
	}
	protected createInstance(): CFG_HandbookSuit {
		return new CFG_HandbookSuit();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HandbookSuit implements IDesignData {
	//序号
	protected id: number = 0;
	//加成属性
	protected attrs: BattAttr[] = [];
	//备注
	protected describe: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getAttrs(): BattAttr[] {
		return this.attrs;
	}
	public getDescribe(): string {
		return this.describe;
	}
	private formatAttrs(): BattAttr {
		return new BattAttr();
	}
}