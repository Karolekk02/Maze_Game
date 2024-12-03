import java.util.Random;

public class MazeModel {
    private int width;
    private int height;
    private boolean[][] verticalWalls;   // Pionowe ściany
    private boolean[][] horizontalWalls; // Poziome ściany
    private int travelerX;
    private int travelerY;

    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        generateMaze();
    }

    private void generateMaze() {
        // Tworzenie pełnych ścian
        verticalWalls = new boolean[height][width + 1];
        horizontalWalls = new boolean[height + 1][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x <= width; x++) {
                verticalWalls[y][x] = true;
            }
        }

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x < width; x++) {
                horizontalWalls[y][x] = true;
            }
        }

        // Algorytm generowania drzewa (DFS)
        generateTree(0, 0, new boolean[height][width]);

        // Początkowe położenie podróżnika
        travelerX = 0;
        travelerY = 0;
    }

    public void blockRandomWall() {
        Random rand = new Random();
        boolean isVertical = rand.nextBoolean();
    
        if (isVertical) {
            int y = rand.nextInt(height);
            int x = rand.nextInt(width + 1);
            verticalWalls[y][x] = true; // Zamurowanie pionowej ściany
        } else {
            int y = rand.nextInt(height + 1);
            int x = rand.nextInt(width);
            horizontalWalls[y][x] = true; // Zamurowanie poziomej ściany
        }
    }
    

    private void generateTree(int x, int y, boolean[][] visited) {
        visited[y][x] = true;
        int[] directions = {0, 1, 2, 3}; // 0: góra, 1: dół, 2: lewo, 3: prawo
        Random rand = new Random();

        // Losowa kolejność kierunków
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
                // Usuwanie odpowiedniej ściany
                if (dir == 0) horizontalWalls[y][x] = false; // Góra
                if (dir == 1) horizontalWalls[y + 1][x] = false; // Dół
                if (dir == 2) verticalWalls[y][x] = false; // Lewo
                if (dir == 3) verticalWalls[y][x + 1] = false; // Prawo

                generateTree(nx, ny, visited);
            }
        }
    }

    public boolean moveTraveler(int dx, int dy) {
        int newX = travelerX + dx;
        int newY = travelerY + dy;

        if (isValidMove(newX, newY)) {
            travelerX = newX;
            travelerY = newY;
            return true;
        }
        return false;
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return false;

        // Sprawdzanie ścian
        if (x > travelerX && verticalWalls[travelerY][travelerX + 1]) return false; // Prawo
        if (x < travelerX && verticalWalls[travelerY][travelerX]) return false;     // Lewo
        if (y > travelerY && horizontalWalls[travelerY + 1][travelerX]) return false; // Dół
        if (y < travelerY && horizontalWalls[travelerY][travelerX]) return false;    // Góra

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

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }
}
