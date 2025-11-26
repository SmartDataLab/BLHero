
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
export default class BattleAttrCache extends DesignCache<CFG_BattleAttr> {
	private static instance: BattleAttrCache = null;
	public static get Instance(): BattleAttrCache {
		if(BattleAttrCache.instance === null) {
			BattleAttrCache.instance = new BattleAttrCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BattleAttrCache.instance.fileName);
			BattleAttrCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BattleAttrCache.instance;
	}
	public get fileName(): string {
		return "4战斗属性表_BattleAttr";
	}
	protected createInstance(): CFG_BattleAttr {
		return new CFG_BattleAttr();
	}

	protected attrNameIndex: Map<string, CFG_BattleAttr> = null;


	protected loadAutoGenerate(): void {
		//构建索引attrNameIndex
		let attrNameIndex: Map<string, CFG_BattleAttr> = new Map<string, CFG_BattleAttr>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_BattleAttr = this.all()[i];
			attrNameIndex.set(data.getAttrName(), data);
		}
		this.attrNameIndex = attrNameIndex;
	}

	public getInAttrNameIndex(attrName: string): CFG_BattleAttr {
		let t: CFG_BattleAttr = this.attrNameIndex.get(attrName);
		if(t === undefined) {
			throw new DesignDataNotFoundError("BattleAttrCache.getInAttrNameIndex", attrName);
		}
		return t;
	}

	public findInAttrNameIndex(attrName: string): CFG_BattleAttr {
		let t: CFG_BattleAttr = this.attrNameIndex.get(attrName);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_BattleAttr implements IDesignData {
	//属性ID
	protected id: number = 0;
	//属性名字
	protected nameLang: string = "";
	//属性字段名
	protected attrName: string = "";
	//运算时/xx
	protected type: number = 0;
	//属性类型
	protected attrType: number = 0;
	//说明
	protected commentLang: string = "";
	//属性图标
	protected icon: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getAttrName(): string {
		return this.attrName;
	}
	public getType(): number {
		return this.type;
	}
	public getAttrType(): number {
		return this.attrType;
	}
	public getCommentLang(): string {
		return this.commentLang;
	}
	public getIcon(): number {
		return this.icon;
	}
}