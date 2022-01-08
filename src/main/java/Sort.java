import java.util.Arrays;

public class Sort {
    public final int[] array;

    public Sort(int[] array) {
        this.array = array;
        sort(array);
    }

    private static void sort(int[] array) {
        int[] merge = new int[array.length];
        sort(array, merge, 0, array.length);
    }

    private static void sort(int[] array, int[] merge, int lo, int hi) {
        if(hi - lo <= 1) return;

        int mid = lo + (hi-lo)/2;
        sort(array, merge, lo, mid);
        sort(array, merge, mid, hi);

        int i = lo, j = mid;
        for(int k = lo; k < hi; k++)
            if (i == mid) merge[k] = array[j++];
            else if (j == hi) merge[k] = array[i++];
            else if (array[j] < array[i]) merge[k] = array[j++];
            else merge[k] = array[i++];

        for(int k = lo; k < hi; k++)
            array[k] = merge[k];
    }

    public static void main(String[] args) {
        int[] array = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
    }
}
