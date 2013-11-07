package com.bj.asm.tree;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;


public class TreeHelloWorldDemo {
    public static void main(String [] args) throws Exception{
        
        ClassNode classNode=new ClassNode(4);//4 is just the API version number
        
        //These properties of the classNode must be set
        classNode.version=Opcodes.V1_6;//The generated class will only run on JRE 1.6 or above
        classNode.access=Opcodes.ACC_PUBLIC;
        classNode.signature="Lcom/geekyarticles/asm/Generated;";
        classNode.name="com/geekyarticles/asm/Generated";
        classNode.superName="java/lang/Object";
        
        //Create a method
        MethodNode mainMethod=new MethodNode(4,Opcodes.ACC_PUBLIC|Opcodes.ACC_STATIC,"main", "([Ljava/lang/String;)V",null, null);
        
        mainMethod.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        mainMethod.instructions.add(new LdcInsnNode("Hello World!"));
        mainMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
        mainMethod.instructions.add(new InsnNode(Opcodes.RETURN));

        //Add the method to the classNode
        classNode.methods.add(mainMethod);
        
        //Write the class
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        
        //Dump the class in a file
        File outDir=new File("out/com/geekyarticles/asm");
        outDir.mkdirs();
        DataOutputStream dout=new DataOutputStream(new FileOutputStream(new File(outDir,"Generated.class")));
        dout.write(cw.toByteArray());
        dout.flush();
        dout.close();
        
    }
}