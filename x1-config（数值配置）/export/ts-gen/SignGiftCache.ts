
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
export default class SignGiftCache extends DesignCache<CFG_SignGift> {
	private static instance: SignGiftCache = null;
	public static get Instance(): SignGiftCache {
		if(SignGiftCache.instance === null) {
			SignGiftCache.instance = new SignGiftCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SignGiftCache.instance.fileName);
			SignGiftCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SignGiftCache.instance;
	}
	public get fileName(): string {
		return "Q签到表_SignGift";
	}
	protected createInstance(): CFG_SignGift {
		return new CFG_SignGift();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_SignGift implements IDesignData {
	//天数
	protected id: number = 0;
	//奖励
	protected reward: Reward = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getReward(): Reward {
		return this.reward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}