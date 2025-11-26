
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
export default class HandbookCatalogCache extends DesignCache<CFG_HandbookCatalog> {
	private static instance: HandbookCatalogCache = null;
	public static get Instance(): HandbookCatalogCache {
		if(HandbookCatalogCache.instance === null) {
			HandbookCatalogCache.instance = new HandbookCatalogCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HandbookCatalogCache.instance.fileName);
			HandbookCatalogCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HandbookCatalogCache.instance;
	}
	public get fileName(): string {
		return "T图鉴层级表_HandbookCatalog";
	}
	protected createInstance(): CFG_HandbookCatalog {
		return new CFG_HandbookCatalog();
	}


	protected typeCollector: Map<number, CFG_HandbookCatalog[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_HandbookCatalog[]> = new Map<number, CFG_HandbookCatalog[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_HandbookCatalog = this.all()[i];
			let collector: CFG_HandbookCatalog[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}



	public getInTypeCollector(type: number) : CFG_HandbookCatalog[] {
		let ts: CFG_HandbookCatalog[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("HandbookCatalogCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInTypeCollector(type: number) : CFG_HandbookCatalog[] {
		let ts: CFG_HandbookCatalog[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_HandbookCatalog implements IDesignData {
	//序号
	protected id: number = 0;
	//图鉴类型
	protected type: number = 0;
	//分组
	protected group: number = 0;
	//组名
	protected groupNameLang: string = "";
	//子类
	protected subtype: number = 0;
	//子类名
	protected subtypeName: string = "";
	//子类名
	protected subtypeNameLang: string = "";
	//品质
	protected quality: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getGroup(): number {
		return this.group;
	}
	public getGroupNameLang(): string {
		return this.groupNameLang;
	}
	public getSubtype(): number {
		return this.subtype;
	}
	public getSubtypeName(): string {
		return this.subtypeName;
	}
	public getSubtypeNameLang(): string {
		return this.subtypeNameLang;
	}
	public getQuality(): number {
		return this.quality;
	}
}