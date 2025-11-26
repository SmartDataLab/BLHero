
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
export default class MainlineTaskCache extends DesignCache<CFG_MainlineTask> {
	private static instance: MainlineTaskCache = null;
	public static get Instance(): MainlineTaskCache {
		if(MainlineTaskCache.instance === null) {
			MainlineTaskCache.instance = new MainlineTaskCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MainlineTaskCache.instance.fileName);
			MainlineTaskCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MainlineTaskCache.instance;
	}
	public get fileName(): string {
		return "Z主线场景任务表_MainlineTask";
	}
	protected createInstance(): CFG_MainlineTask {
		return new CFG_MainlineTask();
	}

	protected preTaskIndex: Map<number, CFG_MainlineTask> = null;


	protected loadAutoGenerate(): void {
		//构建索引preTaskIndex
		let preTaskIndex: Map<number, CFG_MainlineTask> = new Map<number, CFG_MainlineTask>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_MainlineTask = this.all()[i];
			preTaskIndex.set(data.getPreTask(), data);
		}
		this.preTaskIndex = preTaskIndex;
	}

	public getInPreTaskIndex(preTask: number): CFG_MainlineTask {
		let t: CFG_MainlineTask = this.preTaskIndex.get(preTask);
		if(t === undefined) {
			throw new DesignDataNotFoundError("MainlineTaskCache.getInPreTaskIndex", preTask);
		}
		return t;
	}

	public findInPreTaskIndex(preTask: number): CFG_MainlineTask {
		let t: CFG_MainlineTask = this.preTaskIndex.get(preTask);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_MainlineTask implements IDesignData {
	//任务ID
	protected id: number = 0;
	//主线ID
	protected mainlineId: number = 0;
	//前置任务
	protected preTask: number = 0;
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
	//任务奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getMainlineId(): number {
		return this.mainlineId;
	}
	public getPreTask(): number {
		return this.preTask;
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