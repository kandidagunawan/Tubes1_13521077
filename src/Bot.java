import javafx.scene.control.Button;

public class Bot {

    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst) {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}