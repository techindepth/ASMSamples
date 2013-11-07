package com.bj.asm.tree;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TreeClassReader {

    public static void main(String[] args) throws Exception{
        InputStream in=new FileInputStream("out/com/geekyarticles/asm/Generated.class");
        
        ClassReader cr=new ClassReader(in);
        ClassNode classNode=new ClassNode();
        
        //ClassNode is a ClassVisitor
        cr.accept(classNode, 0);
        
        //Let's move through all the methods
        List<MethodNode> methods = classNode.methods;
        for(MethodNode methodNode:methods){
            System.out.println(methodNode.name+"  "+methodNode.desc);
        }

    }

}