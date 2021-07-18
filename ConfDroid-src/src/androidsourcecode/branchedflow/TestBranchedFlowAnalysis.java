/*    */ package androidsourcecode.branchedflow;
/*    */ 
/*    */ import callgraph.fcgbuilder.FCGBuilder;
/*    */ import org.junit.Test;
/*    */ import soot.Scene;
/*    */ import soot.SootClass;
/*    */ import soot.SootMethod;
/*    */ import soot.util.Chain;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestBranchedFlowAnalysis
/*    */ {
/*    */   @Test
/*    */   public void testInitCallGraph() {
/* 16 */     String appPath = "/Users/name1/Downloads/AttrMiner/out/production/AttrMiner";
/* 17 */     FCGBuilder.initSoot(appPath);
/*    */     
/* 19 */     Chain<SootClass> classes = Scene.v().getApplicationClasses();
/* 20 */     for (SootClass cls : classes) {
/* 21 */       System.out.println(cls);
/* 22 */       for (SootMethod mtd : cls.getMethods())
/* 23 */         System.out.println(mtd.getActiveBody()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/TestBranchedFlowAnalysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */