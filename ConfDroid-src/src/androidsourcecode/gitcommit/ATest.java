/*    */ package androidsourcecode.gitcommit;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import soot.Body;
/*    */ import soot.SootClass;
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ import soot.UnitPatchingChain;
/*    */ import soot.jimple.Stmt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ATest
/*    */ {
/*    */   public static void main(String[] args) {}
/*    */   
/*    */   public static Set<SootMethod> removeAll(HashSet<SootMethod> aList, HashSet<SootMethod> bList) {
/* 28 */     HashSet<SootMethod> newAList = new HashSet<>();
/* 29 */     for (SootMethod aMethod : aList) {
/* 30 */       String aSig = aMethod.getSignature();
/* 31 */       boolean flag = false;
/* 32 */       for (SootMethod bMethod : bList) {
/* 33 */         String bSig = bMethod.getSignature();
/* 34 */         if (aSig.equals(bSig)) {
/* 35 */           flag = true;
/*    */         }
/*    */       } 
/* 38 */       if (!flag) {
/* 39 */         newAList.add(aMethod);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 44 */     return newAList;
/*    */   }
/*    */   
/*    */   public static ArrayList<Stmt> getStatementsByLineNumber(ArrayList<SootClass> classList, int lineNumber) {
/* 48 */     ArrayList<Stmt> retStmts = new ArrayList<>();
/* 49 */     for (SootClass cls : classList) {
/* 50 */       List<SootMethod> methods = cls.getMethods();
/* 51 */       for (SootMethod method : methods) {
/* 52 */         if (!method.hasActiveBody()) {
/*    */           continue;
/*    */         }
/* 55 */         Body body = method.getActiveBody();
/* 56 */         UnitPatchingChain unitPatchingChain = body.getUnits();
/* 57 */         for (Unit unit : unitPatchingChain) {
/*    */ 
/*    */           
/* 60 */           if (unit.getJavaSourceStartLineNumber() == lineNumber) {
/* 61 */             retStmts.add((Stmt)unit);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 67 */     return retStmts;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/ATest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */