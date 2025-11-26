
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
export default class SceneZoneCache extends DesignCache<CFG_SceneZone> {
	private static instance: SceneZoneCache = null;
	public static get Instance(): SceneZoneCache {
		if(SceneZoneCache.instance === null) {
			SceneZoneCache.instance = new SceneZoneCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SceneZoneCache.instance.fileName);
			SceneZoneCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SceneZoneCache.instance;
	}
	public get fileName(): string {
		return "C场景区域表_SceneZone";
	}
	protected createInstance(): CFG_SceneZone {
		return new CFG_SceneZone();
	}

	protected sceneIdZoneIdIndex: Map<number, Map<number, CFG_SceneZone>> = null;

	protected sceneIdCollector: Map<number, CFG_SceneZone[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引sceneIdZoneIdIndex
		let sceneIdZoneIdIndex: Map<number, Map<number, CFG_SceneZone>> = new Map<number, Map<number, CFG_SceneZone>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneZone = this.all()[i];
			let layer1Map: Map<number, CFG_SceneZone> = sceneIdZoneIdIndex.get(data.getSceneId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SceneZone>();
				sceneIdZoneIdIndex.set(data.getSceneId(), layer1Map);
			}
			layer1Map.set(data.getZoneId(), data);
		}
		this.sceneIdZoneIdIndex = sceneIdZoneIdIndex;
		//构建收集器sceneIdCollector
		let sceneIdCollector: Map<number, CFG_SceneZone[]> = new Map<number, CFG_SceneZone[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneZone = this.all()[i];
			let collector: CFG_SceneZone[] = sceneIdCollector.get(data.getSceneId());
			if(collector === undefined) {
				collector = [];
				sceneIdCollector.set(data.getSceneId(), collector);
			}
			collector.push(data);
		}
		this.sceneIdCollector = sceneIdCollector;
	}

	public getInSceneIdZoneIdIndex(sceneId: number, zoneId: number): CFG_SceneZone {
		let layer1Map: Map<number, CFG_SceneZone> = this.sceneIdZoneIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SceneZoneCache.getInSceneIdZoneIdIndex", sceneId, zoneId);
		}
		let t: CFG_SceneZone = layer1Map.get(zoneId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SceneZoneCache.getInSceneIdZoneIdIndex", sceneId, zoneId);
		}
		return t;
	}

	public findInSceneIdZoneIdIndex(sceneId: number, zoneId: number): CFG_SceneZone {
		let layer1Map: Map<number, CFG_SceneZone> = this.sceneIdZoneIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SceneZone = layer1Map.get(zoneId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInSceneIdCollector(sceneId: number) : CFG_SceneZone[] {
		let ts: CFG_SceneZone[] = this.sceneIdCollector.get(sceneId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("SceneZoneCache.getInSceneIdCollector", sceneId);
		}
		return ts;
	}

	public findInSceneIdCollector(sceneId: number) : CFG_SceneZone[] {
		let ts: CFG_SceneZone[] = this.sceneIdCollector.get(sceneId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_SceneZone implements IDesignData {
	//序号
	protected id: number = 0;
	//场景ID
	protected sceneId: number = 0;
	//场景类型
	protected sceneType: number = 0;
	//区域ID
	protected zoneId: number = 0;
	//地区刷挂数据文件
	protected mapData: string = "";
	//是否安全区
	protected safeZone: number = 0;
	//背景音效
	protected bgMusic: string = "";
	//地图资源
	protected mapResource: string = "";
	//场景名字程序字
	protected nameLang: string = "";
	//场景名字艺术字
	protected mapName: string = "";
	//英雄视野
	protected heroVision: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getSceneType(): number {
		return this.sceneType;
	}
	public getZoneId(): number {
		return this.zoneId;
	}
	public getMapData(): string {
		return this.mapData;
	}
	public getSafeZone(): number {
		return this.safeZone;
	}
	public getBgMusic(): string {
		return this.bgMusic;
	}
	public getMapResource(): string {
		return this.mapResource;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getMapName(): string {
		return this.mapName;
	}
	public getHeroVision(): number {
		return this.heroVision;
	}
}