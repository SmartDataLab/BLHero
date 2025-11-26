
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
export default class QiRiMuBiaoStageCache extends DesignCache<CFG_QiRiMuBiaoStage> {
	private static instance: QiRiMuBiaoStageCache = null;
	public static get Instance(): QiRiMuBiaoStageCache {
		if(QiRiMuBiaoStageCache.instance === null) {
			QiRiMuBiaoStageCache.instance = new QiRiMuBiaoStageCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + QiRiMuBiaoStageCache.instance.fileName);
			QiRiMuBiaoStageCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return QiRiMuBiaoStageCache.instance;
	}
	public get fileName(): string {
		return "P1007七日目标阶段奖励_QiRiMuBiaoStage";
	}
	protected createInstance(): CFG_QiRiMuBiaoStage {
		return new CFG_QiRiMuBiaoStage();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_QiRiMuBiaoStage implements IDesignData {
	//阶段ID
	protected id: number = 0;
	//阶段目标任务数
	protected condition: number = 0;
	//任务奖励
	protected reward: Reward = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getCondition(): number {
		return this.condition;
	}
	public getReward(): Reward {
		return this.reward;
	}
	private formatReward(): Reward {
		return new Reward();
	}
}