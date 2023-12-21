public class UpdateCheckerThread extends Thread {
    public String[] result;
    OhHellScoreboardV2 sc;
    UpdateCheckerThread(OhHellScoreboardV2 sc) {
        this.sc = sc;
    }
    @Override
    public void run() {
        result = sc.loadStrings("https://raw.githubusercontent.com/JustinTimeCuber/OHSCv2/main/src/version.txt");
        if(result != null) {
            if(ScreenManager.currentScreen() == UpdateScreen.INSTANCE) {
                ScreenManager.popScreen();
            }
            UpdateScreen.INSTANCE.data = result;
            ScreenManager.pushScreen(UpdateScreen.INSTANCE);
        } else {
            sc.displayError("Failed to check for updates");
        }
    }
}
