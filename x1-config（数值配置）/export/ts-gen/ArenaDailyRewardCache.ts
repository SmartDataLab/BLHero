
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
export default class ArenaDailyRewardCache extends DesignCache<CFG_ArenaDailyReward> {
	private static instance: ArenaDailyRewardCache = null;
	public static get Instance(): ArenaDailyRewardCache {
		if(ArenaDailyRewardCache.instance === null) {
			ArenaDailyRewardCache.instance = new ArenaDailyRewardCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ArenaDailyRewardCache.instance.fileName);
			ArenaDailyRewardCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ArenaDailyRewardCache.instance;
	}
	public get fileName(): string {
		return "J竞技场每日奖励表_ArenaDailyReward";
	}
	protected createInstance(): CFG_ArenaDailyReward {
		return new CFG_ArenaDailyReward();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ArenaDailyReward implements IDesignData {
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