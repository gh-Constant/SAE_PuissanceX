package boardifier.control;

public class Logger {

    public static int LOGGER_NONE = 0; // no log
    public static int LOGGER_INFO = 1; // level used for informations messages
    public static int LOGGER_DEBUG = 2; // level used to debug projects using boardifier but without messages from boardifier
    public static int LOGGER_TRACE = 3; // level used for internal debug of boardifier
    private static int level = LOGGER_NONE;

    public static int VERBOSE_NONE = 0; // just display what is given as a parameter
    public static int VERBOSE_BASIC = 1; // display class+method names before parameter
    public static int VERBOSE_HIGH = 2; // display class+method names + object ref, before parameter
    private static int verboseLevel = VERBOSE_BASIC; // default level of verbosity

    public static void  setLevel(int l) {
        level = l;
    }

    public static void setVerbosity(int v) {
        verboseLevel = v;
    }

    /* different methods to log
     * Note that there is much duplicate code because of the stack must be in the same state
     * for every methods
     */
    public static void info(String s) {
        if (level >= 1) {
            print(s, null);
        }
    }

    public static void info(String s, Object caller) {
        if (level >= 1) {
            print(s, caller);
        }
    }

    public static void debug(String s) {
        if (level >= 2) {
            print(s, null);
        }
    }

    public static void debug(String s, Object caller) {
        if (level >= 2) {
            print(s, caller);
        }
    }

    public static void trace(String s) {
        if (level >= 3) {
            print(s, null);
        }
    }

    public static void trace(String s, Object caller) {
        if (level >= 3) {
            print(s, caller);
        }
    }

    private static void print(String s, Object caller) {
        if (verboseLevel == VERBOSE_BASIC) {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            s = st[3] + " - "+s;
        }
        else if (verboseLevel == VERBOSE_HIGH) {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            s = st[3] + " - "+s;
            if (caller != null) s = "["+caller+"] -> "+s;
        }
        System.out.println(s);
    }
}
