
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
export default class ModelHeightCache extends DesignCache<CFG_ModelHeight> {
	private static instance: ModelHeightCache = null;
	public static get Instance(): ModelHeightCache {
		if(ModelHeightCache.instance === null) {
			ModelHeightCache.instance = new ModelHeightCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ModelHeightCache.instance.fileName);
			ModelHeightCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ModelHeightCache.instance;
	}
	public get fileName(): string {
		return "M模型高度表_ModelHeight";
	}
	protected createInstance(): CFG_ModelHeight {
		return new CFG_ModelHeight();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ModelHeight implements IDesignData {
	//模型id
	protected id: number = 0;
	//影子缩放比列
	protected shadowScale: number = 0;
	//受击高度
	protected beAttackHeight: number = 0;
	//碰撞体积半径
	protected bodyRadius: number = 0;
	//血条高度
	protected hpHeight: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getShadowScale(): number {
		return this.shadowScale;
	}
	public getBeAttackHeight(): number {
		return this.beAttackHeight;
	}
	public getBodyRadius(): number {
		return this.bodyRadius;
	}
	public getHpHeight(): number {
		return this.hpHeight;
	}
}