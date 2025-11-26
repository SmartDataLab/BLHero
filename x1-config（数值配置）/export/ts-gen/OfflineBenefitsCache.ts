
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
export default class OfflineBenefitsCache extends DesignCache<CFG_OfflineBenefits> {
	private static instance: OfflineBenefitsCache = null;
	public static get Instance(): OfflineBenefitsCache {
		if(OfflineBenefitsCache.instance === null) {
			OfflineBenefitsCache.instance = new OfflineBenefitsCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + OfflineBenefitsCache.instance.fileName);
			OfflineBenefitsCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return OfflineBenefitsCache.instance;
	}
	public get fileName(): string {
		return "L离线收益表_OfflineBenefits";
	}
	protected createInstance(): CFG_OfflineBenefits {
		return new CFG_OfflineBenefits();
	}


	protected monsterCollector: Map<number, CFG_OfflineBenefits[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器monsterCollector
		let monsterCollector: Map<number, CFG_OfflineBenefits[]> = new Map<number, CFG_OfflineBenefits[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_OfflineBenefits = this.all()[i];
			let collector: CFG_OfflineBenefits[] = monsterCollector.get(data.getMonster());
			if(collector === undefined) {
				collector = [];
				monsterCollector.set(data.getMonster(), collector);
			}
			collector.push(data);
		}
		this.monsterCollector = monsterCollector;
	}



	public getInMonsterCollector(monster: number) : CFG_OfflineBenefits[] {
		let ts: CFG_OfflineBenefits[] = this.monsterCollector.get(monster);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("OfflineBenefitsCache.getInMonsterCollector", monster);
		}
		return ts;
	}

	public findInMonsterCollector(monster: number) : CFG_OfflineBenefits[] {
		let ts: CFG_OfflineBenefits[] = this.monsterCollector.get(monster);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_OfflineBenefits implements IDesignData {
	//序号
	protected id: number = 0;
	//怪物id
	protected monster: number = 0;
	//道具id
	protected item: number = 0;
	//最小数量
	protected min: number = 0;
	//最大数量
	protected max: number = 0;
	//基础时间（单位：分）
	protected time: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getMonster(): number {
		return this.monster;
	}
	public getItem(): number {
		return this.item;
	}
	public getMin(): number {
		return this.min;
	}
	public getMax(): number {
		return this.max;
	}
	public getTime(): number {
		return this.time;
	}
}