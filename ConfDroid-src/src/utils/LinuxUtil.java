/*    */ package utils;
/*    */ import java.io.*;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ import java.util.Enumeration;
/*    */ import java.util.List;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ 
/*    */ public class LinuxUtil {
/*    */   public static void decompress(String zipPath, String targetPath) throws IOException, FileNotFoundException {
/* 18 */     File file = new File(zipPath);
/* 19 */     if (!file.isFile()) {
/* 20 */       throw new FileNotFoundException("file not exist!");
/*    */     }
/* 22 */     if (targetPath == null || "".equals(targetPath)) {
/* 23 */       targetPath = file.getParent();
/*    */     }
/* 25 */     System.out.println(zipPath);
/* 26 */     ZipFile zipFile = new ZipFile(file);
/* 27 */     Enumeration<? extends ZipEntry> files = zipFile.entries();
/* 28 */     ZipEntry entry = null;
/* 29 */     File outFile = null;
/* 30 */     BufferedInputStream bin = null;
/* 31 */     BufferedOutputStream bout = null;
/* 32 */     while (files.hasMoreElements()) {
/* 33 */       entry = files.nextElement();
/* 34 */       outFile = new File(targetPath + File.separator + entry.getName());
/* 35 */       if (entry.isDirectory()) {
/* 36 */         outFile.mkdirs();
/*    */         continue;
/*    */       } 
/* 39 */       if (!outFile.getParentFile().exists()) {
/* 40 */         outFile.getParentFile().mkdirs();
/*    */       }
/* 42 */       outFile.createNewFile();
/* 43 */       if (!outFile.canWrite()) {
/*    */         continue;
/*    */       }
/*    */       try {
/* 47 */         bin = new BufferedInputStream(zipFile.getInputStream(entry));
/* 48 */         bout = new BufferedOutputStream(new FileOutputStream(outFile));
/* 49 */         byte[] buffer = new byte[1024];
/* 50 */         int readCount = -1;
/* 51 */         while ((readCount = bin.read(buffer)) != -1) {
/* 52 */           bout.write(buffer, 0, readCount);
/*    */         }
/*    */       } finally {
/*    */         try {
/* 56 */           bin.close();
/* 57 */           bout.flush();
/* 58 */           bout.close();
/* 59 */         } catch (Exception exception) {}
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void exec(List<String> commands) throws Exception {
/* 70 */     File wd = new File("/");
/* 71 */     Process process = null;
/* 72 */     process = Runtime.getRuntime().exec("/bin/bash", (String[])null, wd);
/* 73 */     if (process != null) {
/*    */       
/* 75 */       BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
/*    */       
/* 77 */       PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
/*    */       
/* 79 */       for (String cmd : commands) {
/* 80 */         out.println(cmd);
/*    */       }
/* 82 */       out.println("exit");
/* 83 */       in.close();
/* 84 */       out.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/LinuxUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */