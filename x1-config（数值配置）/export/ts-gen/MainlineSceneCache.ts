
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
export default class MainlineSceneCache extends DesignCache<CFG_MainlineScene> {
	private static instance: MainlineSceneCache = null;
	public static get Instance(): MainlineSceneCache {
		if(MainlineSceneCache.instance === null) {
			MainlineSceneCache.instance = new MainlineSceneCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MainlineSceneCache.instance.fileName);
			MainlineSceneCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MainlineSceneCache.instance;
	}
	public get fileName(): string {
		return "Z主线场景表_MainlineScene";
	}
	protected createInstance(): CFG_MainlineScene {
		return new CFG_MainlineScene();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_MainlineScene implements IDesignData {
	//主线ID
	protected id: number = 0;
	//场景名称
	protected name: string = "";
	//场景名称
	protected nameLang: string = "";
	//复活安全区ID
	protected reviveZone: number = 0;
	//关联场景
	protected sceneId: number = 0;
	//最后BOSS怪物ID
	protected bossId: number = 0;
	//下一主线场景ID
	protected nextScene: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getName(): string {
		return this.name;
	}
	public getNameLang(): string {
		return this.nameLang;
	}
	public getReviveZone(): number {
		return this.reviveZone;
	}
	public getSceneId(): number {
		return this.sceneId;
	}
	public getBossId(): number {
		return this.bossId;
	}
	public getNextScene(): number {
		return this.nextScene;
	}
}