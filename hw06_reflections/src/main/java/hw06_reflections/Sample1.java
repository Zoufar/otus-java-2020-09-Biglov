package hw06_reflections;


public class Sample1{
    private double field;

    public Sample1(double f){this.field=f;}

    @Test
    public void methodTest(Double z){
        if(z<0.6*field){throw new RuntimeException();}
    }

    @After
    public void methodAfter(Double z){
        if(z<0.8*field){    throw new RuntimeException();}
    }

    @Before
    public void methodBefore(Double z) {
        if(z<0.4*field){throw new RuntimeException();}
            }

    public double getField() {return field;}

   }
