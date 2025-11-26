
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
export default class NpcModelCache extends DesignCache<CFG_NpcModel> {
	private static instance: NpcModelCache = null;
	public static get Instance(): NpcModelCache {
		if(NpcModelCache.instance === null) {
			NpcModelCache.instance = new NpcModelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + NpcModelCache.instance.fileName);
			NpcModelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return NpcModelCache.instance;
	}
	public get fileName(): string {
		return "NNpc模型表_NpcModel";
	}
	protected createInstance(): CFG_NpcModel {
		return new CFG_NpcModel();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_NpcModel implements IDesignData {
	//npc模型ID
	protected id: number = 0;
	//npc名字
	protected name: string = "";
	//npc名字
	protected nameLang: string = "";
	//形象模型
	protected model: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getModel(): string {
		return this.model;
	}
}