
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
export default class ActivityRewardsCache extends DesignCache<CFG_ActivityRewards> {
	private static instance: ActivityRewardsCache = null;
	public static get Instance(): ActivityRewardsCache {
		if(ActivityRewardsCache.instance === null) {
			ActivityRewardsCache.instance = new ActivityRewardsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ActivityRewardsCache.instance.fileName);
			ActivityRewardsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ActivityRewardsCache.instance;
	}
	public get fileName(): string {
		return "H活动-奖励配置表_ActivityRewards";
	}
	protected createInstance(): CFG_ActivityRewards {
		return new CFG_ActivityRewards();
	}


	protected templateIdCollector: Map<number, CFG_ActivityRewards[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器templateIdCollector
		let templateIdCollector: Map<number, CFG_ActivityRewards[]> = new Map<number, CFG_ActivityRewards[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ActivityRewards = this.all()[i];
			let collector: CFG_ActivityRewards[] = templateIdCollector.get(data.getTemplateId());
			if(collector === undefined) {
				collector = [];
				templateIdCollector.set(data.getTemplateId(), collector);
			}
			collector.push(data);
		}
		this.templateIdCollector = templateIdCollector;
	}



	public getInTemplateIdCollector(templateId: number) : CFG_ActivityRewards[] {
		let ts: CFG_ActivityRewards[] = this.templateIdCollector.get(templateId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ActivityRewardsCache.getInTemplateIdCollector", templateId);
		}
		return ts;
	}

	public findInTemplateIdCollector(templateId: number) : CFG_ActivityRewards[] {
		let ts: CFG_ActivityRewards[] = this.templateIdCollector.get(templateId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ActivityRewards implements IDesignData {
	//任务ID
	protected id: number = 0;
	//活动ID
	protected templateId: number = 0;
	//显示顺序
	protected index: number = 0;
	//任务类型
	protected taskType: string = "";
	//任务参数
	protected taskParams: number[] = [];
	//任务完成目标数量
	protected taskTargetNum: number = 0;
	//自选奖励
	protected selectRewards: Reward[] = [];
	//固定奖励
	protected rewards: Reward[] = [];
	//描述
	protected desc: string = "";
	//描述
	protected descLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getTemplateId(): number {
		return this.templateId;
	}
	public getIndex(): number {
		return this.index;
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
	public getSelectRewards(): Reward[] {
		return this.selectRewards;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	public getDesc(): string {
		return this.desc;
	}
	public getDescLang(): string {
		return this.descLang;
	}
	private formatTaskParams(): number {
		return 0;
	}
	private formatSelectRewards(): Reward {
		return new Reward();
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}