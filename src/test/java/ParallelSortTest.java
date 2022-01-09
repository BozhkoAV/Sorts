import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
        String check = in.nextLine();
        if (check.isEmpty()) flag = false;
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
        flag = true;
        while(flag) {
            System.out.println("Вывести график: ");
            int graph_number = in.nextInt();
            if (graph_number == 1) {
                int size = 1_000_000;
                int test_number = 50;
                int[] thread_numbers = {1, 2, 4, 8, 16, 32, 64};
                double[] time = new double[thread_numbers.length];
                for (int i = 0; i < thread_numbers.length; i++) {
                    time[i] = sort_time(size, thread_numbers[i], test_number);
                }

                XYSeries series = new XYSeries("Array Size = " + size + "; Test Number = " + test_number);
                for(int i = 0; i < thread_numbers.length; i++){
                    series.add(thread_numbers[i], time[i]);
                }
                XYDataset xyDataset = new XYSeriesCollection(series);
                JFreeChart chart = ChartFactory
                        .createXYLineChart("Performance", "thread_number",
                                "time (ms)", xyDataset, PlotOrientation.VERTICAL,
                                true, true, true);
                JFrame frame = new JFrame("Chart");
                // Помещаем график на фрейм
                frame.getContentPane().add(new ChartPanel(chart));
                frame.setSize(900,600);
                frame.show();
            } else if (graph_number == 2) {
                //int[] sizes = {100, 500, 1000, 5000, 10_000};
                //int[] sizes = {10_000, 15_000, 20_000, 25_000, 30_000, 40_000, 50_000};
                int[] sizes = {50_000, 100_000, 500_000, 1_000_000, 5_000_000};
                int test_number = 30;
                int[] thread_numbers = {1, 2, 4, 8};
                double[][] time = new double[thread_numbers.length][sizes.length];
                for (int i = 0; i < thread_numbers.length; i++) {
                    for (int j = 0; j < sizes.length; j++) {
                        time[i][j] = sort_time(sizes[j], thread_numbers[i], test_number);
                    }
                }

                XYSeries series1 = new XYSeries("1 thread");
                XYSeries series2 = new XYSeries("2 threads");
                XYSeries series3 = new XYSeries("4 threads");
                XYSeries series4 = new XYSeries("8 threads");

                for(int i = 0; i < sizes.length; i++){
                    series1.add(sizes[i], time[0][i]);
                    series2.add(sizes[i], time[1][i]);
                    series3.add(sizes[i], time[2][i]);
                    series4.add(sizes[i], time[3][i]);
                }

                XYSeriesCollection xyDataset = new XYSeriesCollection();
                xyDataset.addSeries(series1);
                xyDataset.addSeries(series2);
                xyDataset.addSeries(series3);
                xyDataset.addSeries(series4);

                JFreeChart chart = ChartFactory
                        .createXYLineChart("Performance", "Array size",
                                "time (ms)", xyDataset, PlotOrientation.VERTICAL,
                                true, true, true);
                JFrame frame = new JFrame("Chart");
                // Помещаем график на фрейм
                frame.getContentPane().add(new ChartPanel(chart));
                frame.setSize(900,600);
                frame.show();
            } else flag = false;
        }
        in.close();
    }

    private static double sort_time(int size, int thread_number, int test_number) throws Exception {
        long total_time = 0;
        for(int i = 0; i < test_number; i++) {
            int[] array = getInitArray(size);
            long time = System.currentTimeMillis();
            if (thread_number == 1) {
                new Sort(array);
            } else {
                new ParallelSort(array, thread_number);
            }
            total_time += (System.currentTimeMillis() - time);
        }
        return (double) total_time / test_number;
    }
}