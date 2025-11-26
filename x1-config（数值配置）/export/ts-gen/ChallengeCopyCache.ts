
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
export default class ChallengeCopyCache extends DesignCache<CFG_ChallengeCopy> {
	private static instance: ChallengeCopyCache = null;
	public static get Instance(): ChallengeCopyCache {
		if(ChallengeCopyCache.instance === null) {
			ChallengeCopyCache.instance = new ChallengeCopyCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ChallengeCopyCache.instance.fileName);
			ChallengeCopyCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ChallengeCopyCache.instance;
	}
	public get fileName(): string {
		return "T挑战副本列表_ChallengeCopy";
	}
	protected createInstance(): CFG_ChallengeCopy {
		return new CFG_ChallengeCopy();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_ChallengeCopy implements IDesignData {
	//序号
	protected id: number = 0;
	//副本类型
	protected type: number = 0;
	//备注
	protected beizhu: string = "";
	//1级目录
	protected tab1: number = 0;
	//2级目录
	protected tab2: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getBeizhu(): string {
		return this.beizhu;
	}
	public getTab1(): number {
		return this.tab1;
	}
	public getTab2(): number {
		return this.tab2;
	}
}