import java.awt.*;
import javafx.scene.control.Button;

public class Bot {

    public int objectiveFunction(int[] currentPost, Button[][] button) {
        // ngitung berapa banyak jumlah lawan yang bisa keganti dari posisi kita sekarang
        // initiates position from up left right bottom
        int[][] positions = {{currentPost[0], currentPost[1] + 1}, {currentPost[0]- 1, currentPost[1]}, {currentPost[0] + 1, currentPost[1]}, {currentPost[0], currentPost[1] - 1}};

        int count = 0;
        for (int[] post : positions) {
            int i = post[0];
            int j = post[1];
            if (button[i][j].getText().equals("X")) {
                count++;
            }
        }

        return count;
    }

    public int[] move() {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
