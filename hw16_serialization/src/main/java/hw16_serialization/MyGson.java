package hw16_serialization;

import java.lang.reflect.*;
import java.util.*;
import java.util.Arrays;

public class MyGson {
    private final StringBuilder JSON = new StringBuilder();
    private final List PRIMITIVES = new ArrayList(Arrays.asList("String","Character",
            "char","Byte","Short","Integer","Long","Float","Double",
            "Boolean","byte","short","int","long","float","double","boolean"));
    Object obj=null;

    public String toJson(Object obj) throws IllegalAccessException, ClassNotFoundException {
        JSON.delete(0,JSON.length());
        if (obj == null) {
            return "null";
        }
        Class<?> clazz = obj.getClass();
        String type = clazz.getSimpleName();

        if (PRIMITIVES.contains(type)){
            setElement(type, obj, false);
            return JSON.toString();
        }

        if (setMassivesValuesIfAny(clazz, obj, false)){
            return JSON.toString();
        }

        try {
            this.obj = obj;
            JSON.append("{");

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                boolean isTransient = Modifier.isTransient(field.getModifiers());

                if (isTransient) {
                    continue;
                }
                JSON.append("\"").append(field.getName()).append("\":");
                if (!setFieldValue(field)) {
                    System.out.println("not serializable 1");
                    return null;
                }
            }
            JSON.replace(JSON.lastIndexOf(","), JSON.lastIndexOf(",") + 1, "");
            JSON.append("}");
            return JSON.toString();
        } catch (Exception e) {
            System.out.println("not serializable 2: "+e.getMessage());
            return null;
        }
    }

    private boolean setFieldValue(Field field)
            throws IllegalArgumentException, IllegalAccessException,ClassNotFoundException {

        Object o = field.get(obj);
        Class clazzz = field.getType();

        if (setMassivesValuesIfAny(clazzz, o, true)){return true;};

        String type = field.getType().getSimpleName();
        if (PRIMITIVES.contains(type)){
            setElement(type, o, false);
        }
        else if (field.getType().getCanonicalName().contains("java")) {
            return false;
        }

        new MyGson().toJson(o);
        JSON.append(",");
        return true;
    }

    private boolean setMassivesValuesIfAny(Class clazz, Object o, boolean addComma) throws  IllegalAccessException, ClassNotFoundException {
        String fin="";
        if(addComma){fin=fin+",";}
        if (clazz.getTypeName().contains("[]")) {
            JSON.append("[");
            setArrayValues(o);
            JSON.append("]").append(fin);
            return true;
        }
        if (Class.forName("java.util.Collection").isAssignableFrom(clazz)) {
            JSON.append("[");
            setCollectionValues(o);
            JSON.append("]").append(fin);
            return true;
        }

        if (Class.forName("java.util.Map").isAssignableFrom(clazz)) {
            JSON.append("{");
            setMapValues(o);
            JSON.append("}").append(fin);
            return true;
        }
        return false;
    }

    private void setElement(String type, Object o, boolean addComma) {
        String fin = "";
        if(addComma){fin = fin + ",";}
        switch (type) {
            case "String":
            case "Character":
            case "char":
                JSON.append("\"").append(o).append("\"").append(fin);
                break;
            case "Byte":
            case "Short":
            case "Integer":
            case "Long":
            case "Float":
            case "Double":
            case "Boolean":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "boolean":
                JSON.append(o).append(fin);
                break;
            default:
                break;
        }
    }

    private void setCollectionValues(Object oColl) throws IllegalArgumentException, IllegalAccessException {
        Collection coll = (Collection) oColl;

        if (coll.size() > 0) {
            coll.stream().forEach(c -> setElement(c.getClass().getSimpleName(),c, true));
        }
        JSON.replace(JSON.lastIndexOf(","), JSON.lastIndexOf(",") + 1, "");
    }

    private void setArrayValues(Object oArr)
            throws IllegalArgumentException, IllegalAccessException {
        Object o = oArr;
        String type = o.getClass().getComponentType().getSimpleName();
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++) {
            setElement(type, Array.get(o, i),true);
        }
        JSON.replace(JSON.lastIndexOf(","), JSON.lastIndexOf(",") + 1, "");
    }

    private void setMapValues(Object oMap) throws IllegalArgumentException, IllegalAccessException {
        Map map = (Map) oMap;
        if (map.size() > 0) {
            map.forEach((k,v)->{
                JSON.append("\"").append(k).append("\":");
                setElement(v.getClass().getSimpleName(), v, true);
            });
        } JSON.replace(JSON.lastIndexOf(","), JSON.lastIndexOf(",") + 1, "");
    }
}
