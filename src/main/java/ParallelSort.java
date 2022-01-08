import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class MergeSort extends RecursiveAction {
    private final int[] array, merge;
    private final int lo, hi, thread_number;

    public MergeSort(int[] array, int thread_number) throws Exception{
        this.array = array;
        this.lo = 0;
        this.hi = array.length;
        this.merge = new int[array.length];
        int check_thread_number = thread_number;
        if (check_thread_number <= 0) throw new Exception("Thread_number должно быть > 0");
        while (check_thread_number > 1) {
            if (check_thread_number % 2 != 0) throw new Exception("Thread_number должно быть степенью 2");
            check_thread_number /= 2;
        }
        this.thread_number = thread_number;
    }

    public MergeSort(int[] array, int[] merge, int lo, int hi, int thread_number) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
        this.merge = merge;
        this.thread_number = thread_number;
    }

    @Override
    protected void compute() {
        if (hi - lo <= 1) return;
        if (thread_number <= 1) {
            //System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
            Sort singleThreadSort = new Sort(Arrays.copyOfRange(array, lo, hi));
            int[] part = singleThreadSort.array;
            int ind = 0;
            for(int k = lo; k < hi; k++) {
                array[k] = part[ind];
                ind++;
            }
            return;
        }

        int mid = lo + (hi-lo)/2;
        MergeSort firstHalfArrayValueSumCounter = new MergeSort(array, merge, lo, mid, thread_number/2);
        MergeSort secondHalfArrayValueSumCounter = new MergeSort(array, merge, mid, hi, thread_number/2);

        invokeAll(firstHalfArrayValueSumCounter, secondHalfArrayValueSumCounter);

        secondHalfArrayValueSumCounter.join();
        firstHalfArrayValueSumCounter.join();

        int i = lo, j = mid;
        for(int k = lo; k < hi; k++)
            if (i == mid) merge[k] = array[j++];
            else if (j == hi) merge[k] = array[i++];
            else if (array[j] < array[i]) merge[k] = array[j++];
            else merge[k] = array[i++];

        for(int k = lo; k < hi; k++)
            array[k] = merge[k];
    }
}

public class ParallelSort {
    private static int[] array = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static int thread_number = 8;

    public ParallelSort(int[] array, int thread_number) throws Exception {
        ParallelSort.array = array;
        ParallelSort.thread_number = thread_number;
        mySort();
    }

    public static void main(String[] args) throws Exception {
        mySort();
    }

    public static void mySort() throws Exception {
        /* Всего в моей системе 12 процессоров
           int numOfProcessors = Runtime.getRuntime().availableProcessors();
           System.out.println(numOfProcessors); */

        if (array.length <= 20) System.out.println(Arrays.toString(array));
        MergeSort sorter = new MergeSort(array, thread_number);
        ForkJoinPool forkJoinPool = new ForkJoinPool(thread_number);
        forkJoinPool.invoke(sorter);
        if (array.length <= 20) System.out.println(Arrays.toString(array));
    }
}
