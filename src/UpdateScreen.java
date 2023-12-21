import java.net.URI;

public class UpdateScreen implements Screen {
    static final UpdateScreen INSTANCE = new UpdateScreen();
    String[] data;
    int stable = 0;
    int snapshot = 0;
    int current = 0;
    String stable_str = "";
    String snapshot_str = "";
    String current_str = "";
    Tile ignore_once_button;
    Tile toggle_update_checking_button;
    Tile toggle_release_channel_button;
    Tile download_update_button;
    UpdateMode other = UpdateMode.NONE;
    String update_url = "";
    boolean manual = false;

    private UpdateScreen() {
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        sc.textSize(sc.width * 0.05f);
        String text = "OHSCv2 is up-to-date!";
        boolean update = false;
        boolean update_checking = Config.update_mode != UpdateMode.NONE;
        if(snapshot > current) {
            update = true;
            text = "New snapshot available!";
        }
        if(stable > current) {
            update = true;
            text = "New update available!";
        }
        sc.fill(update ? Theme.theme.highlight_text_color : Theme.theme.overbid_color);
        sc.text(text, sc.width * 0.5f, sc.height * 0.15f);
        sc.textSize(sc.width * 0.02f);
        sc.fill(Theme.theme.text_color);
        sc.text("Current version: " + current_str + " (" + current + ")", sc.width * 0.5f, sc.height * 0.35f);
        sc.fill(snapshot > current ? Theme.theme.highlight_text_color : Theme.theme.text_color);
        sc.text("Latest snapshot: " + snapshot_str + " (" + snapshot + ")", sc.width * 0.5f, sc.height * 0.55f);
        sc.fill(stable > current ? Theme.theme.highlight_text_color : Theme.theme.text_color);
        sc.text("Latest stable: " + stable_str + " (" + stable + ")", sc.width * 0.5f, sc.height * 0.45f);
        sc.drawButton(ignore_once_button, update ? "Ignore Once" : "Return", 0.015f, true, true);
        sc.drawButton(toggle_update_checking_button, (update_checking ? "Disable" : "Enable") + "\nUpdate Checking", 0.015f, true, true);
        sc.drawButton(toggle_release_channel_button, "Release Channel:\n" + Config.update_mode, 0.015f, update_checking, true);
        sc.drawButton(download_update_button, update ? "Download Update" : "View Downloads", 0.015f, true, true);
        sc.textSize(sc.width * 0.015f);
        if(ignore_once_button.mouseInTile()) {
            sc.drawTooltip("You can return to this screen using the Settings menu.", true);
        } else if(toggle_update_checking_button.mouseInTile()) {
            sc.drawTooltip(update_checking ? "You can return to this screen using the Settings menu." : "Change release channel to " + other, true);
        } else if(toggle_release_channel_button.mouseInTile() && update_checking) {
            sc.drawTooltip("Change release channel to " + other, true);
        } else if(download_update_button.mouseInTile()) {
            sc.drawTooltip(update_url, true);
        }
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        boolean update_checking = Config.update_mode != UpdateMode.NONE;
        if(ignore_once_button.mouseInTile()) {
            ScreenManager.popScreen();
        } else if(toggle_update_checking_button.mouseInTile()) {
            if(update_checking) {
                other = Config.update_mode;
                Config.update_mode = UpdateMode.NONE;
                Config.fixFile(sc);
                ScreenManager.popScreen();
            } else {
                Config.update_mode = other;
                Config.fixFile(sc);
                updateButtons();
            }
        } else if(toggle_release_channel_button.mouseInTile() && update_checking) {
            Config.update_mode = other;
            Config.fixFile(sc);
            updateButtons();
        } else if(download_update_button.mouseInTile()) {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create(update_url));
            } catch(Exception e) {
                sc.displayError("Failed to open webpage: " + e.getMessage());
            }
        }
    }

    @Override
    public void onLoad(OhHellScoreboardV2 sc) {
        if(data == null) {
            ScreenManager.popScreen();
            return;
        }
        for(String d : data) {
            String[] spl = d.split(":");
            switch(spl[0]) {
                case "STABLE" -> {
                    stable = Integer.parseInt(spl[1]);
                    stable_str = spl[2];
                }
                case "SNAPSHOT" -> {
                    snapshot = Integer.parseInt(spl[1]);
                    snapshot_str = spl[2];
                }
            }
        }
        String[] cv = sc.loadStrings("version.txt");
        for(String d : cv) {
            String[] spl = d.split(":");
            if(spl[0].equals("SNAPSHOT")) {
                current = Integer.parseInt(spl[1]);
                current_str = spl[2];
            }
        }
        if(!manual && (Config.update_mode == UpdateMode.NONE ||
                (Config.update_mode == UpdateMode.STABLE && current >= stable) ||
                (Config.update_mode == UpdateMode.SNAPSHOT && current >= snapshot))) {
            ScreenManager.popScreen();
            return;
        }
        updateButtons();
    }

    void updateButtons() {
        if(Config.update_mode == UpdateMode.STABLE) {
            other = UpdateMode.SNAPSHOT;
            update_url = "https://github.com/JustinTimeCuber/OHSCv2/releases/latest";
        } else if(Config.update_mode == UpdateMode.SNAPSHOT) {
            other = UpdateMode.STABLE;
            update_url = "https://github.com/JustinTimeCuber/OHSCv2/releases";
        }
    }

    @Override
    public void init(OhHellScoreboardV2 sc) {
        ignore_once_button = new Tile(0.08, 0.8, 0.23, 0.9);
        toggle_update_checking_button = new Tile(0.31, 0.8, 0.46, 0.9);
        toggle_release_channel_button = new Tile(0.54, 0.8, 0.69, 0.9);
        download_update_button = new Tile(0.77, 0.8, 0.92, 0.9);
    }
}
