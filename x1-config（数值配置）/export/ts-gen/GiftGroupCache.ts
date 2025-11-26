
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
export default class GiftGroupCache extends DesignCache<CFG_GiftGroup> {
	private static instance: GiftGroupCache = null;
	public static get Instance(): GiftGroupCache {
		if(GiftGroupCache.instance === null) {
			GiftGroupCache.instance = new GiftGroupCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GiftGroupCache.instance.fileName);
			GiftGroupCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GiftGroupCache.instance;
	}
	public get fileName(): string {
		return "L礼品组_GiftGroup";
	}
	protected createInstance(): CFG_GiftGroup {
		return new CFG_GiftGroup();
	}


	protected typeGroupIdCollector: Map<number, Map<number, CFG_GiftGroup[]>> = null;

	protected loadAutoGenerate(): void {
		//构建收集器typeGroupIdCollector
		let typeGroupIdCollector: Map<number, Map<number, CFG_GiftGroup[]>> = new Map<number, Map<number, CFG_GiftGroup[]>>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_GiftGroup = this.all()[i];
			let layer1Map: Map<number, CFG_GiftGroup[]> = typeGroupIdCollector.get(data.getType());
			if(layer1Map === undefined) {
				layer1Map = new Map<number, CFG_GiftGroup[]>();
				typeGroupIdCollector.set(data.getType(), layer1Map);
			}
			let collector: CFG_GiftGroup[] = layer1Map.get(data.getGroupId());
			if(collector === undefined) {
				collector = [];
				layer1Map.set(data.getGroupId(), collector);
			}
			collector.push(data);
		}
		this.typeGroupIdCollector = typeGroupIdCollector;
	}



	public getInTypeGroupIdCollector(type: number, groupId: number) : CFG_GiftGroup[] {
		let layer1Map: Map<number, CFG_GiftGroup[]> = this.typeGroupIdCollector.get(type);
		if(layer1Map === undefined) {
			throw new DesignDataNotFoundError("GiftGroupCache.getInTypeGroupIdCollector", type, groupId);
		}
		let ts: CFG_GiftGroup[] = layer1Map.get(groupId);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("GiftGroupCache.getInTypeGroupIdCollector", type, groupId);
		}
		return ts;
	}

	public findInTypeGroupIdCollector(type: number, groupId: number) : CFG_GiftGroup[] {
		let layer1Map: Map<number, CFG_GiftGroup[]> = this.typeGroupIdCollector.get(type);
		if(layer1Map === undefined) {
			return null;
		}
		let ts: CFG_GiftGroup[] = layer1Map.get(groupId);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_GiftGroup implements IDesignData {
	//id
	protected id: number = 0;
	//类型
	protected type: number = 0;
	//备注
	protected commentLang: string = "";
	//礼品组
	protected groupId: number = 0;
	//等级
	protected level: number = 0;
	//固定奖励
	protected fixationReward: Reward[] = [];
	//随机奖励
	protected randomReward: RandomItem[] = [];
	//自选奖励
	protected optionReward: Reward[] = [];
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): number {
		return this.type;
	}
	public getCommentLang(): string {
		return this.commentLang;
	}
	public getGroupId(): number {
		return this.groupId;
	}
	public getLevel(): number {
		return this.level;
	}
	public getFixationReward(): Reward[] {
		return this.fixationReward;
	}
	public getRandomReward(): RandomItem[] {
		return this.randomReward;
	}
	public getOptionReward(): Reward[] {
		return this.optionReward;
	}
	private formatFixationReward(): Reward {
		return new Reward();
	}
	private formatRandomReward(): RandomItem {
		return new RandomItem();
	}
	private formatOptionReward(): Reward {
		return new Reward();
	}
}