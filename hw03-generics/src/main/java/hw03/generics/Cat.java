package hw03.generics;

public class Cat extends Animal {
    private Double voiceTone=1.0;

    Cat(String name) {
        super(name);
    }

    Cat(String name, Double voiceTone) {
        super(name);
        this.voiceTone=voiceTone;
    }
    Double getTone(){return voiceTone;}
    void setTone(Double voiceTone){this.voiceTone=voiceTone;}

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + this.getName() +'\''+ "  voiceTone="+getTone()+'}';
    }
}