/*    */ package callgraph.graph;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import soot.SootClass;
/*    */ 
/*    */ 
/*    */ public class FrameworkCallGraph
/*    */ {
/* 12 */   public static Map<Integer, FrameworkCallGraphInApiLevel> fcgMap = new HashMap<>();
/*    */   
/*    */   public static FrameworkCallGraphInApiLevel getFCG(int apiLevel) {
/* 15 */     return fcgMap.get(Integer.valueOf(apiLevel));
/*    */   }
/*    */   
/*    */   public static void updateFCG(FrameworkCallGraphInApiLevel fcg, int apiLevel) {
/* 19 */     fcgMap.put(Integer.valueOf(apiLevel), fcg);
/*    */   }
/*    */   
/*    */   public static void initialFCG(int apiLevel) {
/* 23 */     fcgMap.put(Integer.valueOf(apiLevel), new FrameworkCallGraphInApiLevel(apiLevel));
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public static void initialFCG(int apiLevel, List<SootClass> sootClasses) {
/* 28 */     fcgMap.put(Integer.valueOf(apiLevel), new FrameworkCallGraphInApiLevel(apiLevel, sootClasses));
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/graph/FrameworkCallGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */