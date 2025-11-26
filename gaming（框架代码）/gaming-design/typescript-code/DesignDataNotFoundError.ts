
export default class DesignDataNotFoundError implements Error {

    private method: string = "";
    private args: any = null;

    constructor(method: string, ...args: any) {
        this.method = method;
        this.args = args;
    }

    get name(): string {
        return "DesignDataNotFoundError";
    }
    get message(): string {
        return this.method + " with args " + JSON.stringify(this.args) + " can not find config";
    }
    stack?: string;
}