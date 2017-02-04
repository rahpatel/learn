package search;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class subsetTest {
    subset obj;
    int[]  numbers;
    int[]  subset1;
    int[]  superset1;
    int[]  subset2;
    int[]  superset2;
    int[]  subset3;
    int[]  superset3;
    int[]  subset11;
    int[]  superset11;
    int[]  subset22;
    int[]  superset22;
    int[]  subset33;
    int[]  superset33;
    int[]  subsetsum;
    int[]  supersetsum;

    @Before
    public void setUp() throws Exception {
        obj = new subset();
        numbers = new int[5];
        for (int i = 0; i < 5; i++)
            numbers[i] = 10 * (i + 1);

        subset1 = subset.randomArray(10, 100, 10);
        superset1 = subset.randomArray(20, 100, 10);
        subset2 = subset.randomArray(20, 100, 20);
        superset2 = subset.randomArray(40, 100, 20);
        subset3 = subset.randomArray(40, 100, 30);
        superset3 = subset.randomArray(80, 100, 30);
        int[] subset111 = { 50, 35, 20, 10, 40 };
        int[] superset111 = { 70, 20, 10, 80, 90, 60, 30, 50, 100, 40 };
        int[] subset222 = { 50, 35, 20, 10, 40 };
        int[] superset222 = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
        int[] subset333 = { 10, 20, 35, 40, 50 };
        int[] superset333 = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };

        subset11 = subset111;
        superset11 = superset111;
        subset22 = subset222;
        superset22 = superset222;
        subset33 = subset333;
        superset33 = superset333;

        obj.randomize(subset1);
        obj.randomize(superset1);

        obj.randomize(subset2);
        obj.randomize(superset2);

        obj.randomize(subset3);
        obj.randomize(superset3);

        Arrays.sort(superset2);
        Arrays.sort(subset3);
        Arrays.sort(superset3);
        supersetsum = subset.createProblem(2000, 20, 2);

    }

    @Test
    public final void testLinearSearch() {
        assertTrue(obj.linearSearch(10, numbers));
        assertFalse(obj.linearSearch(25, numbers));

    }

    @Test
    public final void testBinarySearch() {
        assertTrue(obj.linearSearch(10, numbers));
        assertFalse(obj.linearSearch(1, numbers));
    }

    @Test
    public final void testSubset1() {

        assertTrue(obj.subset1(subset1, superset1));
        assertFalse(obj.subset1(subset11, superset11));
    }

    @Test
    public final void testSubset2() {
        assertTrue(obj.subset2(subset2, superset2));
        assertFalse(obj.subset2(subset22, superset22));

    }

    @Test
    public final void testSubset3() {
        assertTrue(obj.subset3(subset3, superset3));
        assertFalse(obj.subset3(subset33, superset33));

    }

    @Test
    public final void testFindSubset() {
        int sum = 0;
        subsetsum = obj.findSubset(supersetsum, 1000);
        for (int i = 0; i < subsetsum.length; i++)
            sum = sum + subsetsum[i];
        assertTrue(obj.subset1(subsetsum, supersetsum));
        assertEquals(1000, sum);

    }

}
