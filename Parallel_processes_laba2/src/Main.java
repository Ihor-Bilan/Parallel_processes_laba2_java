import java.util.Random;

class MinElementParallel {
    private static final int arrayLength = 100;
    private static final int threadCount = 4;

    private final Thread[] threads = new Thread[threadCount];
    private final int[] array = new int[arrayLength];
    private int minIndex = -1;
    private int minValue = Integer.MAX_VALUE;

    public static void main(String[] args) {
        MinElementParallel main = new MinElementParallel();
        main.initArray();
        main.runThreads();
        main.findMinElement();
        System.out.println("Мiнiмальний елемент: " + main.minValue + ", його iндекс: " + main.minIndex);
    }

    private void initArray() {
        Random rnd = new Random();
        for (int i = 0; i < arrayLength; i++) {
            array[i] = rnd.nextInt(2000) - 1000;
        }
    }

    private void runThreads() {
        int partSize = arrayLength / threadCount;
        for (int i = 0; i < threadCount; i++) {
            int start = i * partSize;
            int end = (i == threadCount - 1) ? arrayLength : (i + 1) * partSize;
            threads[i] = new Thread(() -> findMinInPart(start, end));
            threads[i].start();
        }
    }

    private void findMinInPart(int start, int end) {
        int minVal = Integer.MAX_VALUE;
        int minIdx = -1;
        for (int i = start; i < end; i++) {
            if (array[i] < minVal) {
                minVal = array[i];
                minIdx = i;
            }
        }
        synchronized (this) {
            if (minVal < minValue) {
                minValue = minVal;
                minIndex = minIdx;
            }
        }
    }

    private void findMinElement() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
