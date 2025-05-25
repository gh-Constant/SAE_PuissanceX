package boardifier.model;

import java.util.HashMap;
import java.util.Map;

public final class ElementTypes {
    public static final Map<String,Integer> types;
    static {
        types = new HashMap<>();
        types.put("basic",0);
        types.put("static",1);
        types.put("background",3);
        types.put("container",10);
        types.put("text",20);
        types.put("sprite",30);
        types.put("dice",31);
    }

    private ElementTypes() {
    }

    public static int getType(String name) {
        if (types.containsKey(name)) return types.get(name);
        return 0;
    }

    /**
     * Register a type of game element
     * It is possible to re-register a type but only with the same name+value. If a different value is
     * provided, an exception is raised.
     * It is not possible to register any type with an already existing integer value, associated to another name.
     * @param name the name of the element
     * @param typeNumber the integer value associated to the name
     * @throws IllegalArgumentException
     */
    public static void register(String name, int typeNumber) throws IllegalArgumentException {
        // check re-registering with another value, or new register with an existing value
        if (((types.containsKey(name)) && (types.get(name) != typeNumber)) ||
                ((!types.containsKey(name)) && (types.containsValue(typeNumber)) )) {
            throw new IllegalArgumentException();
        }
        // no pb, register
        types.put(name, typeNumber);
    }

    public static boolean isValid(int typeNumber) {
        return types.containsValue(typeNumber);
    }
    public static boolean isValid(String name) {
        return types.containsKey(name);
    }
}
