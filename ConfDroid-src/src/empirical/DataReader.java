/*    */ package empirical;
/*    */ 
/*    */ import java.io.File;
import java.io.FileNotFoundException;
/*    */ import java.util.*;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class DataReader {
/*    */   private static class ValueComparator implements Comparator<Map.Entry<String, Integer>> { public int compare(Map.Entry<String, Integer> m, Map.Entry<String, Integer> n) {
/* 15 */       return ((Integer)n.getValue()).intValue() - ((Integer)m.getValue()).intValue();
/*    */     }
/*    */     private ValueComparator() {} }
/*    */   
/*    */   @Test
/*    */   public void testMain() {
/* 21 */     Map<String, Integer> dataMap = new HashMap<>();
/* 22 */     int sum = 0;
/* 23 */     String pathname = "/Users/name1/Documents/GitHub/mine-config/ConfDroid/data.txt";
/*    */     try {
/* 25 */       Scanner scanner = new Scanner(new File(pathname));
/* 26 */       while (scanner.hasNextLine()) {
/* 27 */         String str = scanner.nextLine();
/* 28 */         String freqStr = scanner.nextLine();
/*    */         
/* 30 */         int freq = Integer.parseInt(freqStr.split(" ")[1]);
/* 31 */         if (!dataMap.containsKey(str)) {
/* 32 */           dataMap.put(str, Integer.valueOf(0));
/*    */         }
/* 34 */         sum += freq;
/* 35 */         int prevFreq = ((Integer)dataMap.get(str)).intValue();
/* 36 */         dataMap.put(str, Integer.valueOf(prevFreq + freq));
/*    */       } 
/*    */ 
/*    */       
/* 40 */       List<Map.Entry<String, Integer>> list = new ArrayList<>();
/* 41 */       list.addAll(dataMap.entrySet());
/* 42 */       ValueComparator vc = new ValueComparator();
/* 43 */       Collections.sort(list, vc);
/* 44 */       for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); )
/*    */       {
/* 46 */         Map.Entry<String, Integer> next = it.next();
/* 47 */         System.out.println(next + " " + (((Integer)next.getValue()).intValue() / sum * 100.0F) + "%");
/*    */       }
/*    */     
/* 50 */     } catch (FileNotFoundException e) {
/* 51 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/empirical/DataReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */