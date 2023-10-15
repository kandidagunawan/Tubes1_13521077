import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    public int calculateObjectiveFunction(Button[][] button) {
        return countPlayerScore(button, "O") - countPlayerScore(button, "X");
        // Belum implement max adjacent
    }
    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst) {
        System.out.println("Masuk move");
        return alphaBetaSearch(button, roundLeft, isBotFirst);
    }
    public int[] alphaBetaSearch(Button[][] button, int roundLeft, boolean isBotFirst) {
        System.out.println("Masuk alpha beta");
        resultMinMax res = maxValue(button, roundLeft, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, isBotFirst);
        return res.getNextPosition();
    }
    public resultMinMax maxValue(Button[][] button, int roundLeft, double alpha, double beta, boolean isBotFirst) {
        System.out.println("Max " + roundLeft);
        List<int[]> emptyPos = possibleAction(button);
        if (roundLeft == 0 || emptyPos.size() == 0) {
            System.out.println("--- Daun ---");
            return new resultMinMax(null, calculateObjectiveFunction(button));
        }

        int newRoundLeft = roundLeft;
        if (!isBotFirst) newRoundLeft--;

        double currentValue = Double.NEGATIVE_INFINITY;
        int[] currentMove = null;

        for (int[] possiblePos : emptyPos) {
            List<int[]> changeLabel = changeAdjacent(possiblePos, button, "O");

            // Change label button
            button[possiblePos[0]][possiblePos[1]].setText("O");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText("O");
                }
            }

            System.out.println(possiblePos[0] + ", " + possiblePos[1] + " = " + countPlayerScore(button, "X") + ", " + countPlayerScore(button, "O"));

            resultMinMax result = minValue(button, newRoundLeft, alpha, beta, isBotFirst);
            System.out.println("Value = " + result.getValue());
            double nextValue = result.getValue();
            if (nextValue > currentValue) {
                currentValue = nextValue;
                currentMove = possiblePos;
                alpha = Math.max(alpha, currentValue);
            }
            if (currentValue >= beta) return new resultMinMax(currentMove, currentValue);

            // Undo label changed
            button[possiblePos[0]][possiblePos[1]].setText("");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText("X");
                }
            }
            System.out.println("Choosen " + currentMove[0] + ", " + currentMove[1] + " Val " + currentValue);
        }
        return new resultMinMax(currentMove, currentValue);
    }

    public resultMinMax minValue(Button[][] button, int roundLeft, double alpha, double beta, boolean isBotFirst) {
        System.out.println("Min " + roundLeft);
        List<int[]> emptyPos = possibleAction(button);
        if (roundLeft == 0 || emptyPos.size() == 0) {
            System.out.println("--- Daun ---");
            return new resultMinMax(null, calculateObjectiveFunction(button));
        }

        int newRoundLeft = roundLeft;
        if (isBotFirst) newRoundLeft--;

        double currentValue = Double.POSITIVE_INFINITY;
        int[] currentMove = null;

        for (int[] possiblePos : emptyPos) {
            List<int[]> changeLabel = changeAdjacent(possiblePos, button, "X");

            // Change label button
            button[possiblePos[0]][possiblePos[1]].setText("X");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText("X");
                }
            }

            System.out.println(possiblePos[0] + ", " + possiblePos[1] + " = " + countPlayerScore(button, "X") + ", " + countPlayerScore(button, "O"));
            resultMinMax result = maxValue(button, newRoundLeft, alpha, beta, isBotFirst);
            System.out.println("Value = " + result.getValue());
            double nextValue = result.getValue();
            if (nextValue < currentValue) {
                currentValue = nextValue;
                currentMove = possiblePos;
                beta = Math.min(beta, currentValue);
            }
            if (currentValue <= alpha) return new resultMinMax(currentMove, currentValue);

            // Undo label changed
            button[possiblePos[0]][possiblePos[1]].setText("");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText("O");
                }
            }
            System.out.println("Choosen " + currentMove[0] + ", " + currentMove[1] + " Val " + currentValue);
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
    public List<int[]> changeAdjacent(int[] currentPos, Button[][] button, String playerTurn) {
        // Adjacent Position
        int[][] adjacent = {
                {currentPos[0] - 1, currentPos[1]}, // Up
                {currentPos[0] + 1, currentPos[1]}, // Bottom
                {currentPos[0], currentPos[1] + 1}, // Right
                {currentPos[0], currentPos[1] - 1}  // Left
        };
        List<int[]> labelChanged = new ArrayList<>();

        for (int[] adjPos : adjacent) {
            int x = adjPos[0];
            int y = adjPos[1];
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                // Not out of scope
                if (playerTurn.equals("O")) {
                    if (button[x][y].getText().equals("X")) {
                        int[] pos = {x, y};
                        labelChanged.add(pos);
                    }
                } else if (playerTurn.equals("X")) {
                    if (button[x][y].getText().equals("O")) {
                        int[] pos = {x, y};
                        labelChanged.add(pos);
                    }
                }
            }
        }
        return labelChanged;
    }
}
