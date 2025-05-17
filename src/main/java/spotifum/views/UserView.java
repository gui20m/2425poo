package spotifum.views;

import java.util.*;

import spotifum.exceptions.EntityNotFoundException;
import spotifum.menu.*;
import spotifum.users.SubscriptionPlans;

import static spotifum.utils.ValidEmail.isValidEmail;

public class UserView extends SpotifUMView {
    public UserView(){
        super();
    }

    public void login(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ load data [y/n]: ");
        String s = scanner.nextLine();

        if (s.equalsIgnoreCase("y")) {
            try{
                System.out.print("➤ load file: ");
                String fileName = scanner.nextLine();
                this.db.loadState(fileName);
                System.out.println("[app-log] successfully loaded data!");
            } catch (Exception e) {
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
            this.db.createUser();
        } catch (Exception e){
            System.out.println("[app-log] could not create user!");
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
            this.db.login(email);
            System.out.println("["+db.getName()+"] successfully logged in!");
            Menu spotifumMainMenu = createSpotifUMMenu();
            setCurrentMenu(spotifumMainMenu);
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
        } catch (EntityNotFoundException e){
            System.out.println("[app-log] user with email " + e.getMessage() + " doest not exist!");
            System.out.print("➤ would you like to create it [y/n]: ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("y")) {
                createUser(e.getMessage());
                System.out.println("["+db.getName()+"] sucessfully created user!");
            } else {
                System.out.println("[app-log] leaving..");
            }
        }
    }

    public Menu createSpotifUMMenu(){
        List<MenuItem> items = new ArrayList<>();

        SubscriptionPlans currentPlan = db.getUserPlan();

        items.add(new MenuItem("user profile", ()-> getUserDetails()));
        Menu updateDetails = createUpdateDetailsMenu();
        items.add(new MenuItem("change details", ()->{
            getUserDetails();
            updateDetails.run();
        }));
        Menu premiumPlans = createPremiumPlansMenu();
        items.add(new MenuItem("buy premium plans", ()-> setCurrentMenu(premiumPlans)));
        Menu playSongs = createPlaySongsMenu();
        items.add(new MenuItem("play music", ()->setCurrentMenu(playSongs)));
        if (currentPlan.equals(SubscriptionPlans.PremiumBase) || currentPlan.equals(SubscriptionPlans.PremiumTop)) {
            items.add(new MenuItem("create playlist", ()-> createPlaylist()));
            items.add(new MenuItem("generate playlist", ()->generatePlaylist()));
            items.add(new MenuItem("manage playlist", ()->updatePlaylist()));
            items.add(new MenuItem("delete playlist", ()->deletePlaylist()));
        }
        items.add(new MenuItem("time simulating", ()->timeSimulation()));
        items.add(new MenuItem("save current system state", ()-> {
            saveState();
        }));

        return new Menu(items, true);
    }

}
