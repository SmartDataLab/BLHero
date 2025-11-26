
using YYDesign;

public class X1SeparatorDesignParser : SeparatorDesignParser {
    protected override string fieldSeparator(){
        return "#";
    }

    protected override string objSeparator(){
        return "|";
    }
}