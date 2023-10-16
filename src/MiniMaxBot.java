import javafx.scene.control.Button;



public class MiniMaxBot extends Bot {

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
    }

    public int minimax(Button[][] board, int depth, int max_depth, boolean isMax, String player, String opponent, int alpha, int beta, int[] currentPos) {
        if (depth == max_depth) {
            return objectiveFunction(currentPos, board);
        }

        int bestVal;

        if (isMax) {
            bestVal = Integer.MIN_VALUE;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].getText().equals("")) {
                        board[i][j].setText(player);
                        currentPos[0] = i;
                        currentPos[1] = j;
                        int value = minimax(board, depth + 1, max_depth, !isMax, player, opponent, alpha, beta, currentPos);
                        bestVal = Math.max(bestVal, value);
                        alpha = Math.max(alpha, bestVal);
                        board[i][j].setText("");

                        if (beta <= alpha) {
                            break; // Beta cut-off
                        }
                    }
                }
            }

            return bestVal;
        } else {
            bestVal = Integer.MAX_VALUE;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].getText().equals("")) {
                        board[i][j].setText(opponent);
                        currentPos[0] = i;
                        currentPos[1] = j;
                        int value = minimax(board, depth + 1, max_depth, !isMax, player, opponent, alpha, beta, currentPos);
                        bestVal = Math.min(bestVal, value);
                        beta = Math.min(beta, bestVal);
                        board[i][j].setText("");

                        if (beta <= alpha) {
                            break; // Alpha cut-off
                        }
                    }
                }
            }
            return bestVal;
        }
    }

    public int[] move(Button[][]board, int max_depth, String player, String opponent, Boolean isMax, int alpha, int beta, int[] currentPos){
        int bestVal;
        if(player.equals('O')){
            bestVal = Integer.MIN_VALUE;
        } else{
            bestVal = Integer.MAX_VALUE;
        }
        int row = -1;
        int col = -1;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].getText() == ""){
                    board[i][j].setText(player);
                    int value = minimax(board, 0, max_depth, isMax, player, opponent, alpha, beta, currentPos);
                    board[i][j].setText("");
                    if(player.equals('O')){
                        if(value > bestVal){
                            row = i;
                            col = j;
                            bestVal = value;
                        }
                    }else{
                        if(value < bestVal){
                            row = i;
                            col = j;
                            bestVal = value;
                        }
                    }

                }
            }
        }
        int[] move = new int[2];
        move[0] = row;
        move[1] = col;
        return move;
    }
}

