import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Scanner;

class ParallelSortTest {
    @org.junit.jupiter.api.Test
    void test() throws Exception {
        //int[] sizes = {10_000, 100_000, 500_000, 1_000_000, 2_000_000, 5_000_000, 10_000_000};
        int[] sizes = {100000};
        int test_number = 20;
        //int[] thread_numbers = {1, 2, 4, 8};
        int[] thread_numbers = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};
        for (int size : sizes) {
            System.out.println("Array size = " + size + "; Tests = " + test_number);
            for (int thread_number : thread_numbers) {
                System.out.println("Threads = " + thread_number);
                sort(size, thread_number, test_number);
            }
            System.out.println();
        }
    }

    private static void sort(int size, int thread_number, int test_number) throws Exception {
        long total_time = 0;
        for(int i = 0; i < test_number; i++) {
            int[] array = getInitArray(size);
            int[] array_unsorted = Arrays.copyOf(array, array.length);

            long time = System.currentTimeMillis();
            if (thread_number == 1) {
                new Sort(array);
            } else {
                new ParallelSort(array, thread_number);
            }
            total_time += (System.currentTimeMillis() - time);

            //assertArrayEquals(array_unsorted, array);
            Arrays.sort(array_unsorted);
            assertArrayEquals(array_unsorted, array);
        }
        System.out.print((double) total_time / test_number);
        System.out.println(" ms");
    }

    public static int[] getInitArray(int capacity) {
        int[] array = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = (int) (Math.random() * 1000) + 1;
        }
        return array;
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        boolean flag = true;
        while(flag) {
            System.out.print("Array size = ");
            int size = in.nextInt();
            System.out.print("Threads = ");
            int thread_number = in.nextInt();
            System.out.print("Tests = ");
            int test_number = in.nextInt();
            sort(size, thread_number, test_number);
            Scanner in2 = new Scanner(System.in);
            String line = in2.nextLine();
            if (!line.isEmpty()) flag = false;
        }
        in.close();
    }
}