
package hw16_serialization;

import com.google.gson.Gson;

import java.util.*;

public class MyGsonDemo {

    public static void main(String[] args)
            throws IllegalAccessException,ClassNotFoundException {

        int value1 = 42;
        float value2 = 3.14f;
        String value3 = "value3";
        int[] valueArray = {9, 14, 19};
        ArrayList<Double> valueArrList = new ArrayList<>(List.of(3.14, 2.71, 1.41));
        Set<String> valueSet = new HashSet<>();
        Map<String, Double> valueMap = new HashMap<>();

        valueSet.add("valueSet1");
        valueSet.add("valueSet2");
        valueMap.put("valueMap1", 1.1);
        valueMap.put("valueMap2", 2.2);
        valueMap.put("valueMap3", 3.3);

        AniObject anyObject =  new AniObject(value1, value2, true, value3,
        valueArray, valueArrList, valueSet, valueMap);

        System.out.println(anyObject);
        MyGson myGson = new MyGson();
        String serAniObject = myGson.toJson(anyObject);
        System.out.println("\nmyGsoned AniObject:\n" + serAniObject +"\n");
        Gson gson = new Gson();
        System.out.println("gSoned AniObject:\n" + gson.toJson(anyObject) +"\n");

        AniObject reAniObject = gson.fromJson(serAniObject, AniObject.class);
        reAniObject.setDate();
        System.out.println("reGsoned MyGsoned AniObject:\n" + reAniObject + "\n");

        System.out.println("equals? "+ anyObject.equals(reAniObject)+ "\n");

        System.out.println("Gsoned null: " + gson.toJson(null));
        System.out.println("MyGsoned null: " + myGson.toJson(null));
    }
}
