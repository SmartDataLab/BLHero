
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
export default class GmDefineCache extends DesignCache<CFG_GmDefine> {
	private static instance: GmDefineCache = null;
	public static get Instance(): GmDefineCache {
		if(GmDefineCache.instance === null) {
			GmDefineCache.instance = new GmDefineCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + GmDefineCache.instance.fileName);
			GmDefineCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return GmDefineCache.instance;
	}
	public get fileName(): string {
		return "GGm指令说明_GmDefine";
	}
	protected createInstance(): CFG_GmDefine {
		return new CFG_GmDefine();
	}


	protected funcIdxCollector: Map<number, CFG_GmDefine[]> = null;

	protected loadAutoGenerate(): void {
		//构建收集器funcIdxCollector
		let funcIdxCollector: Map<number, CFG_GmDefine[]> = new Map<number, CFG_GmDefine[]>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_GmDefine = this.all()[i];
			let collector: CFG_GmDefine[] = funcIdxCollector.get(data.getFuncIdx());
			if(collector === undefined) {
				collector = [];
				funcIdxCollector.set(data.getFuncIdx(), collector);
			}
			collector.push(data);
		}
		this.funcIdxCollector = funcIdxCollector;
	}



	public getInFuncIdxCollector(funcIdx: number) : CFG_GmDefine[] {
		let ts: CFG_GmDefine[] = this.funcIdxCollector.get(funcIdx);
		if(ts === undefined) {
			throw new DesignDataNotFoundError("GmDefineCache.getInFuncIdxCollector", funcIdx);
		}
		return ts;
	}

	public findInFuncIdxCollector(funcIdx: number) : CFG_GmDefine[] {
		let ts: CFG_GmDefine[] = this.funcIdxCollector.get(funcIdx);
		if(ts === undefined) {
			return null;
		}
		return ts;
	}

}
//当前类代码由导表工具生成，请勿修改
export class CFG_GmDefine implements IDesignData {
	//序号
	protected idx: number = 0;
	//功能序号
	protected funcIdx: number = 0;
	//功能名字
	protected funcName: string = "";
	//GM指令
	protected gmName: string = "";
	//调用示例，注意参数之间有英文空格
	protected example: string = "";
	//是否为无参命令
	protected noArgs: number = 0;
	//预设参数
	protected preArgs: string = "";
	//标题
	protected title: string = "";
	public Id(): number {
		return this.idx;
	}
	public getIdx(): number {
		return this.idx;
	}
	public getFuncIdx(): number {
		return this.funcIdx;
	}
	public getFuncName(): string {
		return this.funcName;
	}
	public getGmName(): string {
		return this.gmName;
	}
	public getExample(): string {
		return this.example;
	}
	public getNoArgs(): number {
		return this.noArgs;
	}
	public getPreArgs(): string {
		return this.preArgs;
	}
	public getTitle(): string {
		return this.title;
	}
}