import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class MinesweeperGUI {
    private final MinesweeperGame game;
    private int size;
    private int squareRows;
    private int squareCols;
    private JFrame frame;

    private JLayeredPane gameSquare;
    private JLabel[][] backSquare;
    private JButton[][] frontSquare;

    private JLabel mineCounter;
    private JLabel difficulty;
    private JButton toggleFlag;

    private ImageIcon frontCovered;
    private ImageIcon frontFlagged;
    private ImageIcon backMine;
    private ImageIcon revealedMine;
    private ImageIcon[] backNumbers;
    private Map<JButton,int[]> buttonCoordinates;


// constructor
    public MinesweeperGUI(MinesweeperGame game) {
        this.game = game;
        size = game.getDiff();
        squareRows = game.getRows() - 2;
        squareCols = game.getCols() - 2;
        frame = new JFrame();
        gameSquare = new JLayeredPane();

        backSquare = new JLabel[squareRows][squareCols];
        frontSquare = new JButton[squareRows][squareCols];
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                backSquare[i][j] = new JLabel();
                frontSquare[i][j] = new JButton();
            }
        }

        mineCounter = new JLabel("MINES LEFT: " + game.getMines());
        switch(size) {
            case 0: {
                difficulty = new JLabel("EASY");
                difficulty.setForeground(Color.GREEN);
                break;
            }
            case 1: {
                difficulty = new JLabel("MEDIUM");
                difficulty.setForeground(Color.YELLOW);
                break;
            }
            default: {
                difficulty = new JLabel("HARD");
                difficulty.setForeground(Color.RED);
            }
        }
        toggleFlag = new JButton("MODE: CLICK");

        frontCovered = new ImageIcon("cover.png");
        frontFlagged = new ImageIcon("flagged.png");
        backMine = new ImageIcon("mine.png");
        revealedMine = new ImageIcon("mined.png");

        backNumbers = new ImageIcon[9];
        for (int i = 0; i < 9; i++) {
            String fileName = i + ".png";
            backNumbers[i] = new ImageIcon(fileName);
        }
        constructSquares();
        buttonCoordinates = new HashMap<>();
        createMap();
    }
    private void constructSquares() {
        game.startGame();
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                int backGridRef = game.getBackGrid()[i+1][j+1];
                if (backGridRef == -1) {
                    backSquare[i][j].setIcon(backMine);
                } else if (backGridRef > -1) {
                    backSquare[i][j].setIcon(backNumbers[backGridRef]);
                }
                frontSquare[i][j].setIcon(frontCovered);
            }
        }
    }
    private void createMap() {
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                int[] coordinates = {i + 1, j + 1};
                buttonCoordinates.put(frontSquare[i][j], coordinates);
            }
        }
    }


// GUI setup
    public void setUpGUI() {
        Container c = frame.getContentPane();
        c.setLayout(null);

        setUpComponents(c);
        setUpGameState();

        frame.setSize(470 + 160*size, 510 + 160*size);
        frame.setTitle("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private void setUpComponents(Container c) {
        gameSquare.setBounds(25, 50, 400 + 160*size, 400 + 160*size);
        gameSquare.setLayout(null);
        c.add(gameSquare);

        mineCounter.setBounds(25, 15, 100, 25);
        c.add(mineCounter);

        difficulty.setBounds(195+80*size, 15, 100, 25);
        c.add(difficulty);

        toggleFlag.setBounds(300+160*size, 15, 125, 25);
        c.add(toggleFlag);
    }
    private void setUpGameState() {
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                gameSquare.add(backSquare[i][j]);
                gameSquare.add(frontSquare[i][j]);
                backSquare[i][j].setBounds(0 + 40*j, 0 + 40*i, 40, 40);
                frontSquare[i][j].setBounds(0 + 40*j, 0 + 40*i, 40, 40);
                gameSquare.setLayer(backSquare[i][j], 0);
                gameSquare.setLayer(frontSquare[i][j], 1);
            }
        }
    }


// ButtonListener setup
    public void setUpButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent p) {
                Object button = p.getSource();
                if (button == toggleFlag) {
                    if (toggleFlag.getText().equals("MODE: CLICK")) {
                        toggleFlag.setText("MODE: FLAG");
                    } else {
                        toggleFlag.setText("MODE: CLICK");
                    }
                } else if (true) {
                    for (int i = 0; i < squareRows; i++) {
                        for (int j = 0; j < squareCols; j++) {
                            if (button == frontSquare[i][j]) {
                                if (toggleFlag.getText().equals("MODE: CLICK")) {
                                    game.clicked(buttonCoordinates.get(frontSquare[i][j])[0], buttonCoordinates.get(frontSquare[i][j])[1]);
                                    game.checkWinner();
                                    if (game.hasLoser()) {
                                        backSquare[i][j].setIcon(revealedMine);
                                        finishedGame();
                                        mineCounter.setText("You lose.");
                                    } else if (game.hasWinner()) {
                                        finishedGame();
                                        mineCounter.setText("You win.");
                                    } else {
                                        update();
                                    }
                                } else if (toggleFlag.getText().equals("MODE: FLAG")) {
                                    game.flagged(buttonCoordinates.get(frontSquare[i][j])[0], buttonCoordinates.get(frontSquare[i][j])[1]);
                                    update();
                                }
                            }
                        }
                    }
                }
            }
        };
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                frontSquare[i][j].addActionListener(buttonListener);
            }
        }
        toggleFlag.addActionListener(buttonListener);
    }


// update
    private void update() {
        for (Component c : gameSquare.getComponents()) {
            if (c instanceof JButton) {
                gameSquare.remove(c);
            }
        }
        gameSquare.repaint();
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                if (game.getFrontGrid()[i+1][j+1] == '-') {
                    gameSquare.add(frontSquare[i][j]);
                    frontSquare[i][j].setIcon(frontCovered);
                    frontSquare[i][j].setBounds(0 + 40*j, 0 + 40*i, 40, 40);
                    gameSquare.setLayer(frontSquare[i][j], 1);
                } else if (game.getFrontGrid()[i+1][j+1] == 'F') {
                    gameSquare.add(frontSquare[i][j]);
                    frontSquare[i][j].setIcon(frontFlagged);
                    frontSquare[i][j].setBounds(0 + 40*j, 0 + 40*i, 40, 40);
                    gameSquare.setLayer(frontSquare[i][j], 1);
                }
            }
        }
        mineCounter.setText("MINES LEFT: " + game.getMines());
    }
    private void finishedGame() {
        for (int i = 0; i < squareRows; i++) {
            for (int j = 0; j < squareCols; j++) {
                if (game.getFrontGrid()[i+1][j+1] == '-' && game.getBackGrid()[i+1][j+1] == -1) {
                    gameSquare.remove(frontSquare[i][j]);
                    gameSquare.repaint();
                }
            }
        }
        for (Component c : gameSquare.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(false);
            }
        }
    }
}