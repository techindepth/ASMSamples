package com.bj.asm.tree.modify;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class LoggingInsertion {

    public static void main(String[] args) throws Exception{
        InputStream in=LoggingInsertion.class.getResourceAsStream("/com/bj/asm/tree/modify/LoggingTest.class");

        ClassReader cr=new ClassReader(in);
        ClassNode classNode=new ClassNode();
        cr.accept(classNode, 0);

        //Let's move through all the methods 
        List<MethodNode> methodenodes = classNode.methods;
        for(MethodNode methodNode :methodenodes){        	
            System.out.println(methodNode.name+"  "+methodNode.desc);
            boolean hasAnnotation=false;
            if(methodNode.visibleAnnotations!=null){
            	List <AnnotationNode> annoNodes = methodNode.visibleAnnotations;
                for(AnnotationNode annotationNode :annoNodes){
                    if(annotationNode.desc.equals("Lcom/bj/asm/tree/modify/Loggable;")){
                        hasAnnotation=true;
                        break;
                    }
                }
            }
            if(hasAnnotation){
                //Lets insert the begin logger
                InsnList beginList=new InsnList();
                beginList.add(new LdcInsnNode(methodNode.name));
                beginList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/bj/asm/tree/modify/Logger", "logMethodStart", "(Ljava/lang/String;)V"));
                
                Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
                while(insnNodes.hasNext()){
                    System.out.println(insnNodes.next().getOpcode());
                }
                
                methodNode.instructions.insert(beginList);
                System.out.println(methodNode.instructions);
                
                
                //A method can have multiple places for return
                //All of them must be handled.
                insnNodes=methodNode.instructions.iterator();
                while(insnNodes.hasNext()){
                    AbstractInsnNode insn=insnNodes.next();
                    System.out.println(insn.getOpcode());
                    
                    if(insn.getOpcode()==Opcodes.IRETURN
                            ||insn.getOpcode()==Opcodes.RETURN
                            ||insn.getOpcode()==Opcodes.ARETURN
                            ||insn.getOpcode()==Opcodes.LRETURN
                            ||insn.getOpcode()==Opcodes.DRETURN){
                        InsnList endList=new InsnList();
                        endList.add(new LdcInsnNode(methodNode.name));
                        endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/bj/asm/tree/modify/Logger", "logMethodReturn", "(Ljava/lang/String;)V"));
                        methodNode.instructions.insertBefore(insn, endList);
                    }
                    
                }
                
            }
        }

        //We are done now. so dump the class
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        File outDir=new File("out/com/geekyarticles/asm");
        outDir.mkdirs();
        DataOutputStream dout=new DataOutputStream(new FileOutputStream(new File(outDir,"LoggingTest.class")));
        dout.write(cw.toByteArray());
        dout.flush();
        dout.close();

    }

}