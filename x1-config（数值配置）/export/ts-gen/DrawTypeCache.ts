
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
export default class DrawTypeCache extends DesignCache<CFG_DrawType> {
	private static instance: DrawTypeCache = null;
	public static get Instance(): DrawTypeCache {
		if(DrawTypeCache.instance === null) {
			DrawTypeCache.instance = new DrawTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DrawTypeCache.instance.fileName);
			DrawTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DrawTypeCache.instance;
	}
	public get fileName(): string {
		return "C抽奖类型表_DrawType";
	}
	protected createInstance(): CFG_DrawType {
		return new CFG_DrawType();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_DrawType implements IDesignData {
	//抽奖类型
	protected id: number = 0;
	//免费次数
	protected freeNum: number = 0;
	//广告次数
	protected advNum: number = 0;
	//单抽消耗道具
	protected oneCostItem: Cost = null;
	//单抽消耗仙玉
	protected oneCostDiamond: Cost = null;
	//十抽仙玉折扣
	protected tenDiscount: number = 0;
	//积分奖励
	protected point: Reward = null;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getFreeNum(): number {
		return this.freeNum;
	}
	public getAdvNum(): number {
		return this.advNum;
	}
	public getOneCostItem(): Cost {
		return this.oneCostItem;
	}
	public getOneCostDiamond(): Cost {
		return this.oneCostDiamond;
	}
	public getTenDiscount(): number {
		return this.tenDiscount;
	}
	public getPoint(): Reward {
		return this.point;
	}
	private formatOneCostItem(): Cost {
		return new Cost();
	}
	private formatOneCostDiamond(): Cost {
		return new Cost();
	}
	private formatPoint(): Reward {
		return new Reward();
	}
}