package life.majian.community.test;

public class Child implements Father {
    private String name;
    Child(String name){
        this.name=name;
    }
    @Override
    public void print() {
        System.out.println(name);
    }
}
