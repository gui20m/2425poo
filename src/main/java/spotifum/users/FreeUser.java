package spotifum.users;

/**
 * The type Free user.
 */
public class FreeUser extends User {

    /**
     * Instantiates a new Free user.
     */
    public FreeUser() {
        super();
        setUserPlan(SubscriptionPlans.Free);
    }

    /**
     * Instantiates a new Free user.
     *
     * @param user the user
     */
    public FreeUser(User user) {
        super(user);
        setUserPlan(SubscriptionPlans.Free);
    }

    /**
     * Instantiates a new Free user.
     *
     * @param email    the email
     * @param username the username
     */
    public FreeUser(String email, String username) {
        super(email,username);
        setUserPlan(SubscriptionPlans.Free);
    }

    /**
     * Creates and returns a copy of this FreeUser.
     *
     * @return a new FreeUser object that is a copy of this instance
     */
    public FreeUser clone(){
        return new FreeUser(this);
    }
}
