
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
export default class EquipattrStashCache extends DesignCache<CFG_EquipattrStash> {
	private static instance: EquipattrStashCache = null;
	public static get Instance(): EquipattrStashCache {
		if(EquipattrStashCache.instance === null) {
			EquipattrStashCache.instance = new EquipattrStashCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EquipattrStashCache.instance.fileName);
			EquipattrStashCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EquipattrStashCache.instance;
	}
	public get fileName(): string {
		return "Z装备附加属性库_EquipattrStash";
	}
	protected createInstance(): CFG_EquipattrStash {
		return new CFG_EquipattrStash();
	}


	protected stashIdCollector: Map<number, CFG_EquipattrStash[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器stashIdCollector
		let stashIdCollector: Map<number, CFG_EquipattrStash[]> = new Map<number, CFG_EquipattrStash[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EquipattrStash = this.all()[i];
			let collector: CFG_EquipattrStash[] = stashIdCollector.get(data.getStashId());
			if(collector === undefined) {
				collector = [];
				stashIdCollector.set(data.getStashId(), collector);
			}
			collector.push(data);
		}
		this.stashIdCollector = stashIdCollector;
	}



	public getInStashIdCollector(stashId: number) : CFG_EquipattrStash[] {
		let ts: CFG_EquipattrStash[] = this.stashIdCollector.get(stashId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("EquipattrStashCache.getInStashIdCollector", stashId);
		}
		return ts;
	}

	public findInStashIdCollector(stashId: number) : CFG_EquipattrStash[] {
		let ts: CFG_EquipattrStash[] = this.stashIdCollector.get(stashId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_EquipattrStash implements IDesignData {
	//序号
	protected id: number = 0;
	//库id
	protected stashId: number = 0;
	//属性类型
	protected type: number = 0;
	//Max属性值
	protected max: number = 0;
	//随机权重
	protected weight: number = 0;
	//评分
	protected score: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getStashId(): number {
		return this.stashId;
	}
	public getType(): number {
		return this.type;
	}
	public getMax(): number {
		return this.max;
	}
	public getWeight(): number {
		return this.weight;
	}
	public getScore(): number {
		return this.score;
	}
}