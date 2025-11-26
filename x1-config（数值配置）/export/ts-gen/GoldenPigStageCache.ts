
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
export default class GoldenPigStageCache extends DesignCache<CFG_GoldenPigStage> {
	private static instance: GoldenPigStageCache = null;
	public static get Instance(): GoldenPigStageCache {
		if(GoldenPigStageCache.instance === null) {
			GoldenPigStageCache.instance = new GoldenPigStageCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GoldenPigStageCache.instance.fileName);
			GoldenPigStageCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GoldenPigStageCache.instance;
	}
	public get fileName(): string {
		return "H黄金猪挑战难度表_GoldenPigStage";
	}
	protected createInstance(): CFG_GoldenPigStage {
		return new CFG_GoldenPigStage();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_GoldenPigStage implements IDesignData {
	//难度
	protected id: number = 0;
	//通关奖励
	protected reward: Reward = null;
	//怪物
	protected monsters: monster = null;
	//刷怪点类型（0固定 1随机）
	protected refreshType: number = 0;
	//关联场景ID
	protected sceneId: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getReward(): Reward {
		return this.reward;
	}
	public getMonsters(): monster {
		return this.monsters;
	}
	public getRefreshType(): number {
		return this.refreshType;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	private formatReward(): Reward {
		return new Reward();
	}
	private formatMonsters(): monster {
		return new monster();
	}
}