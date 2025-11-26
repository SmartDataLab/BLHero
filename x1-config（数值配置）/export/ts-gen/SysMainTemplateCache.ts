
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
export default class SysMainTemplateCache extends DesignCache<CFG_SysMainTemplate> {
	private static instance: SysMainTemplateCache = null;
	public static get Instance(): SysMainTemplateCache {
		if(SysMainTemplateCache.instance === null) {
			SysMainTemplateCache.instance = new SysMainTemplateCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SysMainTemplateCache.instance.fileName);
			SysMainTemplateCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SysMainTemplateCache.instance;
	}
	public get fileName(): string {
		return "F福利-类型配置表_SysMainTemplate";
	}
	protected createInstance(): CFG_SysMainTemplate {
		return new CFG_SysMainTemplate();
	}

	protected logicTypeIndex: Map<number, CFG_SysMainTemplate> = null;


	protected loadAutoGenerate(): void {
		//构建索引logicTypeIndex
		let logicTypeIndex: Map<number, CFG_SysMainTemplate> = new Map<number, CFG_SysMainTemplate>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_SysMainTemplate = this.all()[i];
			logicTypeIndex.set(data.getLogicType(), data);
		}
		this.logicTypeIndex = logicTypeIndex;
	}

	public getInLogicTypeIndex(logicType: number): CFG_SysMainTemplate {
		let t: CFG_SysMainTemplate = this.logicTypeIndex.get(logicType);
		if(t === undefined) {
			throw new DesignDataNotFoundError("SysMainTemplateCache.getInLogicTypeIndex", logicType);
		}
		return t;
	}

	public findInLogicTypeIndex(logicType: number): CFG_SysMainTemplate {
		let t: CFG_SysMainTemplate = this.logicTypeIndex.get(logicType);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_SysMainTemplate implements IDesignData {
	//系统ID（唯一）
	protected id: number = 0;
	//活动逻辑分类
	protected logicType: number = 0;
	//跨服类型
	protected crossType: number = 0;
	//当前所属模块名称
	protected modeName: string = "";
	//入口图标
	protected entryIcon: string = "";
	//按钮开的的界面名称
	protected openModeName: string = "";
	//预制体模块
	protected prefabName: string = "";
	//页面标题
	protected viewTitle: string = "";
	//页签名字
	protected tabName: string = "";
	//页签icon
	protected tabIcon: string = "";
	//入口位置
	protected layout: number = 0;
	//入口排序(主)
	protected layoutSort: number = 0;
	//是否隐藏按钮
	protected active: number = 0;
	//活动排序ID
	protected sortId: number = 0;
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
	public getModeName(): string {
		return this.modeName;
	}
	public getEntryIcon(): string {
		return this.entryIcon;
	}
	public getOpenModeName(): string {
		return this.openModeName;
	}
	public getPrefabName(): string {
		return this.prefabName;
	}
	public getViewTitle(): string {
		return this.viewTitle;
	}
	public getTabName(): string {
		return this.tabName;
	}
	public getTabIcon(): string {
		return this.tabIcon;
	}
	public getLayout(): number {
		return this.layout;
	}
	public getLayoutSort(): number {
		return this.layoutSort;
	}
	public getActive(): number {
		return this.active;
	}
	public getSortId(): number {
		return this.sortId;
	}
}