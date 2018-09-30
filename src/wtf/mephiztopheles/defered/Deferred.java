package wtf.mephiztopheles.defered;

import java.util.*;

public class Deferred<RESOLVE, REJECT, PROGRESS> {

    private final Promise<RESOLVE, REJECT, PROGRESS> promise;
    private int succeed;

    public Deferred() {
        promise = new Promise<>();
    }

    public static Promise<List<Object>, Object, Object> all(Promise... all) {

        Deferred<List<Object>, Object, Object> deferred = new Deferred<>();
        deferred.succeed = 0;

        for (Promise<?, ?, ?> promise : all) {

            promise.then((arg) -> {
                deferred.succeed++;
                if (deferred.succeed == all.length)
                    deferred.resolve(null);
            }, (arg) -> {
                if (deferred.promise.state.equals(State.PENDING))
                    deferred.reject(arg);
            }, null);
        }
        return deferred.promise;
    }

    public void progress(PROGRESS argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback<PROGRESS> callback : promise.progress) {
            callback.call(argument);
        }
    }

    public void resolve(RESOLVE argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback<RESOLVE> callback : promise.resolve) {
            callback.call(argument);
        }

        promise.state = State.RESOLVED;
    }

    public void reject(REJECT argument) {

        if (!promise.state.equals(State.PENDING))
            throw new IllegalStateException(promise.state.message);

        for (Callback<REJECT> callback : promise.reject) {
            callback.call(argument);
        }

        promise.state = State.REJECTED;
    }

    public Promise<RESOLVE, REJECT, PROGRESS> promise() {
        return promise;
    }

}
