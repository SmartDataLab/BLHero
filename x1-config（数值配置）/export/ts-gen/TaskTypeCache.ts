
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
export default class TaskTypeCache extends DesignCache<CFG_TaskType> {
	private static instance: TaskTypeCache = null;
	public static get Instance(): TaskTypeCache {
		if(TaskTypeCache.instance === null) {
			TaskTypeCache.instance = new TaskTypeCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + TaskTypeCache.instance.fileName);
			TaskTypeCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return TaskTypeCache.instance;
	}
	public get fileName(): string {
		return "R任务类型说明表_TaskType";
	}
	protected createInstance(): CFG_TaskType {
		return new CFG_TaskType();
	}

	protected typeIndex: Map<string, CFG_TaskType> = null;


	protected loadAutoGenerate(): void {
		//构建索引typeIndex
		let typeIndex: Map<string, CFG_TaskType> = new Map<string, CFG_TaskType>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_TaskType = this.all()[i];
			typeIndex.set(data.getType(), data);
		}
		this.typeIndex = typeIndex;
	}

	public getInTypeIndex(type: string): CFG_TaskType {
		let t: CFG_TaskType = this.typeIndex.get(type);
		if(t === undefined) {
			throw new DesignDataNotFoundError("TaskTypeCache.getInTypeIndex", type);
		}
		return t;
	}

	public findInTypeIndex(type: string): CFG_TaskType {
		let t: CFG_TaskType = this.typeIndex.get(type);
		if(t === undefined) {
			return null;
		}
		return t;
	}



}
//当前类代码由导表工具生成，请勿修改
export class CFG_TaskType implements IDesignData {
	//序号
	protected id: number = 0;
	//任务类型
	protected type: string = "";
	//任务描述
	protected describeLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getType(): string {
		return this.type;
	}
	public getDescribeLang(): string {
		return this.describeLang;
	}
}