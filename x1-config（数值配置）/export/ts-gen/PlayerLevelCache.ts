
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
export default class PlayerLevelCache extends DesignCache<CFG_PlayerLevel> {
	private static instance: PlayerLevelCache = null;
	public static get Instance(): PlayerLevelCache {
		if(PlayerLevelCache.instance === null) {
			PlayerLevelCache.instance = new PlayerLevelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PlayerLevelCache.instance.fileName);
			PlayerLevelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PlayerLevelCache.instance;
	}
	public get fileName(): string {
		return "J角色等级表_PlayerLevel";
	}
	protected createInstance(): CFG_PlayerLevel {
		return new CFG_PlayerLevel();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PlayerLevel implements IDesignData {
	//等级
	protected id: number = 0;
	//升到下一级需要的经验
	protected exp: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getExp(): number {
		return this.exp;
	}
}