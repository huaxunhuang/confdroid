/*    */ package utils;
/*    */ 
/*    */ public class ClassSignature
/*    */ {
/*    */   public static final String DEFAULT = "DEFAULT";
/*  6 */   private String signature = "";
/*    */ 
/*    */   
/*    */   public ClassSignature(String signature) {
/* 10 */     this.signature = signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInnerClass() {
/* 15 */     return this.signature.contains("$");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPackageName() {
/* 20 */     String pkg = this.signature;
/*    */     
/* 22 */     if (pkg.contains("$"))
/*    */     {
/* 24 */       pkg = pkg.substring(0, pkg.indexOf('$'));
/*    */     }
/*    */     
/* 27 */     if (!pkg.contains(".")) {
/*    */       
/* 29 */       pkg = "DEFAULT";
/*    */     }
/*    */     else {
/*    */       
/* 33 */       int pos = pkg.lastIndexOf('.');
/* 34 */       pkg = pkg.substring(0, pos);
/*    */     } 
/*    */     
/* 37 */     return pkg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPackageName(int level) {
/* 48 */     return getPackageName(getPackageName(), level);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getPackageName(String pkgName, int level) {
/* 53 */     String[] segments = pkgName.split("\\.");
/*    */     
/* 55 */     if (segments.length <= level)
/*    */     {
/* 57 */       return pkgName;
/*    */     }
/*    */     
/* 60 */     String pkg = segments[0];
/* 61 */     for (int i = 1; i < level; i++)
/*    */     {
/* 63 */       pkg = pkg + "." + segments[i];
/*    */     }
/*    */     
/* 66 */     return pkg;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/ClassSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */