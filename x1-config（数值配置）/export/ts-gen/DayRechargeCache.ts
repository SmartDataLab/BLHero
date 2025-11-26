
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
export default class DayRechargeCache extends DesignCache<CFG_DayRecharge> {
	private static instance: DayRechargeCache = null;
	public static get Instance(): DayRechargeCache {
		if(DayRechargeCache.instance === null) {
			DayRechargeCache.instance = new DayRechargeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DayRechargeCache.instance.fileName);
			DayRechargeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DayRechargeCache.instance;
	}
	public get fileName(): string {
		return "M每日充值_DayRecharge";
	}
	protected createInstance(): CFG_DayRecharge {
		return new CFG_DayRecharge();
	}

	protected productIdIndex: Map<number, CFG_DayRecharge> = null;

	protected roundCollector: Map<number, CFG_DayRecharge[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引productIdIndex
		let productIdIndex: Map<number, CFG_DayRecharge> = new Map<number, CFG_DayRecharge>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DayRecharge = this.all()[i];
			productIdIndex.set(data.getProductId(), data);
		}
		this.productIdIndex = productIdIndex;
		//构建收集器roundCollector
		let roundCollector: Map<number, CFG_DayRecharge[]> = new Map<number, CFG_DayRecharge[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DayRecharge = this.all()[i];
			let collector: CFG_DayRecharge[] = roundCollector.get(data.getRound());
			if(collector === undefined) {
				collector = [];
				roundCollector.set(data.getRound(), collector);
			}
			collector.push(data);
		}
		this.roundCollector = roundCollector;
	}

	public getInProductIdIndex(productId: number): CFG_DayRecharge {
		let t: CFG_DayRecharge = this.productIdIndex.get(productId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("DayRechargeCache.getInProductIdIndex", productId);
		}
		return t;
	}

	public findInProductIdIndex(productId: number): CFG_DayRecharge {
		let t: CFG_DayRecharge = this.productIdIndex.get(productId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInRoundCollector(round: number) : CFG_DayRecharge[] {
		let ts: CFG_DayRecharge[] = this.roundCollector.get(round);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("DayRechargeCache.getInRoundCollector", round);
		}
		return ts;
	}

	public findInRoundCollector(round: number) : CFG_DayRecharge[] {
		let ts: CFG_DayRecharge[] = this.roundCollector.get(round);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_DayRecharge implements IDesignData {
	//序号
	protected id: number = 0;
	//轮数
	protected round: number = 0;
	//充值商品ID
	protected productId: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRound(): number {
		return this.round;
	}
	public getProductId(): number {
		return this.productId;
	}
}