
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
export default class AccessCache extends DesignCache<CFG_Access> {
	private static instance: AccessCache = null;
	public static get Instance(): AccessCache {
		if(AccessCache.instance === null) {
			AccessCache.instance = new AccessCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + AccessCache.instance.fileName);
			AccessCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return AccessCache.instance;
	}
	public get fileName(): string {
		return "H获取途径_Access";
	}
	protected createInstance(): CFG_Access {
		return new CFG_Access();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Access implements IDesignData {
	//获取途径ID
	protected id: number = 0;
	//获取途径名称
	protected nameLang: string = "";
	//前往类型
	protected funcType: number = 0;
	//建筑ID
	protected npcId: number = 0;
	//界面枚举名字
	protected enumName: string = "";
	//功能表id
	protected functionId: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getFuncType(): number {
		return this.funcType;
	}
	public getNpcId(): number {
		return this.npcId;
	}
	public getEnumName(): string {
		return this.enumName;
	}
	public getFunctionId(): number {
		return this.functionId;
	}
}