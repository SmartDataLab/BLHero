
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
export default class ItemRandomPackCache extends DesignCache<CFG_ItemRandomPack> {
	private static instance: ItemRandomPackCache = null;
	public static get Instance(): ItemRandomPackCache {
		if(ItemRandomPackCache.instance === null) {
			ItemRandomPackCache.instance = new ItemRandomPackCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + ItemRandomPackCache.instance.fileName);
			ItemRandomPackCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return ItemRandomPackCache.instance;
	}
	public get fileName(): string {
		return "D掉落表_ItemRandomPack";
	}
	protected createInstance(): CFG_ItemRandomPack {
		return new CFG_ItemRandomPack();
	}


	protected groupCollector: Map<number, CFG_ItemRandomPack[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器groupCollector
		let groupCollector: Map<number, CFG_ItemRandomPack[]> = new Map<number, CFG_ItemRandomPack[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_ItemRandomPack = this.all()[i];
			let collector: CFG_ItemRandomPack[] = groupCollector.get(data.getGroup());
			if(collector === undefined) {
				collector = [];
				groupCollector.set(data.getGroup(), collector);
			}
			collector.push(data);
		}
		this.groupCollector = groupCollector;
	}



	public getInGroupCollector(group: number) : CFG_ItemRandomPack[] {
		let ts: CFG_ItemRandomPack[] = this.groupCollector.get(group);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("ItemRandomPackCache.getInGroupCollector", group);
		}
		return ts;
	}

	public findInGroupCollector(group: number) : CFG_ItemRandomPack[] {
		let ts: CFG_ItemRandomPack[] = this.groupCollector.get(group);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_ItemRandomPack implements IDesignData {
	//序号
	protected id: number = 0;
	//掉落组ID
	protected group: number = 0;
	//掉落奖励(概率#道具id#数量|)
	protected reward: RandomItem[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getGroup(): number {
		return this.group;
	}
	public getReward(): RandomItem[] {
		return this.reward;
	}
	private formatReward(): RandomItem {
		return new RandomItem();
	}
}