
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
export default class ArenaSeasonRewardCache extends DesignCache<CFG_ArenaSeasonReward> {
	private static instance: ArenaSeasonRewardCache = null;
	public static get Instance(): ArenaSeasonRewardCache {
		if(ArenaSeasonRewardCache.instance === null) {
			ArenaSeasonRewardCache.instance = new ArenaSeasonRewardCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ArenaSeasonRewardCache.instance.fileName);
			ArenaSeasonRewardCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ArenaSeasonRewardCache.instance;
	}
	public get fileName(): string {
		return "J竞技场赛季奖励表_ArenaSeasonReward";
	}
	protected createInstance(): CFG_ArenaSeasonReward {
		return new CFG_ArenaSeasonReward();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ArenaSeasonReward implements IDesignData {
	//序号
	protected id: number = 0;
	//排名上
	protected rankUp: number = 0;
	//排名下
	protected rankDown: number = 0;
	//奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRankUp(): number {
		return this.rankUp;
	}
	public getRankDown(): number {
		return this.rankDown;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}