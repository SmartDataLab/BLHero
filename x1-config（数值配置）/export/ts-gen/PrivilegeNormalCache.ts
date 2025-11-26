
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
export default class PrivilegeNormalCache extends DesignCache<CFG_PrivilegeNormal> {
	private static instance: PrivilegeNormalCache = null;
	public static get Instance(): PrivilegeNormalCache {
		if(PrivilegeNormalCache.instance === null) {
			PrivilegeNormalCache.instance = new PrivilegeNormalCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PrivilegeNormalCache.instance.fileName);
			PrivilegeNormalCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PrivilegeNormalCache.instance;
	}
	public get fileName(): string {
		return "P普通特权表_PrivilegeNormal";
	}
	protected createInstance(): CFG_PrivilegeNormal {
		return new CFG_PrivilegeNormal();
	}

	protected rechargeIdIndex: Map<number, CFG_PrivilegeNormal> = null;


	protected loadAutoGenerate(): void {
		//构建索引rechargeIdIndex
		let rechargeIdIndex: Map<number, CFG_PrivilegeNormal> = new Map<number, CFG_PrivilegeNormal>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_PrivilegeNormal = this.all()[i];
			rechargeIdIndex.set(data.getRechargeId(), data);
		}
		this.rechargeIdIndex = rechargeIdIndex;
	}

	public getInRechargeIdIndex(rechargeId: number): CFG_PrivilegeNormal {
		let t: CFG_PrivilegeNormal = this.rechargeIdIndex.get(rechargeId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("PrivilegeNormalCache.getInRechargeIdIndex", rechargeId);
		}
		return t;
	}

	public findInRechargeIdIndex(rechargeId: number): CFG_PrivilegeNormal {
		let t: CFG_PrivilegeNormal = this.rechargeIdIndex.get(rechargeId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_PrivilegeNormal implements IDesignData {
	//特权ID
	protected id: number = 0;
	//充值商品表ID
	protected rechargeId: number = 0;
	//持续天数
	protected days: number = 0;
	//是否开启
	protected active: number = 0;
	//特权描述
	protected describe: string = "";
	//特权描述
	protected describeLang: string = "";
	//充值奖励
	protected rechargeRewards: Reward[] = [];
	//每日奖励
	protected dailyRewards: Reward[] = [];
	//充值自选奖励[废弃]
	protected chooseRewards: Reward[] = [];
	//框的背景
	protected itemBg: string = "";
	//奖励框的背景
	protected itemlistBg: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRechargeId(): number {
		return this.rechargeId;
	}
	public getDays(): number {
		return this.days;
	}
	public getActive(): number {
		return this.active;
	}
	public getDescribe(): string {
		return this.describe;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getRechargeRewards(): Reward[] {
		return this.rechargeRewards;
	}
	public getDailyRewards(): Reward[] {
		return this.dailyRewards;
	}
	public getChooseRewards(): Reward[] {
		return this.chooseRewards;
	}
	public getItemBg(): string {
		return this.itemBg;
	}
	public getItemlistBg(): string {
		return this.itemlistBg;
	}
	private formatRechargeRewards(): Reward {
		return new Reward();
	}
	private formatDailyRewards(): Reward {
		return new Reward();
	}
	private formatChooseRewards(): Reward {
		return new Reward();
	}
}