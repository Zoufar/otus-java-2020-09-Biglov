
package hw16_serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class MyGson {

    StringBuilder json = new StringBuilder();
    Object obj=null;

    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        if(clazz.equals(Byte.class)){
            return Byte.toString((byte) obj);
        }

        if (clazz.equals(Short.class)) {
            return Short.toString((short) obj);
        }

        if (clazz.equals(Integer.class)) {
            return Integer.toString((int) obj);
        }

        if (clazz.equals(Long.class)) {
            return Long.toString((long) obj);
        }

        if (clazz.equals(Float.class)) {
            return Float.toString((float) obj);
        }

        if (clazz.equals(Double.class)) {
            return Double.toString((double) obj);
        }
        if(clazz.equals(Character.class)||clazz.equals(String.class)){
            return "\""+(String)obj+"\"";
        }
        if(clazz.equals(Boolean.class)){
            return obj.toString();
        }
        try {
//            objectStack.push(obj);
            this.obj = obj;
            json.append("{");

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                
                if (isTransient) {
                    continue;
                }
                json.append("\"").append(field.getName()).append("\":");
                if (!setFieldValue(field)) {
                    System.out.println("not serializable");
                    return null;
                }
            }
            json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
            json.append("}");
            return json.toString();
        } catch (Exception e) {
            System.out.println("not serializable: "+e.getMessage());
            return null;
        }
    }

    private boolean setFieldValue(Field field)
            throws IllegalArgumentException, IllegalAccessException {       
        String type = field.getType().getSimpleName();        
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "double":
            case "long":
            case "float":
            case "boolean":
                json.append(field.get(obj));
                break;
            case "char":
            case "String":
                json.append("\"").append(field.get(obj)).append("\"");
                break;
            case "byte[]":
            case "short[]":    
            case "int[]":
            case "long[]":
            case "float[]":
            case "double[]":
            case "boolean[]":
            case "String[]":
            case "char[]":
                json.append("[");
                setArrayValues(field);
                json.append("]");
                break;
            case "ArrayList":
            case "LinkedList":
                json.append("[");
                setListValues(field);
                json.append("]");
                break;
            case "Set":   
                json.append("[");
                setSetValues(field);
                json.append("]");
                break;
            case "Queue":
                json.append("[");
                setQueueValues(field);
                json.append("]");
                break;
            case "Map":
                json.append("{");
                setMapValues(field);
                json.append("}");
                break;
            default:
                if (field.getType().getCanonicalName().contains("java")) {                 
                    return false;
                }

                Object o = field.get(obj);
                new MyGson().toJson(o);
                break;
        }
        json.append(",");
        return true;
    }

    private void setListValues(Field field) throws IllegalArgumentException, IllegalAccessException {
        List list =(List) field.get(obj);
        if (list.size() > 0) {
            String type = list.get(0).getClass().getSimpleName();          
            for (int i = 0; i < list.size(); i++) {
                setElement(type, list.get(i));
            }
            json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
        }
    }

    private void setMapValues(Field field) throws IllegalArgumentException, IllegalAccessException {
        Map map = (Map) field.get(obj);
        if (map.size() > 0) {
            map.forEach((k,v)->{
                json.append("\"").append(k).append("\":");
                setElement(v.getClass().getSimpleName(),v);
                });       
        } json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, ""); 
    }
    
    private void setSetValues(Field field) throws IllegalArgumentException, IllegalAccessException{
        Set set=(Set)field.get(obj);
        if(set.size()>0){
            set.forEach(e->{
            setElement(e.getClass().getSimpleName(),e);
            });
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
    }
    
    private void setArrayValues(Field field)
            throws IllegalArgumentException, IllegalAccessException {
        Object o = field.get(obj);
        String type = o.getClass().getComponentType().getSimpleName();
        
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++) {
            setElement(type, Array.get(o, i));
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
    }
    public void setQueueValues(Field field) throws IllegalArgumentException, IllegalAccessException{
        Queue queue=(Queue)field.get(obj);
        if(queue.size()>0){
            queue.forEach(e->{
            setElement(e.getClass().getSimpleName(),e);
            });
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
        
    }
    private void setElement(String type, Object o) {
        switch (type) {
            case "String":
            case "Character":
            case "char":
                json.append("\"").append(o).append("\",");
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
                json.append(o).append(",");
                break;
            default:
                break;
        }
    }
}
