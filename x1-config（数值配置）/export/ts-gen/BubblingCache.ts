
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
export default class BubblingCache extends DesignCache<CFG_Bubbling> {
	private static instance: BubblingCache = null;
	public static get Instance(): BubblingCache {
		if(BubblingCache.instance === null) {
			BubblingCache.instance = new BubblingCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BubblingCache.instance.fileName);
			BubblingCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BubblingCache.instance;
	}
	public get fileName(): string {
		return "M冒泡配置表_Bubbling";
	}
	protected createInstance(): CFG_Bubbling {
		return new CFG_Bubbling();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Bubbling implements IDesignData {
	//冒泡ID
	protected id: number = 0;
	//触发类型
	protected objectType: number = 0;
	//对话文本
	protected dialogue: string = "";
	//对话文本
	protected dialogueLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getObjectType(): number {
		return this.objectType;
	}
	public getDialogue(): string {
		return this.dialogue;
	}
	public getDialogueLang(): string {
		return this.dialogueLang;
	}
}