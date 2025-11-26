
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
export default class ZhangLingNormalCache extends DesignCache<CFG_ZhangLingNormal> {
	private static instance: ZhangLingNormalCache = null;
	public static get Instance(): ZhangLingNormalCache {
		if(ZhangLingNormalCache.instance === null) {
			ZhangLingNormalCache.instance = new ZhangLingNormalCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ZhangLingNormalCache.instance.fileName);
			ZhangLingNormalCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ZhangLingNormalCache.instance;
	}
	public get fileName(): string {
		return "Z普通战令表_ZhangLingNormal";
	}
	protected createInstance(): CFG_ZhangLingNormal {
		return new CFG_ZhangLingNormal();
	}

	protected rechargeIdIndex: Map<number, CFG_ZhangLingNormal> = null;


	protected loadAutoGenerate(): void {
		//构建索引rechargeIdIndex
		let rechargeIdIndex: Map<number, CFG_ZhangLingNormal> = new Map<number, CFG_ZhangLingNormal>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ZhangLingNormal = this.all()[i];
			rechargeIdIndex.set(data.getRechargeId(), data);
		}
		this.rechargeIdIndex = rechargeIdIndex;
	}

	public getInRechargeIdIndex(rechargeId: number): CFG_ZhangLingNormal {
		let t: CFG_ZhangLingNormal = this.rechargeIdIndex.get(rechargeId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("ZhangLingNormalCache.getInRechargeIdIndex", rechargeId);
		}
		return t;
	}

	public findInRechargeIdIndex(rechargeId: number): CFG_ZhangLingNormal {
		let t: CFG_ZhangLingNormal = this.rechargeIdIndex.get(rechargeId);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_ZhangLingNormal implements IDesignData {
	//战令ID
	protected id: number = 0;
	//对应领取奖励ID
	protected rechargeId: number = 0;
	// ZhanLingExpID
	protected expZLID: number = 0;
	//框的背景
	protected itemBg: string = "";
	//奖励框的背景
	protected itemlistBg: string = "";
	//是否开启1开启2隐藏
	protected active: number = 0;
	//打开视图的名称
	protected openModeView: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getRechargeId(): number {
		return this.rechargeId;
	}
	public getExpZLID(): number {
		return this.expZLID;
	}
	public getItemBg(): string {
		return this.itemBg;
	}
	public getItemlistBg(): string {
		return this.itemlistBg;
	}
	public getActive(): number {
		return this.active;
	}
	public getOpenModeView(): string {
		return this.openModeView;
	}
}