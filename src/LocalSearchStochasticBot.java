import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchStochasticBot extends Bot{

    public int objectiveFunction(int[] currentPost, Button[][] button) {
        int nilaiAwal = 4;
        int nilaiMin = 0;
        int count = 1;

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                int[][] positions = {{i, j + 1}, {i - 1, j}, {i + 1, j}, {i, j - 1}};
                if (button[i][j].getText().equals("O")) {
                    count++;
                    for (int[] post : positions) {
                        int x = post[0];
                        int y = post[1];
                        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                            if (x != currentPost[0] && y != currentPost[1] && button[x][y].getText().equals("")) {
                                nilaiMin--;
                            }
                        }
                    }
                }
            }
        }

        int[][] positions = {{currentPost[0], currentPost[1] + 1}, {currentPost[0]- 1, currentPost[1]}, {currentPost[0] + 1, currentPost[1]}, {currentPost[0], currentPost[1] - 1}};
        for (int[] post : positions) {
            int i = post[0];
            int j = post[1];
            if (i >= 0 && i <= 7 && j >= 0 && j <= 7) {
                if (button[i][j].getText().equals("X")) {
                    count++;
                    int[][] positions2 = {{i, j + 1}, {i - 1, j}, {i + 1, j}, {i, j - 1}};
                    for (int[] post2 : positions2) {
                        int x = post2[0];
                        int y = post2[1];
                        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                            if (x != currentPost[0] && y != currentPost[1] && button[x][y].getText().equals("")) {
                                nilaiMin--;
                            }
                        }
                    }
                }
                else if (button[i][j].getText().equals("")) {
                    nilaiMin--;
                }
            }
        }

        int result = nilaiAwal * count + nilaiMin;

        return result;
    }

    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst) {
        // create random move
        List<int[]> positions = generate_empty_cell(button);

        if (roundLeft == 0 || positions.size() == 0) {
            return new int[]{};
        }

        long startTime = System.currentTimeMillis();
        int[] current = new int[2];
        int[] next = new int[2];

        Random random = new Random();
        int randomIdx = random.nextInt(0, positions.size());
        current[0] = positions.get(randomIdx)[0];
        current[1] = positions.get(randomIdx)[1];

        int maxIteration = positions.size();
        int iteration = 0;
        while (iteration < maxIteration) {
            int randomIdx2 = random.nextInt(0, positions.size());
            next[0] = positions.get(randomIdx2)[0];
            next[1] = positions.get(randomIdx2)[1];

            if (objectiveFunction(next, button) > objectiveFunction(current, button)) {
                current[0] = next[0];
                current[1] = next[1];
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 5000) {
                System.out.println("System time out");
                return new int[]{current[0], current[1]};
            }
            iteration++;
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
