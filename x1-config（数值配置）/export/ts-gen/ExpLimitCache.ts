
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
export default class ExpLimitCache extends DesignCache<CFG_ExpLimit> {
	private static instance: ExpLimitCache = null;
	public static get Instance(): ExpLimitCache {
		if(ExpLimitCache.instance === null) {
			ExpLimitCache.instance = new ExpLimitCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ExpLimitCache.instance.fileName);
			ExpLimitCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ExpLimitCache.instance;
	}
	public get fileName(): string {
		return "J角色经验获取限制_ExpLimit";
	}
	protected createInstance(): CFG_ExpLimit {
		return new CFG_ExpLimit();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ExpLimit implements IDesignData {
	//序号
	protected id: number = 0;
	//最小限制
	protected limitMin: number = 0;
	//最大限制
	protected limitMax: number = 0;
	//经验加成
	protected expAdd: number = 0;
	//名字
	protected name: string = "";
	//名字
	protected nameLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLimitMin(): number {
		return this.limitMin;
	}
	public getLimitMax(): number {
		return this.limitMax;
	}
	public getExpAdd(): number {
		return this.expAdd;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
}