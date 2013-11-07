package com.bj.classmodifiers;

public class ClassModificationDemo {

private int version;
    
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static  void parse(){
    	int i=0;
    }
    
    @Override
    public String toString(){
        return "ClassCreationDemo : "+version;
    }
    
    public static void main(String[] args) {
        System.out.println(new ClassModificationDemo());
        parse();
    }

}