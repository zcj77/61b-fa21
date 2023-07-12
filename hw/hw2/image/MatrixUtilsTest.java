package image;

import org.junit.Test;
import static org.junit.Assert.*;

public class MatrixUtilsTest {
    @Test
    public void accumulateVerticalTests(){
        double[][] prob = {{1000000, 1000000, 1000000, 1000000},
                            {1000000, 75990, 30003, 1000000},
                            {1000000, 30002, 103046, 1000000},
                            {1000000, 29515 , 38273, 1000000},
                            {1000000, 73403,  35399, 1000000},
                            {1000000, 1000000, 1000000, 1000000}};
        double[][] expected = {{1000000, 1000000, 1000000, 1000000},
                                {2000000, 1075990, 1030003, 2000000},
                                {2075990, 1060005, 1133049, 2030003},
                                {2060005, 1089520, 1098278, 2133049},
                                {2089520, 1162923, 1124919, 2098278},
                                {2162923, 2124919, 2124919, 2124919}};
        MatrixUtils.accumulateVertical(prob);
        assertArrayEquals(expected, MatrixUtils.accumulateVertical(prob));
    }

    @Test
    public void accumulateTests(){
        double[][] prob = {{1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515 , 38273, 1000000},
                {1000000, 73403,  35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] expected = {{1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}};
        MatrixUtils.accumulate(prob, MatrixUtils.Orientation.VERTICAL);
        assertArrayEquals(expected, MatrixUtils.accumulateVertical(prob));

        double[][] prob2 = {{1000000, 1000000, 1000000, 1000000},
                            {1000000, 75990, 30003, 1000000},
                            {1000000, 30002, 103046, 1000000}};
        double[][] expected2 = {{1000000, 1000000, 1000000},
                                {2000000, 1075990, 1030002},
                                {2075990, 1060005, 1133048},
                                {2060005, 2060005, 2060005}};
        MatrixUtils.accumulate(prob2, MatrixUtils.Orientation.HORIZONTAL);
        assertArrayEquals(expected2, MatrixUtils.accumulate(prob2, MatrixUtils.Orientation.HORIZONTAL));

    }

    public void findSeamTests(){
        //
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
