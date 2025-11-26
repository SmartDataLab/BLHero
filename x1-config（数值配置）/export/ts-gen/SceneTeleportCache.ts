
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
export default class SceneTeleportCache extends DesignCache<CFG_SceneTeleport> {
	private static instance: SceneTeleportCache = null;
	public static get Instance(): SceneTeleportCache {
		if(SceneTeleportCache.instance === null) {
			SceneTeleportCache.instance = new SceneTeleportCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SceneTeleportCache.instance.fileName);
			SceneTeleportCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SceneTeleportCache.instance;
	}
	public get fileName(): string {
		return "Z主线野外传送点表_SceneTeleport";
	}
	protected createInstance(): CFG_SceneTeleport {
		return new CFG_SceneTeleport();
	}

	protected mainlineIdTeleportIdIndex: Map<number, Map<number, CFG_SceneTeleport>> = null;


	protected loadAutoGenerate(): void {
		//构建索引mainlineIdTeleportIdIndex
		let mainlineIdTeleportIdIndex: Map<number, Map<number, CFG_SceneTeleport>> = new Map<number, Map<number, CFG_SceneTeleport>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneTeleport = this.all()[i];
			let layer1Map: Map<number, CFG_SceneTeleport> = mainlineIdTeleportIdIndex.get(data.getMainlineId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SceneTeleport>();
				mainlineIdTeleportIdIndex.set(data.getMainlineId(), layer1Map);
			}
			layer1Map.set(data.getTeleportId(), data);
		}
		this.mainlineIdTeleportIdIndex = mainlineIdTeleportIdIndex;
	}

	public getInMainlineIdTeleportIdIndex(mainlineId: number, teleportId: number): CFG_SceneTeleport {
		let layer1Map: Map<number, CFG_SceneTeleport> = this.mainlineIdTeleportIdIndex.get(mainlineId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SceneTeleportCache.getInMainlineIdTeleportIdIndex", mainlineId, teleportId);
		}
		let t: CFG_SceneTeleport = layer1Map.get(teleportId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SceneTeleportCache.getInMainlineIdTeleportIdIndex", mainlineId, teleportId);
		}
		return t;
	}

	public findInMainlineIdTeleportIdIndex(mainlineId: number, teleportId: number): CFG_SceneTeleport {
		let layer1Map: Map<number, CFG_SceneTeleport> = this.mainlineIdTeleportIdIndex.get(mainlineId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SceneTeleport = layer1Map.get(teleportId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SceneTeleport implements IDesignData {
	//序号
	protected id: number = 0;
	//主线场景ID
	protected mainlineId: number = 0;
	//传送点ID
	protected teleportId: number = 0;
	//传送点名字
	protected teleportNameLang: string = "";
	//开启消耗
	protected openCosts: Cost[] = [];
	//推荐战力
	protected fighting: number = 0;
	//归属大陆
	protected belongLand: number = 0;
	//大陆名字
	protected belongNameLang: string = "";
	//传送点分组
	protected group: number = 0;
	//分组名字
	protected groupNameLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getMainlineId(): number {
		return this.mainlineId;
	}
	public getTeleportId(): number {
		return this.teleportId;
	}
	public getTeleportNameLang(): string {
		return this.teleportNameLang;
	}
	public getOpenCosts(): Cost[] {
		return this.openCosts;
	}
	public getFighting(): number {
		return this.fighting;
	}
	public getBelongLand(): number {
		return this.belongLand;
	}
	public getBelongNameLang(): string {
		return this.belongNameLang;
	}
	public getGroup(): number {
		return this.group;
	}
	public getGroupNameLang(): string {
		return this.groupNameLang;
	}
	private formatOpenCosts(): Cost {
		return new Cost();
	}
}