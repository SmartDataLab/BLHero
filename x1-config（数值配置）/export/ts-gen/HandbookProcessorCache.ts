
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
export default class HandbookProcessorCache extends DesignCache<CFG_HandbookProcessor> {
	private static instance: HandbookProcessorCache = null;
	public static get Instance(): HandbookProcessorCache {
		if(HandbookProcessorCache.instance === null) {
			HandbookProcessorCache.instance = new HandbookProcessorCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HandbookProcessorCache.instance.fileName);
			HandbookProcessorCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HandbookProcessorCache.instance;
	}
	public get fileName(): string {
		return "T图鉴收集进度奖励表_HandbookProcessor";
	}
	protected createInstance(): CFG_HandbookProcessor {
		return new CFG_HandbookProcessor();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HandbookProcessor implements IDesignData {
	//等级
	protected level: number = 0;
	//所需积分
	protected needPoint: number = 0;
	//奖励
	protected rewards: Reward[] = [];
	public Id(): number {
		return this.level;
	}
	public getLevel(): number {
		return this.level;
	}
	public getNeedPoint(): number {
		return this.needPoint;
	}
	public getRewards(): Reward[] {
		return this.rewards;
	}
	private formatRewards(): Reward {
		return new Reward();
	}
}