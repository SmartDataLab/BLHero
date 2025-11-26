using System;

namespace YYDesign {
    public interface IDesignParser {
        object parseList(string value, Type type);
        object parseMap(string value, Type keyType, Type valueType);
        object parseStruct(string value, Type type);
    }
}
