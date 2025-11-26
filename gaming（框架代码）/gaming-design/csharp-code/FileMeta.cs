using System;
using System.Collections.Generic;

namespace YYDesign {

    public class FileMeta {
        /**
        * 文件名
        */
        public string Name {get; set;}
        /**
        * 文件上的宏
        */
        public string[] Macro {get; set;}
        /**
        * 文件字段定义读取类型
        */
        public string[] ReadType {get; set;}
        /**
        * 文件字段定义类型
        */
        public string[] Type {get; set;}
        /**
        * 文件字段定义头
        */
        public string[] Head {get; set;}
        /**
        * 文件字段注释
        */
        public string[] Comment {get; set;}
        /**
        * 文件数据
        */
        public List<string[]> Data {get; set;}
    }
}
