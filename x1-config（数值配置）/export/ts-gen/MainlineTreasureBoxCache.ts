
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
export default class MainlineTreasureBoxCache extends DesignCache<CFG_MainlineTreasureBox> {
	private static instance: MainlineTreasureBoxCache = null;
	public static get Instance(): MainlineTreasureBoxCache {
		if(MainlineTreasureBoxCache.instance === null) {
			MainlineTreasureBoxCache.instance = new MainlineTreasureBoxCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MainlineTreasureBoxCache.instance.fileName);
			MainlineTreasureBoxCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MainlineTreasureBoxCache.instance;
	}
	public get fileName(): string {
		return "C场景宝箱配置表_MainlineTreasureBox";
	}
	protected createInstance(): CFG_MainlineTreasureBox {
		return new CFG_MainlineTreasureBox();
	}

	protected typeTypeArgIndex: Map<number, Map<number, CFG_MainlineTreasureBox>> = null;

	protected typeCollector: Map<number, CFG_MainlineTreasureBox[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引typeTypeArgIndex
		let typeTypeArgIndex: Map<number, Map<number, CFG_MainlineTreasureBox>> = new Map<number, Map<number, CFG_MainlineTreasureBox>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_MainlineTreasureBox = this.all()[i];
			let layer1Map: Map<number, CFG_MainlineTreasureBox> = typeTypeArgIndex.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_MainlineTreasureBox>();
				typeTypeArgIndex.set(data.getType(), layer1Map);
			}
			layer1Map.set(data.getTypeArg(), data);
		}
		this.typeTypeArgIndex = typeTypeArgIndex;
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_MainlineTreasureBox[]> = new Map<number, CFG_MainlineTreasureBox[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_MainlineTreasureBox = this.all()[i];
			let collector: CFG_MainlineTreasureBox[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}

	public getInTypeTypeArgIndex(type: number, typeArg: number): CFG_MainlineTreasureBox {
		let layer1Map: Map<number, CFG_MainlineTreasureBox> = this.typeTypeArgIndex.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("MainlineTreasureBoxCache.getInTypeTypeArgIndex", type, typeArg);
		}
		let t: CFG_MainlineTreasureBox = layer1Map.get(typeArg);
		if(t === undefined) {
			throw new DesignDataNotFoundError("MainlineTreasureBoxCache.getInTypeTypeArgIndex", type, typeArg);
		}
		return t;
	}

	public findInTypeTypeArgIndex(type: number, typeArg: number): CFG_MainlineTreasureBox {
		let layer1Map: Map<number, CFG_MainlineTreasureBox> = this.typeTypeArgIndex.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_MainlineTreasureBox = layer1Map.get(typeArg);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInTypeCollector(type: number) : CFG_MainlineTreasureBox[] {
		let ts: CFG_MainlineTreasureBox[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("MainlineTreasureBoxCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_MainlineTreasureBox[] {
		let ts: CFG_MainlineTreasureBox[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_MainlineTreasureBox implements IDesignData {
	//序号
	protected idx: number = 0;
	//宝箱类型
	protected type: number = 0;
	//类型参数
	protected typeArg: number = 0;
	//奖励预览
	protected rewardShow: number[] = [];
	public Id(): number {
		return this.idx;
	}
	public getIdx(): number {
		return this.idx;
	}
	public getType(): number {
		return this.type;
	}
	public getTypeArg(): number {
		return this.typeArg;
	}
	public getRewardShow(): number[] {
		return this.rewardShow;
	}
	private formatRewardShow(): number {
		return 0;
	}
}