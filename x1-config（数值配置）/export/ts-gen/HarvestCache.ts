
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
export default class HarvestCache extends DesignCache<CFG_Harvest> {
	private static instance: HarvestCache = null;
	public static get Instance(): HarvestCache {
		if(HarvestCache.instance === null) {
			HarvestCache.instance = new HarvestCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HarvestCache.instance.fileName);
			HarvestCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HarvestCache.instance;
	}
	public get fileName(): string {
		return "S收割资源类型表_Harvest";
	}
	protected createInstance(): CFG_Harvest {
		return new CFG_Harvest();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Harvest implements IDesignData {
	//收割资源ID
	protected id: number = 0;
	//资源名称
	protected name: string = "";
	//资源名称
	protected nameLang: string = "";
	//资源类型
	protected type: number = 0;
	//刷新时间秒
	protected refreshTime: number = 0;
	//需收割次数
	protected needNum: number = 0;
	//收割产出
	protected produce: Reward = null;
	//资源模型
	protected model: string = "";
	//放大系数
	protected scale: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getType(): number {
		return this.type;
	}
	public getRefreshTime(): number {
		return this.refreshTime;
	}
	public getNeedNum(): number {
		return this.needNum;
	}
	public getProduce(): Reward {
		return this.produce;
	}
	public getModel(): string {
		return this.model;
	}
	public getScale(): number {
		return this.scale;
	}
	private formatProduce(): Reward {
		return new Reward();
	}
}