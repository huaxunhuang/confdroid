package falseattranalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompareAttrs {

    public static void main(String[] args) throws Exception{
        int apiLevel = 25;
        File file1 = new File("styleable"+apiLevel+".txt");
        File file2 = new File("styleable"+(apiLevel+1)+".txt");
        FileInputStream fis1 = new FileInputStream(file1);
        FileInputStream fis2 = new FileInputStream(file2);
        Scanner scan1 = new Scanner(fis1);
        Scanner scan2 = new Scanner(fis2);
        List<String> array1 = new ArrayList<>();
        List<String> array2 = new ArrayList<>();
        while(scan1.hasNextLine()){
            String nextLine = scan1.nextLine();
            if(nextLine.trim().startsWith("public static final int")){
                if(nextLine.contains("_")) {
//                    System.out.println("nextLine = " + nextLine.trim().split(" ")[4]);
                    array1.add(nextLine.trim().split(" ")[4]);
                }
            }
        }
        while(scan2.hasNextLine()){
            String nextLine = scan2.nextLine();
            if(nextLine.trim().startsWith("public static final int")){
                if(nextLine.contains("_")) {
//                    System.out.println("nextLine = " + nextLine.trim().split(" ")[4]);
                    array2.add(nextLine.trim().split(" ")[4]);
                }
            }
        }
        
        List<String> arr = new ArrayList<>(array2);
        arr.removeAll(array1);
        System.out.println("arr.size() = " + arr.size());
        for(String a : arr){
            System.out.println(a);
        }
    }
}
