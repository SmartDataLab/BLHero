
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
export default class VillageRankCache extends DesignCache<CFG_VillageRank> {
	private static instance: VillageRankCache = null;
	public static get Instance(): VillageRankCache {
		if(VillageRankCache.instance === null) {
			VillageRankCache.instance = new VillageRankCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + VillageRankCache.instance.fileName);
			VillageRankCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return VillageRankCache.instance;
	}
	public get fileName(): string {
		return "X仙境排行奖励表_VillageRank";
	}
	protected createInstance(): CFG_VillageRank {
		return new CFG_VillageRank();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_VillageRank implements IDesignData {
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