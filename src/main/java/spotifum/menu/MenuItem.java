package spotifum.menu;

import java.io.Serializable;

/**
 * The type Menu item.
 */
public class MenuItem implements Serializable {
    /**
     * The interface Pre condition.
     */
    public interface PreCondition{
        /**
         * Validate boolean.
         *
         * @return the boolean
         */
        boolean validate();
    }

    /**
     * The interface Handler.
     */
    public interface Handler{
        /**
         * Execute.
         */
        void execute();
    }

    /**
     * The display name/text of this menu item.
     */
    private String name;

    /**
     * The precondition that must be satisfied for this menu item to be executable.
     */
    private PreCondition preCondition;

    /**
     * The action handler that implements the menu item's functionality.
     */
    private Handler action;

    /**
     * Instantiates a new Menu item.
     *
     * @param name   the name
     * @param action the action
     */
    public MenuItem(String name, Handler action) {
        this.name = name;
        this.action = action;
        this.preCondition = ()->true;
    }

    /**
     * Instantiates a new Menu item.
     *
     * @param name         the name
     * @param preCondition the pre condition
     * @param action       the action
     */
    public MenuItem(String name, PreCondition preCondition, Handler action) {
        this.name = name;
        this.preCondition = preCondition;
        this.action = action;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Execute.
     */
    public void execute() {
        if (preCondition.validate()) {
            action.execute();
        } else {
            System.out.println("Error: Invalid Option");
        }
    }

    /**
     * Is valid boolean.
     *
     * @return the boolean
     */
    public boolean isValid() {
        return preCondition.validate();
    }

    /**
     * Returns the string representation of this menu item (its name).
     * @return the name of the menu item
     */
    public String toString(){
        return this.name;
    }
}