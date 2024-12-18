import java.util.Random;

public class MinesweeperGame {
    private int diff; // 0, 1, 2
    private int rows;
    private int cols;
    private int mines;
    private int[][] backGrid;
    private char[][] frontGrid;
    private boolean winner;
    private boolean loser;
    private Random r;


// constructor
    public MinesweeperGame(int diff) {
        this.diff = diff;
        rows = 10 + 4*diff + 2;
        cols = 10 + 4*diff + 2;
        switch(diff) {
            case 0: 
                mines = 10;
                break;
            case 1: 
                mines = 25;
                break;
            default: 
                mines = 40;
        }
        backGrid = new int[rows][cols];
        frontGrid = new char[rows][cols];
        winner = false;
        loser = false;
        r = new Random();
    }


// initialize
    public void startGame() {
        initializeGrids();
        mineAdding();
    }
    private void initializeGrids() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean inBorder = (i == 0 || i == rows - 1) || (j == 0 || j == cols - 1);
                if (!inBorder) {
                    backGrid[i][j] = 0;
                    frontGrid[i][j] = '-';
                } else if (inBorder) {
                    backGrid[i][j] = -2;
                    frontGrid[i][j] = '+';
                }
            }
        }
    }
    private void mineAdding() {
        for (int i = 0; i < mines; i++) {
            int targetRow = r.nextInt(rows);
            int targetCol = r.nextInt(cols);
            if (backGrid[targetRow][targetCol] >= 0) {
                backGrid[targetRow][targetCol] = -1;
                addNumbers(targetRow, targetCol);
            } else {
                i--;
            }
        }
    }
    private void addNumbers(int targetRow, int targetCol) {
        for (int i = targetRow - 1; i <= targetRow + 1; i++) {
            for (int j = targetCol - 1; j <= targetCol + 1; j++) {
                if (backGrid[i][j] >= 0) {
                    backGrid[i][j]++;
                }
            }
        }
    }
    

// accessors
    public int getDiff() {
        return diff;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public int getMines() {
        return mines;
    }
    public int[][] getBackGrid() {
        return backGrid;
    }
    public char[][] getFrontGrid() {
        return frontGrid;
    }
    public boolean hasWinner() {
        return winner;
    }
    public boolean hasLoser() {
        return loser;
    }


// mutators
    public void clicked(int x, int y) {
        if (backGrid[x][y] > 0) {
            frontGrid[x][y] = (char) (backGrid[x][y] + '0');
        } else if (backGrid[x][y] == -1) {
            loser = true;
        } else if (backGrid[x][y] == 0) {
            frontGrid[x][y] = ' ';
            clickSpread(x, y);
        }
    }
    public void flagged(int x, int y) {
        if (frontGrid[x][y] == '-') {
            frontGrid[x][y] = 'F';
            mines--;
        } else if (frontGrid[x][y] == 'F') {
            frontGrid[x][y] = '-';
            mines++;
        }
    }
    private void clickSpread(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (backGrid[i][j] > 0 && frontGrid[i][j] == '-' && !(frontGrid[i][j] == 'F')) {
                    frontGrid[i][j] = (char) (backGrid[x][y] + '0');
                } else if (backGrid[i][j] == 0 && frontGrid[i][j] == '-' && !(frontGrid[i][j] == 'F')) {
                    frontGrid[x][y] = ' ';
                    clickSpread(i, j);
                }
            }
        }
    }


// additional
    public void checkWinner() {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (backGrid[i][j] > -1 && frontGrid[i][j] == '-') {
                    return;
                }
            }
        }
        winner = true;
    }
}

/*
 * frontGrid
 * '-' : covered
 * 'F' : flagged
 * ' ' : revealed space
 * '#' : revealed number
 * '+' : border
 * 
 * backGrid
 * 0 : empty space
 * # : number
 * -1 : mine
 * -2 : border
 */