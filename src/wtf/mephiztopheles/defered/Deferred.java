package wtf.mephiztopheles.defered;

import java.util.*;

public class Deferred<RESOLVE, REJECT, PROGRESS> {

    private final Promise<RESOLVE, REJECT, PROGRESS> promise;
    private int successed;

    public Deferred() {
        promise = new Promise();
    }

    public static <RESOLVE,REJECT,PROGRESS> Promise all(Promise<RESOLVE,REJECT,PROGRESS>... all) {

        Deferred deferred = new Deferred();
        deferred.successed = 0;

        for (Promise promise : all) {

            promise.then((Callback) (Object arg) -> {
                deferred.successed++;
                if (deferred.successed == all.length)
                    deferred.resolve(null);
            }, (Callback) (Object arg) -> {
                if (deferred.promise.state.equals(State.PENDING))
                    deferred.reject(arg);
            }, null);
        }
        return deferred.promise;
    }

    public void progress(PROGRESS argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback callback : promise.progress) {
            callback.call(argument);
        }
    }

    public void resolve(RESOLVE argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback callback : promise.resolve) {
            callback.call(argument);
        }

        promise.state = State.RESOLVED;
    }

    public void reject(REJECT argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback callback : promise.reject) {
            callback.call(argument);
        }

        promise.state = State.REJECTED;
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

        public Promise then(Callback resolve, Callback reject, Callback progress) {
            if (resolve != null)
                this.resolve.add(resolve);

            if (reject != null)
                this.reject.add(reject);

            if (progress != null)
                this.progress.add(progress);

            return this;
        }

        public Promise then(Callback resolve, Callback reject) {
            return then(resolve, reject, null);
        }

        public Promise then(Callback resolve) {
            return then(resolve, null, null);
        }
    }
}
