package spotifum.users;

import java.io.Serializable;

/**
 * The enum Subscription plans.
 */
public enum SubscriptionPlans implements Serializable {
    /**
     * Free subscription plans.
     */
    Free(0),
    /**
     * Premium base subscription plans.
     */
    PremiumBase(5),
    /**
     * Premium top subscription plans.
     */
    PremiumTop(10);

    /**
     * The price associated with the subscription plan.
     */
    private final double price;

    /**
     * Constructs a SubscriptionPlan with the specified price.
     *
     * @param price the price of the subscription plan
     */
    SubscriptionPlans(double price){
        this.price = price;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the name of the subscription plan in lowercase.
     *
     * @return the subscription plan name as a lowercase string
     */
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Compares the enum name with its lowercase string representation.
     *
     * @return true if the name equals its lowercase string representation, false otherwise
     */
    public boolean equals(){
        return this.name().equals(this.toString());
    }
}


