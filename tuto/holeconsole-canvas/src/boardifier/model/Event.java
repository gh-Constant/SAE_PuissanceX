package boardifier.model;

public final class Event {

    public enum EventType {
        LOCATION, VISIBILITY, SELECTION, IN_CONTAINER, OUT_CONTAINER, MOVE_CONTAINER, FACE
    };
    private EventType type;
    private Object[] params; // avoid ArrayList overload, since there is a macimum numer of parameters.
    private int paramCount;

    public Event(EventType type) {
        this.type = type;
        params = new Object[5];
        paramCount = 0;
    }

    public EventType getType() {
        return type;
    }

    public void addParameter(Object o) {
        params[paramCount++] = o;
    }

    public Object getParameter(int index) {
        return params[index];
    }

    public boolean isLocationEvent() {
        return type==EventType.LOCATION?true:false;
    }
    public boolean isVisibilityEvent() {
        return type==EventType.VISIBILITY?true:false;
    }
    public boolean isSelectionEvent() {
        return type==EventType.SELECTION?true:false;
    }
    public boolean isInContainerEvent() {
        return type==EventType.IN_CONTAINER ?true:false;
    }
    public boolean isOutContainerEvent() {
        return type==EventType.OUT_CONTAINER ?true:false;
    }
    public boolean isMoveInContainerEvent() {
        return type==EventType.MOVE_CONTAINER ?true:false;
    }
    public boolean isFaceEvent() {
        return type==EventType.FACE?true:false;
    }
}