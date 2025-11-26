
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
export default class ActiveTemplateCache extends DesignCache<CFG_ActiveTemplate> {
	private static instance: ActiveTemplateCache = null;
	public static get Instance(): ActiveTemplateCache {
		if(ActiveTemplateCache.instance === null) {
			ActiveTemplateCache.instance = new ActiveTemplateCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ActiveTemplateCache.instance.fileName);
			ActiveTemplateCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ActiveTemplateCache.instance;
	}
	public get fileName(): string {
		return "H活动-类型配置表_ActiveTemplate";
	}
	protected createInstance(): CFG_ActiveTemplate {
		return new CFG_ActiveTemplate();
	}


	protected logicTypeCollector: Map<number, CFG_ActiveTemplate[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器logicTypeCollector
		let logicTypeCollector: Map<number, CFG_ActiveTemplate[]> = new Map<number, CFG_ActiveTemplate[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ActiveTemplate = this.all()[i];
			let collector: CFG_ActiveTemplate[] = logicTypeCollector.get(data.getLogicType());
			if(collector === undefined) {
				collector = [];
				logicTypeCollector.set(data.getLogicType(), collector);
			}
			collector.push(data);
		}
		this.logicTypeCollector = logicTypeCollector;
	}



	public getInLogicTypeCollector(logicType: number) : CFG_ActiveTemplate[] {
		let ts: CFG_ActiveTemplate[] = this.logicTypeCollector.get(logicType);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ActiveTemplateCache.getInLogicTypeCollector", logicType);
		}
		return ts;
	}

	public findInLogicTypeCollector(logicType: number) : CFG_ActiveTemplate[] {
		let ts: CFG_ActiveTemplate[] = this.logicTypeCollector.get(logicType);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ActiveTemplate implements IDesignData {
	//活动ID（唯一）
	protected id: number = 0;
	//活动逻辑分类
	protected logicType: number = 0;
	//跨服类型
	protected crossType: number = 0;
	//是否开启
	protected status: number = 0;
	//入口图标
	protected entryIcon: string = "";
	//模块名称
	protected moduleType: string = "";
	//页签名称
	protected tabIcon: string = "";
	//背景图资源
	protected bgPic: string = "";
	//入口位置
	protected layout: number = 0;
	//入口排序
	protected layoutSort: number = 0;
	//活动排序ID
	protected sortId: number = 0;
	//开始时间
	protected startTime: string = "";
	//结束时间
	protected endTime: string = "";
	//活动参数
	protected openParams: number[] = [];
	//持续天数
	protected openDuration: number = 0;
	//开启周期
	protected openPeriod: number[] = [];
	//开服天数 
	protected openDay: number = 0;
	//拓展字段2
	protected content2: string = "";
	//拓展字段2
	protected content2Lang: string = "";
	//渠道ID
	protected channelId: string = "";
	//最小等级
	protected minLevel: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLogicType(): number {
		return this.logicType;
	}
	public getCrossType(): number {
		return this.crossType;
	}
	public getStatus(): number {
		return this.status;
	}
	public getEntryIcon(): string {
		return this.entryIcon;
	}
	public getModuleType(): string {
		return this.moduleType;
	}
	public getTabIcon(): string {
		return this.tabIcon;
	}
	public getBgPic(): string {
		return this.bgPic;
	}
	public getLayout(): number {
		return this.layout;
	}
	public getLayoutSort(): number {
		return this.layoutSort;
	}
	public getSortId(): number {
		return this.sortId;
	}
	public getStartTime(): string {
		return this.startTime;
	}
	public getEndTime(): string {
		return this.endTime;
	}
	public getOpenParams(): number[] {
		return this.openParams;
	}
	public getOpenDuration(): number {
		return this.openDuration;
	}
	public getOpenPeriod(): number[] {
		return this.openPeriod;
	}
	public getOpenDay(): number {
		return this.openDay;
	}
	public getContent2(): string {
		return this.content2;
	}
	public getContent2Lang(): string {
		return this.content2Lang;
	}
	public getChannelId(): string {
		return this.channelId;
	}
	public getMinLevel(): number {
		return this.minLevel;
	}
	private formatOpenParams(): number {
		return 0;
	}
	private formatOpenPeriod(): number {
		return 0;
	}
}