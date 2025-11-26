
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
export default class XianShiLiBaoCache extends DesignCache<CFG_XianShiLiBao> {
	private static instance: XianShiLiBaoCache = null;
	public static get Instance(): XianShiLiBaoCache {
		if(XianShiLiBaoCache.instance === null) {
			XianShiLiBaoCache.instance = new XianShiLiBaoCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + XianShiLiBaoCache.instance.fileName);
			XianShiLiBaoCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return XianShiLiBaoCache.instance;
	}
	public get fileName(): string {
		return "P1005限时礼包_XianShiLiBao";
	}
	protected createInstance(): CFG_XianShiLiBao {
		return new CFG_XianShiLiBao();
	}


	protected typeCollector: Map<number, CFG_XianShiLiBao[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_XianShiLiBao[]> = new Map<number, CFG_XianShiLiBao[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_XianShiLiBao = this.all()[i];
			let collector: CFG_XianShiLiBao[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}



	public getInTypeCollector(type: number) : CFG_XianShiLiBao[] {
		let ts: CFG_XianShiLiBao[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("XianShiLiBaoCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_XianShiLiBao[] {
		let ts: CFG_XianShiLiBao[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_XianShiLiBao implements IDesignData {
	//礼包ID
	protected id: number = 0;
	//礼包类型
	protected type: number = 0;
	//充值表ID
	protected chargeProductId: number = 0;
	//Vip等级
	protected vipLevel: number = 0;
	//触发条件
	protected condition: number[] = [];
	//个性化奖励
	protected diffReward: Reward[] = [];
	//有效期/分钟
	protected countdown: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getChargeProductId(): number {
		return this.chargeProductId;
	}
	public getVipLevel(): number {
		return this.vipLevel;
	}
	public getCondition(): number[] {
		return this.condition;
	}
	public getDiffReward(): Reward[] {
		return this.diffReward;
	}
	public getCountdown(): number {
		return this.countdown;
	}
	private formatCondition(): number {
		return 0;
	}
	private formatDiffReward(): Reward {
		return new Reward();
	}
}