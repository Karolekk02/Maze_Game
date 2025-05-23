import java.util.Random;

public class MazeModel {
    private int width;
    private int height;
    private int[][] verticalWalls;
    private int[][] horizontalWalls;
    private int travelerX;
    private int travelerY;

    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        generateMaze();
    }

    private void generateMaze() {
        verticalWalls = new int[height][width + 1];
        horizontalWalls = new int[height + 1][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x <= width; x++) {
                verticalWalls[y][x] = 1;
            }
        }

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x < width; x++) {
                horizontalWalls[y][x] = 1;
            }
        }

        generateTree(0, 0, new boolean[height][width]);

        travelerX = 0;
        travelerY = 0;
    }

    public void blockRandomWall() {
        Random rand = new Random();
        boolean isVertical = rand.nextBoolean();
    
        if (isVertical) {
            int y = rand.nextInt(height);
            int x = rand.nextInt(1, width);
            if (verticalWalls[y][x] == 0) {
                verticalWalls[y][x] = 2;
            }
        } else {
            int y = rand.nextInt(1, height);
            int x = rand.nextInt(width);
            if (horizontalWalls[y][x] == 0) {
                horizontalWalls[y][x] = 2;
            }
        }
    }
    

    private void generateTree(int x, int y, boolean[][] visited) {
        visited[y][x] = true;
        int[] directions = {0, 1, 2, 3}; // 0: gora, 1: dol, 2: lewo, 3: prawo
        Random rand = new Random();

        for (int i = 0; i < directions.length; i++) {
            int j = rand.nextInt(directions.length);
            int temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        for (int dir : directions) {
            int nx = x + (dir == 3 ? 1 : dir == 2 ? -1 : 0);
            int ny = y + (dir == 1 ? 1 : dir == 0 ? -1 : 0);

            if (nx >= 0 && ny >= 0 && nx < width && ny < height && !visited[ny][nx]) {
                if (dir == 0) horizontalWalls[y][x] = 0; // gora
                if (dir == 1) horizontalWalls[y + 1][x] = 0; // dol
                if (dir == 2) verticalWalls[y][x] = 0; // lewo
                if (dir == 3) verticalWalls[y][x + 1] = 0; // prawo

                generateTree(nx, ny, visited);
            }
        }
    }

    public boolean canMove(int dx, int dy) {
        if (dy == -1) {
            return horizontalWalls[travelerY][travelerX] == 0;
        }
        if (dy == 1) {
            return horizontalWalls[travelerY + 1][travelerX] == 0;
        }
        if (dx == -1) {
            return verticalWalls[travelerY][travelerX] == 0;
        }
        if (dx == 1) {
            return verticalWalls[travelerY][travelerX + 1] == 0;
        }
        return false;
    }

    public boolean moveTraveler(int dx, int dy) {
        travelerX += dx;
        travelerY += dy;
        return true;
    }

    public boolean isAtExit() {
        return travelerX == width - 1 && travelerY == height - 1;
    }

    public int getTravelerX() {
        return travelerX;
    }

    public int getTravelerY() {
        return travelerY;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[][] getVerticalWalls() {
        return verticalWalls;
    }

    public int[][] getHorizontalWalls() {
        return horizontalWalls;
    }
}
