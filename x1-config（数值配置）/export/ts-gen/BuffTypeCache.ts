
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
export default class BuffTypeCache extends DesignCache<CFG_BuffType> {
	private static instance: BuffTypeCache = null;
	public static get Instance(): BuffTypeCache {
		if(BuffTypeCache.instance === null) {
			BuffTypeCache.instance = new BuffTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BuffTypeCache.instance.fileName);
			BuffTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BuffTypeCache.instance;
	}
	public get fileName(): string {
		return "BBuff类型说明表_BuffType";
	}
	protected createInstance(): CFG_BuffType {
		return new CFG_BuffType();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_BuffType implements IDesignData {
	//序号
	protected id: number = 0;
	//名字
	protected name: string = "";
	//枚举名
	protected enumName: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getEnumName(): string {
		return this.enumName;
	}
}