
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
export default class FairylandCache extends DesignCache<CFG_Fairyland> {
	private static instance: FairylandCache = null;
	public static get Instance(): FairylandCache {
		if(FairylandCache.instance === null) {
			FairylandCache.instance = new FairylandCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + FairylandCache.instance.fileName);
			FairylandCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return FairylandCache.instance;
	}
	public get fileName(): string {
		return "X仙境守卫(村庄)_Fairyland";
	}
	protected createInstance(): CFG_Fairyland {
		return new CFG_Fairyland();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Fairyland implements IDesignData {
	//仙境关卡Id
	protected id: number = 0;
	//通关奖励
	protected reward: Reward = null;
	//神像血量
	protected godHP: number = 0;
	//栅栏血量
	protected fenceHP: number = 0;
	//栅栏消耗
	protected fenceBuild: Cost = null;
	//随机奖励
	protected randomReward: number[] = [];
	//展示奖励
	protected showRewrad: number[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getReward(): Reward {
		return this.reward;
	}
	public getGodHP(): number {
		return this.godHP;
	}
	public getFenceHP(): number {
		return this.fenceHP;
	}
	public getFenceBuild(): Cost {
		return this.fenceBuild;
	}
	public getRandomReward(): number[] {
		return this.randomReward;
	}
	public getShowRewrad(): number[] {
		return this.showRewrad;
	}
	private formatReward(): Reward {
		return new Reward();
	}
	private formatFenceBuild(): Cost {
		return new Cost();
	}
	private formatRandomReward(): number {
		return 0;
	}
	private formatShowRewrad(): number {
		return 0;
	}
}