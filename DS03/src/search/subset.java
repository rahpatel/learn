package search;

import java.util.*;

public class subset {
    // have to check when sub or super is null

    /**
     * Creates an array of random numbers in the range 1 to <code>limit</code>.
     * No number will occur more than once in this array, but all calls with the
     * same value of <code>seed</code> will return the same sequence of numbers,
     * regardless of the size of the array.
     * 
     * @param size
     *            The size of the array to create.
     * @param limit
     *            All returned numbers will be less than or equal to this value.
     * @param seed
     *            A seed for the random number generator.
     * @return An array of unique random numbers.
     */

    public static int[] randomArray(int size, int limit, int seed) {
        assert size <= limit;
        java.util.Random rand = new java.util.Random(seed);
        int[] numbers = new int[size];
        outerLoop: for (int i = 0; i < numbers.length; i++) {
        int candidate = rand.nextInt(limit) + 1;
        for (int j = 0; j < i; j++) {
        if (numbers[j] == candidate) {
        i--;
        continue outerLoop;
        }
        }
        numbers[i] = candidate;
        }
        return numbers;
    }

    /**
     * Randomizes an array in place.
     * 
     * @param array
     *            The array to be randomized (shuffled).
     */

    public static void randomize(int[] array) {
        java.util.Random rand = new java.util.Random();
        for (int i = array.length; i > 1; i--) {
        int choice = rand.nextInt(i);
        int temp = array[choice];
        array[choice] = array[i - 1];
        array[i - 1] = temp;
        }
    }

    /**
     * Tests whether the <code>target</code> is one of the elements of the
     * <code>array</array>
     * using Linear Search Algorithm.
     * 
     * @param target
     *            The value to be searched for in the array.
     * @param array
     *            The elements in which the<code>target</code> is to be searched
     *            for.
     * @return Returns true if the<code>target</code> is one of the elements of
     *         the<code>array</code> else returns false.
     */

    public boolean linearSearch(int target, int[] array) {
        for (int i = 0; i < array.length; i++) {
        if (target == array[i])
            return true;
        }
        return false;
    }

    /**
     * Tests whether the <code>target</code> is one of the elements of the array
     * <code>array</array>
     * using Binary Search Algorithm.
     * 
     * @param target
     *            The value to be searched for in the array.
     * @param array
     *            The elements in which the<code>target</code> is to be searched
     *            for.
     * @param left
     *            The left bound for the Binary search.
     * @param right
     *            The right bound for the Binary search.
     * @return Returns the position of the the target in the array if it is
     *         present.Else returns -1.
     */

    public int binarySearch(int target, int[] array, int left, int right) {

        if (left > right)
            return -1;

        int m = (left + right) / 2;
        if (target == array[m])
            return m;

        if (target > array[m])
            return binarySearch(target, array, m + 1, right);
        return binarySearch(target, array, left, m - 1);
    }

    /**
     * Tests whether array<code>sub</code> is a subset of array
     * <code>super</code>. Both sub and super are unsorted arrays.
     * 
     * @param sub
     *            The array to be tested for subset condition.
     * @param superset
     *            The larger array.
     * @return Returns either true or false depending whether the
     *         <code>sub</code> array is a subset of the<code>superset</code>
     *         array.
     */

    public boolean subset1(int[] sub, int[] superset) {
        for (int x : sub) {
        if (!linearSearch(x, superset))
            return false;
        }
        return true;
    }

    /**
     * Tests whether array<code>sub</code> is a subset of array
     * <code>super</code>.Array sub is unsorted, super is sorted.
     * 
     * @param sub
     *            The array to be tested for subset condition.
     * @param superset
     *            The larger array.
     * @return Returns either true or false depending whether the
     *         <code>sub</code> array is a subset of the<code>superset</code>
     *         array.
     */

    public boolean subset2(int[] sub, int[] superset) {

        for (int x : sub) {
        if (binarySearch(x, superset, 0, superset.length - 1) == -1)
            return false;
        }
        return true;
    }

    /**
     * Tests whether array<code>sub</code> is a subset of array
     * <code>super</code>. Both sub and super are sorted arrays.
     * 
     * @param sub
     *            The array to be tested for subset condition.
     * @param superset
     *            The larger array.
     * @return Returns either true or false depending whether the
     *         <code>sub</code> array is a subset of the<code>superset</code>
     *         array.
     */

    public boolean subset3(int[] sub, int[] superset) {
        int startleft = 0;
        int index;
        for (int x : sub) {

        index = binarySearch(x, superset, startleft, superset.length - 1);
        if (index == -1)
            return false;
        startleft = index + 1;
        }
        return true;
    }

