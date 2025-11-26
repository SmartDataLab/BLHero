import DesignDataNotFoundError from "./DesignDataNotFoundError";
import { SeparatorDesignParser } from "./SeparatorDesignParser";

export default abstract class DesignCache<T extends IDesignData> {

    private list: T[];
    private map: Map<number, T>;
	private parser: SeparatorDesignParser;
	/**
	 * 后续实现
	 */
    public loadData(text: string): void {
        let list: T[] = [];
        let datas: object[] = JSON.parse(text);
        for(let i = 0; i < datas.length; i++) {
            let data: object = datas[i];
            console.log(data);
        }
    }
	
	/**
	 * 通过原数据与分隔符解析器加载配置对象
	 */
    public loadObjects(datas: object[], parser: SeparatorDesignParser): void {
    	this.parser = parser;
        const list: T[] = [];
        const map: Map<number, T> = new Map<number, T>();
        try {
            for(let i = 0; i < datas.length; i++) {
                let temp: any = datas[i];
                let t: T = this.createInstance();
                for(const key in t) {
                    if(temp[key] === undefined) {
                        if(typeof t[key] === "number") {
                        } else if(typeof t[key] === "string") {
                        } else if(Array.isArray(t[key])) {
                        } else if(typeof t[key] === "object") {
                            let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                            let obj: any = t["format" + upperFirst]();
                            t[key] = this.parser.parseStruct("", obj);
                        }
                    } else {
                        if(typeof t[key] === "number") {
                            t[key] = temp[key];
                        } else if(typeof t[key] === "string") {
                            t[key] = temp[key];
                        } else if(Array.isArray(t[key])) {
                            let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                            let obj: any = t["format" + upperFirst]();
                            t[key] = this.parser.parseList(temp[key].toString(), obj);
                        } else if(typeof t[key] === "object") {
                            let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                            let obj: any = t["format" + upperFirst]();
                            t[key] = this.parser.parseStruct(temp[key].toString(), obj);
                        }
                    }
                }
                list.push(t);
                map.set(t.Id(), t);
            }
        } catch (error) {
            console.log(`加载${this.fileName}发生异常${error}`);
        }
        this.list = list;
        this.map = map;

        this.loadAutoGenerate();
    }

    protected abstract get fileName(): string;
    //创建对象
    protected abstract createInstance(): T;
    //加载自动生成的索引结构
    protected abstract loadAutoGenerate(): void;

    public all(): T[] {
        return this.list;
    }

    public getOrNull(id: number): T {
        let t: T = this.map.get(id);
        if(t === undefined) {
            return null;
        }
        return t;
    }

    public getOrThrow(id: number): T {
        let t: T = this.map.get(id);
        if(t === undefined) {
            throw new DesignDataNotFoundError(this.fileName + ".getOrThrow", id);
        }
        return t;
    }
    
    /**
     * 加载常量
     */
    protected loadConst(map: Map<string, string>): void {
        let t: any = this;
    	for(const key in t) {
            if(key === "list" || key === "map" || key === "parser") {
                continue;
            }
            if(map.get(key) === undefined) {
                if(typeof t[key] === "number") {
                } else if(typeof t[key] === "string") {
                } else if(Array.isArray(t[key])) {
                } else if(typeof t[key] === "object") {
                    let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                    let obj: any = t["format" + upperFirst]();
                    t[key] = this.parser.parseStruct("", obj);
                }
            } else {
                if(typeof t[key] === "number") {
                    t[key] = parseInt(map.get(key));
                } else if(typeof t[key] === "string") {
                    t[key] = map.get(key);
                } else if(Array.isArray(t[key])) {
                    let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                    let obj: any = t["format" + upperFirst]();
                    t[key] = this.parser.parseList(map.get(key), obj);
                } else if(typeof t[key] === "object") {
                    let upperFirst: string = key.substring(0, 1).toUpperCase() + key.substring(1);
                    let obj: any = t["format" + upperFirst]();
                    t[key] = this.parser.parseStruct(map.get(key), obj);
                }
            }
        }
    }

}