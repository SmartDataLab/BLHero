
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
export default class TongTianTaTeQuanProductCache extends DesignCache<CFG_TongTianTaTeQuanProduct> {
	private static instance: TongTianTaTeQuanProductCache = null;
	public static get Instance(): TongTianTaTeQuanProductCache {
		if(TongTianTaTeQuanProductCache.instance === null) {
			TongTianTaTeQuanProductCache.instance = new TongTianTaTeQuanProductCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TongTianTaTeQuanProductCache.instance.fileName);
			TongTianTaTeQuanProductCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TongTianTaTeQuanProductCache.instance;
	}
	public get fileName(): string {
		return "P1004特权系统通天塔充值档次_TongTianTaTeQuanProduct";
	}
	protected createInstance(): CFG_TongTianTaTeQuanProduct {
		return new CFG_TongTianTaTeQuanProduct();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_TongTianTaTeQuanProduct implements IDesignData {
	//充值商品ID
	protected id: number = 0;
	//塔类型
	protected towerType: number = 0;
	//奖励期数
	protected round: number = 0;
	//购买商品需要VIP等级
	protected vipLevel: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getTowerType(): number {
		return this.towerType;
	}
	public getRound(): number {
		return this.round;
	}
	public getVipLevel(): number {
		return this.vipLevel;
	}
}