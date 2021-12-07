/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author thomas
 */
public class CollectionUtils {

    public static <K, V> Map<K, Set<V>> upsertMapOfSet(Map<K, Set<V>> map, K key, V element) {
        Set<V> l = map.get(key);

        if (l != null) {
            l.add(element);
            map.put(key, l);
        } else {
            map.put(key, new LinkedHashSet<>(Arrays.asList(element))); //LinkedHashSet to ensure insertion order
        }
        return map;
    }

    public static List<Map<String, String>> mapToListofMaps(Map<String, String> in) {
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : in.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Map map = new HashMap();
            map.put(key, value);
            result.add(map);
        }
        return result;
    }

    public static Map<String, String> listOfmapsToMap(List<Map<String, String>> in) {
        Map<String, String> result = new HashMap<>();
        in.stream().forEach(map -> {
            result.putAll(map.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue())));
        });
        return result;
    }
}
