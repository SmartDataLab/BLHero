
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
export default class TroopPositionCache extends DesignCache<CFG_TroopPosition> {
	private static instance: TroopPositionCache = null;
	public static get Instance(): TroopPositionCache {
		if(TroopPositionCache.instance === null) {
			TroopPositionCache.instance = new TroopPositionCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TroopPositionCache.instance.fileName);
			TroopPositionCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TroopPositionCache.instance;
	}
	public get fileName(): string {
		return "D队伍站位解锁表_TroopPosition";
	}
	protected createInstance(): CFG_TroopPosition {
		return new CFG_TroopPosition();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_TroopPosition implements IDesignData {
	//站位数量
	protected id: number = 0;
	//角色等级
	protected level: number = 0;
	//开服天数
	protected openDay: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLevel(): number {
		return this.level;
	}
	public getOpenDay(): number {
		return this.openDay;
	}
}