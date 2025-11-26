
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
export default class DrawRateCache extends DesignCache<CFG_DrawRate> {
	private static instance: DrawRateCache = null;
	public static get Instance(): DrawRateCache {
		if(DrawRateCache.instance === null) {
			DrawRateCache.instance = new DrawRateCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + DrawRateCache.instance.fileName);
			DrawRateCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return DrawRateCache.instance;
	}
	public get fileName(): string {
		return "C抽奖概率表_DrawRate";
	}
	protected createInstance(): CFG_DrawRate {
		return new CFG_DrawRate();
	}


	protected drawTypeCollector: Map<number, CFG_DrawRate[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器drawTypeCollector
		let drawTypeCollector: Map<number, CFG_DrawRate[]> = new Map<number, CFG_DrawRate[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_DrawRate = this.all()[i];
			let collector: CFG_DrawRate[] = drawTypeCollector.get(data.getDrawType());
			if(collector === undefined) {
				collector = [];
				drawTypeCollector.set(data.getDrawType(), collector);
			}
			collector.push(data);
		}
		this.drawTypeCollector = drawTypeCollector;
	}



	public getInDrawTypeCollector(drawType: number) : CFG_DrawRate[] {
		let ts: CFG_DrawRate[] = this.drawTypeCollector.get(drawType);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("DrawRateCache.getInDrawTypeCollector", drawType);
		}
		return ts;
	}

	public findInDrawTypeCollector(drawType: number) : CFG_DrawRate[] {
		let ts: CFG_DrawRate[] = this.drawTypeCollector.get(drawType);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_DrawRate implements IDesignData {
	//序号
	protected id: number = 0;
	//抽奖类型
	protected drawType: number = 0;
	//奖励组
	protected group: number = 0;
	//奖项ID
	protected drawId: number = 0;
	//数量上限
	protected num: number = 0;
	//显示概率
	protected showWeight: number = 0;
	//广播ID
	protected marqueeId: number = 0;
	//抽中是否特殊展示
	protected needShow: number = 0;
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getDrawType(): number {
		return this.drawType;
	}
	public getGroup(): number {
		return this.group;
	}
	public getDrawId(): number {
		return this.drawId;
	}
	public getNum(): number {
		return this.num;
	}
	public getShowWeight(): number {
		return this.showWeight;
	}
	public getMarqueeId(): number {
		return this.marqueeId;
	}
	public getNeedShow(): number {
		return this.needShow;
	}
}