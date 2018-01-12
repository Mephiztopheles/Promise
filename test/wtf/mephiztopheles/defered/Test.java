package wtf.mephiztopheles.defered;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    public static void main(String[] args) {

        Callback<String> callback = (Callback<String>) (String argument) -> {
            System.out.println(argument);
        };

        Promise promise = doWork();

        promise.then(callback, (Callback<Integer>) (Integer argument) -> {
            System.err.println(argument + " times checked");
        }, callback);

        Deferred.all(promise, promise).then((Callback) (Object argument) -> {
            System.out.println("All done: " + argument);
        }, (Callback) (Object argument) -> {
            System.err.println("oh, got error: " + argument);
        }, callback);
    }

    public static Promise<String, Integer, String> doWork() {

        Deferred<String, Integer, String> defer = new Deferred();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                int randomNum = ThreadLocalRandom.current().nextInt(2, 6);

                try {

                    for (int i = 0; i < randomNum; i++) {
                        defer.progress("I'm working on it...");
                        Thread.sleep(200);
                    }

                    if (randomNum % 2 == 0)
                        defer.resolve("It works");
                    else
                        defer.reject(randomNum);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    timer.cancel();
                }
            }
        }, 500);

        return defer.promise();
    }
}
