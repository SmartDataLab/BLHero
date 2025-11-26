
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
export default class EvilCatalogCache extends DesignCache<CFG_EvilCatalog> {
	private static instance: EvilCatalogCache = null;
	public static get Instance(): EvilCatalogCache {
		if(EvilCatalogCache.instance === null) {
			EvilCatalogCache.instance = new EvilCatalogCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EvilCatalogCache.instance.fileName);
			EvilCatalogCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EvilCatalogCache.instance;
	}
	public get fileName(): string {
		return "Y妖录_EvilCatalog";
	}
	protected createInstance(): CFG_EvilCatalog {
		return new CFG_EvilCatalog();
	}

	protected identityLevelIndex: Map<number, Map<number, CFG_EvilCatalog>> = null;

	protected identityCollector: Map<number, CFG_EvilCatalog[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引identityLevelIndex
		let identityLevelIndex: Map<number, Map<number, CFG_EvilCatalog>> = new Map<number, Map<number, CFG_EvilCatalog>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EvilCatalog = this.all()[i];
			let layer1Map: Map<number, CFG_EvilCatalog> = identityLevelIndex.get(data.getIdentity());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_EvilCatalog>();
				identityLevelIndex.set(data.getIdentity(), layer1Map);
			}
			layer1Map.set(data.getLevel(), data);
		}
		this.identityLevelIndex = identityLevelIndex;
		//构建收集器identityCollector
		let identityCollector: Map<number, CFG_EvilCatalog[]> = new Map<number, CFG_EvilCatalog[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_EvilCatalog = this.all()[i];
			let collector: CFG_EvilCatalog[] = identityCollector.get(data.getIdentity());
			if(collector === undefined) {
				collector = [];
				identityCollector.set(data.getIdentity(), collector);
			}
			collector.push(data);
		}
		this.identityCollector = identityCollector;
	}

	public getInIdentityLevelIndex(identity: number, level: number): CFG_EvilCatalog {
		let layer1Map: Map<number, CFG_EvilCatalog> = this.identityLevelIndex.get(identity);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("EvilCatalogCache.getInIdentityLevelIndex", identity, level);
		}
		let t: CFG_EvilCatalog = layer1Map.get(level);
		if(t === undefined) {
			throw new DesignDataNotFoundError("EvilCatalogCache.getInIdentityLevelIndex", identity, level);
		}
		return t;
	}

	public findInIdentityLevelIndex(identity: number, level: number): CFG_EvilCatalog {
		let layer1Map: Map<number, CFG_EvilCatalog> = this.identityLevelIndex.get(identity);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_EvilCatalog = layer1Map.get(level);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInIdentityCollector(identity: number) : CFG_EvilCatalog[] {
		let ts: CFG_EvilCatalog[] = this.identityCollector.get(identity);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("EvilCatalogCache.getInIdentityCollector", identity);
		}
		return ts;
	}

	public findInIdentityCollector(identity: number) : CFG_EvilCatalog[] {
		let ts: CFG_EvilCatalog[] = this.identityCollector.get(identity);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_EvilCatalog implements IDesignData {
	//序号
	protected id: number = 0;
	//标识
	protected identity: number = 0;
	//阶级
	protected level: number = 0;
	//消耗本体数量
	protected costBody: number = 0;
	//属性加成
	protected attr: BattAttr[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getIdentity(): number {
		return this.identity;
	}
	public getLevel(): number {
		return this.level;
	}
	public getCostBody(): number {
		return this.costBody;
	}
	public getAttr(): BattAttr[] {
		return this.attr;
	}
	private formatAttr(): BattAttr {
		return new BattAttr();
	}
}