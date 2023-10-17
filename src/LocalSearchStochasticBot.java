import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchStochasticBot extends Bot{
    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst) {
        // create random move
        List<int[]> positions = generate_empty_cell(button);

        if (roundLeft == 0 || positions.size() == 0) {
            return new int[]{(int) Double.POSITIVE_INFINITY, (int) Double.POSITIVE_INFINITY};
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

            if (objectiveFunction(next, button, roundLeft) > objectiveFunction(current, button, roundLeft)) {
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
}
