
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
export default class NpcBuildingCache extends DesignCache<CFG_NpcBuilding> {
	private static instance: NpcBuildingCache = null;
	public static get Instance(): NpcBuildingCache {
		if(NpcBuildingCache.instance === null) {
			NpcBuildingCache.instance = new NpcBuildingCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + NpcBuildingCache.instance.fileName);
			NpcBuildingCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return NpcBuildingCache.instance;
	}
	public get fileName(): string {
		return "J建筑物表_NpcBuilding";
	}
	protected createInstance(): CFG_NpcBuilding {
		return new CFG_NpcBuilding();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_NpcBuilding implements IDesignData {
	//建筑物ID
	protected id: number = 0;
	//建筑物名称
	protected name: string = "";
	//建筑物名称
	protected nameLang: string = "";
	//建筑类型
	protected type: number = 0;
	//缩略图ID
	protected icon: string = "";
	//预制体路径
	protected prefabPath: string = "";
	//前置建筑ID
	protected preBuildingId: number = 0;
	//功能表id
	protected functionId: number = 0;
	//开启条件
	protected openCondition: number = 0;
	//解锁消耗道具
	protected costThing: Cost[] = [];
	//建筑等级
	protected level: number = 0;
	//NPC模型状态处理
	protected activeOperate: number = 0;
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
	public getType(): number {
		return this.type;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getPrefabPath(): string {
		return this.prefabPath;
	}
	public getPreBuildingId(): number {
		return this.preBuildingId;
	}
	public getFunctionId(): number {
		return this.functionId;
	}
	public getOpenCondition(): number {
		return this.openCondition;
	}
	public getCostThing(): Cost[] {
		return this.costThing;
	}
	public getLevel(): number {
		return this.level;
	}
	public getActiveOperate(): number {
		return this.activeOperate;
	}
	private formatCostThing(): Cost {
		return new Cost();
	}
}