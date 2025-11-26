
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
export default class SceneFogCache extends DesignCache<CFG_SceneFog> {
	private static instance: SceneFogCache = null;
	public static get Instance(): SceneFogCache {
		if(SceneFogCache.instance === null) {
			SceneFogCache.instance = new SceneFogCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SceneFogCache.instance.fileName);
			SceneFogCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SceneFogCache.instance;
	}
	public get fileName(): string {
		return "Z主线野外迷雾表_SceneFog";
	}
	protected createInstance(): CFG_SceneFog {
		return new CFG_SceneFog();
	}

	protected mainlineIdFogIdIndex: Map<number, Map<number, CFG_SceneFog>> = null;


	protected loadAutoGenerate(): void {
		//构建索引mainlineIdFogIdIndex
		let mainlineIdFogIdIndex: Map<number, Map<number, CFG_SceneFog>> = new Map<number, Map<number, CFG_SceneFog>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneFog = this.all()[i];
			let layer1Map: Map<number, CFG_SceneFog> = mainlineIdFogIdIndex.get(data.getMainlineId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SceneFog>();
				mainlineIdFogIdIndex.set(data.getMainlineId(), layer1Map);
			}
			layer1Map.set(data.getFogId(), data);
		}
		this.mainlineIdFogIdIndex = mainlineIdFogIdIndex;
	}

	public getInMainlineIdFogIdIndex(mainlineId: number, fogId: number): CFG_SceneFog {
		let layer1Map: Map<number, CFG_SceneFog> = this.mainlineIdFogIdIndex.get(mainlineId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SceneFogCache.getInMainlineIdFogIdIndex", mainlineId, fogId);
		}
		let t: CFG_SceneFog = layer1Map.get(fogId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SceneFogCache.getInMainlineIdFogIdIndex", mainlineId, fogId);
		}
		return t;
	}

	public findInMainlineIdFogIdIndex(mainlineId: number, fogId: number): CFG_SceneFog {
		let layer1Map: Map<number, CFG_SceneFog> = this.mainlineIdFogIdIndex.get(mainlineId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SceneFog = layer1Map.get(fogId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SceneFog implements IDesignData {
	//序号
	protected id: number = 0;
	//场景ID
	protected mainlineId: number = 0;
	//迷雾点ID
	protected fogId: number = 0;
	//开启消耗
	protected openCosts: Cost[] = [];
	//开启奖励
	protected openRewards: Reward = null;
	//开启等级
	protected openLevel: number = 0;
	//NPC模型状态处理
	protected activeOperate: number = 0;
	//解锁点所在迷雾区域
	protected unlockArea: number = 0;
	//解锁进度
	protected chapter: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getMainlineId(): number {
		return this.mainlineId;
	}
	public getFogId(): number {
		return this.fogId;
	}
	public getOpenCosts(): Cost[] {
		return this.openCosts;
	}
	public getOpenRewards(): Reward {
		return this.openRewards;
	}
	public getOpenLevel(): number {
		return this.openLevel;
	}
	public getActiveOperate(): number {
		return this.activeOperate;
	}
	public getUnlockArea(): number {
		return this.unlockArea;
	}
	public getChapter(): string {
		return this.chapter;
	}
	private formatOpenCosts(): Cost {
		return new Cost();
	}
	private formatOpenRewards(): Reward {
		return new Reward();
	}
}