
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
export default class PurgatoryMonsterCache extends DesignCache<CFG_PurgatoryMonster> {
	private static instance: PurgatoryMonsterCache = null;
	public static get Instance(): PurgatoryMonsterCache {
		if(PurgatoryMonsterCache.instance === null) {
			PurgatoryMonsterCache.instance = new PurgatoryMonsterCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + PurgatoryMonsterCache.instance.fileName);
			PurgatoryMonsterCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return PurgatoryMonsterCache.instance;
	}
	public get fileName(): string {
		return "L炼狱轮回怪物表_PurgatoryMonster";
	}
	protected createInstance(): CFG_PurgatoryMonster {
		return new CFG_PurgatoryMonster();
	}

	protected difficultyZoneIdRefreshIdIndex: Map<number, Map<number, Map<number, CFG_PurgatoryMonster>>> = null;

	protected difficultyCollector: Map<number, CFG_PurgatoryMonster[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引difficultyZoneIdRefreshIdIndex
		let difficultyZoneIdRefreshIdIndex: Map<number, Map<number, Map<number, CFG_PurgatoryMonster>>> = new Map<number, Map<number, Map<number, CFG_PurgatoryMonster>>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_PurgatoryMonster = this.all()[i];
			let layer1Map: Map<number, Map<number, CFG_PurgatoryMonster>> = difficultyZoneIdRefreshIdIndex.get(data.getDifficulty());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, Map<number, CFG_PurgatoryMonster>>();
				difficultyZoneIdRefreshIdIndex.set(data.getDifficulty(), layer1Map);
			}
			let layer2Map: Map<number, CFG_PurgatoryMonster> = layer1Map.get(data.getZoneId());
			if(layer2Map === undefined) {
				layer2Map = new Map<number, CFG_PurgatoryMonster>();
				layer1Map.set(data.getZoneId(), layer2Map);
			}
			layer2Map.set(data.getRefreshId(), data);
		}
		this.difficultyZoneIdRefreshIdIndex = difficultyZoneIdRefreshIdIndex;
		//构建收集器difficultyCollector
		let difficultyCollector: Map<number, CFG_PurgatoryMonster[]> = new Map<number, CFG_PurgatoryMonster[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_PurgatoryMonster = this.all()[i];
			let collector: CFG_PurgatoryMonster[] = difficultyCollector.get(data.getDifficulty());
			if(collector === undefined) {
				collector = [];
				difficultyCollector.set(data.getDifficulty(), collector);
			}
			collector.push(data);
		}
		this.difficultyCollector = difficultyCollector;
	}

	public getInDifficultyZoneIdRefreshIdIndex(difficulty: number, zoneId: number, refreshId: number): CFG_PurgatoryMonster {
		let layer1Map: Map<number, Map<number, CFG_PurgatoryMonster>> = this.difficultyZoneIdRefreshIdIndex.get(difficulty);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		let layer2Map: Map<number, CFG_PurgatoryMonster> = layer1Map.get(zoneId);
		if(layer2Map === undefined) {
			throw new DesignDataNotFoundError("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		let t: CFG_PurgatoryMonster = layer2Map.get(refreshId);
		if(t === undefined) {
			throw new DesignDataNotFoundError("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		return t;
	}

	public findInDifficultyZoneIdRefreshIdIndex(difficulty: number, zoneId: number, refreshId: number): CFG_PurgatoryMonster {
		let layer1Map: Map<number, Map<number, CFG_PurgatoryMonster>> = this.difficultyZoneIdRefreshIdIndex.get(difficulty);
		if(layer1Map === undefined) {
			return null;
		}
		let layer2Map: Map<number, CFG_PurgatoryMonster> = layer1Map.get(zoneId);
		if(layer2Map === undefined) {
			return null;
		}
		let t: CFG_PurgatoryMonster = layer2Map.get(refreshId);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInDifficultyCollector(difficulty: number) : CFG_PurgatoryMonster[] {
		let ts: CFG_PurgatoryMonster[] = this.difficultyCollector.get(difficulty);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("PurgatoryMonsterCache.getInDifficultyCollector", difficulty);
		}
		return ts;
	}

	public findInDifficultyCollector(difficulty: number) : CFG_PurgatoryMonster[] {
		let ts: CFG_PurgatoryMonster[] = this.difficultyCollector.get(difficulty);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_PurgatoryMonster implements IDesignData {
	//序号
	protected id: number = 0;
	//难度
	protected difficulty: number = 0;
	//场景区域ID
	protected zoneId: number = 0;
	//怪物刷新点
	protected refreshId: number = 0;
	//怪物
	protected monster: Keyv[] = [];
	//刷怪点类型（0固定 1随机）
	protected refreshType: number = 0;
	//时间/秒
	protected time: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getDifficulty(): number {
		return this.difficulty;
	}
	public getZoneId(): number {
		return this.zoneId;
	}
	public getRefreshId(): number {
		return this.refreshId;
	}
	public getMonster(): Keyv[] {
		return this.monster;
	}
	public getRefreshType(): number {
		return this.refreshType;
	}
	public getTime(): number {
		return this.time;
	}
	private formatMonster(): Keyv {
		return new Keyv();
	}
}