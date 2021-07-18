/*     */ package utils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ public class CommonUtils
/*     */ {
/*     */   public static boolean isStringEmpty(String str) {
/*  21 */     boolean isEmpty = false;
/*     */     
/*  23 */     if (null == str || str.isEmpty())
/*     */     {
/*  25 */       isEmpty = true;
/*     */     }
/*     */     
/*  28 */     return isEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFileName(String path) {
/*  34 */     String fileName = path;
/*  35 */     if (fileName.contains("/"))
/*     */     {
/*  37 */       fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
/*     */     }
/*     */     
/*  40 */     return fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeResultToFile(String path, String content) {
/*     */     try {
/*  46 */       PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
/*  47 */       out.print(content);
/*  48 */       out.close();
/*  49 */     } catch (IOException e) {
/*  50 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int totalValue(Map<String, Integer> map) {
/*  56 */     int total = 0;
/*     */     
/*  58 */     for (Map.Entry<String, Integer> entry : map.entrySet())
/*     */     {
/*  60 */       total += ((Integer)entry.getValue()).intValue();
/*     */     }
/*     */     
/*  63 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<String> loadFile(String filePath) {
/*  68 */     Set<String> lines = new HashSet<>();
/*     */     
/*     */     try {
/*  71 */       BufferedReader br = new BufferedReader(new FileReader(filePath));
/*  72 */       String line = "";
/*  73 */       while ((line = br.readLine()) != null)
/*     */       {
/*  75 */         lines.add(line);
/*     */       }
/*     */       
/*  78 */       br.close();
/*     */     }
/*  80 */     catch (Exception ex) {
/*     */       
/*  82 */       ex.printStackTrace();
/*     */     } 
/*     */     
/*  85 */     return lines;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> loadFileToList(String filePath) {
/*  90 */     List<String> lines = new ArrayList<>();
/*     */     
/*     */     try {
/*  93 */       BufferedReader br = new BufferedReader(new FileReader(filePath));
/*  94 */       String line = "";
/*  95 */       while ((line = br.readLine()) != null)
/*     */       {
/*  97 */         lines.add(line);
/*     */       }
/*     */       
/* 100 */       br.close();
/*     */     }
/* 102 */     catch (Exception ex) {
/*     */       
/* 104 */       ex.printStackTrace();
/*     */     } 
/*     */     
/* 107 */     return lines;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> loadFileToList(String filePath, String prefix) {
/* 112 */     if ("NULL".equals(prefix))
/*     */     {
/* 114 */       return loadFileToList(filePath);
/*     */     }
/*     */     
/* 117 */     List<String> lines = new ArrayList<>();
/*     */     
/*     */     try {
/* 120 */       BufferedReader br = new BufferedReader(new FileReader(filePath));
/* 121 */       String line = "";
/* 122 */       while ((line = br.readLine()) != null) {
/*     */         
/* 124 */         if (null != prefix && line.startsWith(prefix))
/*     */         {
/* 126 */           lines.add(line.replace(prefix, ""));
/*     */         }
/*     */       } 
/*     */       
/* 130 */       br.close();
/*     */     }
/* 132 */     catch (Exception ex) {
/*     */       
/* 134 */       ex.printStackTrace();
/*     */     } 
/*     */     
/* 137 */     return lines;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<String> cloneSet(Set<String> src) {
/* 142 */     Set<String> dest = new HashSet<>();
/*     */     
/* 144 */     for (String str : src)
/*     */     {
/* 146 */       dest.add(str);
/*     */     }
/*     */     
/* 149 */     return dest;
/*     */   }
/*     */ 
/*     */   
/*     */   public static TreeMap<String, Integer> sort(Map<String, Integer> map) {
/* 154 */     ValueComparator bvc = new ValueComparator(map);
/* 155 */     TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);
/* 156 */     sorted_map.putAll(map);
/*     */     
/* 158 */     return sorted_map;
/*     */   }
/*     */   
/*     */   static class ValueComparator implements Comparator<String> {
/*     */     Map<String, Integer> base;
/*     */     
/*     */     public ValueComparator(Map<String, Integer> base) {
/* 165 */       this.base = base;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(String a, String b) {
/* 170 */       if (((Integer)this.base.get(a)).intValue() >= ((Integer)this.base.get(b)).intValue()) {
/* 171 */         return -1;
/*     */       }
/* 173 */       return 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double computeSimilarity(int identical, int similar, int _new, int deleted) {
/* 180 */     int total1 = identical + similar + deleted;
/* 181 */     int total2 = identical + similar + _new;
/*     */     
/* 183 */     int total = (total1 < total2) ? total1 : total2;
/*     */     
/* 185 */     double rate = identical / total;
/*     */     
/* 187 */     return rate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void put(Map<String, Set<T>> map1, String key, T value) {
/* 194 */     if (map1.containsKey(key)) {
/*     */       
/* 196 */       Set<T> values = map1.get(key);
/* 197 */       values.add(value);
/* 198 */       map1.put(key, values);
/*     */     }
/*     */     else {
/*     */       
/* 202 */       Set<T> values = new HashSet<>();
/* 203 */       values.add(value);
/* 204 */       map1.put(key, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void put(Map<String, Set<String>> dest, Map<String, Set<String>> src) {
/* 210 */     for (Map.Entry<String, Set<String>> entry : src.entrySet()) {
/*     */       
/* 212 */       String cls = entry.getKey();
/* 213 */       Set<String> set2 = entry.getValue();
/*     */       
/* 215 */       if (dest.containsKey(cls)) {
/*     */         
/* 217 */         Set<String> set = dest.get(cls);
/* 218 */         set.addAll(set2);
/* 219 */         dest.put(cls, set);
/*     */         
/*     */         continue;
/*     */       } 
/* 223 */       Set<String> set1 = new HashSet<>();
/* 224 */       set1.addAll(set2);
/* 225 */       dest.put(cls, set1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/CommonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */