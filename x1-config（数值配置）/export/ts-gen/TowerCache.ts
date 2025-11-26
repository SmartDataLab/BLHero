
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
export default class TowerCache extends DesignCache<CFG_Tower> {
	private static instance: TowerCache = null;
	public static get Instance(): TowerCache {
		if(TowerCache.instance === null) {
			TowerCache.instance = new TowerCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TowerCache.instance.fileName);
			TowerCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TowerCache.instance;
	}
	public get fileName(): string {
		return "T通天塔_Tower";
	}
	protected createInstance(): CFG_Tower {
		return new CFG_Tower();
	}

	protected typeLayerIndex: Map<number, Map<number, CFG_Tower>> = null;


	protected loadAutoGenerate(): void {
		//构建索引typeLayerIndex
		let typeLayerIndex: Map<number, Map<number, CFG_Tower>> = new Map<number, Map<number, CFG_Tower>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_Tower = this.all()[i];
			let layer1Map: Map<number, CFG_Tower> = typeLayerIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_Tower>();
				typeLayerIndex.set(data.getType(), layer1Map);
			}
			layer1Map.set(data.getLayer(), data);
		}
		this.typeLayerIndex = typeLayerIndex;
	}

	public getInTypeLayerIndex(type: number, layer: number): CFG_Tower {
		let layer1Map: Map<number, CFG_Tower> = this.typeLayerIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("TowerCache.getInTypeLayerIndex", type, layer);
		}
		let t: CFG_Tower = layer1Map.get(layer);
		if(t === undefined) {
			throw new DesignDataNotFoundError("TowerCache.getInTypeLayerIndex", type, layer);
		}
		return t;
	}

	public findInTypeLayerIndex(type: number, layer: number): CFG_Tower {
		let layer1Map: Map<number, CFG_Tower> = this.typeLayerIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_Tower = layer1Map.get(layer);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_Tower implements IDesignData {
	//序号
	protected id: number = 0;
	//塔类型
	protected type: number = 0;
	//层数
	protected layer: number = 0;
	//通关奖励
	protected rewards: Reward[] = [];
	//怪物
	protected monster: monster[] = [];
	//刷怪点类型（0固定 1随机）
	protected refreshType: number = 0;
	//展示模型
	protected modelShow: monster[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getLayer(): number {
		return this.layer;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	public getMonster(): monster[] {
		return this.monster;
	}
	public getRefreshType(): number {
		return this.refreshType;
	}
	public getModelShow(): monster[] {
		return this.modelShow;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
	private formatMonster(): monster {
		return new monster();
	}
	private formatModelShow(): monster {
		return new monster();
	}
}