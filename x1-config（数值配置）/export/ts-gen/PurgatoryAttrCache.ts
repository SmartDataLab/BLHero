
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
export default class PurgatoryAttrCache extends DesignCache<CFG_PurgatoryAttr> {
	private static instance: PurgatoryAttrCache = null;
	public static get Instance(): PurgatoryAttrCache {
		if(PurgatoryAttrCache.instance === null) {
			PurgatoryAttrCache.instance = new PurgatoryAttrCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatoryAttrCache.instance.fileName);
			PurgatoryAttrCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatoryAttrCache.instance;
	}
	public get fileName(): string {
		return "L炼狱属性库_PurgatoryAttr";
	}
	protected createInstance(): CFG_PurgatoryAttr {
		return new CFG_PurgatoryAttr();
	}


	protected stashIdCollector: Map<number, CFG_PurgatoryAttr[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器stashIdCollector
		let stashIdCollector: Map<number, CFG_PurgatoryAttr[]> = new Map<number, CFG_PurgatoryAttr[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_PurgatoryAttr = this.all()[i];
			let collector: CFG_PurgatoryAttr[] = stashIdCollector.get(data.getStashId());
			if(collector === undefined) {
				collector = [];
				stashIdCollector.set(data.getStashId(), collector);
			}
			collector.push(data);
		}
		this.stashIdCollector = stashIdCollector;
	}



	public getInStashIdCollector(stashId: number) : CFG_PurgatoryAttr[] {
		let ts: CFG_PurgatoryAttr[] = this.stashIdCollector.get(stashId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("PurgatoryAttrCache.getInStashIdCollector", stashId);
		}
		return ts;
	}

	public findInStashIdCollector(stashId: number) : CFG_PurgatoryAttr[] {
		let ts: CFG_PurgatoryAttr[] = this.stashIdCollector.get(stashId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_PurgatoryAttr implements IDesignData {
	//序号
	protected id: number = 0;
	//属性库ID
	protected stashId: number = 0;
	//属性
	protected attr: BattAttr = null;
	//权重
	protected weight: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getStashId(): number {
		return this.stashId;
	}
	public getAttr(): BattAttr {
		return this.attr;
	}
	public getWeight(): number {
		return this.weight;
	}
	private formatAttr(): BattAttr {
		return new BattAttr();
	}
}