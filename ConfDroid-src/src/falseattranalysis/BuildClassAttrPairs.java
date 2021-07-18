package falseattranalysis;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javatuples.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildClassAttrPairs {
    public static List<Pair<String, String>> pairs = new ArrayList<>();
    public static void init() throws Exception {

        File excelFile = new File("/Users/name1/IdeaProjects/First/attribute-pairs.xls");
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
        HSSFSheet sheet = wb.getSheetAt(0);

        for (Row row : sheet) {
            Pair<String, String> pair = new Pair<>(row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue());
            pairs.add(pair);
        }
    }

    public static String checkPairs(String tag){
        String ret = "";
        for(Pair<String, String> pair : pairs){
            String anothertag = "<"+tag+">";
            if(pair.getValue0().equals(tag) || pair.getValue0().equals(anothertag)) {
                ret = ret + "android:" + pair.getValue1() + " ";
            }
        }
        return ret;
    }
}
