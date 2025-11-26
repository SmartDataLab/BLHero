
export abstract class SeparatorDesignParser {

    protected abstract fieldSeparator(): string;
	//对象之间的分隔符
	protected abstract objSeparator(): string;

    public parseList(value: string, obj: any): any {
        if(typeof obj === "number") {
            let result: number[] = [];
            let valueParts: string[] = value.split(this.fieldSeparator());
			for(let valuePart of valueParts) {
				if(valuePart === "") {
					continue;
				}
				result.push(Number(valuePart));
			}
			return result;
        } else if(typeof obj === "string") {
            let result: string[] = [];
            let valueParts: string[] = value.split(this.fieldSeparator());
			for(let valuePart of valueParts) {
				if(valuePart === "") {
					continue;
				}
				result.push(valuePart);
			}
            return result;
        } else {
            let result: object[] = [];
            let valueParts: string[] = value.split(this.objSeparator());
            for(let valuePart of valueParts) {
				if(valuePart === "") {
					continue;
				}
				result.push(this.parseStruct(valuePart, obj));
			}
            return result;
        }
    }

	public parseStruct(value: string, obj: any): any {
        let result: object = {};
        if(value === "") {
            for(let key in obj) {
                if(typeof obj[key] === "number") {
                    result[key] = 0;
                } else if(typeof obj[key] === "string") {
                    result[key] = "";
                }
            }
        } else {
            let valueParts: string[] = value.split(this.fieldSeparator());
            let i: number = 0;
            for(let key in obj) {
                if(typeof obj[key] === "number") {
                    if(i >= valueParts.length) {
                        result[key] = 0;
                    } else {
                        result[key] = Number(valueParts[i]);
                    }
                } else if(typeof obj[key] === "string") {
                    if(i >= valueParts.length) {
                        result[key] = "";
                    } else {
                        result[key] = valueParts[i];
                    }
                }
                i++;
            }
        }
        return result;
    }
}