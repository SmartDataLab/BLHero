
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
export default class SkillModelCache extends DesignCache<CFG_SkillModel> {
	private static instance: SkillModelCache = null;
	public static get Instance(): SkillModelCache {
		if(SkillModelCache.instance === null) {
			SkillModelCache.instance = new SkillModelCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + SkillModelCache.instance.fileName);
			SkillModelCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return SkillModelCache.instance;
	}
	public get fileName(): string {
		return "2技能效果表_SkillModel";
	}
	protected createInstance(): CFG_SkillModel {
		return new CFG_SkillModel();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_SkillModel implements IDesignData {
	//技能表现ID
	protected id: number = 0;
	//施法特效：
挂在施法者身上的特效都配置在这里
特效名称#特效节点位置#方向
显示位置：
1、人物前（特效挡住人物）
2、人物后（人物挡住特效）
3、人物身上（特效跟随人物移动）
4、地图下层（特效贴在地面上）
5、人物层（特效参与排序，根据位置显示身前或背后)
6、地图上层（特效显示在地图上层，比如：暴风雪技能）
方向:
0、无方向
1、人物面向目标
2、人物面向目标(根据情况翻转)

	protected selfModel: string = "";
	//攻击特效：非施法者自身的特效都配置在这里
特效名称#特效节点位置#方向
显示位置：
1、人物前（特效挡住人物）
2、人物后（人物挡住特效）
3、人物身上（特效跟随人物移动）
4、地图下层（特效贴在地面上）
5、人物层（特效参与排序，根据位置显示身前或背后)
6、地图上层（特效显示在地图上层，比如：暴风雪技能）
方向:
0、无方向
1、人物面向目标
2、人物面向目标(根据情况翻转)

	protected targetModel: string = "";
	//受击特效
Administrator:
特效名称#特效节点位置#方向

显示位置：
1、人物前（特效挡住人物）
2、人物后（人物挡住特效）
3、人物身上（特效跟随人物移动）
4、地图下层（特效贴在地面上）
5、人物层（特效参与排序，根据位置显示身前或背后)
6、地图上层（特效显示在地图上层，比如：暴风雪技能）
方向:
0、无方向
1、人物面向目标
2、人物面向目标(根据情况翻转)

	protected hitModel: string = "";
	//特效位置：
0. 头部
1. 腰部
2. 脚底

	protected targetSeat: number = 0;
	//偏移值
	protected offset: number[] = [];
	//技能音效延迟
	protected soundDelay: number = 0;
	//技能音效
	protected skillSound: string = "";
	//受击音效
	protected hitSound: string = "";
	//飘字类型
	protected floaterType: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getSelfModel(): string {
		return this.selfModel;
	}
	public getTargetModel(): string {
		return this.targetModel;
	}
	public getHitModel(): string {
		return this.hitModel;
	}
	public getTargetSeat(): number {
		return this.targetSeat;
	}
	public getOffset(): number[] {
		return this.offset;
	}
	public getSoundDelay(): number {
		return this.soundDelay;
	}
	public getSkillSound(): string {
		return this.skillSound;
	}
	public getHitSound(): string {
		return this.hitSound;
	}
	public getFloaterType(): string {
		return this.floaterType;
	}
	private formatOffset(): number {
		return 0;
	}
}