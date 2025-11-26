
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
export default class QiRiMuBiaoTaskCache extends DesignCache<CFG_QiRiMuBiaoTask> {
	private static instance: QiRiMuBiaoTaskCache = null;
	public static get Instance(): QiRiMuBiaoTaskCache {
		if(QiRiMuBiaoTaskCache.instance === null) {
			QiRiMuBiaoTaskCache.instance = new QiRiMuBiaoTaskCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + QiRiMuBiaoTaskCache.instance.fileName);
			QiRiMuBiaoTaskCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return QiRiMuBiaoTaskCache.instance;
	}
	public get fileName(): string {
		return "P1007七日目标_QiRiMuBiaoTask";
	}
	protected createInstance(): CFG_QiRiMuBiaoTask {
		return new CFG_QiRiMuBiaoTask();
	}


	protected dayCollector: Map<number, CFG_QiRiMuBiaoTask[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器dayCollector
		let dayCollector: Map<number, CFG_QiRiMuBiaoTask[]> = new Map<number, CFG_QiRiMuBiaoTask[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_QiRiMuBiaoTask = this.all()[i];
			let collector: CFG_QiRiMuBiaoTask[] = dayCollector.get(data.getDay());
			if(collector === undefined) {
				collector = [];
				dayCollector.set(data.getDay(), collector);
			}
			collector.push(data);
		}
		this.dayCollector = dayCollector;
	}



	public getInDayCollector(day: number) : CFG_QiRiMuBiaoTask[] {
		let ts: CFG_QiRiMuBiaoTask[] = this.dayCollector.get(day);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("QiRiMuBiaoTaskCache.getInDayCollector", day);
		}
		return ts;
	}

	public findInDayCollector(day: number) : CFG_QiRiMuBiaoTask[] {
		let ts: CFG_QiRiMuBiaoTask[] = this.dayCollector.get(day);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_QiRiMuBiaoTask implements IDesignData {
	//任务ID
	protected id: number = 0;
	//天数
	protected day: number = 0;
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
	//完成积分
	protected score: number = 0;
	//任务奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getDay(): number {
		return this.day;
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
	public getScore(): number {
		return this.score;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	private formatTaskParams(): number {
		return 0;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}