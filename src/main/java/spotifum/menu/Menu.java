package spotifum.menu;

import spotifum.utils.ConsoleColors;

import java.io.Serializable;
import java.util.*;

import static spotifum.utils.DisplayInformation.displayMenu;


/**
 * The type Menu.
 */
public class Menu implements Serializable {
    private List<MenuItem> items;
    private boolean keepRunning;
    private boolean showExitOption;

    /**
     * Instantiates a new Menu.
     *
     * @param items          the items
     * @param showExitOption the show exit option
     */
    public Menu(List<MenuItem> items, boolean showExitOption) {
        this.items = items;
        this.keepRunning = true;
        this.showExitOption = showExitOption;
    }

    /**
     * Display Menu.
     */
    public void display() {
        displayMenu(this.items, this.showExitOption);
    }

    /**
     * Removes ANSI escape codes (color formatting) from a string.
     * This is useful for getting the plain text version of strings that contain
     * console color codes.
     *
     * @param input the string containing ANSI escape codes
     * @return the string with all ANSI escape sequences removed
     */
    private static String stripAnsi(String input) {
        return input.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Gets user choice.
     *
     * @return the user choice
     */
    public int getUserChoice() {
        Scanner sc = new Scanner(System.in);
        int choice;
        while(true){
            System.out.print("➤ option: ");
            if (sc.hasNextInt()){
                choice = sc.nextInt();
                sc.nextLine();
                if (choice > 0 && choice <= items.size()+1)
                    break;
            } else {
                System.out.print(ConsoleColors.RESET+"➤ option: "+ConsoleColors.RESET);
                sc.nextLine();
            }
            System.out.println(ConsoleColors.RED+"➤ error: invalid option!"+ConsoleColors.RESET);
        }
        return choice;
    }

    /**
     * Execute selected option Menu.
     */
    public void executeSelectedOption() {
        int choice = getUserChoice();

        if (showExitOption && choice == items.size() + 1) {
            keepRunning = false;
        } else if (choice > 0 && choice <= items.size()) {
            items.get(choice - 1).execute();
        } else {
            System.out.println(ConsoleColors.RED+"error: invalid option!"+ConsoleColors.RESET);
        }
    }

    /**
     * Run Menu.
     */
    public void run(){
        this.keepRunning = true;
        while (keepRunning){
            display();
            executeSelectedOption();
        }
    }

    /**
     * Close Menu.
     */
    public void close(){
        this.keepRunning = false;
    }
}