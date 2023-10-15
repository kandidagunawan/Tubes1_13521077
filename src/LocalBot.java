import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class LocalBot extends Bot {
    @Override
    public int[] move(int[][] currentBoard, int round, boolean botFirst, int scoreX, int scoreO) {
        /* Variables for the new moves */
        int[] selectedMoves = new int[2];

        /* Generate all possible moves */
        List<int[]> possibleMoves = possibleAction(currentBoard);

        /* Get current state value */

        int maxX = countMaxAdjacent(possibleMoves, currentBoard, "X");
        int maxO = countMaxAdjacent(possibleMoves, currentBoard, "O");
        int currentStateValue = calculateObjectiveFunction(scoreX, scoreO, maxX, maxO);

        /* Get highest neighbor with timeout */
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = generateHighestNeighbor(currentBoard);
        long elapsedTime = System.currentTimeMillis() - startTime;

        int[] move = (int[]) result.get("move");
        int neighborStateValue = (int) result.get("score");

        Random random = new Random();
        int randomIndex = random.nextInt(possibleMoves.size());

        if (botFirst) {
            // Continue with the regular logic
            if(round == 0){
                return new int[]{};
            } else {
                if (!possibleMoves.isEmpty()) {
                    if (elapsedTime < 5000) {
                        if (currentStateValue < neighborStateValue) {
                            System.out.println("masuk neighbor gede");
                            return move;
                        } else {
                            System.out.println("masuk neighbor kecil");
                            return possibleMoves.get(randomIndex);
                        }
                    } else {
                        System.out.println("masuk timeout");
                        return possibleMoves.get(randomIndex);
                    }
                } else {
                    System.out.println("masa ga ada possible moves");
                    return new int[]{(int) (Math.random() * 8), (int) (Math.random() * 8)};
                }
            }
        } else {
            // Regular logic for the case when bot doesn't start first
            if (!possibleMoves.isEmpty()) {
                if (elapsedTime < 5000) {
                    if (currentStateValue < neighborStateValue) {
                        System.out.println("masuk neighbor gede");
                        return move;
                    } else {
                        System.out.println("masuk neighbor gkecil");
                        return possibleMoves.get(randomIndex);
                    }
                } else {
                    System.out.println("masuk timeout");
                    return possibleMoves.get(randomIndex);
                }
            } else {
                System.out.println("masa ga ada possible moves");
                return new int[]{(int) (Math.random() * 8), (int) (Math.random() * 8)};
            }
        }
    }

    public int countScore(int[][] currentBoard, String player) {
        int score = 0;
        int playerValue = (player.equals("X")) ? 1 : 2;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                if(currentBoard[i][j] == playerValue){
                    score++;
                }
            }
        }

        return score;
    }


    public int calculateObjectiveFunction(int scoreX, int scoreO, int maxX, int maxO){
        return scoreX - scoreO - maxX + maxO; //kuubah
    }

    public int countAdjacentPossibility(int[] currentPos, int[][] currentBoard, String playerTurn){
        int[][] adjacent = {
                {currentPos[0], currentPos[1] + 1}, // Up
                {currentPos[0], currentPos[1] - 1}, // Bottom
                {currentPos[0] + 1, currentPos[1]}, // Right
                {currentPos[0] - 1, currentPos[1]}  // Left
        };

        int ctr = 0;
        for(int[] adjPos : adjacent){
            int y = adjPos[0];
            int x = adjPos[1];
            if (x >= 0 && x < 8 && y >= 0 && y < 8){
                // Not out of scope
                if (playerTurn.equals("O")){
                    if (currentBoard[y][x] == 1){
                        ctr++;
                    }
                } else if (playerTurn.equals("X")){
                    if (currentBoard[y][x] == 2){
                        ctr++;
                    }
                }
            }
        }
        return ctr;
    }


    public int countMaxAdjacent(List<int[]> possibleActions, int[][] currentBoard, String player) {
        int totalMax = 0;

        for (int i = 0; i < possibleActions.size(); i++) {
            int[] currentPos = possibleActions.get(i);
            int[][] adjacent = {
                    {currentPos[0], currentPos[1] + 1}, // Right
                    {currentPos[0], currentPos[1] - 1}, // Left
                    {currentPos[0] + 1, currentPos[1]}, // Down
                    {currentPos[0] - 1, currentPos[1]}  // Up
            };

            int ctr = 0;
            for (int[] adjPos : adjacent) {
                int y = adjPos[0];
                int x = adjPos[1];
                if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    // Not out of scope
                    int playerValue = (player.equals("X")) ? 1 : 2;
                    if (currentBoard[y][x] == playerValue) {
                        ctr++;
                    }
                }
            }
            totalMax = Math.max(totalMax, ctr);
        }

        return totalMax;
    }




    public List<int[]> possibleAction(int[][] currentBoard){
        List<int[]> possibleMoves = new ArrayList<>();
        for(int i = 0; i<currentBoard.length; i++){
            for(int j = 0; j <currentBoard[0].length;j++){
                if(currentBoard[i][j] == 0){
                    int[] position = {i,j};
                    possibleMoves.add(position);
                }
            }
        }
        return possibleMoves;
    }

    public Map<String, Object> generateHighestNeighbor(int[][] currentBoard) {
        List<int[]> possibleMoves = possibleAction(currentBoard);
        int[] highestNeighborMove = null;
        int maxNeighborScore = Integer.MIN_VALUE;

        for (int i = 0; i < possibleMoves.size(); i++) {
            int[] currentMove = possibleMoves.get(i);

            // Generate all possible moves
            int[][] currentBoardNeighbor = generateNeighborBoard(currentMove, currentBoard, "O");
            List<int[]> possibleMovesNeighbor = possibleAction(currentBoardNeighbor);
            for (int k = 0; k < currentBoardNeighbor.length; k++) {
                for (int j = 0; j < currentBoardNeighbor[0].length; j++) {
                    System.out.print(currentBoardNeighbor[k][j] + " ");
                }
                System.out.println();  // Move to the next line after each row
            }
            // Get current state value
            int scoreX = countScore(currentBoardNeighbor, "X");
            int scoreO = countScore(currentBoardNeighbor, "O");
            System.out.println("Score bru X" + scoreX);
            System.out.println("Score bru O" + scoreO);
            int maxX = countMaxAdjacent(possibleMovesNeighbor, currentBoardNeighbor, "X");
            System.out.println("max X" + maxX);
            int maxO = countMaxAdjacent(possibleMovesNeighbor, currentBoardNeighbor, "O");
            System.out.println("max O" + maxO);
            int currentStateValue = calculateObjectiveFunction(scoreX, scoreO, maxX, maxO);

            // Update highestNeighborMove if a higher score is found
            if (currentStateValue > maxNeighborScore) {
                maxNeighborScore = currentStateValue;
                highestNeighborMove = currentMove.clone();
            }
        }

        // Create a map to store the result
        Map<String, Object> result = new HashMap<>();
        result.put("move", highestNeighborMove);
        result.put("score", maxNeighborScore);

        return result;
    }


    public int[][] generateNeighborBoard(int[] nextMove, int[][] currentBoard, String player) {
        int[][] neighborBoard = new int[currentBoard.length][currentBoard[0].length];

        for (int i = 0; i < currentBoard.length; i++) {
            System.arraycopy(currentBoard[i], 0, neighborBoard[i], 0, currentBoard[i].length);
        }

        int y = nextMove[0];
        int x = nextMove[1];

        int playerValue = (player.equals("X")) ? 1 : 2;

        neighborBoard[y][x] = playerValue;

        return neighborBoard;
    }

}
