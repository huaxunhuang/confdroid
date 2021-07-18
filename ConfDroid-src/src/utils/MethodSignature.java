/*     */ package utils;
/*     */ 
/*     */ public class MethodSignature
/*     */ {
/*   5 */   private String signature = "";
/*     */   
/*     */   private String pkg;
/*     */   
/*     */   private String cls;
/*     */   
/*     */   private String methodName;
/*     */   
/*     */   private String returnType;
/*     */   private int parameterNumber;
/*     */   private String[] parameterTypes;
/*     */   private boolean containsGenericType = false;
/*     */   
/*     */   public MethodSignature(String signature) {
/*  19 */     this.signature = signature;
/*  20 */     parse(signature);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateSignature(String signature) {
/*  25 */     String regex = "<\\w: *\\w *<*\\w>*(.*)>";
/*  26 */     return signature.matches(regex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(String signature) {
/*  31 */     int posColon = signature.indexOf(':');
/*  32 */     this.cls = signature.substring(1, posColon);
/*     */ 
/*     */     
/*  35 */     ClassSignature cs = new ClassSignature(this.cls);
/*  36 */     this.pkg = cs.getPackageName();
/*     */     
/*  38 */     int posStartBracket = signature.indexOf('(');
/*  39 */     int posSpaceBeforeMethodName = signature.lastIndexOf(' ', posStartBracket);
/*     */     
/*  41 */     this.returnType = signature.substring(posColon + 2, posSpaceBeforeMethodName);
/*  42 */     this.methodName = signature.substring(posSpaceBeforeMethodName + 1, posStartBracket);
/*     */     
/*  44 */     int posEndBracket = signature.lastIndexOf(')');
/*  45 */     String parameters = signature.substring(posStartBracket + 1, posEndBracket);
/*     */     
/*  47 */     if (parameters.isEmpty()) {
/*     */       
/*  49 */       this.parameterNumber = 0;
/*  50 */       this.parameterTypes = null;
/*     */ 
/*     */     
/*     */     }
/*  54 */     else if (parameters.contains(",")) {
/*     */       
/*  56 */       String[] params = parameters.split(",");
/*  57 */       this.parameterNumber = params.length;
/*  58 */       this.parameterTypes = new String[this.parameterNumber];
/*     */       
/*  60 */       for (int i = 0; i < this.parameterNumber; i++)
/*     */       {
/*  62 */         this.parameterTypes[i] = params[i].trim();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  67 */       this.parameterNumber = 1;
/*  68 */       this.parameterTypes = new String[this.parameterNumber];
/*  69 */       this.parameterTypes[0] = parameters.trim();
/*     */     } 
/*     */   }
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
/*     */   public String getSignatureWithoutGPItems() {
/*  83 */     this.returnType = this.returnType.replaceAll("<.+>", "");
/*  84 */     if (this.returnType.length() == 1)
/*     */     {
/*  86 */       this.containsGenericType = true;
/*     */     }
/*     */     
/*  89 */     String paramStr = "";
/*  90 */     if (null != this.parameterTypes)
/*     */     {
/*  92 */       for (int i = 0; i < this.parameterTypes.length; i++) {
/*     */         
/*  94 */         if (this.parameterTypes[i].length() == 1)
/*     */         {
/*  96 */           this.containsGenericType = true;
/*     */         }
/*     */         
/*  99 */         if (0 == i) {
/* 100 */           paramStr = paramStr + this.parameterTypes[i];
/*     */         } else {
/* 102 */           paramStr = paramStr + "," + this.parameterTypes[i];
/*     */         } 
/*     */       } 
/*     */     }
/* 106 */     paramStr = paramStr.replaceAll("<.+>", "");
/*     */     
/* 108 */     String sig = "<" + this.cls + ": " + this.returnType + " " + this.methodName + "(" + paramStr + ")>";
/*     */     
/* 110 */     if (this.containsGenericType)
/*     */     {
/* 112 */       System.out.println(sig);
/*     */     }
/*     */     
/* 115 */     return sig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCompactSignature() {
/* 122 */     StringBuilder sb = new StringBuilder();
/* 123 */     sb.append(this.cls);
/* 124 */     sb.append("." + this.methodName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCompactSignatureWithParams() {
/* 136 */     StringBuilder sb = new StringBuilder();
/* 137 */     sb.append(this.cls);
/* 138 */     sb.append("." + this.methodName);
/*     */     
/* 140 */     for (String param : this.parameterTypes)
/*     */     {
/* 142 */       sb.append("." + param);
/*     */     }
/*     */     
/* 145 */     System.out.println(sb.toString());
/*     */     
/* 147 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsVarargs() {
/* 152 */     return this.signature.contains("...");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsGenericReturnType() {
/* 157 */     boolean containsGenericReturnType = false;
/*     */     
/* 159 */     this.returnType = this.returnType.replaceAll("<.+>", "").replaceAll("\\.\\.\\.", "");
/* 160 */     if (this.returnType.length() == 1)
/*     */     {
/* 162 */       containsGenericReturnType = true;
/*     */     }
/*     */     
/* 165 */     return containsGenericReturnType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsGenericType() {
/* 170 */     boolean containsGenericType = false;
/*     */     
/* 172 */     this.returnType = this.returnType.replaceAll("<.+>", "").replaceAll("\\.\\.\\.", "");
/* 173 */     if (this.returnType.length() == 1)
/*     */     {
/* 175 */       containsGenericType = true;
/*     */     }
/*     */     
/* 178 */     if (null != this.parameterTypes)
/*     */     {
/* 180 */       for (int i = 0; i < this.parameterTypes.length; i++) {
/*     */         
/* 182 */         this.parameterTypes[i] = this.parameterTypes[i].replaceAll("<.+>", "").replaceAll("\\.\\.\\.", "");
/*     */         
/* 184 */         if (this.parameterTypes[i].length() == 1)
/*     */         {
/* 186 */           containsGenericType = true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 191 */     return containsGenericType;
/*     */   }
/*     */   
/*     */   public String getSignature() {
/* 195 */     return this.signature;
/*     */   }
/*     */   
/*     */   public String getPkg() {
/* 199 */     return this.pkg;
/*     */   }
/*     */   
/*     */   public String getCls() {
/* 203 */     return this.cls;
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/* 207 */     return this.methodName;
/*     */   }
/*     */   
/*     */   public String getReturnType() {
/* 211 */     return this.returnType;
/*     */   }
/*     */   
/*     */   public int getParameterNumber() {
/* 215 */     return this.parameterNumber;
/*     */   }
/*     */   
/*     */   public String[] getParameterTypes() {
/* 219 */     return this.parameterTypes;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/MethodSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */