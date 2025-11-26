
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
export default class SceneStageCache extends DesignCache<CFG_SceneStage> {
	private static instance: SceneStageCache = null;
	public static get Instance(): SceneStageCache {
		if(SceneStageCache.instance === null) {
			SceneStageCache.instance = new SceneStageCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SceneStageCache.instance.fileName);
			SceneStageCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SceneStageCache.instance;
	}
	public get fileName(): string {
		return "C场景难度阶段表_SceneStage";
	}
	protected createInstance(): CFG_SceneStage {
		return new CFG_SceneStage();
	}

	protected sceneIdStageIndex: Map<number, Map<number, CFG_SceneStage>> = null;


	protected loadAutoGenerate(): void {
		//构建索引sceneIdStageIndex
		let sceneIdStageIndex: Map<number, Map<number, CFG_SceneStage>> = new Map<number, Map<number, CFG_SceneStage>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneStage = this.all()[i];
			let layer1Map: Map<number, CFG_SceneStage> = sceneIdStageIndex.get(data.getSceneId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SceneStage>();
				sceneIdStageIndex.set(data.getSceneId(), layer1Map);
			}
			layer1Map.set(data.getStage(), data);
		}
		this.sceneIdStageIndex = sceneIdStageIndex;
	}

	public getInSceneIdStageIndex(sceneId: number, stage: number): CFG_SceneStage {
		let layer1Map: Map<number, CFG_SceneStage> = this.sceneIdStageIndex.get(sceneId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SceneStageCache.getInSceneIdStageIndex", sceneId, stage);
		}
		let t: CFG_SceneStage = layer1Map.get(stage);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SceneStageCache.getInSceneIdStageIndex", sceneId, stage);
		}
		return t;
	}

	public findInSceneIdStageIndex(sceneId: number, stage: number): CFG_SceneStage {
		let layer1Map: Map<number, CFG_SceneStage> = this.sceneIdStageIndex.get(sceneId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SceneStage = layer1Map.get(stage);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SceneStage implements IDesignData {
	//序号
	protected id: number = 0;
	//场景ID
	protected sceneId: number = 0;
	//难度阶段
	protected stage: number = 0;
	//首通奖励
	protected firstRewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getStage(): number {
		return this.stage;
	}
	public getFirstRewards(): Reward[] {
		return this.firstRewards;
	}
	private formatFirstRewards(): Reward {
		return new Reward();
	}
}