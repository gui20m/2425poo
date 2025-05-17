package spotifum;

import spotifum.menu.*;
import spotifum.views.*;

import java.util.*;

/**
 * The type Spotif um app.
 */
public class SpotifUMApp {
    private Menu mainMenu;
    private UserView userView;
    private ArtistView artistView;
    private AdminView adminView;

    /**
     * Init.
     */
    public void init() {
        this.userView = new UserView();
        this.artistView = new ArtistView();
        this.adminView = new AdminView();
        this.mainMenu = createMainMenu();
    }

    /**
     * Start.
     */
    public void start() {
        mainMenu.run();
    }

    /**
     * Creates and configures the main menu of the application.
     */
    private Menu createMainMenu() {
        List<MenuItem> mainMenuItems = new ArrayList<>();

        mainMenuItems.add(new MenuItem("user app", () -> userView.login()));
        mainMenuItems.add(new MenuItem("artist app", () -> artistView.login()));
        mainMenuItems.add(new MenuItem("admin app", ()-> adminView.login()));

        return new Menu(mainMenuItems, true);
    }
}