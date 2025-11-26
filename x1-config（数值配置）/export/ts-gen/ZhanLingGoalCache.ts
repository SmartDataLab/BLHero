
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
export default class ZhanLingGoalCache extends DesignCache<CFG_ZhanLingGoal> {
	private static instance: ZhanLingGoalCache = null;
	public static get Instance(): ZhanLingGoalCache {
		if(ZhanLingGoalCache.instance === null) {
			ZhanLingGoalCache.instance = new ZhanLingGoalCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ZhanLingGoalCache.instance.fileName);
			ZhanLingGoalCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ZhanLingGoalCache.instance;
	}
	public get fileName(): string {
		return "P1002目标类战令奖励_ZhanLingGoal";
	}
	protected createInstance(): CFG_ZhanLingGoal {
		return new CFG_ZhanLingGoal();
	}


	protected activityIdCollector: Map<number, CFG_ZhanLingGoal[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器activityIdCollector
		let activityIdCollector: Map<number, CFG_ZhanLingGoal[]> = new Map<number, CFG_ZhanLingGoal[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhanLingGoal = this.all()[i];
			let collector: CFG_ZhanLingGoal[] = activityIdCollector.get(data.getActivityId());
			if(collector === undefined) {
				collector = [];
				activityIdCollector.set(data.getActivityId(), collector);
			}
			collector.push(data);
		}
		this.activityIdCollector = activityIdCollector;
	}



	public getInActivityIdCollector(activityId: number) : CFG_ZhanLingGoal[] {
		let ts: CFG_ZhanLingGoal[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ZhanLingGoalCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public findInActivityIdCollector(activityId: number) : CFG_ZhanLingGoal[] {
		let ts: CFG_ZhanLingGoal[] = this.activityIdCollector.get(activityId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ZhanLingGoal implements IDesignData {
	//任务ID
	protected id: number = 0;
	//活动类型ID
	protected activityId: number = 0;
	//排序编号
	protected sortIndex: number = 0;
	//任务类型
	protected taskType: string = "";
	//任务参数
	protected taskParams: number[] = [];
	//任务完成目标数量
	protected taskTargetNum: number = 0;
	//免费奖励
	protected freeReward: Reward[] = [];
	//高级奖励
	protected premiumReward: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getActivityId(): number {
		return this.activityId;
	}
	public getSortIndex(): number {
		return this.sortIndex;
	}
	public getTaskType(): string {
		return this.taskType;
	}
	public getTaskParams(): number[] {
		return this.taskParams;
	}
	public getTaskTargetNum(): number {
		return this.taskTargetNum;
	}
	public getFreeReward(): Reward[] {
		return this.freeReward;
	}
	public getPremiumReward(): Reward[] {
		return this.premiumReward;
	}
	private formatTaskParams(): number {
		return 0;
	}
	private formatFreeReward(): Reward {
		return new Reward();
	}
	private formatPremiumReward(): Reward {
		return new Reward();
	}
}