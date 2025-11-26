
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
export default class GuideCache extends DesignCache<CFG_Guide> {
	private static instance: GuideCache = null;
	public static get Instance(): GuideCache {
		if(GuideCache.instance === null) {
			GuideCache.instance = new GuideCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GuideCache.instance.fileName);
			GuideCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GuideCache.instance;
	}
	public get fileName(): string {
		return "X引导配置表_Guide";
	}
	protected createInstance(): CFG_Guide {
		return new CFG_Guide();
	}

	protected groupSubIdIndex: Map<number, Map<number, CFG_Guide>> = null;


	protected loadAutoGenerate(): void {
		//构建索引groupSubIdIndex
		let groupSubIdIndex: Map<number, Map<number, CFG_Guide>> = new Map<number, Map<number, CFG_Guide>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_Guide = this.all()[i];
			let layer1Map: Map<number, CFG_Guide> = groupSubIdIndex.get(data.getGroup());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_Guide>();
				groupSubIdIndex.set(data.getGroup(), layer1Map);
			}
			layer1Map.set(data.getSubId(), data);
		}
		this.groupSubIdIndex = groupSubIdIndex;
	}

	public getInGroupSubIdIndex(group: number, subId: number): CFG_Guide {
		let layer1Map: Map<number, CFG_Guide> = this.groupSubIdIndex.get(group);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("GuideCache.getInGroupSubIdIndex", group, subId);
		}
		let t: CFG_Guide = layer1Map.get(subId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("GuideCache.getInGroupSubIdIndex", group, subId);
		}
		return t;
	}

	public findInGroupSubIdIndex(group: number, subId: number): CFG_Guide {
		let layer1Map: Map<number, CFG_Guide> = this.groupSubIdIndex.get(group);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_Guide = layer1Map.get(subId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_Guide implements IDesignData {
	//序号
	protected id: number = 0;
	//引导组ID
	protected group: number = 0;
	//步骤
	protected subId: number = 0;
	//触发指引条件
	protected task_id: string = "";
	//引导类型
	protected type: number = 0;
	//目标参数
	protected targetType: string = "";
	//目标ID
	protected targetId: string = "";
	//控件名字
	protected targetName: string = "";
	//控件所在UI
	protected targetUI: string = "";
	//动画所在路径
	protected moviePath: string = "";
	//单步触发条件
	protected condition: number = 0;
	//条件参数
	protected conditionParam: string = "";
	//对话ID
	protected dialogue: number = 0;
	//手指方向
	protected fingerDiction: number = 0;
	//指引文本框方向
	protected diction: string = "";
	//气泡文本
	protected bubbleTxt: string = "";
	//气泡文本
	protected bubbleTxtLang: string = "";
	//指引文本
	protected guideTxt: string = "";
	//指引文本
	protected guideTxtLang: string = "";
	//是否聚焦
	protected focus: number = 0;
	//是否强指引
	protected force: number = 0;
	//断链处理方法
	protected repeat: string = "";
	//备注
	protected beizhu: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getGroup(): number {
		return this.group;
	}
	public getSubId(): number {
		return this.subId;
	}
	public getTask_id(): string {
		return this.task_id;
	}
	public getType(): number {
		return this.type;
	}
	public getTargetType(): string {
		return this.targetType;
	}
	public getTargetId(): string {
		return this.targetId;
	}
	public getTargetName(): string {
		return this.targetName;
	}
	public getTargetUI(): string {
		return this.targetUI;
	}
	public getMoviePath(): string {
		return this.moviePath;
	}
	public getCondition(): number {
		return this.condition;
	}
	public getConditionParam(): string {
		return this.conditionParam;
	}
	public getDialogue(): number {
		return this.dialogue;
	}
	public getFingerDiction(): number {
		return this.fingerDiction;
	}
	public getDiction(): string {
		return this.diction;
	}
	public getBubbleTxt(): string {
		return this.bubbleTxt;
	}
	public getBubbleTxtLang(): string {
		return this.bubbleTxtLang;
	}
	public getGuideTxt(): string {
		return this.guideTxt;
	}
	public getGuideTxtLang(): string {
		return this.guideTxtLang;
	}
	public getFocus(): number {
		return this.focus;
	}
	public getForce(): number {
		return this.force;
	}
	public getRepeat(): string {
		return this.repeat;
	}
	public getBeizhu(): string {
		return this.beizhu;
	}
}