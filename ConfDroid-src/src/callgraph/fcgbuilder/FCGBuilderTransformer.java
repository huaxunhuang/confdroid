/*    */ package callgraph.fcgbuilder;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import soot.Body;
/*    */ import soot.Scene;
/*    */ import soot.SceneTransformer;
/*    */ import soot.SootClass;
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ import soot.UnitPatchingChain;
/*    */ import soot.jimple.Stmt;
/*    */ import soot.util.Chain;
/*    */ import utils.CommonUtils;
/*    */ import utils.Config;
/*    */ 
/*    */ 
/*    */ public class FCGBuilderTransformer
/*    */   extends SceneTransformer
/*    */ {
/* 25 */   public Set<String> accessedAndroidAPIs = new HashSet<>();
/* 26 */   public Map<String, Set<String>> api2callers = new HashMap<>();
/* 27 */   public int apiLevel = -1;
/*    */   
/*    */   public FCGBuilderTransformer(int apiLevel) {
/* 30 */     this.apiLevel = apiLevel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void extract(Body b) {
/* 37 */     String callerMethodSig = b.getMethod().getSignature();
/*    */     
/* 39 */     UnitPatchingChain unitPatchingChain = b.getUnits();
/*    */     
/* 41 */     for (Iterator<Unit> unitIter = unitPatchingChain.snapshotIterator(); unitIter.hasNext(); ) {
/*    */       
/* 43 */       Stmt stmt = (Stmt)unitIter.next();
/*    */       
/* 45 */       if (stmt.containsInvokeExpr()) {
/*    */         
/*    */         try {
/* 48 */           SootMethod sootMethod = stmt.getInvokeExpr().getMethod();
/* 49 */           String methodSig = sootMethod.getSignature();
/*    */           
/* 51 */           methodSig = methodSig.replace("$", ".");
/*    */           
/* 53 */           this.accessedAndroidAPIs.add(methodSig);
/* 54 */           CommonUtils.put(this.api2callers, methodSig, callerMethodSig);
/* 55 */         } catch (Exception e) {}
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void internalBodyTransform(Body b) {
/* 65 */     extract(b);
/*    */     
/* 67 */     FCGBuilderBodyTransformer.scan(b, this.apiLevel);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void internalTransform(String arg0, Map<String, String> arg1) {
/* 73 */     Chain<SootClass> sootClasses = Scene.v().getApplicationClasses();
/* 74 */     for (Iterator<SootClass> iter = sootClasses.snapshotIterator(); iter.hasNext(); ) {
/* 75 */       SootClass sc = iter.next();
/*    */       
/* 77 */       if (sc.getName().startsWith("android.support.")) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 82 */       List<SootMethod> methods = sc.getMethods();
/*    */       
/* 84 */       for (int i = 0; i < methods.size(); i++) {
/*    */         
/* 86 */         SootMethod sm = methods.get(i);
/* 87 */         Body body = null;
/*    */         
/*    */         try {
/* 90 */           body = sm.retrieveActiveBody();
/*    */         }
/* 92 */         catch (Exception ex) {
/*    */           
/* 94 */           if (Config.DEBUG) {
/* 95 */             System.out.println("[DEBUG] No body for method " + sm.getSignature());
/*    */           }
/*    */         } 
/* 98 */         if (null != body)
/* 99 */           internalBodyTransform(body); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/fcgbuilder/FCGBuilderTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */