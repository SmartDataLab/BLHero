
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
export default class VipCache extends DesignCache<CFG_Vip> {
	private static instance: VipCache = null;
	public static get Instance(): VipCache {
		if(VipCache.instance === null) {
			VipCache.instance = new VipCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + VipCache.instance.fileName);
			VipCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return VipCache.instance;
	}
	public get fileName(): string {
		return "VIP配置表_Vip";
	}
	protected createInstance(): CFG_Vip {
		return new CFG_Vip();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Vip implements IDesignData {
	//vip等级
	protected level: number = 0;
	//贵族经验奖励
	protected needExp: number = 0;
	//vip特权
	protected privilege: Keyv[] = [];
	//vip奖励
	protected rewards: Reward[] = [];
	//原价
	protected originalPrice: number = 0;
	//现价
	protected currentPrice: number = 0;
	public Id(): number {
		return this.level;
	}
	public getLevel(): number {
		return this.level;
	}
	public getNeedExp(): number {
		return this.needExp;
	}
	public getPrivilege(): Keyv[] {
		return this.privilege;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	public getOriginalPrice(): number {
		return this.originalPrice;
	}
	public getCurrentPrice(): number {
		return this.currentPrice;
	}
	private formatPrivilege(): Keyv {
		return new Keyv();
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}