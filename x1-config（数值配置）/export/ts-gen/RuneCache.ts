
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
export default class RuneCache extends DesignCache<CFG_Rune> {
	private static instance: RuneCache = null;
	public static get Instance(): RuneCache {
		if(RuneCache.instance === null) {
			RuneCache.instance = new RuneCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + RuneCache.instance.fileName);
			RuneCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return RuneCache.instance;
	}
	public get fileName(): string {
		return "Z真言表_Rune";
	}
	protected createInstance(): CFG_Rune {
		return new CFG_Rune();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Rune implements IDesignData {
	//真言ID
	protected id: number = 0;
	//真言穿戴部位
	protected part: number = 0;
	//名称
	protected nameLang: string = "";
	//作为强化素材时的经验
	protected materialExp: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getPart(): number {
		return this.part;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getMaterialExp(): number {
		return this.materialExp;
	}
}