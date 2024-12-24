package project3;

public class Counter {
    double counterValue;
    public double increment(){
        return ++counterValue;
    }
    public double decrement() {
        return --counterValue;
    }
    public double reset() {
        return counterValue=0;
    }
}

class Test{
    public static void main(String args[]) {
        Counter a=new Counter();
        a.counterValue=5;
        System.out.println(a.counterValue);
        System.out.println(a.increment());
        System.out.println(a.decrement());
        System.out.println(a.reset());
    }
}
