import java.util.ArrayList;
import java.util.List;

public class Bot {

    public int[] move(int[][] currentBoard, int round) {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
