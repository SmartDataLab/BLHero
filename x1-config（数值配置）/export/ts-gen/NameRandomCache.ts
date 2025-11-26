
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
export default class NameRandomCache extends DesignCache<CFG_NameRandom> {
	private static instance: NameRandomCache = null;
	public static get Instance(): NameRandomCache {
		if(NameRandomCache.instance === null) {
			NameRandomCache.instance = new NameRandomCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + NameRandomCache.instance.fileName);
			NameRandomCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return NameRandomCache.instance;
	}
	public get fileName(): string {
		return "M名字随机_NameRandom";
	}
	protected createInstance(): CFG_NameRandom {
		return new CFG_NameRandom();
	}


	protected sexTypeCollector: Map<number, Map<number, CFG_NameRandom[]>> = null;
	protected typeCollector: Map<number, CFG_NameRandom[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器sexTypeCollector
		let sexTypeCollector: Map<number, Map<number, CFG_NameRandom[]>> = new Map<number, Map<number, CFG_NameRandom[]>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_NameRandom = this.all()[i];
			let layer1Map: Map<number, CFG_NameRandom[]> = sexTypeCollector.get(data.getSex());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_NameRandom[]>();
				sexTypeCollector.set(data.getSex(), layer1Map);
			}
			let collector: CFG_NameRandom[] = layer1Map.get(data.getType());
			if(collector === undefined) {
				collector = [];
				layer1Map.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.sexTypeCollector = sexTypeCollector;
		//构建收集器typeCollector
		let typeCollector: Map<number, CFG_NameRandom[]> = new Map<number, CFG_NameRandom[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_NameRandom = this.all()[i];
			let collector: CFG_NameRandom[] = typeCollector.get(data.getType());
			if(collector === undefined) {
				collector = [];
				typeCollector.set(data.getType(), collector);
			}
			collector.push(data);
		}
		this.typeCollector = typeCollector;
	}



	public getInSexTypeCollector(sex: number, type: number) : CFG_NameRandom[] {
		let layer1Map: Map<number, CFG_NameRandom[]> = this.sexTypeCollector.get(sex);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("NameRandomCache.getInSexTypeCollector", sex, type);
		}
		let ts: CFG_NameRandom[] = layer1Map.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("NameRandomCache.getInSexTypeCollector", sex, type);
		}
		return ts;
	}
	public getInTypeCollector(type: number) : CFG_NameRandom[] {
		let ts: CFG_NameRandom[] = this.typeCollector.get(type);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("NameRandomCache.getInTypeCollector", type);
		}
		return ts;
	}

	public findInSexTypeCollector(sex: number, type: number) : CFG_NameRandom[] {
		let layer1Map: Map<number, CFG_NameRandom[]> = this.sexTypeCollector.get(sex);
		if(layer1Map === undefined) {
			return null;
		}
		let ts: CFG_NameRandom[] = layer1Map.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}
	public findInTypeCollector(type: number) : CFG_NameRandom[] {
		let ts: CFG_NameRandom[] = this.typeCollector.get(type);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_NameRandom implements IDesignData {
	//序号
	protected id: number = 0;
	//性别
	protected sex: number = 0;
	//类型
	protected type: number = 0;
	//字库
	protected name: string = "";
	//字库
	protected nameLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSex(): number {
		return this.sex;
	}
	public getType(): number {
		return this.type;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
}