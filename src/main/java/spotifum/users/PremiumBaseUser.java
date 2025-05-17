package spotifum.users;

/**
 * The type Premium base user.
 */
public class PremiumBaseUser extends User {

    /**
     * Instantiates a new Premium base user.
     */
    public PremiumBaseUser(){
        super();
        setUserPlan(SubscriptionPlans.PremiumBase);
    }

    /**
     * Instantiates a new Premium base user.
     *
     * @param user the user
     */
    public PremiumBaseUser(User user) {
        super(user);
        setUserPlan(SubscriptionPlans.PremiumBase);
    }

    public PremiumBaseUser(String email, String name) {
        super(email, name);
        setUserPlan(SubscriptionPlans.PremiumBase);
    }
    /**
     * Instantiates a new Premium base user.
     *
     * @param premiumBaseUser the premium base user
     */
    public  PremiumBaseUser(PremiumBaseUser premiumBaseUser) {
        super(premiumBaseUser);
    }

    /**
     * Creates and returns a copy of this PremiumBaseUser.
     *
     * @return a new PremiumBaseUser object that is a copy of this instance
     */
    public PremiumBaseUser clone(){
        return new PremiumBaseUser(this);
    }
}
