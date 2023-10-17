public class resultMinMax {
    private int[] nextPosition;
    private double value;

    public resultMinMax(int[] nextPosition, double value) {
        this.nextPosition = nextPosition;
        this.value = value;
    }

    public int[] getNextPosition() {
        return nextPosition;
    }
    public double getValue() {
        return value;
    }
}
