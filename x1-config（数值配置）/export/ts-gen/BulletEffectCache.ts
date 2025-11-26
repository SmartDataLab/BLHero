
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
export default class BulletEffectCache extends DesignCache<CFG_BulletEffect> {
	private static instance: BulletEffectCache = null;
	public static get Instance(): BulletEffectCache {
		if(BulletEffectCache.instance === null) {
			BulletEffectCache.instance = new BulletEffectCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BulletEffectCache.instance.fileName);
			BulletEffectCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BulletEffectCache.instance;
	}
	public get fileName(): string {
		return "2子弹表_BulletEffect";
	}
	protected createInstance(): CFG_BulletEffect {
		return new CFG_BulletEffect();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_BulletEffect implements IDesignData {
	//子弹ID
	protected id: number = 0;
	//子弹消失方式
	protected vanishType: number = 0;
	//消失参数
	protected parameter: number = 0;
	//作用范围
	protected rangeWay: string = "";
	//子弹速度
	protected bulletExpression: string = "";
	//附加buff
	protected buffs: string = "";
	//子弹模型
	protected bulletModel: string = "";
	//受击特效
	protected hitModel: string = "";
	//技能音效
	protected skillSound: string = "";
	//受击音效
	protected hitSound: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getVanishType(): number {
		return this.vanishType;
	}
	public getParameter(): number {
		return this.parameter;
	}
	public getRangeWay(): string {
		return this.rangeWay;
	}
	public getBulletExpression(): string {
		return this.bulletExpression;
	}
	public getBuffs(): string {
		return this.buffs;
	}
	public getBulletModel(): string {
		return this.bulletModel;
	}
	public getHitModel(): string {
		return this.hitModel;
	}
	public getSkillSound(): string {
		return this.skillSound;
	}
	public getHitSound(): string {
		return this.hitSound;
	}
}