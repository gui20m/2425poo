package spotifum.views;

import spotifum.menu.Menu;
import spotifum.menu.MenuItem;

import java.util.*;

import static spotifum.utils.ValidEmail.isValidEmail;

public class ArtistView extends SpotifUMView {

    public ArtistView() {
        super();
    }

    public void login(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ load previous state [y/n]: ");
        String s = scanner.nextLine();

        if (s.equalsIgnoreCase("y")) {
            try {
                System.out.print("➤ load file: ");
                String fileName = scanner.nextLine();
                this.db.loadState(fileName);
                System.out.println("[app-log] sucessfully loaded data!");
            } catch (Exception e){
                System.out.println("[app-log] could not load previous state!");
            }
        }

        Menu mainMenu = createMainMenu();
        mainMenu.run();
    }

    private Menu createMainMenu() {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("log in", ()->doLogin()));
        items.add(new MenuItem("sign up", ()->doSignUp()));

        return new Menu(items, true);
    }

    private void doSignUp(){
        try {
            createArtist(null);
        } catch (Exception e){
            System.out.println("[app-log] could not create artist!");
        }
    }

    private void doLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("[app] log in");
        try{
            String email;
            do {
                System.out.print("➤ enter a valid email: ");
                email = scanner.nextLine();
            } while (!isValidEmail(email));
            this.db.loginArtist(email);
            System.out.println("["+db.getName()+"] successfully logged in!");
            Menu artistMainMenu = createSpotifUMMenu();
            artistMainMenu.run();
            String aw;
            do{
                System.out.print("➤ save info [y/n]: ");
                aw = scanner.nextLine();
            } while (!aw.equalsIgnoreCase("y") && !aw.equalsIgnoreCase("n"));
            if (aw.equalsIgnoreCase("y")) {
                try {
                    saveState();
                } catch (Exception e) {
                    System.out.println("[app-log] error: "+e.getMessage());
                }
            }
        } catch (Exception e){
            System.out.println("[app-log] artist with email " + e.getMessage() + " doest not exist!");
            System.out.print("[app] would you like to create it [y/n]: ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("y")) {
                createArtist(e.getMessage());
                System.out.println("["+db.getName()+"] successfully created artist!");
            } else {
                System.out.println("[app-log] leaving..");
            }
        }
    }

    public Menu createSpotifUMMenu(){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("artist details", ()-> getArtistDetails()));
        Menu changeDetails = createUpdateDetailsMenuArtists();
        items.add(new MenuItem("change details", changeDetails::run));
        items.add(new MenuItem("upload music", ()->addMusic()));
        items.add(new MenuItem("edit music", ()->updateMusic()));
        items.add(new MenuItem("remove music", ()->removeMusic()));
        items.add(new MenuItem("register album", ()->registerAlbum()));
        items.add(new MenuItem("update album", ()->updateAlbum()));
        items.add(new MenuItem("remove album", ()->removeAlbum()));
        items.add(new MenuItem("save current system state", ()-> saveState()));

        return new Menu(items, true);
    }

}
