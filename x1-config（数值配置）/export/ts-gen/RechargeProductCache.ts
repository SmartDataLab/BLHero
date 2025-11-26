
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
export default class RechargeProductCache extends DesignCache<CFG_RechargeProduct> {
	private static instance: RechargeProductCache = null;
	public static get Instance(): RechargeProductCache {
		if(RechargeProductCache.instance === null) {
			RechargeProductCache.instance = new RechargeProductCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + RechargeProductCache.instance.fileName);
			RechargeProductCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return RechargeProductCache.instance;
	}
	public get fileName(): string {
		return "C充值商品表_RechargeProduct";
	}
	protected createInstance(): CFG_RechargeProduct {
		return new CFG_RechargeProduct();
	}


	protected productTypeCollector: Map<number, CFG_RechargeProduct[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器productTypeCollector
		let productTypeCollector: Map<number, CFG_RechargeProduct[]> = new Map<number, CFG_RechargeProduct[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_RechargeProduct = this.all()[i];
			let collector: CFG_RechargeProduct[] = productTypeCollector.get(data.getProductType());
			if(collector === undefined) {
				collector = [];
				productTypeCollector.set(data.getProductType(), collector);
			}
			collector.push(data);
		}
		this.productTypeCollector = productTypeCollector;
	}



	public getInProductTypeCollector(productType: number) : CFG_RechargeProduct[] {
		let ts: CFG_RechargeProduct[] = this.productTypeCollector.get(productType);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("RechargeProductCache.getInProductTypeCollector", productType);
		}
		return ts;
	}

	public findInProductTypeCollector(productType: number) : CFG_RechargeProduct[] {
		let ts: CFG_RechargeProduct[] = this.productTypeCollector.get(productType);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_RechargeProduct implements IDesignData {
	//充值商品ID
	protected id: number = 0;
	//商品名称
	protected name: string = "";
	//商品名称
	protected nameLang: string = "";
	//商品逻辑类型
	protected productType: number = 0;
	//商品充值参数
	protected productParam: string = "";
	//金额单位分
	protected money: number = 0;
	//商品编码
	protected productCode: string = "";
	//充值奖励
	protected rewards: Reward[] = [];
	//首充额外奖励
	protected firstRewards: Reward[] = [];
	//贵族经验奖励
	protected noble: Reward = null;
	//价格文本
	protected priceTxtLang: string = "";
	//返利
	protected rebate: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getProductType(): number {
		return this.productType;
	}
	public getProductParam(): string {
		return this.productParam;
	}
	public getMoney(): number {
		return this.money;
	}
	public getProductCode(): string {
		return this.productCode;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	public getFirstRewards(): Reward[] {
		return this.firstRewards;
	}
	public getNoble(): Reward {
		return this.noble;
	}
	public getPriceTxtLang(): string {
		return this.priceTxtLang;
	}
	public getRebate(): number {
		return this.rebate;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
	private formatFirstRewards(): Reward {
		return new Reward();
	}
	private formatNoble(): Reward {
		return new Reward();
	}
}