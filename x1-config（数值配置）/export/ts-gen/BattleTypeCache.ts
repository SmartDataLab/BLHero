
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
export default class BattleTypeCache extends DesignCache<CFG_BattleType> {
	private static instance: BattleTypeCache = null;
	public static get Instance(): BattleTypeCache {
		if(BattleTypeCache.instance === null) {
			BattleTypeCache.instance = new BattleTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BattleTypeCache.instance.fileName);
			BattleTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BattleTypeCache.instance;
	}
	public get fileName(): string {
		return "Z战斗类型表_BattleType";
	}
	protected createInstance(): CFG_BattleType {
		return new CFG_BattleType();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_BattleType implements IDesignData {
	//ID
	protected id: number = 0;
	//名字
	protected name: string = "";
	//默认的地图文件
	protected mapData: string = "";
	//战斗时长毫秒
	protected battleTime: number = 0;
	//是否统计连杀数
	protected sumKill: number = 0;
	//是否可以自动战斗
	protected canAuto: number = 0;
	//是否弹结算界面
	protected showSettle: number = 0;
	//每天获得免费门票
	protected freeTicket: Reward = null;
	//默认场景ID
	protected defaultScene: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getMapData(): string {
		return this.mapData;
	}
	public getBattleTime(): number {
		return this.battleTime;
	}
	public getSumKill(): number {
		return this.sumKill;
	}
	public getCanAuto(): number {
		return this.canAuto;
	}
	public getShowSettle(): number {
		return this.showSettle;
	}
	public getFreeTicket(): Reward {
		return this.freeTicket;
	}
	public getDefaultScene(): number {
		return this.defaultScene;
	}
	private formatFreeTicket(): Reward {
		return new Reward();
	}
}