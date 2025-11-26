
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
export default class EvilTypeCache extends DesignCache<CFG_EvilType> {
	private static instance: EvilTypeCache = null;
	public static get Instance(): EvilTypeCache {
		if(EvilTypeCache.instance === null) {
			EvilTypeCache.instance = new EvilTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EvilTypeCache.instance.fileName);
			EvilTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EvilTypeCache.instance;
	}
	public get fileName(): string {
		return "Y妖傀类型_EvilType";
	}
	protected createInstance(): CFG_EvilType {
		return new CFG_EvilType();
	}


	protected seriesCollector: Map<number, CFG_EvilType[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器seriesCollector
		let seriesCollector: Map<number, CFG_EvilType[]> = new Map<number, CFG_EvilType[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EvilType = this.all()[i];
			let collector: CFG_EvilType[] = seriesCollector.get(data.getSeries());
			if(collector === undefined) {
				collector = [];
				seriesCollector.set(data.getSeries(), collector);
			}
			collector.push(data);
		}
		this.seriesCollector = seriesCollector;
	}



	public getInSeriesCollector(series: number) : CFG_EvilType[] {
		let ts: CFG_EvilType[] = this.seriesCollector.get(series);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("EvilTypeCache.getInSeriesCollector", series);
		}
		return ts;
	}

	public findInSeriesCollector(series: number) : CFG_EvilType[] {
		let ts: CFG_EvilType[] = this.seriesCollector.get(series);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_EvilType implements IDesignData {
	//标识
	protected identity: number = 0;
	//名称
	protected name: string = "";
	//名称
	protected nameLang: string = "";
	//品质
	protected quality: number = 0;
	//系列
	protected series: number = 0;
	public Id(): number {
		return this.identity;
	}
	public getIdentity(): number {
		return this.identity;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getSeries(): number {
		return this.series;
	}
}