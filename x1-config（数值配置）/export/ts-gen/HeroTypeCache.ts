
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
export default class HeroTypeCache extends DesignCache<CFG_HeroType> {
	private static instance: HeroTypeCache = null;
	public static get Instance(): HeroTypeCache {
		if(HeroTypeCache.instance === null) {
			HeroTypeCache.instance = new HeroTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroTypeCache.instance.fileName);
			HeroTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroTypeCache.instance;
	}
	public get fileName(): string {
		return "Y英雄类型表_HeroType";
	}
	protected createInstance(): CFG_HeroType {
		return new CFG_HeroType();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroType implements IDesignData {
	//标识
	protected id: number = 0;
	//名称
	protected name: string = "";
	//名称
	protected nameLang: string = "";
	//种族类型
	protected element: number = 0;
	//职业类型
	protected career: number[] = [];
	//品质
	protected quality: number = 0;
	//英雄头像
	protected head: string = "";
	//立绘资源
	protected inset: number = 0;
	//头像资源
	protected portrait: string = "";
	//模型资源id
	protected modelId: number = 0;
	//放大系数
	protected scale: number = 0;
	//体积半径
	protected volume: number = 0;
	//界面展示立绘缩放
	protected imageScale: number = 0;
	//界面展示立绘高度偏移
	protected imageOffH: number = 0;
	//界面展示立绘左右偏移
	protected imageOffR: number = 0;
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
	public getElement(): number {
		return this.element;
	}
	public getCareer(): number[] {
		return this.career;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getHead(): string {
		return this.head;
	}
	public getInset(): number {
		return this.inset;
	}
	public getPortrait(): string {
		return this.portrait;
	}
	public getModelId(): number {
		return this.modelId;
	}
	public getScale(): number {
		return this.scale;
	}
	public getVolume(): number {
		return this.volume;
	}
	public getImageScale(): number {
		return this.imageScale;
	}
	public getImageOffH(): number {
		return this.imageOffH;
	}
	public getImageOffR(): number {
		return this.imageOffR;
	}
	private formatCareer(): number {
		return 0;
	}
}