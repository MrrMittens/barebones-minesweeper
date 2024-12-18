import java.util.Scanner;
public class MinesweeperApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose Difficulty:");
        System.out.println("   > Easy (10x10 - 10 mines)");
        System.out.println("   > Medium (14x14 - 25 mines)");
        System.out.println("   > Hard (18x18 - 40 mines)");
        System.out.print("> ");
        String chosenDiff = input.next().toUpperCase();
        int diff;
        if (chosenDiff.equals("MEDIUM")) {
            diff = 1;
        } else if (chosenDiff.equals("HARD")) {
            diff = 2;
        } else {
            diff = 0;
        }
        MinesweeperGame game = new MinesweeperGame(diff);
        MinesweeperGUI gui = new MinesweeperGUI(game);
        gui.setUpGUI();
        gui.setUpButtonListener();
    }
}