package wtf.mephiztopheles.defered;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import wtf.mephiztopheles.defered.Defered.Promise;

public class Test {

    public static void main(String[] args) {

        Callback<String> callback = (Callback<String>) (String argument) -> {
            System.out.println(argument);
        };
        doWork().then(callback, (Callback<Integer>) (Integer argument) -> {
            System.out.println(argument);
        }, callback);
    }

    public static Promise<String, Integer, String> doWork() {

        Defered<String, Integer, String> defer = new Defered();

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
                }
            }
        }, 500);

        return defer.promise();
    }
}
