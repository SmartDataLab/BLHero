
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
export default class ItemCache extends DesignCache<CFG_Item> {
	private static instance: ItemCache = null;
	public static get Instance(): ItemCache {
		if(ItemCache.instance === null) {
			ItemCache.instance = new ItemCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ItemCache.instance.fileName);
			ItemCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ItemCache.instance;
	}
	public get fileName(): string {
		return "1道具表_Item";
	}
	protected createInstance(): CFG_Item {
		return new CFG_Item();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Item implements IDesignData {
	//道具ID
	protected id: number = 0;
	//道具名称
	protected nameLang: string = "";
	//道具逻辑类型
	protected kind: number = 0;
	//逻辑类型参数
	protected kindParam: number = 0;
	//品质
	protected quality: number = 0;
	//使用类型
	protected useType: number = 0;
	//使用数据
	protected useData: string = "";
	//道具图标
	protected icon: string = "";
	//排序
	protected sort: number = 0;
	//道具描述
	protected describe: string = "";
	//道具描述
	protected describeLang: string = "";
	//获取途径描述
	protected access: number[] = [];
	//角标
	protected tab: string = "";
	//掉落光柱
	protected beam: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getKind(): number {
		return this.kind;
	}
	public getKindParam(): number {
		return this.kindParam;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getUseType(): number {
		return this.useType;
	}
	public getUseData(): string {
		return this.useData;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getSort(): number {
		return this.sort;
	}
	public getDescribe(): string {
		return this.describe;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getAccess(): number[] {
		return this.access;
	}
	public getTab(): string {
		return this.tab;
	}
	public getBeam(): string {
		return this.beam;
	}
	private formatAccess(): number {
		return 0;
	}
}