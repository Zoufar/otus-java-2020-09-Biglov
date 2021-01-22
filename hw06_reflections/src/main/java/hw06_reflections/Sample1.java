package hw06_reflections;


public class Sample1{
    private double field;

    public Sample1(double f){this.field=f;}

    public void method1 (double z){
        if(z<Math.random()*field){throw new RuntimeException();}
    }

    public void method2 (double z){
        if(z<Math.random()*field){throw new RuntimeException();}
    }

    public double getField() {return field;}

}
