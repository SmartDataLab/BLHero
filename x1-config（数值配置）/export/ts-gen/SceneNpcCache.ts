
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
export default class SceneNpcCache extends DesignCache<CFG_SceneNpc> {
	private static instance: SceneNpcCache = null;
	public static get Instance(): SceneNpcCache {
		if(SceneNpcCache.instance === null) {
			SceneNpcCache.instance = new SceneNpcCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SceneNpcCache.instance.fileName);
			SceneNpcCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SceneNpcCache.instance;
	}
	public get fileName(): string {
		return "Z主线野外NPC表_SceneNpc";
	}
	protected createInstance(): CFG_SceneNpc {
		return new CFG_SceneNpc();
	}

	protected sceneIdNpcIdIndex: Map<number, Map<number, CFG_SceneNpc>> = null;


	protected loadAutoGenerate(): void {
		//构建索引sceneIdNpcIdIndex
		let sceneIdNpcIdIndex: Map<number, Map<number, CFG_SceneNpc>> = new Map<number, Map<number, CFG_SceneNpc>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SceneNpc = this.all()[i];
			let layer1Map: Map<number, CFG_SceneNpc> = sceneIdNpcIdIndex.get(data.getSceneId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_SceneNpc>();
				sceneIdNpcIdIndex.set(data.getSceneId(), layer1Map);
			}
			layer1Map.set(data.getNpcId(), data);
		}
		this.sceneIdNpcIdIndex = sceneIdNpcIdIndex;
	}

	public getInSceneIdNpcIdIndex(sceneId: number, npcId: number): CFG_SceneNpc {
		let layer1Map: Map<number, CFG_SceneNpc> = this.sceneIdNpcIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("SceneNpcCache.getInSceneIdNpcIdIndex", sceneId, npcId);
		}
		let t: CFG_SceneNpc = layer1Map.get(npcId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SceneNpcCache.getInSceneIdNpcIdIndex", sceneId, npcId);
		}
		return t;
	}

	public findInSceneIdNpcIdIndex(sceneId: number, npcId: number): CFG_SceneNpc {
		let layer1Map: Map<number, CFG_SceneNpc> = this.sceneIdNpcIdIndex.get(sceneId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_SceneNpc = layer1Map.get(npcId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SceneNpc implements IDesignData {
	//序号
	protected id: number = 0;
	//场景ID
	protected sceneId: number = 0;
	//npcId
	protected npcId: number = 0;
	//NPC名称
	protected npcName: string = "";
	//NPC名称
	protected npcNameLang: string = "";
	//完成消耗
	protected questCosts: Cost[] = [];
	//完成奖励
	protected finishRewards: Reward[] = [];
	//NPC模型状态处理
	protected activeOperate: number = 0;
	//模型ID
	protected model: number = 0;
	//解锁点所在迷雾区域
	protected unlockArea: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getNpcId(): number {
		return this.npcId;
	}
	public getNpcName(): string {
		return this.npcName;
	}
	public getNpcNameLang(): string {
		return this.npcNameLang;
	}
	public getQuestCosts(): Cost[] {
		return this.questCosts;
	}
	public getFinishRewards(): Reward[] {
		return this.finishRewards;
	}
	public getActiveOperate(): number {
		return this.activeOperate;
	}
	public getModel(): number {
		return this.model;
	}
	public getUnlockArea(): number {
		return this.unlockArea;
	}
	private formatQuestCosts(): Cost {
		return new Cost();
	}
	private formatFinishRewards(): Reward {
		return new Reward();
	}
}