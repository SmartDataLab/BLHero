
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
export default class FirstRechargeCache extends DesignCache<CFG_FirstRecharge> {
	private static instance: FirstRechargeCache = null;
	public static get Instance(): FirstRechargeCache {
		if(FirstRechargeCache.instance === null) {
			FirstRechargeCache.instance = new FirstRechargeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + FirstRechargeCache.instance.fileName);
			FirstRechargeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return FirstRechargeCache.instance;
	}
	public get fileName(): string {
		return "S首充奖励_firstRecharge";
	}
	protected createInstance(): CFG_FirstRecharge {
		return new CFG_FirstRecharge();
	}

	protected rechargeIdDayIndex: Map<number, Map<number, CFG_FirstRecharge>> = null;


	protected loadAutoGenerate(): void {
		//构建索引rechargeIdDayIndex
		let rechargeIdDayIndex: Map<number, Map<number, CFG_FirstRecharge>> = new Map<number, Map<number, CFG_FirstRecharge>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_FirstRecharge = this.all()[i];
			let layer1Map: Map<number, CFG_FirstRecharge> = rechargeIdDayIndex.get(data.getRechargeId());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_FirstRecharge>();
				rechargeIdDayIndex.set(data.getRechargeId(), layer1Map);
			}
			layer1Map.set(data.getDay(), data);
		}
		this.rechargeIdDayIndex = rechargeIdDayIndex;
	}

	public getInRechargeIdDayIndex(rechargeId: number, day: number): CFG_FirstRecharge {
		let layer1Map: Map<number, CFG_FirstRecharge> = this.rechargeIdDayIndex.get(rechargeId);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("FirstRechargeCache.getInRechargeIdDayIndex", rechargeId, day);
		}
		let t: CFG_FirstRecharge = layer1Map.get(day);
		if(t === undefined) {
			throw new DesignDataNotFoundError("FirstRechargeCache.getInRechargeIdDayIndex", rechargeId, day);
		}
		return t;
	}

	public findInRechargeIdDayIndex(rechargeId: number, day: number): CFG_FirstRecharge {
		let layer1Map: Map<number, CFG_FirstRecharge> = this.rechargeIdDayIndex.get(rechargeId);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_FirstRecharge = layer1Map.get(day);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_FirstRecharge implements IDesignData {
	//序号
	protected id: number = 0;
	//充值ID
	protected rechargeId: number = 0;
	//奖励天数
	protected day: number = 0;
	//名字
	protected nameLang: string = "";
	//奖励
	protected reward: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRechargeId(): number {
		return this.rechargeId;
	}
	public getDay(): number {
		return this.day;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getReward(): Reward[] {
		return this.reward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}