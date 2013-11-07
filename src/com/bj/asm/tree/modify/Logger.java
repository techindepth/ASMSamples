package com.bj.asm.tree.modify;

public class Logger {
    public static void logMethodStart(String methodName){
        System.out.println("Starting method: "+methodName);
    }
    
    public static void logMethodReturn(String methodName){
        System.out.println("Ending method: "+methodName);
    }
}
