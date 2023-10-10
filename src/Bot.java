import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    public int[] countScore(int[] currentPos, Button[][] button, String playerTurn, int prevPlayerOScore, int prevPlayerXScore){
        int addScore = countAdjacentPossibility(currentPos, button, playerTurn);
        if (playerTurn.equals("O")){
            // score[0] : update player O score
            // score[1] : update player X score
            return new int[]{prevPlayerOScore + addScore + 1, prevPlayerXScore - addScore};
        } else {
            // score[0] : update player O score
            // score[1] : update player X score
            return new int[]{prevPlayerOScore - addScore, prevPlayerXScore + addScore + 1};
        }
    }
    public int calculateObjectiveFunction(int playerOScore, int playerXScore) {
        return playerOScore - playerXScore;
        // Belum implement max adjacent
    }
    public int countAdjacentPossibility(int[] currentPos, Button[][] button, String playerTurn){
        // Adjacent Position
        int[][] adjacent = {
                {currentPos[0], currentPos[1] + 1}, // Up
                {currentPos[0], currentPos[1] - 1}, // Bottom
                {currentPos[0] + 1, currentPos[1]}, // Right
                {currentPos[0] - 1, currentPos[1]}  // Left
        };

        int ctr = 0;
        for (int[] adjPos : adjacent){
            int y = adjPos[0];
            int x = adjPos[1];
            if (x >= 0 && x < 8 && y >= 0 && y < 8){
                // Not out of scope
                if (playerTurn.equals("O")){
                    if (button[y][x].getText().equals("X")){
                        ctr++;
                    }
                } else if (playerTurn.equals("X")){
                    if (button[y][x].getText().equals("O")){
                        ctr++;
                    }
                }
            }
        }
        return ctr;
    }
    public int[] move(Button[][] button, int playerOScore, int playerXScore, boolean isBotFirst, int roundLeft) {
        return alphaBetaSearch(button, isBotFirst, roundLeft, playerOScore, playerXScore);
    }
    public int[] alphaBetaSearch(Button[][] button, boolean isBotFirst, int roundLeft, int playerOScore, int playerXScore) {
        resultMinMax res;
        if (isBotFirst){
            res = maxValue(button, roundLeft, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, playerOScore, playerXScore);
        } else { // Player First
            res = minValue(button, roundLeft, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, playerOScore, playerXScore);
        }
        return res.getNextPosition();
    }
    public resultMinMax maxValue(Button[][] button, int roundLeft, double alpha, double beta, int playerOScore, int playerXScore){
        if (roundLeft == 0) return new resultMinMax(null, calculateObjectiveFunction(playerOScore, playerXScore));
        List<int[]> emptyPos = possibleAction(button);
        double currentValue = Double.NEGATIVE_INFINITY;
        int[] currentMove = null;

        for (int[] possiblePos : emptyPos){
            button[possiblePos[0]][possiblePos[1]].setText("O");
            int[] updateScore = countScore(possiblePos, button, "O", playerOScore, playerXScore);
            playerOScore = updateScore[0];
            playerXScore = updateScore[1];

            resultMinMax result = minValue(button, roundLeft--, alpha, beta, playerOScore, playerXScore);
            double nextValue = result.getValue();
            if (nextValue > currentValue){
                currentValue = nextValue;
                currentMove = possiblePos;
                alpha = Math.max(alpha, currentValue);
            }
            if (currentValue >= beta) return new resultMinMax(currentMove, currentValue);

        }
        return new resultMinMax(currentMove, currentValue);
    }
    public resultMinMax minValue(Button[][] button, int roundLeft, double alpha, double beta, int playerOScore, int playerXScore){
        if (roundLeft == 0) return new resultMinMax(null, calculateObjectiveFunction(playerOScore, playerXScore));
        List<int[]> emptyPos = possibleAction(button);
        double currentValue = Double.POSITIVE_INFINITY;
        int[] currentMove = null;

        for (int[] possiblePos : emptyPos){
            button[possiblePos[0]][possiblePos[1]].setText("X");
            int[] updateScore = countScore(possiblePos, button, "X", playerOScore, playerXScore);
            playerOScore = updateScore[0];
            playerXScore = updateScore[1];

            resultMinMax result = maxValue(button, roundLeft--, alpha, beta, playerOScore, playerXScore);
            double nextValue = result.getValue();
            if (nextValue < currentValue){
                currentValue = nextValue;
                currentMove = possiblePos;
                beta = Math.min(beta, currentValue);
            }
            if (currentValue <= alpha) return new resultMinMax(currentMove, currentValue);

        }
        return new resultMinMax(currentMove, currentValue);
    }
    public List<int[]> possibleAction(Button[][] button){
        List<int[]> emptyPos = new ArrayList<>();

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (button[i][j].getText().equals("")){
                    int[] position = {i,j};
                    emptyPos.add(position);
                }
            }
        }
        return emptyPos;
    }
}
