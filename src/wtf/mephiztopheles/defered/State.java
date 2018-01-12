package wtf.mephiztopheles.defered;

public enum State {
    PENDING(""),
    REJECTED("The Promise was already rejected"),
    RESOLVED("The Promise was already resolved");

    public final String message;

    State(String message) {
        this.message = message;
    }
}
