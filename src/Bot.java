import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Button;

public class Bot {

    public int objectiveFunction(int[] currentPost, Button[][] button) {
        // ngitung berapa banyak jumlah lawan yang bisa keganti dari posisi kita sekarang
        // initiates positions: up left right bottom
        int[][] positions = {{currentPost[0], currentPost[1] + 1}, {currentPost[0]- 1, currentPost[1]}, {currentPost[0] + 1, currentPost[1]}, {currentPost[0], currentPost[1] - 1}};

        int count = 0;
        for (int[] post : positions) {
            int i = post[0];
            int j = post[1];
            if (i >= 0 && i <= 7 && j >= 0 && j <= 7) {
                if (button[i][j].getText().equals("X")) {
                    count++;
                }
            }
        }

        return count;
    }

    public int[] move(Button[][] button) {
        // create random move
        List<int[]> positions = generate_empty_cell(button);

        int[] current = new int[2];
        int[] next = new int[2];
        Random random = new Random();

        int randomIdx = random.nextInt(0, positions.size());
        current[0] = positions.get(randomIdx)[0];
        current[1] = positions.get(randomIdx)[1];


        while (true) {
            int randomIdx2 = random.nextInt(0, positions.size());
            next[0] = positions.get(randomIdx2)[0];
            next[1] = positions.get(randomIdx2)[1];

            if (objectiveFunction(next, button) <= objectiveFunction(current, button)) {
                break;
            }
            current[0] = next[0];
            current[1] = next[1];
        }

        return new int[]{current[0], current[1]};
    }

    public List<int[]> generate_empty_cell(Button[][] button) {
        List<int[]> positions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (button[i][j].getText().equals("")) {
                    int[] position = {i, j};
                    positions.add(position);
                }
            }
        }
        return positions;
    }
}
