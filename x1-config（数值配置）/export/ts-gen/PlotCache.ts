
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
export default class PlotCache extends DesignCache<CFG_Plot> {
	private static instance: PlotCache = null;
	public static get Instance(): PlotCache {
		if(PlotCache.instance === null) {
			PlotCache.instance = new PlotCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PlotCache.instance.fileName);
			PlotCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PlotCache.instance;
	}
	public get fileName(): string {
		return "J剧情配置表_Plot";
	}
	protected createInstance(): CFG_Plot {
		return new CFG_Plot();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Plot implements IDesignData {
	//对话ID
	protected id: number = 0;
	//对话组
	protected group: number = 0;
	//步骤
	protected subId: number = 0;
	//头像显示位置
	protected diction: number = 0;
	//npc名字
	protected nameLang: string = "";
	//半身像
	protected icon: number = 0;
	//对话文本
	protected dialogueLang: string = "";
	//是否自动播放
	protected autorun: number = 0;
	//剧情对话音效
	protected plotSound: number = 0;
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
	public getDiction(): number {
		return this.diction;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getIcon(): number {
		return this.icon;
	}
	public getDialogueLang(): string {
		return this.dialogueLang;
	}
	public getAutorun(): number {
		return this.autorun;
	}
	public getPlotSound(): number {
		return this.plotSound;
	}
}