
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
export default class EquipSuitAttrCache extends DesignCache<CFG_EquipSuitAttr> {
	private static instance: EquipSuitAttrCache = null;
	public static get Instance(): EquipSuitAttrCache {
		if(EquipSuitAttrCache.instance === null) {
			EquipSuitAttrCache.instance = new EquipSuitAttrCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EquipSuitAttrCache.instance.fileName);
			EquipSuitAttrCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EquipSuitAttrCache.instance;
	}
	public get fileName(): string {
		return "Z装备套装属性表_EquipSuitAttr";
	}
	protected createInstance(): CFG_EquipSuitAttr {
		return new CFG_EquipSuitAttr();
	}


	protected suitIdCollector: Map<number, CFG_EquipSuitAttr[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器suitIdCollector
		let suitIdCollector: Map<number, CFG_EquipSuitAttr[]> = new Map<number, CFG_EquipSuitAttr[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EquipSuitAttr = this.all()[i];
			let collector: CFG_EquipSuitAttr[] = suitIdCollector.get(data.getSuitId());
			if(collector === undefined) {
				collector = [];
				suitIdCollector.set(data.getSuitId(), collector);
			}
			collector.push(data);
		}
		this.suitIdCollector = suitIdCollector;
	}



	public getInSuitIdCollector(suitId: number) : CFG_EquipSuitAttr[] {
		let ts: CFG_EquipSuitAttr[] = this.suitIdCollector.get(suitId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("EquipSuitAttrCache.getInSuitIdCollector", suitId);
		}
		return ts;
	}

	public findInSuitIdCollector(suitId: number) : CFG_EquipSuitAttr[] {
		let ts: CFG_EquipSuitAttr[] = this.suitIdCollector.get(suitId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_EquipSuitAttr implements IDesignData {
	//序号
	protected id: number = 0;
	//套装ID
	protected suitId: number = 0;
	//套装数量
	protected num: number = 0;
	//装备套装属性
	protected attrs: BattAttr = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSuitId(): number {
		return this.suitId;
	}
	public getNum(): number {
		return this.num;
	}
	public getAttrs(): BattAttr {
		return this.attrs;
	}
	private formatAttrs(): BattAttr {
		return new BattAttr();
	}
}