
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
export default class EvilSuitCache extends DesignCache<CFG_EvilSuit> {
	private static instance: EvilSuitCache = null;
	public static get Instance(): EvilSuitCache {
		if(EvilSuitCache.instance === null) {
			EvilSuitCache.instance = new EvilSuitCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EvilSuitCache.instance.fileName);
			EvilSuitCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EvilSuitCache.instance;
	}
	public get fileName(): string {
		return "Y妖傀套装_EvilSuit";
	}
	protected createInstance(): CFG_EvilSuit {
		return new CFG_EvilSuit();
	}


	protected suitIdCollector: Map<number, CFG_EvilSuit[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器suitIdCollector
		let suitIdCollector: Map<number, CFG_EvilSuit[]> = new Map<number, CFG_EvilSuit[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EvilSuit = this.all()[i];
			let collector: CFG_EvilSuit[] = suitIdCollector.get(data.getSuitId());
			if(collector === undefined) {
				collector = [];
				suitIdCollector.set(data.getSuitId(), collector);
			}
			collector.push(data);
		}
		this.suitIdCollector = suitIdCollector;
	}



	public getInSuitIdCollector(suitId: number) : CFG_EvilSuit[] {
		let ts: CFG_EvilSuit[] = this.suitIdCollector.get(suitId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("EvilSuitCache.getInSuitIdCollector", suitId);
		}
		return ts;
	}

	public findInSuitIdCollector(suitId: number) : CFG_EvilSuit[] {
		let ts: CFG_EvilSuit[] = this.suitIdCollector.get(suitId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_EvilSuit implements IDesignData {
	//序号
	protected id: number = 0;
	//套装Id
	protected suitId: number = 0;
	//套装数量
	protected suitNum: number = 0;
	//装备套装属性
	protected attrs: BattAttr[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSuitId(): number {
		return this.suitId;
	}
	public getSuitNum(): number {
		return this.suitNum;
	}
	public getAttrs(): BattAttr[] {
		return this.attrs;
	}
	private formatAttrs(): BattAttr {
		return new BattAttr();
	}
}