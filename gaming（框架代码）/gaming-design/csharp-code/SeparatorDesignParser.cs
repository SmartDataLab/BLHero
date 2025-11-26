
using System;
using System.Collections.Generic;
using System.Reflection;
using UnityEngine;

namespace YYDesign {

    public abstract class SeparatorDesignParser : IDesignParser {

        protected abstract String fieldSeparator();
        //对象之间的分隔符
        protected abstract String objSeparator();

        public object parseList(string value, Type type) {
            if(type == typeof(int)) {
                List<int> list = new List<int>();
                string[] parts = value.Split(fieldSeparator());
                foreach(string part in parts) {
                    if("" == part) {
                        continue;
                    }
                    list.Add(int.Parse(part));
                }
                return list;
            } else if(type == typeof(long)) {
                List<long> list = new List<long>();
                string[] parts = value.Split(fieldSeparator());
                foreach(string part in parts) {
                    if("" == part) {
                        continue;
                    }
                    list.Add(long.Parse(part));
                }
                return list;
            } else if(type == typeof(float)) {
                List<float> list = new List<float>();
                string[] parts = value.Split(fieldSeparator());
                foreach(string part in parts) {
                    if("" == part) {
                        continue;
                    }
                    list.Add(float.Parse(part));
                }
                return list;
            } else if(type == typeof(string)) {
                List<string> list = new List<string>();
                string[] parts = value.Split(fieldSeparator());
                foreach(string part in parts) {
                    if("" == part) {
                        continue;
                    }
                    list.Add(part);
                }
                return list;
            } else {
                Type listType = typeof(List<>).MakeGenericType(type);
                object list = Activator.CreateInstance(listType);
                string[] parts = value.Split(objSeparator());
                MethodInfo method = listType.GetMethod("Add", BindingFlags.Instance | BindingFlags.Public);
                foreach(string part in parts) {
                    if("" == part) {
                        continue;
                    }
                    object obj = parseStruct(part, type);
                    method.Invoke(list, new object[] {obj});
                }
                return list;
            }
        }

        public object parseMap(string value, Type keyType, Type valueType) {
            //目前还没有在配置中使用Map的情况
            return null;
        }

        public object parseStruct(string value, Type type) {
            FieldInfo[] fields = type.GetFields(BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Instance);
            string[] parts = value.Split(fieldSeparator());
            object obj = Activator.CreateInstance(type);
            for(int i = 0; i < parts.Length && i < fields.Length; i++) {
                FieldInfo field = fields[i];
                string part = parts[i];
                object fvalue = null;

                if(field.FieldType == typeof(int)) {
                    fvalue = int.Parse(part);
                } else if(field.FieldType == typeof(long)) {
                    fvalue = long.Parse(part);
                } else if(field.FieldType == typeof(float)) {
                    fvalue = float.Parse(part);
                } else if(field.FieldType == typeof(string)) {
                    fvalue = part;
                }
                field.SetValue(obj, fvalue);
            }
            return obj;
        }
    }
}