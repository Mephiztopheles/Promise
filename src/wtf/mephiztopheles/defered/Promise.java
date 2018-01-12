package wtf.mephiztopheles.defered;

public interface Promise<RESOLVE, REJECT, PROGRESS> {

    public State state();

    public Promise then(Callback<RESOLVE> resolve, Callback<REJECT> reject, Callback<PROGRESS> progress);

    public Promise then(Callback<RESOLVE> resolve, Callback<REJECT> reject);

    public Promise then(Callback<RESOLVE> resolve);
}
