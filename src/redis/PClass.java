package redis;

public abstract class PClass {

    private String f1;
    private String f2;
    
    public void t(){
        System.out.println(this.f1);
        System.out.println(this.f2);
        System.out.println(this.toString());
    }
}
