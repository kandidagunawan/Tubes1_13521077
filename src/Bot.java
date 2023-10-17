import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst, String botChar) {
        // create random move
        return new int[] { (int) (Math.random() * 8), (int) (Math.random() * 8) };
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

    public int objectiveFunction(int[] currentPost, Button[][] button, int roundLeft, String botChar) {
        if (roundLeft > 0) {
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

            int[][] positions = {{currentPost[0], currentPost[1] + 1}, {currentPost[0] - 1, currentPost[1]}, {currentPost[0] + 1, currentPost[1]}, {currentPost[0], currentPost[1] - 1}};
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
                    } else if (button[i][j].getText().equals("")) {
                        nilaiMin--;
                    }
                }
            }

            int result = nilaiAwal * count + nilaiMin;

            return result;
        } else {
            String oppChar;
            if(botChar.equals("O")) oppChar = "X";
            else oppChar = "O";
            return countPlayerScore(button, botChar) - countPlayerScore(button, oppChar);
        }
    }
    public int countPlayerScore(Button[][] button, String player){
        int score = 0;
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (button[i][j].getText().equals(player)){
                    score++;
                }
            }
        }
        return score;
    }
}