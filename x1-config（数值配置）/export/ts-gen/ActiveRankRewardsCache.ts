
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
export default class ActiveRankRewardsCache extends DesignCache<CFG_ActiveRankRewards> {
	private static instance: ActiveRankRewardsCache = null;
	public static get Instance(): ActiveRankRewardsCache {
		if(ActiveRankRewardsCache.instance === null) {
			ActiveRankRewardsCache.instance = new ActiveRankRewardsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ActiveRankRewardsCache.instance.fileName);
			ActiveRankRewardsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ActiveRankRewardsCache.instance;
	}
	public get fileName(): string {
		return "H活动-排行榜奖励配置表_ActiveRankRewards";
	}
	protected createInstance(): CFG_ActiveRankRewards {
		return new CFG_ActiveRankRewards();
	}


	protected activeIdCollector: Map<number, CFG_ActiveRankRewards[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器activeIdCollector
		let activeIdCollector: Map<number, CFG_ActiveRankRewards[]> = new Map<number, CFG_ActiveRankRewards[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ActiveRankRewards = this.all()[i];
			let collector: CFG_ActiveRankRewards[] = activeIdCollector.get(data.getActiveId());
			if(collector === undefined) {
				collector = [];
				activeIdCollector.set(data.getActiveId(), collector);
			}
			collector.push(data);
		}
		this.activeIdCollector = activeIdCollector;
	}



	public getInActiveIdCollector(activeId: number) : CFG_ActiveRankRewards[] {
		let ts: CFG_ActiveRankRewards[] = this.activeIdCollector.get(activeId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ActiveRankRewardsCache.getInActiveIdCollector", activeId);
		}
		return ts;
	}

	public findInActiveIdCollector(activeId: number) : CFG_ActiveRankRewards[] {
		let ts: CFG_ActiveRankRewards[] = this.activeIdCollector.get(activeId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ActiveRankRewards implements IDesignData {
	//序号
	protected id: number = 0;
	//活动id
	protected activeId: number = 0;
	//排名上限
	protected rankUp: number = 0;
	//排名下限
	protected rankDown: number = 0;
	//排名奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getActiveId(): number {
		return this.activeId;
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