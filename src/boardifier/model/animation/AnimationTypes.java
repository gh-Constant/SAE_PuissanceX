package boardifier.model.animation;

import java.util.HashMap;
import java.util.Map;

/* All animation type names must be defined with a strict format
   category/version/flavor

   The 2 first are mandatory

 */
public final class AnimationTypes {
    public static final String NONE = "none";
    public static final int NONE_VALUE = -1;
    public static final String MOVE_TELEPORT = "move/teleport";
    public static final int MOVETELEPORT_VALUE = 10;
    public static final String MOVE_LINEARCST = "move/linearcst";
    public static final int MOVELINEARCST_VALUE = 11;
    public static final String MOVE_LINEARPROP = "move/linearprop";
    public static final int MOVELINEARPROP_VALUE = 12;
    public static final String LOOK_SIMPLE = "look/simple";
    public static final int LOOKSIMPLE_VALUE = 20;
    public static final String LOOK_SEQUENCE = "look/sequence";
    public static final int LOOKSEQUENCE_VALUE = 21;
    public static final String LOOK_RANDOM = "look/random";
    public static final int LOOKRANDOM_VALUE = 22;

    public static final String WAIT_FRAMES = "wait/frames";
    public static final int WAITFRAMES_VALUE = 30;

    public static Map<String,Integer> types;
    static {
        types = new HashMap<>();
        types.put(NONE,NONE_VALUE);
        types.put(MOVE_TELEPORT,MOVETELEPORT_VALUE);
        types.put(MOVE_LINEARCST,MOVELINEARCST_VALUE);
        types.put(MOVE_LINEARPROP,MOVELINEARPROP_VALUE);
        types.put(LOOK_SEQUENCE, LOOKSIMPLE_VALUE);
        types.put(LOOK_SEQUENCE, LOOKSEQUENCE_VALUE);
        types.put(LOOK_RANDOM,LOOKRANDOM_VALUE);
        types.put(WAIT_FRAMES, WAITFRAMES_VALUE);
    }

    private AnimationTypes() {
    }

    public static int getType(String name) {
        if (types.containsKey(name)) return types.get(name);
        return 0;
    }
    public static String getName(int type) {
        String name = types.entrySet().stream()
                .filter(e -> e.getValue() == type)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse("");
        return name;
    }

    public static void register(String name, int typeNumber) throws IllegalArgumentException {
        if ((!types.containsKey(name)) && (!types.containsValue(typeNumber)) )  {
            types.put(name, typeNumber);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValid(int typeNumber) {
        return types.containsValue(typeNumber);
    }
    public static boolean isValid(String name) {
        return types.containsKey(name);
    }
}
