package wtf.mephiztopheles.defered;


import java.util.ArrayList;
import java.util.List;

public class Promise<RESOLVE, REJECT, PROGRESS> {

    List<Callback<RESOLVE>> resolve = new ArrayList<>();
    List<Callback<REJECT>> reject = new ArrayList<>();
    List<Callback<PROGRESS>> progress = new ArrayList<>();
    State state = State.PENDING;

    public State state() {
        return state;
    }

    public Promise then(Callback<RESOLVE> resolve, Callback<REJECT> reject, Callback<PROGRESS> progress) {
        if (resolve != null)
            this.resolve.add(resolve);

        if (reject != null)
            this.reject.add(reject);

        if (progress != null)
            this.progress.add(progress);

        return this;
    }

    public Promise then(Callback<RESOLVE> resolve, Callback<REJECT> reject) {
        return then(resolve, reject, null);
    }

    public Promise then(Callback<RESOLVE> resolve) {
        return then(resolve, null, null);
    }
}