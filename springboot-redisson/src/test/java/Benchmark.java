public class Benchmark {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // Your code to benchmark
        int i = 0;
        long targetTime = startTime + 1_000_000;  // 1 millisecond

        while (System.nanoTime() < targetTime) {
            i = i + 1;
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        // Print the number of iterations per millisecond
        double iterationsPerMillisecond = 1.0e9 / elapsedTime;  // Convert to iterations per second
        System.out.println("Iterations per millisecond: " + iterationsPerMillisecond);
    }
}
