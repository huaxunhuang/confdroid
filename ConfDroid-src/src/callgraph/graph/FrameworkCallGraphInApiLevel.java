/*     */ package callgraph.graph;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.UnitPatchingChain;
/*     */ import utils.MethodSignature;
/*     */ 
/*     */ public class FrameworkCallGraphInApiLevel
/*     */ {
/*  17 */   public static Map<String, Set<Edge>> srcMethod2edges = new HashMap<>();
/*  18 */   public Map<String, Set<Edge>> tgtMethod2edges = new HashMap<>();
/*  19 */   public Map<String, Set<String>> cls2methods = new HashMap<>();
/*     */   
/*  21 */   private Map<String, Edge> existingEdges = new HashMap<>();
/*  22 */   private int apiLevel = -1;
/*  23 */   private List<SootClass> sootClasses = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> visitedCalls;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SootMethod getSootMethodByClassAndLineNum(String className, int lineNum) {
/*  35 */     SootClass sootClass = getSootClass(className);
/*  36 */     if (sootClass != null) {
/*  37 */       List<SootMethod> sootMethods = sootClass.getMethods();
/*  38 */       for (SootMethod sootMethod : sootMethods) {
/*  39 */         UnitPatchingChain unitPatchingChain = sootMethod.getActiveBody().getUnits();
/*  40 */         for (Unit unit : unitPatchingChain) {
/*  41 */           int uLineNum = unit.getJavaSourceStartLineNumber();
/*  42 */           if (uLineNum == lineNum) {
/*  43 */             return sootMethod;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*  48 */     return null;
/*     */   }
/*     */   
/*     */   public SootMethod getMethod(String signature) {
/*  52 */     for (SootClass sootClass : this.sootClasses) {
/*  53 */       List<SootMethod> sootMethods = sootClass.getMethods();
/*  54 */       for (SootMethod sootMethod : sootMethods) {
/*  55 */         if (sootMethod.getSignature().equals(signature)) {
/*  56 */           return sootMethod;
/*     */         }
/*     */       } 
/*     */     } 
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public void setApiLevel(int apiLevel) {
/*  64 */     this.apiLevel = apiLevel;
/*     */   }
/*     */   
/*     */   public int getApiLevel() {
/*  68 */     return getApiLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEdge(Edge edge) {
/*  73 */     if (edge.srcSig.isEmpty() || edge.tgtSig.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (edge.srcSig.equals(edge.tgtSig)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     if (srcMethod2edges.containsKey(edge.srcSig)) {
/*     */       
/*  86 */       Set<Edge> srcEdges = srcMethod2edges.get(edge.srcSig);
/*  87 */       srcEdges.add(edge);
/*  88 */       srcMethod2edges.put(edge.srcSig, srcEdges);
/*     */     }
/*     */     else {
/*     */       
/*  92 */       Set<Edge> srcEdges = new HashSet<>();
/*  93 */       srcEdges.add(edge);
/*  94 */       srcMethod2edges.put(edge.srcSig, srcEdges);
/*     */     } 
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
/* 114 */     if (this.tgtMethod2edges.containsKey(edge.tgtSig)) {
/*     */       
/* 116 */       Set<Edge> tgtEdges = this.tgtMethod2edges.get(edge.tgtSig);
/* 117 */       tgtEdges.add(edge);
/* 118 */       this.tgtMethod2edges.put(edge.tgtSig, tgtEdges);
/*     */     }
/*     */     else {
/*     */       
/* 122 */       Set<Edge> tgtEdges = new HashSet<>();
/* 123 */       tgtEdges.add(edge);
/* 124 */       this.tgtMethod2edges.put(edge.tgtSig, tgtEdges);
/*     */     } 
/*     */     
/* 127 */     if (!edge.srcSig.contains("<init>")) {
/*     */       
/* 129 */       String cls = (new MethodSignature(edge.srcSig)).getCls();
/* 130 */       if (this.cls2methods.containsKey(cls)) {
/*     */         
/* 132 */         Set<String> methods = this.cls2methods.get(cls);
/* 133 */         if (null == methods)
/*     */         {
/* 135 */           methods = new HashSet<>();
/*     */         }
/* 137 */         methods.add(edge.srcSig);
/* 138 */         this.cls2methods.put(cls, methods);
/*     */       }
/*     */       else {
/*     */         
/* 142 */         Set<String> methods = new HashSet<>();
/* 143 */         methods.add(edge.srcSig);
/* 144 */         this.cls2methods.put(cls, methods);
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     if (!edge.tgtSig.contains("<init>")) {
/*     */       
/* 150 */       String cls = (new MethodSignature(edge.tgtSig)).getCls();
/* 151 */       if (this.cls2methods.containsKey(cls)) {
/*     */         
/* 153 */         Set<String> methods = this.cls2methods.get(cls);
/* 154 */         if (null == methods)
/*     */         {
/* 156 */           methods = new HashSet<>();
/*     */         }
/* 158 */         methods.add(edge.tgtSig);
/* 159 */         this.cls2methods.put(cls, methods);
/*     */       }
/*     */       else {
/*     */         
/* 163 */         Set<String> methods = new HashSet<>();
/* 164 */         methods.add(edge.tgtSig);
/* 165 */         this.cls2methods.put(cls, methods);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Edge getEdge(String srcSig, String tgtSig) {
/* 172 */     String key = srcSig + "/" + tgtSig;
/* 173 */     if (getExistingEdges().containsKey(key))
/*     */     {
/* 175 */       return getExistingEdges().get(key);
/*     */     }
/*     */ 
/*     */     
/* 179 */     Edge edge = new Edge();
/* 180 */     edge.srcSig = srcSig;
/* 181 */     edge.tgtSig = tgtSig;
/*     */     
/* 183 */     getExistingEdges().put(key, edge);
/*     */     
/* 185 */     return edge;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void expandConstructors() {
/* 191 */     Set<String> initMethods = new HashSet<>();
/*     */     
/* 193 */     for (String method : this.tgtMethod2edges.keySet()) {
/*     */       
/* 195 */       if (method.contains("<init>"))
/*     */       {
/* 197 */         initMethods.add(method);
/*     */       }
/*     */     } 
/*     */     
/* 201 */     for (String method : initMethods) {
/*     */       
/* 203 */       String cls = (new MethodSignature(method)).getCls();
/*     */       
/* 205 */       if (this.cls2methods.containsKey(cls)) {
/*     */         
/* 207 */         Set<String> methods = this.cls2methods.get(cls);
/* 208 */         for (String m : methods) {
/*     */           
/* 210 */           Edge edge = new Edge();
/* 211 */           edge.srcSig = method;
/* 212 */           edge.tgtSig = m;
/*     */           
/* 214 */           if (this.tgtMethod2edges.containsKey(edge.tgtSig)) {
/*     */             
/* 216 */             Set<Edge> set = this.tgtMethod2edges.get(edge.tgtSig);
/* 217 */             set.add(edge);
/* 218 */             this.tgtMethod2edges.put(edge.tgtSig, set);
/*     */             
/*     */             continue;
/*     */           } 
/* 222 */           Set<Edge> tgtEdges = new HashSet<>();
/* 223 */           tgtEdges.add(edge);
/* 224 */           this.tgtMethod2edges.put(edge.tgtSig, tgtEdges);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> obtainConditions(String methodSig) {
/* 234 */     List<String> conditions = new ArrayList<>();
/*     */     
/* 236 */     if (!this.tgtMethod2edges.containsKey(methodSig))
/*     */     {
/* 238 */       return conditions;
/*     */     }
/*     */     
/* 241 */     Set<Edge> edges = this.tgtMethod2edges.get(methodSig);
/*     */     
/* 243 */     List<Edge> workList = new ArrayList<>();
/* 244 */     workList.addAll(edges);
/*     */     
/* 246 */     Set<Edge> visitedEdges = new HashSet<>();
/*     */     
/* 248 */     while (!workList.isEmpty()) {
/*     */       
/* 250 */       Edge e = workList.remove(0);
/* 251 */       visitedEdges.add(e);
/*     */       
/* 253 */       String cond = e.conditions.toString().replaceAll("\\[", "").replaceAll("]", "");
/*     */       
/* 255 */       if (!cond.isEmpty())
/*     */       {
/* 257 */         conditions.add(e.conditions.toString());
/*     */       }
/*     */       
/* 260 */       edges = this.tgtMethod2edges.get(e.srcSig);
/*     */       
/* 262 */       if (null != edges)
/*     */       {
/* 264 */         for (Edge edge : edges) {
/*     */           
/* 266 */           if (!visitedEdges.contains(edge))
/*     */           {
/* 268 */             workList.add(edge);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 275 */     return conditions;
/*     */   }
/*     */   
/* 278 */   public FrameworkCallGraphInApiLevel(int apiLevel) { this.visitedCalls = null; this.apiLevel = apiLevel; } public FrameworkCallGraphInApiLevel(int apiLevel, List<SootClass> sootClasses) { this.visitedCalls = null;
/*     */     this.apiLevel = apiLevel;
/*     */     this.sootClasses = sootClasses; }
/*     */    public List<String> obtainCallStack(String methodSig) {
/* 282 */     List<String> callStack = new ArrayList<>();
/* 283 */     callStack.add(methodSig + "\n");
/*     */     
/* 285 */     String arrow = "> ";
/*     */     
/* 287 */     this.visitedCalls = new HashSet<>();
/* 288 */     this.visitedCalls.add(methodSig);
/*     */     
/* 290 */     if (null != this.tgtMethod2edges.get(methodSig))
/*     */     {
/* 292 */       for (Edge e : this.tgtMethod2edges.get(methodSig))
/*     */       {
/* 294 */         obtainCallStack(callStack, "--" + arrow, e);
/*     */       }
/*     */     }
/*     */     
/* 298 */     return callStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> obtainCallStack(List<String> callStack, String arrow, Edge edge) {
/* 304 */     if (this.visitedCalls.contains(edge.srcSig))
/*     */     {
/* 306 */       return callStack;
/*     */     }
/*     */ 
/*     */     
/* 310 */     this.visitedCalls.add(edge.srcSig);
/*     */ 
/*     */     
/* 313 */     callStack.add("|" + arrow + edge.srcSig + " " + edge.conditions + "\n");
/*     */     
/* 315 */     if (null != this.tgtMethod2edges.get(edge.srcSig))
/*     */     {
/* 317 */       for (Edge e : this.tgtMethod2edges.get(edge.srcSig))
/*     */       {
/* 319 */         obtainCallStack(callStack, "--" + arrow, e);
/*     */       }
/*     */     }
/*     */     
/* 323 */     return callStack;
/*     */   }
/*     */   
/*     */   public Map<String, Edge> getExistingEdges() {
/* 327 */     return this.existingEdges;
/*     */   }
/*     */   
/*     */   public SootClass getSootClass(String className) {
/* 331 */     List<SootClass> sootClasses = getSootClasses();
/* 332 */     for (SootClass sootClass : sootClasses) {
/* 333 */       String mClassName = sootClass.getName();
/* 334 */       if (mClassName.equals(className)) {
/* 335 */         return sootClass;
/*     */       }
/*     */     } 
/* 338 */     return null;
/*     */   }
/*     */   
/*     */   public List<SootClass> getSootClasses() {
/* 342 */     return this.sootClasses;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/graph/FrameworkCallGraphInApiLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */