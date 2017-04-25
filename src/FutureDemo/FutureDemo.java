package FutureDemo;


import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java program to show how to use Future in Java. Future allows to write
 * asynchronous code in Java, where Future promises result to be available in
 * future
 *
 * @author Javin
 */
public class FutureDemo {

    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);

    private static final int numberOfThreads = 100;
    
    public static void main(String args[]) throws InterruptedException, ExecutionException {

    	CompletionService<Long> completionService = 
    		       new ExecutorCompletionService<Long>(threadpool);

    	
        FactorialCalculator task = new FactorialCalculator(20);
        System.out.println("Submitting Task ...");

        for (int i = 0; i < numberOfThreads; i++) {
        	completionService.submit(task);
        }

        System.out.println("Task is submitted Current Thread: " + Thread.currentThread().getName());

        
        int received = 0;
        boolean errors = false;

        while(received < numberOfThreads && !errors) {
              Future<Long> resultFuture = completionService.take(); //blocks if none available
              try {
                 Long result = resultFuture.get();
                 System.out.println("Factorial of 20 is : " + result);
                 received ++;
              }
              catch(Exception e) {
                 errors = true;
              }
        }
        
       threadpool.shutdown();
    }

    private static class FactorialCalculator implements Callable {

        private final int number;

        public FactorialCalculator(int number) {
            this.number = number;
        }

        @Override
        public Long call() {
            long output = 0;
            try {
                output =  factorial(number);
            } catch (InterruptedException ex) {
                Logger.getLogger(FutureDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return output;
        }

        private long factorial(int number) throws InterruptedException {
        	
        	System.out.println("Task is submitted Current Thread: " + Thread.currentThread().getName());
        	
            if (number < 0) {
                throw new IllegalArgumentException("Number must be greater than zero");
            }
            long result = 1;
            while (number > 0) {
                Thread.sleep(1); // adding delay for example
                result = result * number;
                number--;
            }
            return result;
        }
    }

}
