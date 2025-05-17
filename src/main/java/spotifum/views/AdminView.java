package spotifum.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import spotifum.menu.*;
import spotifum.musics.Music;

import static spotifum.utils.ValidEmail.isValidEmail;

public class AdminView extends SpotifUMView {
    public AdminView(){
        super();
    }

    public void login(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ load data [y/n]: ");
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

        Menu mainMenu = createSpotifUMMenu();
        mainMenu.run();
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
    }

    public void setNewEmail() throws RuntimeException{
        try{
            String email = "";
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.print("➤ email: ");
                email = scanner.nextLine();
            } while (!isValidEmail(email));
            this.db.setEmail(email);
        } catch (Exception e) {
            System.out.println("[app-log] could not set email!");
            throw new RuntimeException(e);
        }
    }

    public Menu createSpotifUMMenu(){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("create user", ()-> {try{setNewEmail();createUser();}catch(Exception e){}}));
        items.add(new MenuItem("remove user", ()-> removeUser()));
        items.add(new MenuItem("update user", ()-> {
            setNewEmail();
            try{
                if (this.db.getUserPlan()!=null){
                    getUserDetails();
                    Menu updateuser = createUpdateDetailsMenu();
                    updateuser.run();
                }
            } catch (Exception e) {
                System.out.println("[app-log] this user does not exist!");
            }
        }));
        items.add(new MenuItem("offer subscription", ()-> {
            setNewEmail();
            try {
                if (this.db.getUserPlan()!=null){
                    getUserDetails();
                    Menu offersub = createOfferSubscriptionMenu();
                    offersub.run();
                }
            } catch (Exception e) {
                System.out.println("[app-log] this user does not exist!");
            }
        }));
        items.add(new MenuItem("list registered users", ()-> printallusers()));
        items.add(new MenuItem("remove artist", ()-> removeArtist()));
        items.add(new MenuItem("remove music", ()-> deleteMusic()));
        items.add(new MenuItem("list registered artists", ()->printallartists()));
        items.add(new MenuItem("execute queries", ()-> seeStatistics()));
        items.add(new MenuItem("save current system state", ()->saveState()));

        return new Menu(items, true);
    }

    public void printallusers(){
        this.db.getAllUsers();
    }

    public void printallartists() {
        this.db.getAllArtists();
    }

    public void removeUser(){
        System.out.println("[app] removing user..");
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ email to delete: ");
        String email = scanner.nextLine();
        this.db.setEmail(email);
        this.db.removeUser();
    }

    public void removeArtist(){
        System.out.println("[app] removing artist..");
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ email to delete: ");
        String email = scanner.nextLine();
        this.db.setEmail(email);
        this.db.removeArtist();
    }

    public void deleteMusic(){
        System.out.println("[app] removing music..");
        System.out.print("➤ music name: ");
        Scanner sc = new Scanner(System.in);
        String musicName = sc.nextLine();

        Map<UUID, Music> musics = this.db.searchMusics(musicName);
        this.db.deleteMusic(musicName, musics);
    }

    public void seeStatistics(){
        System.out.println("[app] consulting statistics..");

        Menu statisticsMenu = createStatisticsMenu();
        statisticsMenu.run();
    }

    public Menu createStatisticsMenu(){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("most played music", ()-> getMostPlayedMusic()));
        items.add(new MenuItem("most played artist", ()-> getMostPlayedArtist()));
        items.add(new MenuItem("most listening time by user", ()-> getMostListeningTime()));
        items.add(new MenuItem("users leaderboard", ()-> getUserLeaderboard()));
        items.add(new MenuItem("most played genres", ()-> getMostPlayedGenres()));
        items.add(new MenuItem("public playlists", ()-> howMuchPublicPlaylistsExist()));
        items.add(new MenuItem("who have most playlists", ()-> whoHaveMostPlaylists()));

        return new Menu(items, true);
    }

    public void getMostPlayedMusic(){
        System.out.println("[app] most played track..");
        this.db.getMostPlayedMusic();
    }

    public void getMostPlayedArtist(){
        System.out.println("[app] most played artist..");
        this.db.getMostPlayedArtist();
    }

    public void getMostListeningTime(){
        System.out.println("[app] most listening time by user..");
        Scanner scanner = new Scanner(System.in);
        String aw;
        do{
            System.out.print("➤ search between dates [y/n]: ");
            aw = scanner.nextLine();
        } while(!aw.equalsIgnoreCase("y") && !aw.equalsIgnoreCase("n"));
        if (aw.equalsIgnoreCase("n")) this.db.getMostListeningTime();
        if (aw.equalsIgnoreCase("y")) {
            LocalDate initialDate;
            LocalDate finalDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            do {
                System.out.print("➤ initial date [yyyy-mm-dd]: ");
                try {
                    initialDate = LocalDate.parse(scanner.nextLine(), formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("[app-log] invalid format!");
                }
            } while (true);

            do {
                System.out.print("➤ final date [yyyy-mm-dd]: ");
                try {
                    finalDate = LocalDate.parse(scanner.nextLine(), formatter);
                    if (finalDate.isBefore(initialDate)) {
                        System.out.println("[app-log] there is no time machine here..");
                    } else {
                        break;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("[app-log] invalid format!");
                }
            } while (true);
            this.db.getMostListeningTime(initialDate, finalDate);
        }
    }

    public void getUserLeaderboard(){
        System.out.println("[app] user with most points..");
        this.db.getUserLeaderboard();
    }

    public void getMostPlayedGenres(){
        System.out.println("[app] most  played genres..");
        this.db.getMostPlayedGenres();
    }

    public void howMuchPublicPlaylistsExist(){
        System.out.println("[app] public playlists..");
        this.db.howMuchPublicPlaylistsExist();
    }

    public void whoHaveMostPlaylists(){
        System.out.println("[app] who have most playlists..");
        this.db.whoHaveMostPlaylists();
    }
}
