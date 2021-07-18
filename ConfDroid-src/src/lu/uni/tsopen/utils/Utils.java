/*     */ package lu.uni.tsopen.utils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import lu.uni.tsopen.logicBombs.PotentialLogicBombsRecovery;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.PathPredicateRecovery;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import org.javatuples.Pair;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import soot.FastHierarchy;
/*     */ import soot.MethodOrMethodContext;
/*     */ import soot.Scene;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.ValueBox;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.InvokeExpr;
/*     */ import soot.jimple.InvokeStmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ import soot.jimple.internal.IdentityRefBox;
/*     */ import soot.jimple.toolkits.callgraph.Edge;
/*     */ import soot.tagkit.StringConstantValueTag;
/*     */ import soot.toolkits.graph.DominatorTree;
/*     */ import soot.toolkits.graph.DominatorsFinder;
/*     */ import soot.toolkits.graph.MHGDominatorsFinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*  78 */   protected static Logger logger = LoggerFactory.getLogger(Utils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCaughtException(Unit u) {
/*  87 */     for (ValueBox useBox : u.getUseBoxes()) {
/*  88 */       if (useBox instanceof IdentityRefBox && (
/*  89 */         (IdentityRefBox)useBox).getValue() instanceof soot.jimple.CaughtExceptionRef) {
/*  90 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean containsTag(Value v, String tag, SymbolicExecution se) {
/*  98 */     List<SymbolicValue> values = getSymbolicValues(v, se);
/*  99 */     if (values != null) {
/* 100 */       for (SymbolicValue sv : values) {
/* 101 */         if (sv.containsTag(tag)) {
/* 102 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean containsTags(Value v, SymbolicExecution se) {
/* 110 */     List<SymbolicValue> values = getSymbolicValues(v, se);
/* 111 */     if (values != null) {
/* 112 */       for (SymbolicValue sv : values) {
/* 113 */         if (sv.hasTag()) {
/* 114 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   public static void propagateTags(Value src, SymbolicValue dst, SymbolicExecution se) {
/* 122 */     List<SymbolicValue> values = getSymbolicValues(src, se);
/* 123 */     if (values != null) {
/* 124 */       for (SymbolicValue sv : values) {
/* 125 */         if (sv != null && 
/* 126 */           sv.hasTag()) {
/* 127 */           for (StringConstantValueTag t : sv.getTags()) {
/* 128 */             if (!dst.containsTag(t.getStringValue())) {
/* 129 */               dst.addTag(new StringConstantValueTag(t.getStringValue()));
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SymbolicValue> getSymbolicValues(Value v, SymbolicExecution se) {
/* 139 */     List<SymbolicValue> values = null;
/* 140 */     ContextualValues contextualValues = null;
/* 141 */     if (v != null) {
/* 142 */       contextualValues = (ContextualValues)se.getContext().get(v);
/* 143 */       if (contextualValues == null && v instanceof soot.jimple.InstanceFieldRef) {
/* 144 */         for (Map.Entry<Value, ContextualValues> e : (Iterable<Map.Entry<Value, ContextualValues>>)se.getContext().entrySet()) {
/* 145 */           if (((Value)e.getKey()).toString().contains(v.toString())) {
/* 146 */             contextualValues = (ContextualValues)se.getContext().get(e.getKey());
/*     */           }
/*     */         } 
/*     */       }
/* 150 */       if (contextualValues != null) {
/* 151 */         values = contextualValues.getAllValues();
/* 152 */         if (values == null) {
/* 153 */           values = contextualValues.getAllValues();
/*     */         }
/*     */       } 
/*     */     } 
/* 157 */     return values;
/*     */   }
/*     */   
/*     */   public static String getFormattedTime(long time) {
/* 161 */     long millis = TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
/* 162 */     long seconds = 0L;
/* 163 */     long minutes = 0L;
/* 164 */     long hours = 0L;
/* 165 */     String strTime = "";
/* 166 */     if (millis >= 1000L) {
/* 167 */       seconds = millis / 1000L;
/* 168 */       if (seconds >= 60L) {
/* 169 */         minutes = seconds / 60L;
/* 170 */         if (minutes >= 60L) {
/* 171 */           hours = minutes / 60L;
/* 172 */           strTime = strTime + String.format("%3s %s", new Object[] { Long.valueOf(hours), (hours > 1L) ? "hours" : "hour" });
/*     */         } else {
/* 174 */           strTime = strTime + String.format("%3s %s", new Object[] { Long.valueOf(minutes), (minutes > 1L) ? "mins" : "min" });
/*     */         } 
/*     */       } else {
/* 177 */         strTime = strTime + String.format("%3s %s", new Object[] { Long.valueOf(seconds), "s" });
/*     */       } 
/*     */     } else {
/* 180 */       strTime = strTime + String.format("%3s %s", new Object[] { Long.valueOf(millis), "ms" });
/*     */     } 
/* 182 */     return strTime;
/*     */   }
/*     */   
/*     */   public static Collection<SootMethod> getInvokedMethods(Unit block, InfoflowCFG icfg) {
/* 186 */     FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
/* 187 */     Collection<SootClass> classes = null;
/* 188 */     Collection<SootMethod> methods = new ArrayList<>();
/* 189 */     SootMethod method = null;
/* 190 */     DefinitionStmt defUnit = null;
/* 191 */     Value value = null;
/* 192 */     if (block instanceof InvokeStmt) {
/* 193 */       methods.addAll(icfg.getCalleesOfCallAt(block));
/* 194 */     } else if (block instanceof DefinitionStmt) {
/* 195 */       defUnit = (DefinitionStmt)block;
/* 196 */       if (defUnit.getRightOp() instanceof InvokeExpr) {
/* 197 */         methods.addAll(icfg.getCalleesOfCallAt((Unit)defUnit));
/*     */       }
/*     */     } 
/* 200 */     if (methods.isEmpty()) {
/* 201 */       for (ValueBox v : block.getUseAndDefBoxes()) {
/* 202 */         value = v.getValue();
/* 203 */         if (value instanceof InvokeExpr) {
/* 204 */           method = ((InvokeExpr)value).getMethod();
/* 205 */           if (method.isAbstract()) {
/* 206 */             classes = fh.getSubclassesOf(method.getDeclaringClass());
/* 207 */             for (SootClass c : classes) {
/* 208 */               for (SootMethod m : c.getMethods()) {
/* 209 */                 if (m.getSubSignature().equals(method.getSubSignature()) && 
/* 210 */                   !methods.contains(m)) {
/* 211 */                   methods.add(m);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             continue;
/*     */           } 
/* 217 */           methods.add(method);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 222 */     return methods;
/*     */   }
/*     */   
/*     */   public static boolean isDummy(SootMethod m) {
/* 226 */     return m.getName().startsWith("dummyMainMethod");
/*     */   }
/*     */   
/*     */   private static List<SootClass> getAllSuperClasses(SootClass sootClass) {
/* 230 */     List<SootClass> classes = new ArrayList<>();
/* 231 */     if (sootClass.hasSuperclass()) {
/* 232 */       classes.add(sootClass.getSuperclass());
/* 233 */       classes.addAll(getAllSuperClasses(sootClass.getSuperclass()));
/*     */     } 
/* 235 */     return classes;
/*     */   }
/*     */   
/*     */   public static String getComponentType(SootClass sc) {
/* 239 */     List<SootClass> classes = getAllSuperClasses(sc);
/* 240 */     for (SootClass c : classes) {
/* 241 */       switch (c.getName()) { case "android.app.Activity":
/* 242 */           return "Activity";
/* 243 */         case "android.content.BroadcastReceiver": return "BroadcastReceiver";
/* 244 */         case "android.content.ContentProvider": return "ContentProvider";
/* 245 */         case "android.app.Service": return "Service"; }
/*     */     
/*     */     } 
/* 248 */     return "BasicClass";
/*     */   }
/*     */   
/*     */   public static boolean isInCallGraph(SootMethod m) {
/* 252 */     MethodOrMethodContext next = null;
/* 253 */     Iterator<MethodOrMethodContext> itMethod = Scene.v().getCallGraph().sourceMethods();
/* 254 */     Iterator<Edge> itEdge = null;
/* 255 */     Edge e = null;
/* 256 */     while (itMethod.hasNext()) {
/* 257 */       next = itMethod.next();
/* 258 */       itEdge = Scene.v().getCallGraph().edgesOutOf(next);
/* 259 */       while (itEdge.hasNext()) {
/* 260 */         e = itEdge.next();
/* 261 */         if (e.tgt().equals(m)) {
/* 262 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 266 */     return false;
/*     */   }
/*     */   
/*     */   public static int getGuardedBlocksDensity(PathPredicateRecovery ppr, IfStmt ifStmt) {
/* 270 */     return ppr.getGuardedBlocks(ifStmt).size();
/*     */   }
/*     */   
/*     */   public static boolean guardedBlocksContainApplicationInvoke(PathPredicateRecovery ppr, IfStmt ifStmt) {
/* 274 */     SootMethod m = null;
/* 275 */     for (Unit u : ppr.getGuardedBlocks(ifStmt)) {
/* 276 */       if (u instanceof InvokeStmt) {
/* 277 */         m = ((InvokeStmt)u).getInvokeExpr().getMethod();
/* 278 */         if (m.getDeclaringClass().isApplicationClass()) {
/* 279 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   public static <T> String join(String sep, List<T> list) {
/* 287 */     String s = "(";
/* 288 */     for (int i = 0; i < list.size(); i++) {
/* 289 */       s = s + list.get(i).toString();
/* 290 */       if (i != list.size() - 1) {
/* 291 */         s = s + sep;
/*     */       }
/*     */     } 
/* 294 */     s = s + ")";
/* 295 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<SootMethod> getLogicBombCallStack(SootMethod m) {
/* 303 */     Iterator<Edge> it = Scene.v().getCallGraph().edgesInto((MethodOrMethodContext)m);
/* 304 */     Edge next = null;
/* 305 */     List<SootMethod> methods = new LinkedList<>();
/* 306 */     methods.add(m);
/*     */     
/* 308 */     if (it.hasNext()) {
/* 309 */       next = it.next();
/* 310 */       methods.addAll(getLogicBombCallStack(next.src()));
/* 311 */       return methods;
/*     */     } 
/* 313 */     return methods;
/*     */   }
/*     */   
/*     */   public static List<Integer> getLengthLogicBombCallStack(SootMethod m, Integer c, List<Integer> l, List<SootMethod> visitedMethods) {
/* 317 */     Iterator<Edge> it = Scene.v().getCallGraph().edgesInto((MethodOrMethodContext)m);
/* 318 */     Edge next = null;
/*     */     
/* 320 */     visitedMethods.add(m);
/* 321 */     if (!it.hasNext()) {
/* 322 */       l.add(Integer.valueOf(c.intValue()));
/*     */     }
/*     */     
/* 325 */     while (it.hasNext()) {
/* 326 */       next = it.next();
/* 327 */       if (visitedMethods.contains(next.src())) {
/*     */         continue;
/*     */       }
/* 330 */       c = Integer.valueOf(c.intValue() + 1);
/* 331 */       getLengthLogicBombCallStack(next.src(), c, l, visitedMethods);
/* 332 */       visitedMethods.remove(m);
/* 333 */       c = Integer.valueOf(c.intValue() - 1);
/*     */     } 
/* 335 */     return l;
/*     */   }
/*     */   
/*     */   public static List<Integer> getLengthLogicBombCallStack(SootMethod m) {
/* 339 */     return getLengthLogicBombCallStack(m, Integer.valueOf(0), new ArrayList<>(), new ArrayList<>());
/*     */   }
/*     */   
/*     */   public static boolean isSensitiveMethod(SootMethod m) {
/* 343 */     InputStream fis = null;
/* 344 */     BufferedReader br = null;
/* 345 */     String line = null;
/*     */     try {
/* 347 */       fis = Utils.class.getResourceAsStream("/sensitiveMethods.pscout");
/* 348 */       br = new BufferedReader(new InputStreamReader(fis));
/* 349 */       while ((line = br.readLine()) != null) {
/* 350 */         if (m.getSignature().equals(line)) {
/* 351 */           br.close();
/* 352 */           fis.close();
/* 353 */           return true;
/*     */         } 
/*     */       } 
/* 356 */     } catch (IOException e) {
/* 357 */       logger.error(e.getMessage());
/*     */     } 
/*     */     try {
/* 360 */       br.close();
/* 361 */       fis.close();
/* 362 */     } catch (IOException e) {
/* 363 */       logger.error(e.getMessage());
/*     */     } 
/* 365 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isNested(IfStmt ifStmt, InfoflowCFG icfg, PotentialLogicBombsRecovery plbr, PathPredicateRecovery ppr) {
/* 369 */     Map<IfStmt, Pair<List<SymbolicValue>, SootMethod>> plbs = plbr.getPotentialLogicBombs();
/* 370 */     IfStmt currentIf = null;
/* 371 */     SootMethod method1 = icfg.getMethodOf((Unit)ifStmt);
/* 372 */     SootMethod method2 = null;
/* 373 */     MHGDominatorsFinder<Unit> df = new MHGDominatorsFinder(icfg.getOrCreateUnitGraph(method1));
/* 374 */     DominatorTree<Unit> dt = new DominatorTree((DominatorsFinder)df);
/* 375 */     for (Map.Entry<IfStmt, Pair<List<SymbolicValue>, SootMethod>> e : plbs.entrySet()) {
/* 376 */       currentIf = e.getKey();
/* 377 */       method2 = icfg.getMethodOf((Unit)currentIf);
/* 378 */       if (ifStmt != currentIf && 
/* 379 */         method1 == method2 && 
/* 380 */         dt.isDominatorOf(dt.getDode(currentIf), dt.getDode(ifStmt)) && 
/* 381 */         ppr.getGuardedBlocks(currentIf).contains(ifStmt)) {
/* 382 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 388 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isFilteredLib(SootClass sc) {
/* 392 */     InputStream fis = null;
/* 393 */     BufferedReader br = null;
/* 394 */     String line = null;
/*     */     try {
/* 396 */       fis = Utils.class.getResourceAsStream("/filteredLibs.txt");
/* 397 */       br = new BufferedReader(new InputStreamReader(fis));
/* 398 */       while ((line = br.readLine()) != null) {
/* 399 */         if (sc.getName().startsWith(line)) {
/* 400 */           br.close();
/* 401 */           fis.close();
/* 402 */           return true;
/*     */         } 
/*     */       } 
/* 405 */     } catch (IOException e) {
/* 406 */       logger.error(e.getMessage());
/*     */     } 
/*     */     try {
/* 409 */       br.close();
/* 410 */       fis.close();
/* 411 */     } catch (IOException e) {
/* 412 */       logger.error(e.getMessage());
/*     */     } 
/* 414 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/utils/Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */