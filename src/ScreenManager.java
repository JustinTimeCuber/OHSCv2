import java.util.Stack;

public class ScreenManager {
    public static OhHellScoreboardV2 sc;
    public final static Screen[] all_screens = new Screen[]{
            SetupScreen.INSTANCE,
            BiddingScreen.INSTANCE,
            TakingScreen.INSTANCE,
            GameOverScreen.INSTANCE,
            StatisticsScreen.INSTANCE
    };
    private final static Stack<Screen> screen_stack = new Stack<>();

    static String stateToString() {
        StringBuilder out = new StringBuilder();
        for(Screen s : screen_stack) {
            out.append(s.getClass().getName()).append(",");
        }
        return out.toString();
    }

    static void setStateFromString(String state) {
        reset();
        String[] screens = state.split(",");
        for(String s : screens) {
            Screen screen = screenFromString(s);
            if(s == null) {
                initDefault();
                break;
            }
            pushScreen(screen);
        }
        screen_stack.peek().onLoad(sc);
    }

    static Screen screenFromString(String s) {
        return switch(s) {
            case "SetupScreen" -> SetupScreen.INSTANCE;
            case "BiddingScreen" -> BiddingScreen.INSTANCE;
            case "TakingScreen" -> TakingScreen.INSTANCE;
            case "GameOverScreen" -> GameOverScreen.INSTANCE;
            case "StatisticsScreen" -> StatisticsScreen.INSTANCE;
            default -> null;
        };
    }

    static void reset() {
        screen_stack.clear();
    }

    static void initDefault() {
        reset();
        pushScreen(BiddingScreen.INSTANCE);
        pushScreen(SetupScreen.INSTANCE);
        SetupScreen.INSTANCE.onLoad(sc);
    }

    static void setScreen(Screen s) {
        if(!screen_stack.empty()) {
            screen_stack.pop();
        }
        screen_stack.push(s);
        s.onLoad(sc);
    }

    static void pushScreen(Screen s) {
        screen_stack.push(s);
        s.onLoad(sc);
    }

    static void popScreen() {
        if(screen_stack.empty()) {
            return;
        }
        screen_stack.pop();
        screen_stack.peek().onLoad(sc);
    }

    static Screen currentScreen() {
        return screen_stack.peek();
    }
}
