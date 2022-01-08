import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class SortTest {
    @org.junit.jupiter.api.Test
    void test() {
        int[] sizes = {10, 20, 100, 10_000, 100_000, 500_000, 1_000_000, 2_000_000, 5_000_000, 10_000_000};
        int test_number = 10;
        for (int size : sizes) {
            System.out.println("Array size = " + size + "; Tests = " + test_number);
            sort(size, test_number);
            System.out.println();
        }
    }

    private static void sort(int size, int test_number) {
        double total_time = 0;
        for(int i = 0; i < test_number; i++) {
            int[] array = getInitArray(size);
            int[] array_unsorted = Arrays.copyOf(array, array.length);

            long time = System.currentTimeMillis();
            new Sort(array);
            total_time += (double) (System.currentTimeMillis() - time) / 1000;

            //assertArrayEquals(array_unsorted, array);
            Arrays.sort(array_unsorted);
            assertArrayEquals(array_unsorted, array);
        }
        System.out.print(total_time / test_number);
        System.out.println(" sec");
    }

    public static int[] getInitArray(int capacity) {
        int[] array = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = (int) (Math.random() * 1000) + 1;
        }
        return array;
    }
}