
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
export default class EvilSoulCache extends DesignCache<CFG_EvilSoul> {
	private static instance: EvilSoulCache = null;
	public static get Instance(): EvilSoulCache {
		if(EvilSoulCache.instance === null) {
			EvilSoulCache.instance = new EvilSoulCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + EvilSoulCache.instance.fileName);
			EvilSoulCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return EvilSoulCache.instance;
	}
	public get fileName(): string {
		return "Y妖魂_EvilSoul";
	}
	protected createInstance(): CFG_EvilSoul {
		return new CFG_EvilSoul();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_EvilSoul implements IDesignData {
	//妖魂ID
	protected id: number = 0;
	//名称
	protected name: string = "";
	//品质
	protected quality: number = 0;
	//炼化时长/时
	protected refineTime: number = 0;
	//固定奖励
	protected reward: Reward[] = [];
	//权重奖励
	protected wightReward: RandomItem[] = [];
	//随机奖励
	protected randomReward: RandomItem[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getRefineTime(): number {
		return this.refineTime;
	}
	public getReward(): Reward[] {
		return this.reward;
	}
	public getWightReward(): RandomItem[] {
		return this.wightReward;
	}
	public getRandomReward(): RandomItem[] {
		return this.randomReward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
	private formatWightReward(): RandomItem {
		return new RandomItem();
	}
	private formatRandomReward(): RandomItem {
		return new RandomItem();
	}
}