
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
export default class PurgatoryRankCache extends DesignCache<CFG_PurgatoryRank> {
	private static instance: PurgatoryRankCache = null;
	public static get Instance(): PurgatoryRankCache {
		if(PurgatoryRankCache.instance === null) {
			PurgatoryRankCache.instance = new PurgatoryRankCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatoryRankCache.instance.fileName);
			PurgatoryRankCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatoryRankCache.instance;
	}
	public get fileName(): string {
		return "L炼狱排行奖励_PurgatoryRank";
	}
	protected createInstance(): CFG_PurgatoryRank {
		return new CFG_PurgatoryRank();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_PurgatoryRank implements IDesignData {
	//序号
	protected id: number = 0;
	//最高排名
	protected rankMax: number = 0;
	//最低排名
	protected rankMin: number = 0;
	//排行奖励
	protected reward: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRankMax(): number {
		return this.rankMax;
	}
	public getRankMin(): number {
		return this.rankMin;
	}
	public getReward(): Reward[] {
		return this.reward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}