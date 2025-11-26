
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
export default class HeroRecruitSpecialCache extends DesignCache<CFG_HeroRecruitSpecial> {
	private static instance: HeroRecruitSpecialCache = null;
	public static get Instance(): HeroRecruitSpecialCache {
		if(HeroRecruitSpecialCache.instance === null) {
			HeroRecruitSpecialCache.instance = new HeroRecruitSpecialCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + HeroRecruitSpecialCache.instance.fileName);
			HeroRecruitSpecialCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return HeroRecruitSpecialCache.instance;
	}
	public get fileName(): string {
		return "Y英雄特殊招募表_HeroRecruitSpecial";
	}
	protected createInstance(): CFG_HeroRecruitSpecial {
		return new CFG_HeroRecruitSpecial();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_HeroRecruitSpecial implements IDesignData {
	//手动刷新次数
	protected id: number = 0;
	//英雄池
	protected heroPool: Keyv[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getHeroPool(): Keyv[] {
		return this.heroPool;
	}
	private formatHeroPool(): Keyv {
		return new Keyv();
	}
}