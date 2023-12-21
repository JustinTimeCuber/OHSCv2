public class UpdateScreen implements Screen {
    static final UpdateScreen INSTANCE = new UpdateScreen();
    String[] data;
    int stable = 0;
    int snapshot = 0;
    int current = 0;
    String stable_str = "";
    String snapshot_str = "";
    String current_str = "";

    private UpdateScreen() {
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        sc.textSize(sc.width * 0.015f);
        sc.text("Current version: " + current_str + " (" + current + ")", sc.width*0.5f, sc.height*0.4f);
        sc.text("Latest stable: " + stable_str + " (" + stable + ")", sc.width*0.5f, sc.height*0.5f);
        sc.text("Latest snapshot: " + snapshot_str + " (" + snapshot + ")", sc.width*0.5f, sc.height*0.6f);
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
    }

    @Override
    public void onLoad(OhHellScoreboardV2 sc) {
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
    }
}
