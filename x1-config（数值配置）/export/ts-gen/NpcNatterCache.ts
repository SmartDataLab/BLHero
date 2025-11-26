
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
export default class NpcNatterCache extends DesignCache<CFG_NpcNatter> {
	private static instance: NpcNatterCache = null;
	public static get Instance(): NpcNatterCache {
		if(NpcNatterCache.instance === null) {
			NpcNatterCache.instance = new NpcNatterCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + NpcNatterCache.instance.fileName);
			NpcNatterCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return NpcNatterCache.instance;
	}
	public get fileName(): string {
		return "NNpc闲聊表_NpcNatter";
	}
	protected createInstance(): CFG_NpcNatter {
		return new CFG_NpcNatter();
	}


	protected npcIdCollector: Map<number, CFG_NpcNatter[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器npcIdCollector
		let npcIdCollector: Map<number, CFG_NpcNatter[]> = new Map<number, CFG_NpcNatter[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_NpcNatter = this.all()[i];
			let collector: CFG_NpcNatter[] = npcIdCollector.get(data.getNpcId());
			if(collector === undefined) {
				collector = [];
				npcIdCollector.set(data.getNpcId(), collector);
			}
			collector.push(data);
		}
		this.npcIdCollector = npcIdCollector;
	}



	public getInNpcIdCollector(npcId: number) : CFG_NpcNatter[] {
		let ts: CFG_NpcNatter[] = this.npcIdCollector.get(npcId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("NpcNatterCache.getInNpcIdCollector", npcId);
		}
		return ts;
	}

	public findInNpcIdCollector(npcId: number) : CFG_NpcNatter[] {
		let ts: CFG_NpcNatter[] = this.npcIdCollector.get(npcId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_NpcNatter implements IDesignData {
	//序号
	protected id: number = 0;
	//npcId
	protected npcId: number = 0;
	//闲聊内容
	protected words: string = "";
	//闲聊内容
	protected wordsLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getNpcId(): number {
		return this.npcId;
	}
	public getWords(): string {
		return this.words;
	}
	public getWordsLang(): string {
		return this.wordsLang;
	}
}