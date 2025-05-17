package spotifum.views;

import spotifum.SpotifUMController;
import spotifum.menu.*;
import spotifum.users.SubscriptionPlans;

import java.io.Serializable;
import java.util.*;

import static spotifum.utils.DisplayInformation.displayExceptions;
import static spotifum.utils.ValidEmail.isValidEmail;

public abstract class SpotifUMView implements Serializable {
    private SubscriptionPlans plan;
    transient protected Menu currentMenu;
    transient protected Menu previousMenu;
    public final SpotifUMController db;

    public SpotifUMView() {
        this.db = new SpotifUMController(this);
        this.plan = SubscriptionPlans.Free;
    }

    public abstract void login();

    public abstract Menu createSpotifUMMenu();

    public void refreshMenu() {
        if (currentMenu != null) {
            currentMenu.close();
        }
        this.currentMenu = createSpotifUMMenu();
        this.currentMenu.run();
    }

    public void setPlan(SubscriptionPlans plan) {
        this.plan = plan;
    }

    public SubscriptionPlans getPlan() {
        return this.db.getUserPlan();
    }

    public void setCurrentMenu(Menu newMenu) {
        if (currentMenu != null && currentMenu != newMenu) {
            this.previousMenu = currentMenu;
        }

        if (currentMenu != null) {
            currentMenu.close();
        }
        this.currentMenu = newMenu;
        currentMenu.run();
    }

    public Menu getPreviousMenu(){
        return this.previousMenu;
    }

    public void returnToPreviousMenu() {
        if (previousMenu != null) {
            setCurrentMenu(previousMenu);
        } else {
            setCurrentMenu(createSpotifUMMenu());
        }
    }

    public void createUser() {
        createUser(this.db.getEmail());
    }

    public void createUser(String email) {
        System.out.println("[app-log] creating new user..");

        Scanner scanner = new Scanner(System.in);

        try{
            System.out.println("[app] sign up");
            if (email==null) {
                do {
                    System.out.print("➤ enter a valid email: ");
                    email = scanner.nextLine();
                } while (!isValidEmail(email));
            } else {
                System.out.println("➤ email: " +  email);
            }
            System.out.print("➤ username: ");
            String username = scanner.nextLine();

            db.createUser(email, username);
            System.out.println("[app-log] user created successfully!");

        } catch (Exception e){
            displayExceptions(e.getMessage());
        }
    }

    public void createArtist(String email) {
        System.out.println("[app-log] creating new artist..");

        Scanner scanner = new Scanner(System.in);

        try{
            System.out.println("[app] sign up");
            if (email==null) {
                System.out.print("➤ email: ");
                email = scanner.nextLine();
            } else {
                System.out.println("➤ email: " +  email);
            }
            System.out.print("➤ username: ");
            String username = scanner.nextLine();

            db.createArtist(email, username);
            System.out.println("[app-log] sign up successfully!");

        } catch (Exception e){
            displayExceptions(e.getMessage());
        }
    }

    public void getUserDetails(){
        try{
            System.out.println("["+this.db.getName()+"] getting user details..");
            System.out.println(this.db.getUserDetails());
        } catch (Exception e){
            System.out.println("[app-log] there was an error getting user details!");
        }
    }

    public void getArtistDetails(){
        System.out.println("["+this.db.getName()+"] getting artist details..");

        try{
            System.out.println(this.db.getArtistDetails());
        } catch (Exception e){
            System.out.println("[app-log] there was an error getting artist details!");
        }
    }

