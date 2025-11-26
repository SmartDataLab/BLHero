
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
export default class BubbleCache extends DesignCache<CFG_Bubble> {
	private static instance: BubbleCache = null;
	public static get Instance(): BubbleCache {
		if(BubbleCache.instance === null) {
			BubbleCache.instance = new BubbleCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BubbleCache.instance.fileName);
			BubbleCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BubbleCache.instance;
	}
	public get fileName(): string {
		return "Q气泡文本配置表_Bubble";
	}
	protected createInstance(): CFG_Bubble {
		return new CFG_Bubble();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Bubble implements IDesignData {
	//气泡ID
	protected id: number = 0;
	//对象类型
	protected objectType: number = 0;
	//播放对象
	protected playObject: number = 0;
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
	public getPlayObject(): number {
		return this.playObject;
	}
	public getDialogueLang(): string {
		return this.dialogueLang;
	}
}