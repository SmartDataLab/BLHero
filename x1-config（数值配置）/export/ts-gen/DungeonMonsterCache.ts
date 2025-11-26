
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
export default class DungeonMonsterCache extends DesignCache<CFG_DungeonMonster> {
	private static instance: DungeonMonsterCache = null;
	public static get Instance(): DungeonMonsterCache {
		if(DungeonMonsterCache.instance === null) {
			DungeonMonsterCache.instance = new DungeonMonsterCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DungeonMonsterCache.instance.fileName);
			DungeonMonsterCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DungeonMonsterCache.instance;
	}
	public get fileName(): string {
		return "M秘境副本刷怪表_DungeonMonster";
	}
	protected createInstance(): CFG_DungeonMonster {
		return new CFG_DungeonMonster();
	}

	protected sceneIdZoneIdRefreshIdIndex: Map<number, Map<number, Map<number, CFG_DungeonMonster>>> = null;


	protected loadAutoGenerate(): void {
		//构建索引sceneIdZoneIdRefreshIdIndex
		let sceneIdZoneIdRefreshIdIndex: Map<number, Map<number, Map<number, CFG_DungeonMonster>>> = new Map<number, Map<number, Map<number, CFG_DungeonMonster>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DungeonMonster = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_DungeonMonster>> = sceneIdZoneIdRefreshIdIndex.get(data.getSceneId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_DungeonMonster>>();
				sceneIdZoneIdRefreshIdIndex.set(data.getSceneId(), layer1Map);
			}
			let layer2Map: Map<number, CFG_DungeonMonster> = layer1Map.get(data.getZoneId());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_DungeonMonster>();
				layer1Map.set(data.getZoneId(), layer2Map);
			}
			layer2Map.set(data.getRefreshId(), data);
		}
		this.sceneIdZoneIdRefreshIdIndex = sceneIdZoneIdRefreshIdIndex;
	}

	public getInSceneIdZoneIdRefreshIdIndex(sceneId: number, zoneId: number, refreshId: number): CFG_DungeonMonster {
		let layer1Map: Map<number, Map<number, CFG_DungeonMonster>> = this.sceneIdZoneIdRefreshIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		let layer2Map: Map<number, CFG_DungeonMonster> = layer1Map.get(zoneId);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		let t: CFG_DungeonMonster = layer2Map.get(refreshId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		return t;
	}

	public findInSceneIdZoneIdRefreshIdIndex(sceneId: number, zoneId: number, refreshId: number): CFG_DungeonMonster {
		let layer1Map: Map<number, Map<number, CFG_DungeonMonster>> = this.sceneIdZoneIdRefreshIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_DungeonMonster> = layer1Map.get(zoneId);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_DungeonMonster = layer2Map.get(refreshId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_DungeonMonster implements IDesignData {
	//序号
	protected id: number = 0;
	//副本ID
	protected sceneId: number = 0;
	//场景区域ID
	protected zoneId: number = 0;
	//怪物刷新点
	protected refreshId: number = 0;
	//怪物ID
	protected monsterId: number = 0;
	//刷怪点类型（0固定 1随机）
	protected refreshType: number = 0;
	//刷怪数量
	protected num: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getZoneId(): number {
		return this.zoneId;
	}
	public getRefreshId(): number {
		return this.refreshId;
	}
	public getMonsterId(): number {
		return this.monsterId;
	}
	public getRefreshType(): number {
		return this.refreshType;
	}
	public getNum(): number {
		return this.num;
	}
}