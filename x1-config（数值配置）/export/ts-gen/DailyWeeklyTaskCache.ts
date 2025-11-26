
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
export default class DailyWeeklyTaskCache extends DesignCache<CFG_DailyWeeklyTask> {
	private static instance: DailyWeeklyTaskCache = null;
	public static get Instance(): DailyWeeklyTaskCache {
		if(DailyWeeklyTaskCache.instance === null) {
			DailyWeeklyTaskCache.instance = new DailyWeeklyTaskCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DailyWeeklyTaskCache.instance.fileName);
			DailyWeeklyTaskCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DailyWeeklyTaskCache.instance;
	}
	public get fileName(): string {
		return "R日常周常任务表_DailyWeeklyTask";
	}
	protected createInstance(): CFG_DailyWeeklyTask {
		return new CFG_DailyWeeklyTask();
	}


	protected typeCollector: Map<number, CFG_DailyWeeklyTask[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_DailyWeeklyTask[]> = new Map<number, CFG_DailyWeeklyTask[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DailyWeeklyTask = this.all()[i];
			let collector: CFG_DailyWeeklyTask[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}



	public getInTypeCollector(type: number) : CFG_DailyWeeklyTask[] {
		let ts: CFG_DailyWeeklyTask[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("DailyWeeklyTaskCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_DailyWeeklyTask[] {
		let ts: CFG_DailyWeeklyTask[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_DailyWeeklyTask implements IDesignData {
	//任务ID
	protected id: number = 0;
	//日常周常
	protected type: number = 0;
	//任务描述
	protected desc: string = "";
	//任务描述
	protected descLang: string = "";
	//任务类型
	protected taskType: string = "";
	//任务参数
	protected taskParams: number[] = [];
	//任务完成目标数量
	protected taskTargetNum: number = 0;
	//奖励
	protected rewardPoints: Reward[] = [];
	//显示等级
	protected displayLevel: number = 0;
	//跳转类型
	protected guideType: string = "";
	//跳转参数
	protected guideParams: string = "";
	//跳转参数
	protected guideParamsLang: string = "";
	//奖励类型
	protected rewardType: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getDesc(): string {
		return this.desc;
	}
	public getDescLang(): string {
		return this.descLang;
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
	public getRewardPoints(): Reward[] {
		return this.rewardPoints;
	}
	public getDisplayLevel(): number {
		return this.displayLevel;
	}
	public getGuideType(): string {
		return this.guideType;
	}
	public getGuideParams(): string {
		return this.guideParams;
	}
	public getGuideParamsLang(): string {
		return this.guideParamsLang;
	}
	public getRewardType(): number {
		return this.rewardType;
	}
	private formatTaskParams(): number {
		return 0;
	}
	private formatRewardPoints(): Reward {
		return new Reward();
	}
}