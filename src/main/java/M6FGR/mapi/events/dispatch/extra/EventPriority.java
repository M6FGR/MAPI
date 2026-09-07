package M6FGR.mapi.events.dispatch.extra;

public enum EventPriority {
    OVERRIDE(6),
    HIGHEST(5),
    HIGH(4),
    MIDDLE(3),
    LOW(2),
    LOWEST(1);

    private final int priority;
    EventPriority(int priorityNumber) {
        this.priority = priorityNumber;
    }

    public int getNumber() {
        return this.priority;
    }

}
