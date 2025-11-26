
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
export default class PurgatoryCache extends DesignCache<CFG_Purgatory> {
	private static instance: PurgatoryCache = null;
	public static get Instance(): PurgatoryCache {
		if(PurgatoryCache.instance === null) {
			PurgatoryCache.instance = new PurgatoryCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatoryCache.instance.fileName);
			PurgatoryCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatoryCache.instance;
	}
	public get fileName(): string {
		return "L炼狱轮回_Purgatory";
	}
	protected createInstance(): CFG_Purgatory {
		return new CFG_Purgatory();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Purgatory implements IDesignData {
	//炼狱阶数
	protected id: number = 0;
	//关联场景ID
	protected sceneId: number = 0;
	//加成掉落
	protected boostDrop: number[] = [];
	//加成Boss掉落
	protected bossDrop: number[] = [];
	//普奖展示
	protected rewardShow: number[] = [];
	//加奖展示
	protected rewardPlusShow: number[] = [];
	//属性库ID
	protected attrStash: number = 0;
	//加成消耗
	protected plusCost: Cost = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getBoostDrop(): number[] {
		return this.boostDrop;
	}
	public getBossDrop(): number[] {
		return this.bossDrop;
	}
	public getRewardShow(): number[] {
		return this.rewardShow;
	}
	public getRewardPlusShow(): number[] {
		return this.rewardPlusShow;
	}
	public getAttrStash(): number {
		return this.attrStash;
	}
	public getPlusCost(): Cost {
		return this.plusCost;
	}
	private formatBoostDrop(): number {
		return 0;
	}
	private formatBossDrop(): number {
		return 0;
	}
	private formatRewardShow(): number {
		return 0;
	}
	private formatRewardPlusShow(): number {
		return 0;
	}
	private formatPlusCost(): Cost {
		return new Cost();
	}
}