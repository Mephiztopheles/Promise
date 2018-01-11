package wtf.mephiztopheles.defered;

import java.util.ArrayList;
import java.util.List;
import wtf.mephiztopheles.defered.Defered.Promise.State;

public class Defered<RESOLVE, REJECT, PROGRESS> {

    private final Promise<RESOLVE, REJECT, PROGRESS> promise;
    private int successed;

    public Defered() {
        promise = new Promise();
    }

    public static Promise all(Promise... all) {

        Defered defered = new Defered();
        defered.successed = 0;

        for (Promise promise : all) {

            promise.then((Callback) (Object arg) -> {
                defered.successed++;
                if (defered.successed == all.length)
                    defered.resolve(null);
            }, (Callback) (Object arg) -> {
                if (defered.promise.state.equals(State.PENDING))
                    defered.reject(arg);
            }, null);
        }
        return defered.promise;
    }

    public void progress(PROGRESS argument) {

        if (!promise.state.equals(Promise.State.PENDING))
            throw new IllegalStateException(promise.state.message);

        promise.progress.forEach((callback) -> {
            callback.call(argument);
        });
    }

    public void resolve(RESOLVE argument) {

        if (!promise.state.equals(Promise.State.PENDING))
            throw new IllegalStateException(promise.state.message);

        promise.resolve.forEach((callback) -> {
            callback.call(argument);
        });

        promise.state = Promise.State.RESOLVED;
    }

    public void reject(REJECT argument) {

        if (!promise.state.equals(Promise.State.PENDING))
            throw new IllegalStateException(promise.state.message);

        promise.reject.forEach((callback) -> {
            callback.call(argument);
        });

        promise.state = Promise.State.REJECTED;
    }

    public Promise<RESOLVE, REJECT, PROGRESS> promise() {
        return promise;
    }

    public static class Promise<RESOLVE, REJECT, PROGRESS> {

        private List<Callback<RESOLVE>> resolve = new ArrayList<>();
        private List<Callback<REJECT>> reject = new ArrayList<>();
        private List<Callback<PROGRESS>> progress = new ArrayList<>();
        private State state = State.PENDING;

        public State state() {
            return state;
        }

        public Promise<RESOLVE, REJECT, PROGRESS> then(Callback resolve, Callback reject, Callback progress) {

            if (resolve != null)
                this.resolve.add(resolve);

            if (reject != null)
                this.reject.add(reject);

            if (progress != null)
                this.progress.add(progress);

            return this;
        }

        public Promise<RESOLVE, REJECT, PROGRESS> then(Callback resolve, Callback reject) {
            return then(resolve, reject, null);
        }

        public Promise<RESOLVE, REJECT, PROGRESS> then(Callback resolve) {
            return then(resolve, null, null);
        }

        public static enum State {

            PENDING(""),
            REJECTED("The Promise was already rejected"),
            RESOLVED("The Promise was already resolved");

            public final String message;

            State(String message) {
                this.message = message;
            }
        }
    }
}
