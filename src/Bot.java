import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;


public class Bot {

    public int[] move(Button[][] button) {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}