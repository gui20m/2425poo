package spotifum.users;

/**
 * The type Premium top user.
 */
public class PremiumTopUser extends PremiumBaseUser {
    /**
     * Instantiates a new Premium top user.
     */
    public PremiumTopUser(){
        super();
        setUserPlan(SubscriptionPlans.PremiumTop);
    }

    /**
     * Instantiates a new Premium top user.
     *
     * @param user the user
     */
    public PremiumTopUser(User user) {
        super(user);
        setUserPlan(SubscriptionPlans.PremiumTop);
    }

    public PremiumTopUser(String email, String name) {
        super(email, name);
        setUserPlan(SubscriptionPlans.PremiumTop);
    }

    /**
     * Creates and returns a copy of this PremiumTopUser.
     *
     * @return a new PremiumTopUser object that is a copy of this instance
     */
    public PremiumTopUser clone(){
        return new PremiumTopUser(this);
    }
}
