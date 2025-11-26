
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
export default class HomeStoreHouseCache extends DesignCache<CFG_HomeStoreHouse> {
	private static instance: HomeStoreHouseCache = null;
	public static get Instance(): HomeStoreHouseCache {
		if(HomeStoreHouseCache.instance === null) {
			HomeStoreHouseCache.instance = new HomeStoreHouseCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HomeStoreHouseCache.instance.fileName);
			HomeStoreHouseCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HomeStoreHouseCache.instance;
	}
	public get fileName(): string {
		return "J家园仓库容量表_HomeStoreHouse";
	}
	protected createInstance(): CFG_HomeStoreHouse {
		return new CFG_HomeStoreHouse();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HomeStoreHouse implements IDesignData {
	//等级
	protected id: number = 0;
	//肉容量
	protected meat: number = 0;
	//木容量
	protected wood: number = 0;
	//矿容量
	protected mine: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getMeat(): number {
		return this.meat;
	}
	public getWood(): number {
		return this.wood;
	}
	public getMine(): number {
		return this.mine;
	}
}