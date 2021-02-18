package hw16_serialization;

import java.util.*;
import java.time.LocalDate;

public class AniObject {

    int value1;
    float value2;
    boolean valueBoolean;
    String value3;
    int[] valueArray;
    List<Double> valueList;
    Set<String> valueSet;
    Map<String, Double> valueMap;

    transient LocalDate dateToDate;

    public AniObject(int value1, float value2, boolean valueBoolean, String value3,
                     int[] valueArray, List<Double> valueList,
                     Set<String> valueSet, Map<String, Double> valueMap) {
        this.value1 = value1;
        this.value2 = value2;
        this.valueBoolean = valueBoolean;
        this.value3 = value3;
        this.valueArray = valueArray;
        this.valueList = valueList;
        this.valueSet = valueSet;
        this.valueMap = valueMap;
        dateToDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "AnyObject [ value1 = " + value1 + ", value2 = " + value2 + ", valueBoolean = " + valueBoolean
                + ", value3 = " + value3 + ", valueArray =" + Arrays.toString(valueArray)
                + ", valueList = " + valueList  + ", valueSet = " + valueSet
                + ", valueMap = " + valueMap
                + ", date = " + dateToDate + "]"
                ;
    }

    public void setDate() {
        dateToDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AniObject that = (AniObject) o;
        return  value1 == that.value1 &&
                value2 == that.value2 &&
                valueBoolean == that.valueBoolean &&
                (Objects.equals(value3, that.value3)) &&
                Arrays.equals(valueArray, that.valueArray) &&
                Objects.equals(valueList, that.valueList) &&
                Objects.equals(valueSet, that.valueSet) &&
                Objects.equals(valueMap, that.valueMap) ;
    }

    public String equalityString (Object o) {
        AniObject that = (AniObject) o;
        String str = "";
        return  str
                + (value1 == that.value1)
                + (value2 == that.value2)
                + (valueBoolean == that.valueBoolean)
                + Objects.equals(value3, that.value3)
                + Arrays.equals(valueArray, that.valueArray)
                + Objects.equals(valueList, that.valueList)
                + Objects.equals(valueSet, that.valueSet)
                + Objects.equals(valueMap, that.valueMap);
    }

}
