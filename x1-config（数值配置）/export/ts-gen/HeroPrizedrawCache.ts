
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
export default class HeroPrizedrawCache extends DesignCache<CFG_HeroPrizedraw> {
	private static instance: HeroPrizedrawCache = null;
	public static get Instance(): HeroPrizedrawCache {
		if(HeroPrizedrawCache.instance === null) {
			HeroPrizedrawCache.instance = new HeroPrizedrawCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroPrizedrawCache.instance.fileName);
			HeroPrizedrawCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroPrizedrawCache.instance;
	}
	public get fileName(): string {
		return "Y英雄抽取表_HeroPrizedraw";
	}
	protected createInstance(): CFG_HeroPrizedraw {
		return new CFG_HeroPrizedraw();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroPrizedraw implements IDesignData {
	//品质
	protected quality: number = 0;
	//抽取权重
	protected weight: number = 0;
	//碎片
	protected fragment: number[] = [];
	//积分
	protected points: number = 0;
	//抽奖多少次后必定中
	protected gottaNum: number = 0;
	//提升阶段变化数量
	protected upStageNum: number = 0;
	public Id(): number {
		return this.quality;
	}
	public getQuality(): number {
		return this.quality;
	}
	public getWeight(): number {
		return this.weight;
	}
	public getFragment(): number[] {
		return this.fragment;
	}
	public getPoints(): number {
		return this.points;
	}
	public getGottaNum(): number {
		return this.gottaNum;
	}
	public getUpStageNum(): number {
		return this.upStageNum;
	}
	private formatFragment(): number {
		return 0;
	}
}