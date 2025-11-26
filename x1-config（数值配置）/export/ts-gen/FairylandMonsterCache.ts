
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
export default class FairylandMonsterCache extends DesignCache<CFG_FairylandMonster> {
	private static instance: FairylandMonsterCache = null;
	public static get Instance(): FairylandMonsterCache {
		if(FairylandMonsterCache.instance === null) {
			FairylandMonsterCache.instance = new FairylandMonsterCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + FairylandMonsterCache.instance.fileName);
			FairylandMonsterCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return FairylandMonsterCache.instance;
	}
	public get fileName(): string {
		return "X仙境守卫怪物表(村庄)_FairylandMonster";
	}
	protected createInstance(): CFG_FairylandMonster {
		return new CFG_FairylandMonster();
	}

	protected difficultyWaveIndex: Map<number, Map<number, CFG_FairylandMonster>> = null;

	protected difficultyCollector: Map<number, CFG_FairylandMonster[]> = null;

	protected loadAutoGenerate(): void {
		//构建索引difficultyWaveIndex
		let difficultyWaveIndex: Map<number, Map<number, CFG_FairylandMonster>> = new Map<number, Map<number, CFG_FairylandMonster>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_FairylandMonster = this.all()[i];
			let layer1Map: Map<number, CFG_FairylandMonster> = difficultyWaveIndex.get(data.getDifficulty());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_FairylandMonster>();
				difficultyWaveIndex.set(data.getDifficulty(), layer1Map);
			}
			layer1Map.set(data.getWave(), data);
		}
		this.difficultyWaveIndex = difficultyWaveIndex;
		//构建收集器difficultyCollector
		let difficultyCollector: Map<number, CFG_FairylandMonster[]> = new Map<number, CFG_FairylandMonster[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_FairylandMonster = this.all()[i];
			let collector: CFG_FairylandMonster[] = difficultyCollector.get(data.getDifficulty());
			if(collector === undefined) {
				collector = [];
				difficultyCollector.set(data.getDifficulty(), collector);
			}
			collector.push(data);
		}
		this.difficultyCollector = difficultyCollector;
	}

	public getInDifficultyWaveIndex(difficulty: number, wave: number): CFG_FairylandMonster {
		let layer1Map: Map<number, CFG_FairylandMonster> = this.difficultyWaveIndex.get(difficulty);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("FairylandMonsterCache.getInDifficultyWaveIndex", difficulty, wave);
		}
		let t: CFG_FairylandMonster = layer1Map.get(wave);
		if(t === undefined) {
			throw new DesignDataNotFoundError("FairylandMonsterCache.getInDifficultyWaveIndex", difficulty, wave);
		}
		return t;
	}

	public findInDifficultyWaveIndex(difficulty: number, wave: number): CFG_FairylandMonster {
		let layer1Map: Map<number, CFG_FairylandMonster> = this.difficultyWaveIndex.get(difficulty);
		if(layer1Map === undefined) {
			return null;
		}
		let t: CFG_FairylandMonster = layer1Map.get(wave);
		if(t === undefined) {
			return null;
		}
		return t;
	}

	public getInDifficultyCollector(difficulty: number) : CFG_FairylandMonster[] {
		let ts: CFG_FairylandMonster[] = this.difficultyCollector.get(difficulty);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("FairylandMonsterCache.getInDifficultyCollector", difficulty);
		}
		return ts;
	}

	public findInDifficultyCollector(difficulty: number) : CFG_FairylandMonster[] {
		let ts: CFG_FairylandMonster[] = this.difficultyCollector.get(difficulty);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_FairylandMonster implements IDesignData {
	//序号
	protected id: number = 0;
	//难度
	protected difficulty: number = 0;
	//波
	protected wave: number = 0;
	//怪物刷新
	protected monsters: monster[] = [];
	//刷怪点类型（0固定 1随机）
	protected refreshType: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getDifficulty(): number {
		return this.difficulty;
	}
	public getWave(): number {
		return this.wave;
	}
	public getMonsters(): monster[] {
		return this.monsters;
	}
	public getRefreshType(): number {
		return this.refreshType;
	}
	private formatMonsters(): monster {
		return new monster();
	}
}