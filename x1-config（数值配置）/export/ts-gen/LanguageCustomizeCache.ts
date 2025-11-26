
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
export default class LanguageCustomizeCache extends DesignCache<CFG_LanguageCustomize> {
	private static instance: LanguageCustomizeCache = null;
	public static get Instance(): LanguageCustomizeCache {
		if(LanguageCustomizeCache.instance === null) {
			LanguageCustomizeCache.instance = new LanguageCustomizeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + LanguageCustomizeCache.instance.fileName);
			LanguageCustomizeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return LanguageCustomizeCache.instance;
	}
	public get fileName(): string {
		return "Y语言表自定义填写_LanguageCustomize";
	}
	protected createInstance(): CFG_LanguageCustomize {
		return new CFG_LanguageCustomize();
	}

	protected langIdIndex: Map<string, CFG_LanguageCustomize> = null;


	protected loadAutoGenerate(): void {
		//构建索引langIdIndex
		let langIdIndex: Map<string, CFG_LanguageCustomize> = new Map<string, CFG_LanguageCustomize>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_LanguageCustomize = this.all()[i];
			langIdIndex.set(data.getLangId(), data);
		}
		this.langIdIndex = langIdIndex;
	}

	public getInLangIdIndex(langId: string): CFG_LanguageCustomize {
		let t: CFG_LanguageCustomize = this.langIdIndex.get(langId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("LanguageCustomizeCache.getInLangIdIndex", langId);
		}
		return t;
	}

	public findInLangIdIndex(langId: string): CFG_LanguageCustomize {
		let t: CFG_LanguageCustomize = this.langIdIndex.get(langId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_LanguageCustomize implements IDesignData {
	//序号
	protected id: number = 0;
	//语言ID
	protected langId: string = "";
	//简体中文
	protected cn: string = "";
	//繁体中文
	protected hk: string = "";
	//英语
	protected en: string = "";
	//日语
	protected jap: string = "";
	//韩语
	protected kr: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getLangId(): string {
		return this.langId;
	}
	public getCn(): string {
		return this.cn;
	}
	public getHk(): string {
		return this.hk;
	}
	public getEn(): string {
		return this.en;
	}
	public getJap(): string {
		return this.jap;
	}
	public getKr(): string {
		return this.kr;
	}
}