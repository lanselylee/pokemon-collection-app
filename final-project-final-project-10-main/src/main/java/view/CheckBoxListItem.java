package view;

import model.Pokemon;

/**
 * Represents an item in a checkbox list that wraps a Pokemon object
 * and tracks whether it's selected or not.
 * <p>
 * Used in the UI to show a list of selectable Pokemon.
 */
public class CheckBoxListItem {
    private final Pokemon pokemon;
    private boolean selected;

    /**
     * Constructs a new CheckBoxListItem with the given Pokemon.
     * By default, the item is not selected.
     *
     * @param pokemon the Pokemon associated with this item
     */
    public CheckBoxListItem(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.selected = false;
    }

    /**
     * Returns the Pokemon object.
     *
     * @return the Pokemon
     */
    public Pokemon getPokemon() {
        return pokemon;
    }

    /**
     * Returns whether this item is selected.
     *
     * @return true if selected, false otherwise
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected state of this item.
     *
     * @param selected true to select, false to deselect
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
