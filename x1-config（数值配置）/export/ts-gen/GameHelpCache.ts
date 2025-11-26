
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
export default class GameHelpCache extends DesignCache<CFG_GameHelp> {
	private static instance: GameHelpCache = null;
	public static get Instance(): GameHelpCache {
		if(GameHelpCache.instance === null) {
			GameHelpCache.instance = new GameHelpCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GameHelpCache.instance.fileName);
			GameHelpCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GameHelpCache.instance;
	}
	public get fileName(): string {
		return "B帮助说明_GameHelp";
	}
	protected createInstance(): CFG_GameHelp {
		return new CFG_GameHelp();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_GameHelp implements IDesignData {
	//属性ID
	protected id: number = 0;
	//键名
	protected keyCol: string = "";
	//帮助说明
	protected describe: string = "";
	//帮助说明
	protected describeLang: string = "";
	//备注
	protected title: string = "";
	//备注
	protected titleLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getKeyCol(): string {
		return this.keyCol;
	}
	public getDescribe(): string {
		return this.describe;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getTitle(): string {
		return this.title;
	}
	public getTitleLang(): string {
		return this.titleLang;
	}
}