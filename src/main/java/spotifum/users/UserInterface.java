package spotifum.users;

import java.util.UUID;

/**
 * The interface User interface.
 */
public interface UserInterface {
    /**
     * Gets uid.
     *
     * @return the uid
     */
    UUID getUid();

    /**
     * Gets username.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

    /**
     * Gets address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Gets user plan.
     *
     * @return the user plan
     */
    SubscriptionPlans getUserPlan();

    /**
     * Sets user plan.
     *
     * @param userPlan the user plan
     */
    void setUserPlan(SubscriptionPlans userPlan);

    /**
     * Sets username.
     *
     * @param username the username
     */
    void setUsername(String username);

    /**
     * Sets email.
     *
     * @param email the email
     */
    void setEmail(String email);

    /**
     * Sets address.
     *
     * @param address the address
     */
    void setAddress(String address);

    /**
     * Returns a string representation of the user.
     *
     * @return the string representation
     */
    String toString();
}
