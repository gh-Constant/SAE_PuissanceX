package boardifier.model;

public class EventQueue {

    private Event[] queue;
    private int size;

    public EventQueue() {
        queue = new Event[1000];
        size = 0;
    }

    public Event[] getQueue() {
        return queue;
    }

    public int getSize() {
        return size;
    }

    public Event getEvent(int index) {
        if ((index < 0) || (index >= size)) return null;
        return queue[index];
    }

    public void addChangeLocationEvent() {
        queue[size++] = new Event(Event.EventType.LOCATION);
    }

    public void addChangeVisibilityEvent() {
        queue[size++] = new Event(Event.EventType.VISIBILITY);
    }

    public void addChangeSelectionEvent() {
        queue[size++] = new Event(Event.EventType.SELECTION);
    }

    public void addChangeFaceEvent() {
        queue[size++] = new Event(Event.EventType.FACE);
    }

    public void addPutInContainerEvent(ContainerElement container, int row, int col) {
        Event e = new Event(Event.EventType.IN_CONTAINER);
        e.addParameter(container);
        e.addParameter(row);
        e.addParameter(col);
        queue[size++] = e;
    }

    public void addRemoveFromContainerEvent(ContainerElement container, int row, int col) {
        Event e = new Event(Event.EventType.OUT_CONTAINER);
        e.addParameter(container);
        e.addParameter(row);
        e.addParameter(col);
        queue[size++] = e;
    }

    public void addMoveInContainerEvent(int rowSrc, int colSrc, int rowDest, int colDest) {
        Event e = new Event(Event.EventType.MOVE_CONTAINER);
        e.addParameter(rowSrc);
        e.addParameter(colSrc);
        e.addParameter(rowDest);
        e.addParameter(colDest);
        queue[size++] = e;
    }

    public Event removeEvent(int index) {
        if ((index <0) || (index >= size)) return null;
        Event e = queue[index];
        for (int i = index; i < size - 1; i++) {
            queue[i] = queue[i + 1];
        }
        queue[size-1] = null;
        size--;
        return e;
    }

    public void clear() {
        for(int i=0;i<size;i++) queue[i] = null;
        size = 0;
    }

    public boolean isChangeFaceEvent() {
        for(Event e : queue) {
            if (e.isFaceEvent()) return true;
        }
        return false;
    }

    public boolean isChangeVisibilityEvent() {
        for(Event e : queue) {
            if (e.isVisibilityEvent()) return true;
        }
        return false;
    }
    public boolean isChangeSelectionEvent() {
        for(Event e : queue) {
            if (e.isSelectionEvent()) return true;
        }
        return false;
    }
    public boolean isChangeLocationEvent() {
        for(Event e : queue) {
            if (e.isLocationEvent()) return true;
        }
        return false;
    }
    public boolean isPutInContainerEvent() {
        for(Event e : queue) {
            if (e.isInContainerEvent()) return true;
        }
        return false;
    }
    public boolean isRemoveFromContainerEvent() {
        for(Event e : queue) {
            if (e.isOutContainerEvent()) return true;
        }
        return false;
    }
    public boolean isMoveInContainerEvent() {
        for(Event e : queue) {
            if (e.isMoveInContainerEvent()) return true;
        }
        return false;
    }
}
