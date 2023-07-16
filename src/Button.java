import java.util.function.Consumer;

public class Button extends Tile {

    Consumer<ClickInfo> onClick;

    Button(double x1, double y1, double x2, double y2, Consumer<ClickInfo> onClick) {
        super(x1, y1, x2, y2);
        this.onClick = onClick;
    }
}
