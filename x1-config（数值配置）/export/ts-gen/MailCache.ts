
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
export default class MailCache extends DesignCache<CFG_Mail> {
	private static instance: MailCache = null;
	public static get Instance(): MailCache {
		if(MailCache.instance === null) {
			MailCache.instance = new MailCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + MailCache.instance.fileName);
			MailCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return MailCache.instance;
	}
	public get fileName(): string {
		return "Y邮件表_Mail";
	}
	protected createInstance(): CFG_Mail {
		return new CFG_Mail();
	}



	protected loadAutoGenerate(): void {
	}





}
//当前类代码由导表工具生成，请勿修改
export class CFG_Mail implements IDesignData {
	//邮件模板ID
	protected id: number = 0;
	//邮件过期天数
	protected ExpirDay: number = 0;
	//标题前端参数格式
	protected clientTitleFormat: string[] = [];
	//内容前端参数格式
	protected clientContentFormat: string[] = [];
	//标题
	protected titleLang: string = "";
	//内容
	protected contentLang: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getExpirDay(): number {
		return this.ExpirDay;
	}
	public getClientTitleFormat(): string[] {
		return this.clientTitleFormat;
	}
	public getClientContentFormat(): string[] {
		return this.clientContentFormat;
	}
	public getTitleLang(): string {
		return this.titleLang;
	}
	public getContentLang(): string {
		return this.contentLang;
	}
	private formatClientTitleFormat(): string {
		return "";
	}
	private formatClientContentFormat(): string {
		return "";
	}
}