/*     */ package instrumentation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import soot.Body;
/*     */ import soot.BodyTransformer;
/*     */ import soot.Local;
/*     */ import soot.PackManager;
/*     */ import soot.PatchingChain;
/*     */ import soot.RefType;
/*     */ import soot.Scene;
/*     */ import soot.SootMethod;
/*     */ import soot.Transform;
/*     */ import soot.Transformer;
/*     */ import soot.Type;
/*     */ import soot.Unit;
/*     */ import soot.UnitPatchingChain;
/*     */ import soot.Value;
/*     */ import soot.jimple.AbstractStmtSwitch;
/*     */ import soot.jimple.InvokeExpr;
/*     */ import soot.jimple.InvokeStmt;
/*     */ import soot.jimple.Jimple;
/*     */ import soot.jimple.StringConstant;
/*     */ import soot.options.Options;
/*     */ import soot.util.Switch;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstrumentationMain
/*     */ {
/*     */   public static void main(String[] args) {
/*  33 */     Options.v().set_src_prec(5);
/*  34 */     Options.v().set_process_dir(Collections.singletonList("/Volumes/DATASET/google_play/app/house/com.remax.remaxmobile.apk"));
/*  35 */     Options.v().set_soot_classpath("/Users/name1/Documents/GitHub/mine-config/ConfDroid/sootclasses-trunk-jar-with-dependencies.jar");
/*  36 */     Options.v().set_force_android_jar("/Users/name1/Library/Android/sdk/platforms/android-26/android.jar");
/*     */ 
/*     */     
/*  39 */     Options.v().set_output_format(10);
/*     */ 
/*     */     
/*  42 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
/*  43 */     Scene.v().addBasicClass("java.lang.System", 2);
/*     */     
/*  45 */     PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", (Transformer)new BodyTransformer()
/*     */           {
/*     */             protected void internalTransform(final Body b, String phaseName, Map options)
/*     */             {
/*  49 */               final UnitPatchingChain units = b.getUnits();
/*     */ 
/*     */               
/*  52 */               for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext(); ) {
/*  53 */                 final Unit u = iter.next();
/*  54 */                 u.apply((Switch)new AbstractStmtSwitch()
/*     */                     {
/*     */                       public void caseInvokeStmt(InvokeStmt stmt) {
/*  57 */                         InvokeExpr invokeExpr = stmt.getInvokeExpr();
/*  58 */                         if (invokeExpr.getMethod().getName().equals("onDraw")) {
/*     */                           
/*  60 */                           Local tmpRef = InstrumentationMain.addTmpRef(b);
/*  61 */                           Local tmpString = InstrumentationMain.addTmpString(b);
/*     */ 
/*     */                           
/*  64 */                           units.insertBefore((Unit)Jimple.v().newAssignStmt((Value)tmpRef, 
/*  65 */                                 (Value)Jimple.v().newStaticFieldRef(
/*  66 */                                   Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), u);
/*     */ 
/*     */                           
/*  69 */                           units.insertBefore((Unit)Jimple.v().newAssignStmt((Value)tmpString, 
/*  70 */                                 (Value)StringConstant.v("HELLO")), u);
/*     */ 
/*     */                           
/*  73 */                           SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
/*  74 */                           units.insertBefore((Unit)Jimple.v().newInvokeStmt(
/*  75 */                                 (Value)Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), (Value)tmpString)), u);
/*     */ 
/*     */                           
/*  78 */                           b.validate();
/*     */                         } 
/*     */                       }
/*     */                     });
/*     */               } 
/*     */             }
/*     */           }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     Scene.v().loadNecessaryClasses();
/*     */     
/*  91 */     PackManager.v().runPacks();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Local addTmpRef(Body body) {
/*  96 */     Local tmpRef = Jimple.v().newLocal("tmpRef", (Type)RefType.v("java.io.PrintStream"));
/*  97 */     body.getLocals().add(tmpRef);
/*  98 */     return tmpRef;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Local addTmpString(Body body) {
/* 103 */     Local tmpString = Jimple.v().newLocal("tmpString", (Type)RefType.v("java.lang.String"));
/* 104 */     body.getLocals().add(tmpString);
/* 105 */     return tmpString;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/instrumentation/InstrumentationMain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */