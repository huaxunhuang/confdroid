/*     */ package z3solver;
/*     */ import com.microsoft.z3.*;
/*     */
/*     */
/*     */
/*     */
/*     */
import org.junit.Test;

import java.util.List;

/*     */
/*     */ public class Z3Utils {
/*     */   @Test
/*     */   public void test() throws Exception {
/*  12 */     Context ctx = new Context();
/*  13 */     Optimize optimize = ctx.mkOptimize();
/*     */     
/*  15 */     Expr str = ctx.mkConst("str", (Sort)ctx.mkStringSort());
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void ExampleOfZ3() throws Exception {
/*  34 */     Context ctx = new Context();
/*     */     
/*  36 */     System.out.println("ArrayExample2");
/*     */     
/*  38 */     IntSort intSort = ctx.getIntSort();
/*  39 */     BoolSort boolSort = ctx.getBoolSort();
/*  40 */     ArraySort arraySort = ctx.mkArraySort((Sort)intSort, (Sort)intSort);
/*     */     
/*  42 */     ArrayExpr attrVal = ctx.mkArrayConst("attrVal", (Sort)intSort, (Sort)intSort);
/*  43 */     ArrayExpr attrRead = ctx.mkArrayConst("attrRead", (Sort)intSort, (Sort)boolSort);
/*     */     
/*  45 */     Expr f = ctx.mkConst("f", (Sort)intSort);
/*  46 */     IntExpr intExpr = ctx.mkIntConst("g");
/*  47 */     Expr h = ctx.mkConst("h", (Sort)intSort);
/*     */     
/*  49 */     BoolExpr expr1 = ctx.mkTrue();
/*  50 */     expr1 = ctx.mkNot(ctx.mkEq(ctx.mkSelect(attrVal, (Expr)ctx.mkInt(14)), (Expr)ctx.mkInt(-100)));
/*  51 */     expr1 = ctx.mkAnd(new BoolExpr[] { expr1, ctx.mkNot(ctx.mkEq(ctx.mkSelect(attrVal, (Expr)ctx.mkInt(15)), (Expr)ctx.mkInt(-100))) });
/*  52 */     expr1 = ctx.mkAnd(new BoolExpr[] { expr1, ctx.mkEq(ctx.mkSelect(attrRead, (Expr)ctx.mkInt(15)), (Expr)ctx.mkTrue()) });
/*  53 */     expr1 = ctx.mkImplies(ctx.mkEq(ctx.mkSelect(attrRead, (Expr)ctx.mkInt(14)), (Expr)ctx.mkTrue()), expr1);
/*     */     
/*  55 */     System.out.println();
/*     */     
/*  57 */     BoolExpr expr2 = ctx.mkTrue();
/*  58 */     expr2 = ctx.mkNot(ctx.mkEq(ctx.mkSelect(attrVal, (Expr)ctx.mkInt(14)), (Expr)ctx.mkInt(-100)));
/*  59 */     expr2 = ctx.mkAnd(new BoolExpr[] { expr2, ctx.mkNot(ctx.mkEq(ctx.mkSelect(attrVal, (Expr)ctx.mkInt(15)), (Expr)ctx.mkInt(-100))) });
/*  60 */     expr2 = ctx.mkAnd(new BoolExpr[] { expr2, ctx.mkEq(ctx.mkSelect(attrRead, (Expr)ctx.mkInt(15)), (Expr)ctx.mkTrue()) });
/*  61 */     expr2 = ctx.mkImplies(ctx.mkAnd(new BoolExpr[] { ctx.mkEq(ctx.mkSelect(attrRead, (Expr)intExpr), (Expr)ctx.mkTrue()), ctx.mkEq((Expr)intExpr, (Expr)ctx.mkInt(14)) }), expr2);
/*     */ 
/*     */     
/*  64 */     BoolExpr expr3 = ctx.mkTrue();
/*     */     
/*  66 */     expr3 = ctx.mkAnd(new BoolExpr[] { ctx.mkNot(expr1), expr2 });
/*  67 */     System.out.println("expr3: " + expr3);
/*     */ 
/*     */ 
/*     */     
/*  71 */     prove(ctx, expr3);
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void firstExampleOfZ3() throws Exception {
/*  76 */     Context ctx = new Context();
/*     */     
/*  78 */     System.out.println("ArrayExample2");
/*     */     
/*  80 */     IntSort intSort = ctx.getIntSort();
/*  81 */     ArraySort arraySort = ctx.mkArraySort((Sort)intSort, (Sort)intSort);
/*     */     
/*  83 */     ArrayExpr attrA = ctx.mkArrayConst("attrA", (Sort)intSort, (Sort)intSort);
/*  84 */     Expr f = ctx.mkConst("f", (Sort)intSort);
/*  85 */     Expr g = ctx.mkConst("g", (Sort)intSort);
/*  86 */     Expr h = ctx.mkConst("h", (Sort)intSort);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     BoolExpr exprs1 = ctx.mkTrue();
/*  92 */     exprs1 = ctx.mkAnd(new BoolExpr[] { exprs1, ctx.mkEq(f, ctx.mkSelect(attrA, (Expr)ctx.mkInt(14))) });
/*  93 */     exprs1 = ctx.mkImplies(exprs1, ctx.mkBool(true));
/*     */ 
/*     */     
/*  96 */     BoolExpr exprs2 = ctx.mkTrue();
/*  97 */     exprs2 = ctx.mkAnd(new BoolExpr[] { exprs2, ctx.mkEq(ctx.mkSelect(attrA, (Expr)ctx.mkInt(29)), f) });
/*  98 */     exprs2 = ctx.mkAnd(new BoolExpr[] { exprs2, ctx.mkNot(ctx.mkEq(g, (Expr)ctx.mkInt(-100))) });
/*  99 */     exprs2 = ctx.mkImplies(ctx.mkEq(f, ctx.mkSelect(attrA, (Expr)ctx.mkInt(14))), exprs2);
/*     */ 
/*     */ 
/*     */     
/* 103 */     BoolExpr exprs3 = ctx.mkAnd(new BoolExpr[] { exprs2, ctx.mkNot(exprs1) });
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
/*     */ 
/*     */     
/* 141 */     prove(ctx, exprs3);
/*     */   }
/*     */ 
/*     */   
/*     */   Model check(Context ctx, BoolExpr f, Status sat) throws Exception {
/* 146 */     Solver s = ctx.mkSolver();
/* 147 */     s.add(new BoolExpr[] { f });
/* 148 */     if (s.check() != sat)
/* 149 */       throw new Exception(); 
/* 150 */     if (sat == Status.SATISFIABLE) {
/* 151 */       return s.getModel();
/*     */     }
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void prove(Context ctx, BoolExpr assumptions) throws Exception {
/* 162 */     System.out.println("proving: " + assumptions.simplify());
/* 163 */     Solver s = ctx.mkSolver();
/* 164 */     Params p = ctx.mkParams();
/* 165 */     s.setParameters(p);
/* 166 */     s.add(new BoolExpr[] { assumptions });
/* 167 */     Status q = s.check();
/*     */     
/* 169 */     switch (q) {
/*     */       case UNKNOWN:
/* 171 */         System.out.println("Unknown because: " + s.getReasonUnknown());
/*     */         break;
/*     */       case SATISFIABLE:
/* 174 */         System.out.println("SATISFIABLE ");
/*     */         break;
/*     */       case UNSATISFIABLE:
/* 177 */         System.out.println("UNSATISFIABLE ");
/*     */         break;
/*     */     } 
/* 180 */     System.out.println("model: " + s.getModel());
/*     */   }
/*     */   
/*     */   void prove(Context ctx, List<BoolExpr> assumptions) throws Exception {
/* 184 */     Solver s = ctx.mkSolver();
/* 185 */     Params p = ctx.mkParams();
/* 186 */     s.setParameters(p);
/* 187 */     for (BoolExpr a : assumptions) {
/* 188 */       s.add(new BoolExpr[] { a });
/* 189 */       System.out.println("a: " + a);
/*     */     } 
/* 191 */     Status q = s.check();
/*     */     
/* 193 */     switch (q) {
/*     */       case UNKNOWN:
/* 195 */         System.out.println("Unknown because: " + s.getReasonUnknown());
/*     */         break;
/*     */       case SATISFIABLE:
/* 198 */         System.out.println("SATISFIABLE ");
/* 199 */         System.out.println(s.getModel());
/*     */         break;
/*     */       case UNSATISFIABLE:
/* 202 */         System.out.println("UNSATISFIABLE ");
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void disprove(Context ctx, BoolExpr f, boolean useMBQI) throws Exception {
/* 209 */     BoolExpr[] a = new BoolExpr[0];
/* 210 */     disprove(ctx, f, useMBQI, a);
/*     */   }
/*     */ 
/*     */   
/*     */   void disprove(Context ctx, BoolExpr f, boolean useMBQI, BoolExpr... assumptions) throws Exception {
/* 215 */     System.out.println("Disproving: " + f);
/* 216 */     Solver s = ctx.mkSolver();
/* 217 */     Params p = ctx.mkParams();
/* 218 */     p.add("mbqi", useMBQI);
/* 219 */     s.setParameters(p);
/* 220 */     for (BoolExpr a : assumptions) {
/* 221 */       s.add(new BoolExpr[] { a });
/* 222 */     }  s.add(new BoolExpr[] { ctx.mkNot(f) });
/* 223 */     Status q = s.check();
/*     */     
/* 225 */     switch (q) {
/*     */       case UNKNOWN:
/* 227 */         System.out.println("Unknown because: " + s.getReasonUnknown());
/*     */         break;
/*     */       case SATISFIABLE:
/* 230 */         System.out.println("OK, model: " + s.getModel());
/*     */         break;
/*     */       case UNSATISFIABLE:
/* 233 */         throw new Exception();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/z3solver/Z3Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */