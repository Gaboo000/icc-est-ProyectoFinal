public class LaberintoModel {
    private int size;
    private boolean[][] blocked;

    public LaberintoModel(int size) {
        this.size = size;
        blocked = new boolean[size][size];
    }

    public void setSize(int newSize) {
        this.size = newSize;
        blocked = new boolean[newSize][newSize];
    }

    public int getSize() {
        return size;
    }

    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    public void toggleBlock(int x, int y) {
        blocked[x][y] = !blocked[x][y];
    }

    public boolean[][] getBlockedMatrix() {
        return blocked;
    }
}