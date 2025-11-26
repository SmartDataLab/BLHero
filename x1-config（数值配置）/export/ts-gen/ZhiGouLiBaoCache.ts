
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
export default class ZhiGouLiBaoCache extends DesignCache<CFG_ZhiGouLiBao> {
	private static instance: ZhiGouLiBaoCache = null;
	public static get Instance(): ZhiGouLiBaoCache {
		if(ZhiGouLiBaoCache.instance === null) {
			ZhiGouLiBaoCache.instance = new ZhiGouLiBaoCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ZhiGouLiBaoCache.instance.fileName);
			ZhiGouLiBaoCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ZhiGouLiBaoCache.instance;
	}
	public get fileName(): string {
		return "P1009直购礼包_ZhiGouLiBao";
	}
	protected createInstance(): CFG_ZhiGouLiBao {
		return new CFG_ZhiGouLiBao();
	}

	protected chargeProductIdIndex: Map<number, CFG_ZhiGouLiBao> = null;

	protected activityIdCollector: Map<number, CFG_ZhiGouLiBao[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引chargeProductIdIndex
		let chargeProductIdIndex: Map<number, CFG_ZhiGouLiBao> = new Map<number, CFG_ZhiGouLiBao>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhiGouLiBao = this.all()[i];
			chargeProductIdIndex.set(data.getChargeProductId(), data);
		}
		this.chargeProductIdIndex = chargeProductIdIndex;
		//构建收集器activityIdCollector
		let activityIdCollector: Map<number, CFG_ZhiGouLiBao[]> = new Map<number, CFG_ZhiGouLiBao[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhiGouLiBao = this.all()[i];
			let collector: CFG_ZhiGouLiBao[] = activityIdCollector.get(data.getActivityId());
			if(collector === undefined) {
				collector = [];
				activityIdCollector.set(data.getActivityId(), collector);
			}
			collector.push(data);
		}
		this.activityIdCollector = activityIdCollector;
	}

	public getInChargeProductIdIndex(chargeProductId: number): CFG_ZhiGouLiBao {
		let t: CFG_ZhiGouLiBao = this.chargeProductIdIndex.get(chargeProductId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("ZhiGouLiBaoCache.getInChargeProductIdIndex", chargeProductId);
		}
		return t;
	}

	public findInChargeProductIdIndex(chargeProductId: number): CFG_ZhiGouLiBao {
		let t: CFG_ZhiGouLiBao = this.chargeProductIdIndex.get(chargeProductId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInActivityIdCollector(activityId: number) : CFG_ZhiGouLiBao[] {
		let ts: CFG_ZhiGouLiBao[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ZhiGouLiBaoCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public findInActivityIdCollector(activityId: number) : CFG_ZhiGouLiBao[] {
		let ts: CFG_ZhiGouLiBao[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ZhiGouLiBao implements IDesignData {
	//礼包ID
	protected id: number = 0;
	//活动ID
	protected activityId: number = 0;
	//充值表ID
	protected chargeProductId: number = 0;
	//个性化奖励
	protected diffReward: Reward[] = [];
	//限购类型
	protected limitType: number = 0;
	//限购次数
	protected limitNum: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getActivityId(): number {
		return this.activityId;
	}
	public getChargeProductId(): number {
		return this.chargeProductId;
	}
	public getDiffReward(): Reward[] {
		return this.diffReward;
	}
	public getLimitType(): number {
		return this.limitType;
	}
	public getLimitNum(): number {
		return this.limitNum;
	}
	private formatDiffReward(): Reward {
		return new Reward();
	}
}