    /**
     * Divide a given number, <code>sum</code>, into <code>numbers</code>
     * addends, such that the addends sum to the given number, and the addends
     * can be partitioned into <code>piles</code> equal piles.
     * 
     * @param sum
     *            The number to be broken into addends.
     * @param numbers
     *            The desired number of addends.
     * @param piles
     *            The desired number of piles.
     * @return An array of numbers to be sorted into piles.
     */
    public static int[] createProblem(int sum, int numbers, int piles) {
        check(sum >= numbers, "You can't have more numbers than their sum.");
        check(numbers >= piles,
                "You must have at least as many numbers as piles");
        check(sum % piles == 0,
                "The sum must be divisible by the number of piles");
        int[] result = new int[numbers];
        // Determine how many numbers to put into each pile (at least 1 each)
        int[] pileCounts;

        result = partitionNumber(sum, numbers, piles);
        randomize(result);
        return result;
    }

    /**
     * Throws an <code>IllegalArgumentException</code> if its boolean input
     * parameter is <code>false</code>.
     * 
     * @param b
     *            The parameter to test.
     * @param string
     *            The message to include in the
     *            <code>IllegalArgumentException</code>.
     */
    private static void check(boolean b, String string) {
        if (b)
            return;
        throw new IllegalArgumentException(string);
    }

    /**
     * Separates a positive number into an array of positive numbers (addends)
     * which, when added, will yield the original number.
     * 
     * @param total
     *            The number to which the addends should sum.
     * @param numberOfNumbers
     *            How many addends are desired.
     * @param piles
     *            The number of equal piles that should be created.
     * @return The array of numbers that sum to <code>sum</code>.
     */
    static int[] partitionNumber(int total, int numberOfNumbers, int piles) {
        int[] result = new int[numberOfNumbers];
        int[] cuts = new int[numberOfNumbers];

        // Insert initial cuts to make equal piles
        for (int i = 0; i < piles; i++) {
        cuts[i] = (i + 1) * (total / piles);
        }
        // Insert remaining cuts
        java.util.Random rand = new java.util.Random();

        for (int i = piles; i < numberOfNumbers; i++) {
        // Choose each cut to be 0 < cut < sum
        cuts[i] = rand.nextInt(total - 1) + 1;
        // Ensure that the new cut is not the same as an existing cut
        for (int j = 0; j < i; j++) {
        if (cuts[j] == cuts[i])
            i--;
        }
        }
        // Transform cuts into ranges
        Arrays.sort(cuts);
        result[0] = cuts[0];
        for (int i = 1; i < numberOfNumbers; i++) {
        result[i] = cuts[i] - cuts[i - 1];
        }
        return result;
    }

    /**
     * Finds and returns a subset of numbers such that the sum of the numbers in
     * the subset is exactly equal to target.
     * 
     * @param numbers
     *            Set of numbers.
     * @param target
     *            A no. to which the numbers in subset sum to.
     * @return An array of subset of numbers such that the sum of the numbers in
     *         the subset is exactly equal to<code>target</code>.
     * 
     */

    public int[] findSubset(int[] numbers, int target) {

        int[] subset1 = new int[numbers.length];
        int count = 0;
        int size = (int) Math.pow(2, numbers.length);
        String binaryvalue;
        for (int i = 1; i < size; i++) {
        binaryvalue = Integer.toBinaryString(i);
        while (binaryvalue.length() < numbers.length)
            binaryvalue = "0" + binaryvalue;
        int sum = 0;
        count = 0;
        for (int j = 0; j < binaryvalue.length(); j++) {
        if (binaryvalue.charAt(j) == '1') {
        subset1[count] = numbers[j];
        count++;
        sum = sum + numbers[j];
        }
        }
        if (sum == target)
            break;
        }

        int[] subset = new int[count];
        for (int i = 0; i < subset.length; i++)
            subset[i] = subset1[i];
        return subset;
    }

    public static int[][] createArrays(int size, int maxvalue, int seed) {
        int[][] problems = new int[25][];
        for (int i = 0; i < 25; i++) {

        problems[i] = randomArray(size, maxvalue, seed);
        randomize(problems[i]);

        seed = seed + 100;
        }
        return problems;
    }

    /**
     * Testing Method for Analyzing subset1,subset2,subset3 methods.
     * 
     * @param args
     */

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        subset obj = new subset();
        int[] subsetsum;
        int[] supersetsum;
        boolean issubset;
        int[][] subset_problems;
        int[][] superset_problems;
        int subsetsize = 20;
        int supersetsize = 40;
        int maxvalue = 100;
        int seed = 1;
        int count = 0;

        System.out
                .println("* * * * * * * * * * * * * * * * * * Analyzing   subset1   Method * * * * * * * * * * * * * * * * * * * *");
        System.out
                .println("Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times each run    Time Taken     ");