    public void addMusic(){
        System.out.println("["+this.db.getName()+"] upload music..");
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("➤ music name: ");
            String musicName = scanner.nextLine();

            System.out.print("➤ music label: ");
            String musicLabel = scanner.nextLine();

            System.out.print("➤ music duration in seconds: ");
            int musicDuration = 0;
            while(true) {
                try {
                    musicDuration = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.print("➤ music duration in seconds: ");
                    scanner.nextLine();
                }
            }

            System.out.print("➤ music genre: ");
            String musicGenre = scanner.nextLine();

            System.out.print("➤ music lyrics: ");
            String musicLyrics = scanner.nextLine();

            List<String> musicPartiture = new ArrayList<>();
            String musicPartitureLine;
            do {
                System.out.print("➤ music partiture [\"q\" to end]: ");
                musicPartitureLine = scanner.nextLine();
                musicPartiture.add(musicPartitureLine);
            } while (!musicPartitureLine.equalsIgnoreCase("q"));

            String aw;
            do{
                System.out.print("➤ explicit music [y/n]: ");
                aw = scanner.nextLine();
            }while(!aw.equalsIgnoreCase("y") && !aw.equalsIgnoreCase("n"));
            boolean explicit = aw.equalsIgnoreCase("y");

            String isMultimedia;
            do{
                System.out.print("➤ multimedia music [y/n]: ");
                isMultimedia = scanner.nextLine();
            }while(!isMultimedia.equalsIgnoreCase("y") && !isMultimedia.equalsIgnoreCase("n"));
            boolean multimedia = isMultimedia.equalsIgnoreCase("y");
            String video = null;
            if (multimedia){
                System.out.print("➤ video link: ");
                video = scanner.nextLine();
            }

            if (this.db.addMusic(musicName, this.db.getName(), musicLabel, musicLyrics, musicPartiture, musicGenre, musicDuration, explicit, multimedia, video)==1)
                System.out.println("[app-log] music successfully added!");

        } catch (Exception e){
            System.out.println("[app-log] error uploading music!");
        }
    }

    public void removeMusic(){
        System.out.println("["+this.db.getName()+"] removing music..");
        if (this.db.removeMusic()==1)
            System.out.println("[app-log] music successfully removed!");
    }

    public void registerAlbum(){
        System.out.println("["+this.db.getName()+"] register album..");
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ register album name: ");
        String album = scanner.nextLine();
        if (this.db.registerAlbum(album)==1)
            System.out.println("[app-log] album successfully registered!");
    }

    public void removeAlbum(){
        System.out.println("["+this.db.getName()+"] removing album..");
        if (this.db.removeAlbum()==1)
            System.out.println("[app-log] album successfully removed!");
    }

    public void updateAlbum(){
        System.out.println("["+this.db.getName()+"] updating album..");
        try {
            this.db.updateAlbum();
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating album!");
        }
    }

    public void updateMusic(){
        System.out.println("["+this.db.getName()+"] updating music..");
        try{
            this.db.updateMusic();
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating music!");
        }
    }

    public Menu createUpdateDetailsMenuArtists(){
        List <MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("update email", ()-> updateEmailArtist()));
        items.add(new MenuItem("update username", ()-> updateUsernameArtist()));

        return new Menu(items, true);
    }

    public void updateEmailArtist(){
        System.out.println("["+this.db.getName()+"] updating email..");
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("➤ new email: ");
            String email = scanner.nextLine();

            this.db.updateEmailArtist(email);
            System.out.println("[app-log] updated email!");
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating email!");
        }
    }

    public void updateUsernameArtist(){
        System.out.println("["+this.db.getName()+"] updating username..");
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("➤ new name: ");
            String username = scanner.nextLine();

            this.db.updateUsernameArtist(username);
            System.out.println("[app-log] updated username!");
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating username!");
        }
    }

    public Menu createUpdateDetailsMenu(){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("update email", ()-> updateEmail()));
        items.add(new MenuItem("update username", ()-> updateUsername()));
        items.add(new MenuItem("update address", ()-> updateAddress()));

        Menu menu = new Menu(items, true);
        this.currentMenu = menu;

        return menu;
    }

    public void updateEmail(){
        System.out.println("["+this.db.getName()+"] updating email..");
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("➤ new email: ");
            String email = scanner.nextLine();

            this.db.updateEmail(email);
            System.out.println("[app-log] updated email!");
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating email!");
        }
    }

    public void updateUsername(){
        System.out.println("["+this.db.getName()+"] updating username..");
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("➤ new name: ");
            String username = scanner.nextLine();

            this.db.updateUsername(username);
            System.out.println("[app-log] updated username!");
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating username!");
        }
    }

    public void updateAddress(){
        System.out.println("["+this.db.getName()+"] updating address..");
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("➤ new address: ");
            String address = scanner.nextLine();

            this.db.updateAddress(address);
            System.out.println("[app-log] updated address!");
        } catch (Exception e){
            System.out.println("[app-log] there was an error while updating address!");
        }
    }

    public Menu createPremiumPlansMenu(){
        List<MenuItem> items = new ArrayList<>();
        for (SubscriptionPlans plan : SubscriptionPlans.values()) {
            items.add(new MenuItem(plan.toString() + " [$"+plan.getPrice()+"]", () -> buyPremium(plan)));
        }

        items.add(new MenuItem("exit", () -> returnToPreviousMenu()));

        Menu menu = new Menu(items, false);
        this.currentMenu = menu;
        return menu;
    }

    public Menu createOfferSubscriptionMenu(){
        List<MenuItem> items = new ArrayList<>();
        for (SubscriptionPlans plan : SubscriptionPlans.values()) {
            items.add(new MenuItem(plan.toString(), ()->{
                this.db.setUserPlan(plan);
                getUserDetails();
            }));
        }
        return new Menu(items, true);
    }

    public void buyPremium(SubscriptionPlans premium){
        System.out.println("["+this.db.getName()+"] premium plans..");
        this.db.updatePlan(premium);
        this.plan = premium;
        refreshMenu();
    }

    public void createPlaylist(){
        System.out.println("["+this.db.getName()+"] creating playlist..");
        System.out.print("➤ playlist name: ");
        Scanner scanner = new Scanner(System.in);
        String playlistName = scanner.nextLine();
        Menu playlistmenu = this.db.createPlaylist(playlistName);
        if (playlistmenu!=null){
            this.currentMenu = playlistmenu;
            setCurrentMenu(playlistmenu);
        }
    }

    public void generatePlaylist(){
        System.out.println("["+this.db.getName()+"] generating playlist..");
        Scanner scanner = new Scanner(System.in);
        try{
            int maxsongs=-1;
            while(maxsongs<0){
                System.out.print("➤ max musics: ");
                maxsongs = scanner.nextInt();
                scanner.nextLine();
            }
            int maxtime=-1;
            while(maxtime<0){
                System.out.print("➤ max time in seconds: ");
                maxtime=scanner.nextInt();
                scanner.nextLine();
            }
            System.out.print("➤ only explicit musics [y/n]: ");
            String explicit = scanner.nextLine();
            boolean onlyExplicit = explicit.equalsIgnoreCase("y");

            this.db.generatePlaylist(maxsongs, maxtime, onlyExplicit);
            System.out.println("[app-log] successfully generated playlist!");
        } catch (Exception e) {
            System.out.println("[app-log] start listening music to build your preferences!");
        }
    }

    public void updatePlaylist(){
        System.out.println("["+this.db.getName()+"] managing playlist..");
        Menu updateplaylistmenu = this.db.updatePlaylist();
        if (updateplaylistmenu!=null){
            this.currentMenu = updateplaylistmenu;
            setCurrentMenu(updateplaylistmenu);
        }
    }

    public void deletePlaylist(){
        System.out.println("["+this.db.getName()+"] deleting playlist..");
        this.db.deletePlaylist();
    }

    public Menu createPlaySongsMenu(){
        this.previousMenu = this.currentMenu;
        Menu menu = this.db.createPlaySongsMenu();
        if (menu!=null){
            this.currentMenu = menu;
            return menu;
        }
        return previousMenu;
    }

    public void timeSimulation(){
        System.out.println("["+this.db.getName()+"] time simulation..");
        try {
            Menu manageTime = createTimeManageMenu();
            manageTime.run();
        } catch (Exception e) {
            System.out.println(e.getMessage()!= null? e.getMessage() : "[app-log] invalid days to simulate time!");
        }
    }

    private Menu createTimeManageMenu(){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("advance time", ()->advanceTime()));
        items.add(new MenuItem("reset time", ()->this.db.resetTime()));

        return new Menu(items, true);
    }

    private void advanceTime(){
        this.db.getCurrentDate();
        System.out.print("➤ how many days: ");
        Scanner scanner = new Scanner(System.in);
        int days = scanner.nextInt();
        this.db.timeSimulation(days);
        this.db.getCurrentDate();
    }

    public void saveState(){
        try{
            System.out.print("➤ file name: ");
            Scanner scanner = new Scanner(System.in);
            String fileName = scanner.nextLine();
            this.db.saveState(fileName);
            System.out.println("[app-log] sucessfully saved data!");
        } catch (Exception e) {
        }
    }
}
