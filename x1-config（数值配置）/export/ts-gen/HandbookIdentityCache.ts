
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
export default class HandbookIdentityCache extends DesignCache<CFG_HandbookIdentity> {
	private static instance: HandbookIdentityCache = null;
	public static get Instance(): HandbookIdentityCache {
		if(HandbookIdentityCache.instance === null) {
			HandbookIdentityCache.instance = new HandbookIdentityCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HandbookIdentityCache.instance.fileName);
			HandbookIdentityCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HandbookIdentityCache.instance;
	}
	public get fileName(): string {
		return "T图鉴列表配置表_HandbookIdentity";
	}
	protected createInstance(): CFG_HandbookIdentity {
		return new CFG_HandbookIdentity();
	}


	protected catalogCollector: Map<number, CFG_HandbookIdentity[]> = null;
	protected suitCollector: Map<number, CFG_HandbookIdentity[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器catalogCollector
		let catalogCollector: Map<number, CFG_HandbookIdentity[]> = new Map<number, CFG_HandbookIdentity[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HandbookIdentity = this.all()[i];
			let collector: CFG_HandbookIdentity[] = catalogCollector.get(data.getCatalog());
			if(collector === undefined) {
				collector = [];
				catalogCollector.set(data.getCatalog(), collector);
			}
			collector.push(data);
		}
		this.catalogCollector = catalogCollector;
		//构建收集器suitCollector
		let suitCollector: Map<number, CFG_HandbookIdentity[]> = new Map<number, CFG_HandbookIdentity[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HandbookIdentity = this.all()[i];
			let collector: CFG_HandbookIdentity[] = suitCollector.get(data.getSuit());
			if(collector === undefined) {
				collector = [];
				suitCollector.set(data.getSuit(), collector);
			}
			collector.push(data);
		}
		this.suitCollector = suitCollector;
	}



	public getInCatalogCollector(catalog: number) : CFG_HandbookIdentity[] {
		let ts: CFG_HandbookIdentity[] = this.catalogCollector.get(catalog);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("HandbookIdentityCache.getInCatalogCollector", catalog);
		}
		return ts;
	}
	public getInSuitCollector(suit: number) : CFG_HandbookIdentity[] {
		let ts: CFG_HandbookIdentity[] = this.suitCollector.get(suit);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("HandbookIdentityCache.getInSuitCollector", suit);
		}
		return ts;
	}

	public findInCatalogCollector(catalog: number) : CFG_HandbookIdentity[] {
		let ts: CFG_HandbookIdentity[] = this.catalogCollector.get(catalog);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}
	public findInSuitCollector(suit: number) : CFG_HandbookIdentity[] {
		let ts: CFG_HandbookIdentity[] = this.suitCollector.get(suit);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_HandbookIdentity implements IDesignData {
	//图鉴ID
	protected id: number = 0;
	//图鉴品质
	protected quality: number = 0;
	//升级方式
	protected upType: number = 0;
	//归属层级
	protected catalog: number = 0;
	//套装ID
	protected suit: number = 0;
	//图鉴icon
	protected icon: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getUpType(): number {
		return this.upType;
	}
	public getCatalog(): number {
		return this.catalog;
	}
	public getSuit(): number {
		return this.suit;
	}
	public getIcon(): string {
		return this.icon;
	}
}