package hw15_patterns.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void addData(String str) {
        if (data==null){
            data = new ArrayList<>();}
        this.data.add(str);
    }

    public ObjectForMessage copy() {
        ObjectForMessage copy = new ObjectForMessage();
        if (!(data==null)){ data.forEach(str -> copy.addData(str));}
        return copy;
    }

    @Override
    public String toString (){
        String toString = "\nobject: {";
        for (String str : data) {toString+=str+"; ";}
        return toString+"}";
    }
}
