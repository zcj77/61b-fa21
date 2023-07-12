import java.util.ArrayList;
/** Multidimensional array
 *  @author Jacky Zhao*/

public class MultiArr {
    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        System.out.println("Rows: "+ arr.length);
        System.out.println("Columns: "+ arr[0].length);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int biggest = 0;
        for (int r = 0; r<arr.length; r++ ){
            for (int c = 0; c < arr[r].length; c++) {
                if(arr[r][c]>biggest){
                    biggest=arr[r][c];
                }
            }
        }
        return biggest;
    }

    /**Return an array where each element is the sum of the corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int[] lst = new int[arr.length];
        for (int r = 0; r<arr.length; r++ ){
            int sum = 0;
            for (int c = 0; c < arr[r].length; c++) {
                sum += arr[r][c];
            }
            lst[r] = sum;
        }
        System.out.println(lst);
        return lst;
    }
}