
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
export default class PrivilegeBoostCache extends DesignCache<CFG_PrivilegeBoost> {
	private static instance: PrivilegeBoostCache = null;
	public static get Instance(): PrivilegeBoostCache {
		if(PrivilegeBoostCache.instance === null) {
			PrivilegeBoostCache.instance = new PrivilegeBoostCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PrivilegeBoostCache.instance.fileName);
			PrivilegeBoostCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PrivilegeBoostCache.instance;
	}
	public get fileName(): string {
		return "VIP特权表_PrivilegeBoost";
	}
	protected createInstance(): CFG_PrivilegeBoost {
		return new CFG_PrivilegeBoost();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PrivilegeBoost implements IDesignData {
	//特权id
	protected type: number = 0;
	//名称
	protected name: string = "";
	//特权类型
	protected privilege: Keyv[] = [];
	public Id(): number {
		return this.type;
	}
	public getType(): number {
		return this.type;
	}
	public getName(): string {
		return this.name;
	}
	public getPrivilege(): Keyv[] {
		return this.privilege;
	}
	private formatPrivilege(): Keyv {
		return new Keyv();
	}
}