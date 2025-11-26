
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
export default class PrivilegeBoostTypeCache extends DesignCache<CFG_PrivilegeBoostType> {
	private static instance: PrivilegeBoostTypeCache = null;
	public static get Instance(): PrivilegeBoostTypeCache {
		if(PrivilegeBoostTypeCache.instance === null) {
			PrivilegeBoostTypeCache.instance = new PrivilegeBoostTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PrivilegeBoostTypeCache.instance.fileName);
			PrivilegeBoostTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PrivilegeBoostTypeCache.instance;
	}
	public get fileName(): string {
		return "VIP特权加成类型表_PrivilegeBoostType";
	}
	protected createInstance(): CFG_PrivilegeBoostType {
		return new CFG_PrivilegeBoostType();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PrivilegeBoostType implements IDesignData {
	//特权类型
	protected type: number = 0;
	//名称
	protected nameLang: string = "";
	//加成途径
	protected path: number[] = [];
	public Id(): number {
		return this.type;
	}
	public getType(): number {
		return this.type;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getPath(): number[] {
		return this.path;
	}
	private formatPath(): number {
		return 0;
	}
}