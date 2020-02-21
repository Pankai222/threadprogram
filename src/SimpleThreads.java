import java.security.MessageDigestSpi;

public class SimpleThreads {

    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    private static class MessageLoop
            implements Runnable {
        //definerer hvad der skal ske når man bruger Thread.start() metoden.
        public void run() {
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
            try {
                //looper igennem importantInfo-array. Sover i 4 sekunder for hvert element, og
                // printer så elementet ud. Når der laves to threads looper den igennem to gange
                // for hvert element.
                for (int i = 0;
                     i < importantInfo.length;
                     i++) {
                    // Pauser i 4 seconds
                    Thread.sleep(4000);
                    // Print a message
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }
    //metoden smider InterruptedException for at kunne få lov at bruge join()-metoden threads, da
    // koden
    // tager
    // højde for, at threads kan blive interrupted imens de venter.
    public static void main(String args[])
            throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        // Laver to nye thread-objekter med MessageLoop-objekter som target.
        Thread t = new Thread(new MessageLoop());
        t.start();
        Thread u = new Thread(new MessageLoop());
        //start()-metoden kører run()-metoden defineret længere oppe
        u.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive() && u.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            u.join(1000);
            //Hvis thread.sleep() defineret længere oppe er mere end en time, så vil der printes
            // en "Tired of waiting" besked ud.
            if (((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive() && u.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                u.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
                u.join();
            }
        }
        threadMessage("Finally!");
    }
}