        while (count < 5) {
        subset_problems = createArrays(subsetsize, maxvalue, seed);
        superset_problems = createArrays(supersetsize, maxvalue, seed);

        // Calling the method for the first time.
        issubset = obj.subset1(subset_problems[0], superset_problems[0]);
        System.gc();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
        for (int j = 0; j < 1000; j++) {
        issubset = obj.subset1(subset_problems[i], superset_problems[i]);
        }
        }
        t1 = System.currentTimeMillis() - t1;
        // if(issubset) {System.out.println("Is a subset");}
        // else {System.out.println("Is not a subset");}
        System.out.println(subsetsize + "                            "
                + supersetsize + "                           25"
                + "                       1000" + "                  " + t1);

        subsetsize = subsetsize * 2;
        supersetsize = supersetsize * 2;
        maxvalue = maxvalue * 5;
        seed = seed + 10;
        count++;

        }

        subsetsize = 20;
        supersetsize = 40;
        maxvalue = 100;
        seed = 1;
        count = 0;

        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");
        System.out
                .println("* * * * * * * * * * * * * * * * * * Analyzing   subset2   Method * * * * * * * * * * * * * * * * * * * *");
        System.out
                .println("Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times each run    Time Taken     ");

        while (count < 5) {
        subset_problems = createArrays(subsetsize, maxvalue, seed);
        superset_problems = createArrays(supersetsize, maxvalue, seed);

        // Sorting the superset Arrays

        for (int i = 0; i < 25; i++) {
        Arrays.sort(superset_problems[i]);
        }
        // Calling the method for the first time.
        System.gc();
        issubset = obj.subset2(subset_problems[0], superset_problems[0]);
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
        for (int j = 0; j < 1000; j++) {
        issubset = obj.subset2(subset_problems[i], superset_problems[i]);
        }
        }
        t2 = System.currentTimeMillis() - t2;
        // if(issubset) {System.out.println("Is a subset");}
        // else {System.out.println("Is not a subset");}
        System.out.println(subsetsize + "                            "
                + supersetsize + "                           25"
                + "                       1000" + "                  " + t2);

        subsetsize = subsetsize * 5;
        supersetsize = supersetsize * 5;
        maxvalue = maxvalue * 10;
        seed = seed + 10;
        count++;

        }
        subsetsize = 20;
        supersetsize = 40;
        maxvalue = 100;
        seed = 1;
        count = 0;

        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");

        System.out
                .println("* * * * * * * * * * * * * * * * * * Analyzing   subset3   Method * * * * * * * * * * * * * * * * * * * *");
        System.out
                .println("Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times each run    Time Taken     ");

        while (count < 5) {
        subset_problems = createArrays(subsetsize, maxvalue, seed);
        superset_problems = createArrays(supersetsize, maxvalue, seed);

        // Sorting the subset and superset Arrays for input to subset3 method.

        for (int i = 0; i < 25; i++) {
        Arrays.sort(subset_problems[i]);
        Arrays.sort(superset_problems[i]);

        }
        // Calling the method for the first time.
        System.gc();
        issubset = obj.subset3(subset_problems[0], superset_problems[0]);
        long t3 = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
        for (int j = 0; j < 1000; j++) {
        issubset = obj.subset3(subset_problems[i], superset_problems[i]);
        }
        }
        t3 = System.currentTimeMillis() - t3;
        // if(issubset) {System.out.println("Is a subset");}
        // else {System.out.println("Is not a subset");}
        System.out.println(subsetsize + "                            "
                + supersetsize + "                           25"
                + "                       1000" + "                  " + t3);

        subsetsize = subsetsize * 5;
        supersetsize = supersetsize * 5;
        maxvalue = maxvalue * 10;
        seed = seed + 10;
        count++;

        }

        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");
        System.out.println("   ");

        System.out
                .println("* * * * * * * * * * * * * * * * * *Analyzing   findSubset   Method * * * * * * * * * * * * * * * * * * * *");
        System.out
                .println("Array Size             No.of times ran        Time Taken");

        int size = 10;
        count = 0;
        while (count < 5) {

        supersetsum = createProblem(2000, size, 2);
        subsetsum = obj.findSubset(supersetsum, 1000);
        System.gc();
        long t4 = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
        subsetsum = obj.findSubset(supersetsum, 1000);
        }
        t4 = System.currentTimeMillis() - t4;
        System.out.println(size
                + "                      10                       " + +t4);

        // System.out.println("Subset for the sum :  ");
        // for(int x:subsetsum)System.out.print(x+"  ");
        size = size + 5;
        count++;

        }
    }
}
