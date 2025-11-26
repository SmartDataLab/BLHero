
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
export default class PurgatorySeasonCache extends DesignCache<CFG_PurgatorySeason> {
	private static instance: PurgatorySeasonCache = null;
	public static get Instance(): PurgatorySeasonCache {
		if(PurgatorySeasonCache.instance === null) {
			PurgatorySeasonCache.instance = new PurgatorySeasonCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatorySeasonCache.instance.fileName);
			PurgatorySeasonCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatorySeasonCache.instance;
	}
	public get fileName(): string {
		return "L炼狱阵容推荐_PurgatorySeason";
	}
	protected createInstance(): CFG_PurgatorySeason {
		return new CFG_PurgatorySeason();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PurgatorySeason implements IDesignData {
	//赛季
	protected id: number = 0;
	//推荐英雄
	protected suggest: number[] = [];
	//提升道具id
	protected drop: number = 0;
	//品质加成
	protected Quality: Keyv[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSuggest(): number[] {
		return this.suggest;
	}
	public getDrop(): number {
		return this.drop;
	}
	public getQuality(): Keyv[] {
		return this.Quality;
	}
	private formatSuggest(): number {
		return 0;
	}
	private formatQuality(): Keyv {
		return new Keyv();
	}
}