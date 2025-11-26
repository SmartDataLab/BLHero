
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
export default class DungeonBookCache extends DesignCache<CFG_DungeonBook> {
	private static instance: DungeonBookCache = null;
	public static get Instance(): DungeonBookCache {
		if(DungeonBookCache.instance === null) {
			DungeonBookCache.instance = new DungeonBookCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DungeonBookCache.instance.fileName);
			DungeonBookCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DungeonBookCache.instance;
	}
	public get fileName(): string {
		return "F副本工具书表_DungeonBook";
	}
	protected createInstance(): CFG_DungeonBook {
		return new CFG_DungeonBook();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_DungeonBook implements IDesignData {
	//书本ID
	protected id: number = 0;
	//技能ID
	protected skillId: number = 0;
	//权重
	protected weight: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSkillId(): number {
		return this.skillId;
	}
	public getWeight(): number {
		return this.weight;
	}
}