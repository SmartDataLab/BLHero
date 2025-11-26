
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
export default class ShopTypeCache extends DesignCache<CFG_ShopType> {
	private static instance: ShopTypeCache = null;
	public static get Instance(): ShopTypeCache {
		if(ShopTypeCache.instance === null) {
			ShopTypeCache.instance = new ShopTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ShopTypeCache.instance.fileName);
			ShopTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ShopTypeCache.instance;
	}
	public get fileName(): string {
		return "S商店类型_ShopType";
	}
	protected createInstance(): CFG_ShopType {
		return new CFG_ShopType();
	}


	protected shopTypeCollector: Map<number, CFG_ShopType[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器shopTypeCollector
		let shopTypeCollector: Map<number, CFG_ShopType[]> = new Map<number, CFG_ShopType[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ShopType = this.all()[i];
			let collector: CFG_ShopType[] = shopTypeCollector.get(data.getShopType());
			if(collector === undefined) {
				collector = [];
				shopTypeCollector.set(data.getShopType(), collector);
			}
			collector.push(data);
		}
		this.shopTypeCollector = shopTypeCollector;
	}



	public getInShopTypeCollector(shopType: number) : CFG_ShopType[] {
		let ts: CFG_ShopType[] = this.shopTypeCollector.get(shopType);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ShopTypeCache.getInShopTypeCollector", shopType);
		}
		return ts;
	}

	public findInShopTypeCollector(shopType: number) : CFG_ShopType[] {
		let ts: CFG_ShopType[] = this.shopTypeCollector.get(shopType);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ShopType implements IDesignData {
	//商店ID
	protected shopId: number = 0;
	//商店名称
	protected shopNameLang: string = "";
	//重置类型
	protected resetType: number = 0;
	//开启条件
	protected functionId: number = 0;
	//商店类型
	protected shopType: number = 0;
	//展示的消耗货币
	protected showCost: number[] = [];
	//页签排序
	protected sort: number = 0;
	public Id(): number {
		return this.shopId;
	}
	public getShopId(): number {
		return this.shopId;
	}
	public getShopNameLang(): string {
		return this.shopNameLang;
	}
	public getResetType(): number {
		return this.resetType;
	}
	public getFunctionId(): number {
		return this.functionId;
	}
	public getShopType(): number {
		return this.shopType;
	}
	public getShowCost(): number[] {
		return this.showCost;
	}
	public getSort(): number {
		return this.sort;
	}
	private formatShowCost(): number {
		return 0;
	}
}