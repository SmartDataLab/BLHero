
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
export default class HeroTacticsCache extends DesignCache<CFG_HeroTactics> {
	private static instance: HeroTacticsCache = null;
	public static get Instance(): HeroTacticsCache {
		if(HeroTacticsCache.instance === null) {
			HeroTacticsCache.instance = new HeroTacticsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroTacticsCache.instance.fileName);
			HeroTacticsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroTacticsCache.instance;
	}
	public get fileName(): string {
		return "Y英雄流派表_HeroTactics";
	}
	protected createInstance(): CFG_HeroTactics {
		return new CFG_HeroTactics();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroTactics implements IDesignData {
	//流派类型
	protected id: number = 0;
	//流派图标
	protected icon: string = "";
	//流派描述
	protected describe: string = "";
	//流派描述
	protected describeLang: string = "";
	//核心伙伴id
	protected heroIdentity: number = 0;
	//羁绊伙伴列表
	protected heroList: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getDescribe(): string {
		return this.describe;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
	public getHeroIdentity(): number {
		return this.heroIdentity;
	}
	public getHeroList(): string {
		return this.heroList;
	}
}