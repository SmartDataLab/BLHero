using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using UnityEngine;
using System.Reflection;

namespace YYDesign {

    public abstract class AbstractDesignCache<T> where T : IDesignData {
        private ReadOnlyCollection<T> list;
        private ReadOnlyDictionary<int, T> map;
        private IDesignParser parser;

        public abstract string FileName {
            get;
        }
        public void LoadData(string text, IDesignParser parser) {
            this.parser = parser;
            FileMeta fileMeta = DesignReader.Form(text);

            Dictionary<int, T> tempMap = new Dictionary<int, T>();
            List<T> tempList = new List<T>();

            Type type = typeof(T);

            foreach(string[] data in fileMeta.Data) {
                T t = Activator.CreateInstance<T>();
                for(int i = 0; i < fileMeta.Head.Length; i++) {
                    string fieldName = fileMeta.Head[i];
                    FieldInfo fieldInfo = type.GetField(fieldName, BindingFlags.NonPublic | BindingFlags.Public | BindingFlags.Instance);
                    if(fieldInfo == null) {
                        Debug.Log("未找到配置类" + this.GetType() + "中" + fieldName + "的字段");
                        continue;
                    }
                    fieldInfo.SetValue(t, FormValue(fieldInfo.FieldType, data[i]));
                }
                tempMap.Add(t.Id(), t);
                tempList.Add(t);
            }
            this.list = new ReadOnlyCollection<T>(tempList);
            this.map = new ReadOnlyDictionary<int, T>(tempMap);

            this.LoadAutoGenerate();
            this.LoadAfterReady();
        }

        private object FormValue(Type fieldType, string value) {
            object valueObj = null;
		
            if(fieldType == typeof(int)) {
                valueObj = int.Parse(value);
                
            } else if(fieldType == typeof(long)) {
                valueObj = long.Parse(value);
                
            } else if(fieldType == typeof(float)) {
                valueObj = float.Parse(value);
                
            } else if(fieldType == typeof(string)) {
                valueObj = value;
                
            } else if(typeof(IList).IsAssignableFrom(fieldType)) {
                Type[] types = fieldType.GetGenericArguments();
                valueObj = parser.parseList(value, types[0]);

            } else if(fieldType == typeof(Dictionary<,>)) {
                Type[] types = fieldType.GetGenericArguments();
                valueObj = parser.parseMap(value, types[0], types[1]);
            } else {
                valueObj = parser.parseStruct(value, fieldType);
            }
            return valueObj;
        }

        public T GetOrThrow(int id) {
            if(map.TryGetValue(id, out T t)) {
                return t;
            }
            throw new Exception("config in [" + FileName + "] with id equals to " + id + " not found");
        }

        public T GetOrNull(int id) {
            if(map.ContainsKey(id)) {
                return map[id];
            }
            return default;
        }

        public ReadOnlyCollection<T> All {
            get {
                return list;
            }
        }

        /// <summary>
        /// 加载索引
        /// </summary>
        protected virtual void LoadAutoGenerate() {
        }

        /// <summary>
        /// 在数据加载完成后处理个性化的数据组装
        /// </summary>
        protected virtual void LoadAfterReady() {
        }

        protected void LoadConst(Dictionary<string, string> constMap) {
            foreach((string fieldName, string value) in constMap) {
                FieldInfo fieldInfo = this.GetType().GetField(fieldName, BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Instance);
                if(fieldInfo == null) {
                    Debug.Log($"加载配置{FileName}常量未找到名为：{fieldName}的字段信息");
                    continue;
                }
                object obj = FormValue(fieldInfo.FieldType, value);
                fieldInfo.SetValue(this, obj);
            }
        }
    }
}