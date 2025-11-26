
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
export default class LoginGiftCache extends DesignCache<CFG_LoginGift> {
	private static instance: LoginGiftCache = null;
	public static get Instance(): LoginGiftCache {
		if(LoginGiftCache.instance === null) {
			LoginGiftCache.instance = new LoginGiftCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + LoginGiftCache.instance.fileName);
			LoginGiftCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return LoginGiftCache.instance;
	}
	public get fileName(): string {
		return "D登录豪礼表_LoginGift";
	}
	protected createInstance(): CFG_LoginGift {
		return new CFG_LoginGift();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_LoginGift implements IDesignData {
	//天数
	protected id: number = 0;
	//奖励
	protected reward: Reward = null;
	//是否大奖
	protected bigGift: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getReward(): Reward {
		return this.reward;
	}
	public getBigGift(): number {
		return this.bigGift;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}