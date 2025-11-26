
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
export default class FunctionOpenCache extends DesignCache<CFG_FunctionOpen> {
	private static instance: FunctionOpenCache = null;
	public static get Instance(): FunctionOpenCache {
		if(FunctionOpenCache.instance === null) {
			FunctionOpenCache.instance = new FunctionOpenCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + FunctionOpenCache.instance.fileName);
			FunctionOpenCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return FunctionOpenCache.instance;
	}
	public get fileName(): string {
		return "G功能开启表_FunctionOpen";
	}
	protected createInstance(): CFG_FunctionOpen {
		return new CFG_FunctionOpen();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_FunctionOpen implements IDesignData {
	//功能ID
	protected id: number = 0;
	//功能名称
	protected name: string = "";
	//开启条件
	protected openCondition: Keyv[] = [];
	//未开启描述
	protected lockRemark: string = "";
	//未开启描述
	protected lockRemarkLang: string = "";
	//未开启入口隐藏
	protected lockHidden: number = 0;
	//入口路径
	protected enterPath: string = "";
	//开启时是否提示
	protected openShow: number = 0;
	//功能图标
	protected icon: string = "";
	//开启描述
	protected openDescribe: string = "";
	//开启描述
	protected openDescribeLang: string = "";
	//建筑ID
	protected npcId: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getOpenCondition(): Keyv[] {
		return this.openCondition;
	}
	public getLockRemark(): string {
		return this.lockRemark;
	}
	public getLockRemarkLang(): string {
		return this.lockRemarkLang;
	}
	public getLockHidden(): number {
		return this.lockHidden;
	}
	public getEnterPath(): string {
		return this.enterPath;
	}
	public getOpenShow(): number {
		return this.openShow;
	}
	public getIcon(): string {
		return this.icon;
	}
	public getOpenDescribe(): string {
		return this.openDescribe;
	}
	public getOpenDescribeLang(): string {
		return this.openDescribeLang;
	}
	public getNpcId(): number {
		return this.npcId;
	}
	private formatOpenCondition(): Keyv {
		return new Keyv();
	}
}