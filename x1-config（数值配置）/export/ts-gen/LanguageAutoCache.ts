
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
export default class LanguageAutoCache extends DesignCache<CFG_LanguageAuto> {
	private static instance: LanguageAutoCache = null;
	public static get Instance(): LanguageAutoCache {
		if(LanguageAutoCache.instance === null) {
			LanguageAutoCache.instance = new LanguageAutoCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + LanguageAutoCache.instance.fileName);
			LanguageAutoCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return LanguageAutoCache.instance;
	}
	public get fileName(): string {
		return "Y语言表自动生成_LanguageAuto";
	}
	protected createInstance(): CFG_LanguageAuto {
		return new CFG_LanguageAuto();
	}

	protected langIdIndex: Map<string, CFG_LanguageAuto> = null;


	protected loadAutoGenerate(): void {
		//构建索引langIdIndex
		let langIdIndex: Map<string, CFG_LanguageAuto> = new Map<string, CFG_LanguageAuto>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_LanguageAuto = this.all()[i];
			langIdIndex.set(data.getLangId(), data);
		}
		this.langIdIndex = langIdIndex;
	}

	public getInLangIdIndex(langId: string): CFG_LanguageAuto {
		let t: CFG_LanguageAuto = this.langIdIndex.get(langId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("LanguageAutoCache.getInLangIdIndex", langId);
		}
		return t;
	}

	public findInLangIdIndex(langId: string): CFG_LanguageAuto {
		let t: CFG_LanguageAuto = this.langIdIndex.get(langId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_LanguageAuto implements IDesignData {
	//序号
	protected id: number = 0;
	//语言ID
	protected langId: string = "";
	//简体中文
	protected cn: string = "";
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
}