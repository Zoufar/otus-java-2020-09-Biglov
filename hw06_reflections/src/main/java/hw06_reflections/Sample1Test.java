package hw06_reflections;


public class Sample1Test {


    @Before
    public void SetUp(){
    }

    @Test
    public void method1Test(){
        System.out.println("Testing method1");
        Sample1 instance=new Sample1(1.0);
        try{instance.method1(0.6);
        }
        catch(RuntimeException e){
            throw new RuntimeException();}
    }

    @Test
    public void method2Test(){
        System.out.println("Testing method2");
        Sample1 instance=new Sample1(1.0);
        try{instance.method2(0.4);
        }
        catch(RuntimeException e){
            throw new RuntimeException();}
    }
    @Test
    public void getFieldTest(){
        System.out.println("Testing getField method");
        double field=1.0;
        double result=0;
        Sample1 instance=new Sample1(field);
        try{result = instance.getField();
            if(result!=field){
                System.out.println("getField gave wrong result");
                throw new RuntimeException();
            }
            }
        catch(RuntimeException e){
            throw new RuntimeException();}
    }

    @After
    public void tearDown(){
        System.out.println("Finished testing ");
    }


}

