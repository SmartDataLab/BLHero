/**
 * 
 */
package org.gaming.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author YY
 *
 */
public class ListMapUtil {

	public static <K, T> Map<K, T> listToMap(List<T> list, Function<T, K> f) {
        Map<K, T> map = new HashMap<>();
        for (T t : list) {
            K keyValue = f.apply(t);
            map.put(keyValue, t);
        }
        return map;
    }

    /**
     * 构建两层的映射集合
     * @param list
     * @param k1
     * @param k2
     * @return
     */
    public static <T, K1, K2> Map<K1, Map<K2, T>> fillTwoLayerMap(List<T> list, Function<T, K1> k1, Function<T, K2> k2) {
        Map<K1, Map<K2, T>> map = new HashMap<>();
        for (T t : list) {
            K1 firstValue = k1.apply(t);
            K2 secondValue = k2.apply(t);

            Map<K2, T> subMap = map.get(firstValue);
            if (subMap == null) {
                subMap = new HashMap<>();
                map.put(firstValue, subMap);
            }
            subMap.put(secondValue, t);
        }
        return map;
    }
    
    public static <T, K1, K2> ConcurrentMap<K1, ConcurrentMap<K2, T>> fillTwoLayerConcurrentMap(List<T> list, Function<T, K1> k1, Function<T, K2> k2) {
    	ConcurrentMap<K1, ConcurrentMap<K2, T>> map = new ConcurrentHashMap<>();
        for (T t : list) {
            K1 firstValue = k1.apply(t);
            K2 secondValue = k2.apply(t);

            ConcurrentMap<K2, T> subMap = map.get(firstValue);
            if (subMap == null) {
                subMap = new ConcurrentHashMap<>();
                map.put(firstValue, subMap);
            }
            subMap.put(secondValue, t);
        }
        return map;
    }

    /**
     * 构建三层的映射集合
     * @param list
     * @param k1
     * @param k2
     * @param k3
     * @return
     */
    public static <T, K1, K2, K3> Map<K1, Map<K2, Map<K3, T>>> fillThreeLayerMap(List<T> list, Function<T, K1> k1, Function<T, K2> k2, Function<T, K3> k3) {
        Map<K1, Map<K2, Map<K3, T>>> map = new HashMap<>();
        for (T t : list) {
            K1 firstKey = k1.apply(t);
            K2 secondKey = k2.apply(t);
            K3 thirdKey = k3.apply(t);
            Map<K2, Map<K3, T>> secondLayerMap = map.get(firstKey);
            if (secondLayerMap == null) {
                secondLayerMap = new HashMap<>();
                map.put(firstKey, secondLayerMap);
            }
            Map<K3, T> thirdLayerMap = secondLayerMap.get(secondKey);
            if (thirdLayerMap == null) {
                thirdLayerMap = new HashMap<>();
                secondLayerMap.put(secondKey, thirdLayerMap);
            }
            thirdLayerMap.put(thirdKey, t);
        }
        return map;
    }

    public static <T, K> Map<K, List<T>> fillListMap(List<T> tlist, Function<T, K> f) {
        Map<K, List<T>> map = new HashMap<>();
        for (T t : tlist) {
            K keyValue = f.apply(t);
            List<T> list = map.get(keyValue);
            if (list == null) {
                list = new ArrayList<>();
                map.put(keyValue, list);
            }
            list.add(t);
        }
        return map;
    }

    public static <T, K1, K2> Map<K1, Map<K2, List<T>>> fillTwoListMap(List<T> tlist, Function<T, K1> f1, Function<T, K2> f2) {
        Map<K1, Map<K2, List<T>>> map = new HashMap<>();
        for (T t : tlist) {
            K1 firstKey = f1.apply(t);
            K2 secondKey = f2.apply(t);
            Map<K2, List<T>> secondMap = map.get(firstKey);
            if (secondMap == null) {
                secondMap = new HashMap<>();
                map.put(firstKey, secondMap);
            }
            List<T> secondList = secondMap.get(secondKey);
            if (secondList == null) {
                secondList = new ArrayList<>();
                secondMap.put(secondKey, secondList);
            }
            secondList.add(t);
        }
        return map;
    }


    public static <K, V> Map<V, List<K>> groupMap(Map<K, V> map) {
        Map<V, List<K>> tMap = new HashMap<>();
        for (Entry<K, V> entry : map.entrySet()) {
            List<K> kList = tMap.get(entry.getValue());
            if (kList == null) {
                kList = new ArrayList<>();
                tMap.put(entry.getValue(), kList);
            }
            kList.add(entry.getKey());
        }
        return tMap;
    }
	
    
    
}
