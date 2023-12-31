import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class MinimaxAlphaBetaBot extends Bot {
    public Button[][] copyButton(Button[][] buttons){
        Button[][] secButton = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                secButton[i][j] = new Button();
                secButton[i][j].setText(buttons[i][j].getText());
            }
        }
        return secButton;
    }
    @Override
    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst, String botChar) {
        return alphaBetaSearch(button, roundLeft, isBotFirst, botChar);
    }
    public int[] alphaBetaSearch(Button[][] button, int roundLeft, boolean isBotFirst, String botChar) {
        Button[][] dumbButton = copyButton(button);
        resultMinMax res = maxValue(dumbButton, roundLeft, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, isBotFirst, botChar);
        return res.getNextPosition();
    }
    public resultMinMax maxValue(Button[][] button, int roundLeft, double alpha, double beta, boolean isBotFirst, String botChar) {
        List<int[]> emptyPos = generate_empty_cell(button);
        if (roundLeft == 0 || emptyPos.size() == 0) {
            return new resultMinMax(null, objectiveFunction(null, button, roundLeft, botChar));
        }

        int newRoundLeft = roundLeft;
        if (!isBotFirst) newRoundLeft--;

        double currentValue = Double.NEGATIVE_INFINITY;
        int[] currentMove = null;
        String oppChar;
        if (botChar.equals("O")) oppChar = "X";
        else oppChar = "O";

        long startTime = System.currentTimeMillis();
        for (int[] possiblePos : emptyPos) {
            List<int[]> changeLabel = changeAdjacent(possiblePos, button, botChar);

            // Change label button
            button[possiblePos[0]][possiblePos[1]].setText(botChar);
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText(botChar);
                }
            }
            resultMinMax result = minValue(button, newRoundLeft, alpha, beta, isBotFirst, oppChar);

            // Undo label changed
            button[possiblePos[0]][possiblePos[1]].setText("");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText(oppChar);
                }
            }

            double nextValue = result.getValue();
            if (nextValue > currentValue) {
                currentValue = nextValue;
                currentMove = possiblePos;
                alpha = Math.max(alpha, currentValue);
            }
            if (currentValue >= beta) return new resultMinMax(currentMove, currentValue);
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 5000){
                System.out.println("System time out");
                return getFallbackMove(button, botChar, botChar);
                /*
                if (currentMove == null){
                    return getRandomMove(button);
                } else {
                    return new resultMinMax(currentMove, currentValue);
                }
                */
            }
        }
        return new resultMinMax(currentMove, currentValue);
    }

    public resultMinMax minValue(Button[][] button, int roundLeft, double alpha, double beta, boolean isBotFirst, String oppChar) {
        List<int[]> emptyPos = generate_empty_cell(button);
        String botChar;
        if (oppChar.equals("O")) botChar = "X";
        else botChar = "O";

        if (roundLeft == 0 || emptyPos.size() == 0) {
            return new resultMinMax(null, objectiveFunction(null, button, roundLeft, botChar));
        }

        int newRoundLeft = roundLeft;
        if (isBotFirst) newRoundLeft--;

        double currentValue = Double.POSITIVE_INFINITY;
        int[] currentMove = null;

        long startTime = System.currentTimeMillis();
        for (int[] possiblePos : emptyPos) {
            List<int[]> changeLabel = changeAdjacent(possiblePos, button, oppChar);

            // Change label button
            button[possiblePos[0]][possiblePos[1]].setText(oppChar);
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText(oppChar);
                }
            }

            resultMinMax result = maxValue(button, newRoundLeft, alpha, beta, isBotFirst, botChar);
            // Undo label changed
            button[possiblePos[0]][possiblePos[1]].setText("");
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText(botChar);
                }
            }

            double nextValue = result.getValue();
            if (nextValue < currentValue) {
                currentValue = nextValue;
                currentMove = possiblePos;
                beta = Math.min(beta, currentValue);
            }
            if (currentValue <= alpha) return new resultMinMax(currentMove, currentValue);

            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 5000){
                System.out.println("System time out");
                return getFallbackMove(button, oppChar, botChar);
                /*
                if (currentMove == null){
                    return getRandomMove(button);
                } else {
                    return new resultMinMax(currentMove, currentValue);
                }
                */
            }
        }
        return new resultMinMax(currentMove, currentValue);
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
    public resultMinMax getRandomMove(Button[][] button, String botChar){
        int i,j;
        do {
            i = (int) (Math.random()*8);
            j = (int) (Math.random()*8);
        } while (button[i][j].getText().equals(""));
        int[] move = {i, j};
        int value = objectiveFunction(move, button, 0, botChar);
        return new resultMinMax(move, value);
    }
    public resultMinMax getFallbackMove(Button[][] button, String playerTurn, String botChar){
        List<int[]> emptyPos = generate_empty_cell(button);
        double maxVal = Double.NEGATIVE_INFINITY;
        int[] maxPosition = {-1, -1};

        for (int[] possiblePos : emptyPos) {
            List<int[]> changeLabel = changeAdjacent(possiblePos, button, playerTurn);

            // Change label button
            button[possiblePos[0]][possiblePos[1]].setText(playerTurn);
            if (changeLabel.size() > 0){
                for(int[] adjPos:changeLabel){
                    button[adjPos[0]][adjPos[1]].setText(playerTurn);
                }
            }

            int value = objectiveFunction(possiblePos, button, 0, botChar);
            if (value > maxVal) {
                maxVal = value;
                maxPosition = possiblePos;
            }

            // Undo label changed
            button[possiblePos[0]][possiblePos[1]].setText("");
            if (changeLabel.size() > 0) {
                for (int[] adjPos: changeLabel) {
                    if (playerTurn.equals("O")){
                        button[adjPos[0]][adjPos[1]].setText("X");
                    } else {
                        button[adjPos[0]][adjPos[1]].setText("O");
                    }
                }
            }
        }
        return new resultMinMax(maxPosition, maxVal);
    }
}
