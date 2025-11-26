
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
export default class AchievementPointsCache extends DesignCache<CFG_AchievementPoints> {
	private static instance: AchievementPointsCache = null;
	public static get Instance(): AchievementPointsCache {
		if(AchievementPointsCache.instance === null) {
			AchievementPointsCache.instance = new AchievementPointsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + AchievementPointsCache.instance.fileName);
			AchievementPointsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return AchievementPointsCache.instance;
	}
	public get fileName(): string {
		return "C成就点数奖励表_AchievementPoints";
	}
	protected createInstance(): CFG_AchievementPoints {
		return new CFG_AchievementPoints();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_AchievementPoints implements IDesignData {
	//奖励ID
	protected id: number = 0;
	//需要点数
	protected needPoint: number = 0;
	//奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNeedPoint(): number {
		return this.needPoint;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}