package com.bj.asm.event;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.InstructionAdapter;

public class DemoClassInstructionViewer {

    public static class MethodPrinterVisitor extends ClassVisitor{

        public MethodPrinterVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        
        public MethodPrinterVisitor(int api) {
            super(api);
        }


        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            
            System.out.println("\n"+name+desc);
            
            MethodVisitor oriMv= new MethodVisitor(4) {
            };
            //An instructionAdapter is a special MethodVisitor that
            //lets us process instructions easily
            InstructionAdapter instMv=new InstructionAdapter(oriMv){

                @Override
                public void visitInsn(int opcode) {
                    System.out.println(opcode);
                    super.visitInsn(opcode);
                }
                
            };
            return instMv;
            
        }
        
        

    }
    
    
    public static void main(String[] args) throws Exception{
        InputStream in=DemoClassInstructionViewer.class.getResourceAsStream("/com/bj/asm/event/ASMHelloWorld.class");
        ClassReader classReader=new ClassReader(in);
        classReader.accept(new MethodPrinterVisitor(4), 0);

    }

}