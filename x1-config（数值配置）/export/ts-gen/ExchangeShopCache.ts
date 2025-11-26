
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
export default class ExchangeShopCache extends DesignCache<CFG_ExchangeShop> {
	private static instance: ExchangeShopCache = null;
	public static get Instance(): ExchangeShopCache {
		if(ExchangeShopCache.instance === null) {
			ExchangeShopCache.instance = new ExchangeShopCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ExchangeShopCache.instance.fileName);
			ExchangeShopCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ExchangeShopCache.instance;
	}
	public get fileName(): string {
		return "D兑换商店_ExchangeShop";
	}
	protected createInstance(): CFG_ExchangeShop {
		return new CFG_ExchangeShop();
	}

	protected shopIdRoundProductIdIndex: Map<number, Map<number, Map<number, CFG_ExchangeShop>>> = null;

	protected shopIdRoundCollector: Map<number, Map<number, CFG_ExchangeShop[]>> = null;

	protected loadAutoGenerate(): void {
		//构建索引shopIdRoundProductIdIndex
		let shopIdRoundProductIdIndex: Map<number, Map<number, Map<number, CFG_ExchangeShop>>> = new Map<number, Map<number, Map<number, CFG_ExchangeShop>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ExchangeShop = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_ExchangeShop>> = shopIdRoundProductIdIndex.get(data.getShopId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_ExchangeShop>>();
				shopIdRoundProductIdIndex.set(data.getShopId(), layer1Map);
			}
			let layer2Map: Map<number, CFG_ExchangeShop> = layer1Map.get(data.getRound());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_ExchangeShop>();
				layer1Map.set(data.getRound(), layer2Map);
			}
			layer2Map.set(data.getProductId(), data);
		}
		this.shopIdRoundProductIdIndex = shopIdRoundProductIdIndex;
		//构建收集器shopIdRoundCollector
		let shopIdRoundCollector: Map<number, Map<number, CFG_ExchangeShop[]>> = new Map<number, Map<number, CFG_ExchangeShop[]>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ExchangeShop = this.all()[i];
			let layer1Map: Map<number, CFG_ExchangeShop[]> = shopIdRoundCollector.get(data.getShopId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_ExchangeShop[]>();
				shopIdRoundCollector.set(data.getShopId(), layer1Map);
			}
			let collector: CFG_ExchangeShop[] = layer1Map.get(data.getRound());
			if(collector === undefined) {
				collector = [];
				layer1Map.set(data.getRound(), collector);
			}
			collector.push(data);
		}
		this.shopIdRoundCollector = shopIdRoundCollector;
	}

	public getInShopIdRoundProductIdIndex(shopId: number, round: number, productId: number): CFG_ExchangeShop {
		let layer1Map: Map<number, Map<number, CFG_ExchangeShop>> = this.shopIdRoundProductIdIndex.get(shopId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		let layer2Map: Map<number, CFG_ExchangeShop> = layer1Map.get(round);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		let t: CFG_ExchangeShop = layer2Map.get(productId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		return t;
	}

	public findInShopIdRoundProductIdIndex(shopId: number, round: number, productId: number): CFG_ExchangeShop {
		let layer1Map: Map<number, Map<number, CFG_ExchangeShop>> = this.shopIdRoundProductIdIndex.get(shopId);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_ExchangeShop> = layer1Map.get(round);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_ExchangeShop = layer2Map.get(productId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInShopIdRoundCollector(shopId: number, round: number) : CFG_ExchangeShop[] {
		let layer1Map: Map<number, CFG_ExchangeShop[]> = this.shopIdRoundCollector.get(shopId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("ExchangeShopCache.getInShopIdRoundCollector", shopId, round);
		}
		let ts: CFG_ExchangeShop[] = layer1Map.get(round);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ExchangeShopCache.getInShopIdRoundCollector", shopId, round);
		}
		return ts;
	}

	public findInShopIdRoundCollector(shopId: number, round: number) : CFG_ExchangeShop[] {
		let layer1Map: Map<number, CFG_ExchangeShop[]> = this.shopIdRoundCollector.get(shopId);
		if(layer1Map === undefined) {
			return null;
		}
		let ts: CFG_ExchangeShop[] = layer1Map.get(round);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ExchangeShop implements IDesignData {
	//序号
	protected id: number = 0;
	//商店ID
	protected shopId: number = 0;
	//期数
	protected round: number = 0;
	//商品ID
	protected productId: number = 0;
	//商品内容
	protected product: Reward = null;
	//开启条件
	protected condition: Keyv = null;
	//限购类型
	protected limitType: number = 0;
	//最大兑换次数
	protected limit: number = 0;
	//兑换消耗
	protected cost: Cost = null;
	//商店子ID
	protected shopSubId: number = 0;
	//免费次数
	protected freeTimes: number = 0;
	//广告次数
	protected advertising: number = 0;
	//开服x天消失
	protected closeDay: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getShopId(): number {
		return this.shopId;
	}
	public getRound(): number {
		return this.round;
	}
	public getProductId(): number {
		return this.productId;
	}
	public getProduct(): Reward {
		return this.product;
	}
	public getCondition(): Keyv {
		return this.condition;
	}
	public getLimitType(): number {
		return this.limitType;
	}
	public getLimit(): number {
		return this.limit;
	}
	public getCost(): Cost {
		return this.cost;
	}
	public getShopSubId(): number {
		return this.shopSubId;
	}
	public getFreeTimes(): number {
		return this.freeTimes;
	}
	public getAdvertising(): number {
		return this.advertising;
	}
	public getCloseDay(): number {
		return this.closeDay;
	}
	private formatProduct(): Reward {
		return new Reward();
	}
	private formatCondition(): Keyv {
		return new Keyv();
	}
	private formatCost(): Cost {
		return new Cost();
	}
}