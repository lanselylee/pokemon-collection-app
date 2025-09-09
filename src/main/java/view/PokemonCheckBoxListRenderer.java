package view;

import javax.swing.*;
import java.awt.*;

import model.Pokemon;
import model.PokemonType;

/**
 * A custom cell renderer for displaying Pokemon items with checkboxes in a JList.
 * <p>
 * This renderer will:
 * - Show Pokemon info as text
 * - Display checkbox based on selection
 * - Apply background color based on Pokemon type
 * - Highlight when item is selected
 */
public class PokemonCheckBoxListRenderer extends JCheckBox implements ListCellRenderer<CheckBoxListItem> {

    /**
     * Constructs the renderer and sets the basic style for the checkbox.
     */
    public PokemonCheckBoxListRenderer() {
        setOpaque(true);
        setFont(new Font("Arial", Font.PLAIN, 14)); // Consistent font style
    }

    /**
     * Renders each item in the JList as a checkbox with Pokemon info.
     *
     * @param list the JList component
     * @param value the CheckBoxListItem to display
     * @param index the index of the item
     * @param isSelected whether the item is selected
     * @param cellHasFocus whether the cell has focus
     * @return the component used for rendering
     */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends CheckBoxListItem> list,
            CheckBoxListItem value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        Pokemon pokemon = value.getPokemon();
        setText(pokemon.toString());
        setSelected(value.isSelected());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());

            // Apply type-based color tint with transparency
            Color typeColor = getTypeColor(pokemon.getTypes().get(0));
            setBackground(new Color(
                    typeColor.getRed(),
                    typeColor.getGreen(),
                    typeColor.getBlue(),
                    30 // Transparency
            ));
        }

        return this;
    }

    /**
     * Returns a color associated with the given Pokemon type.
     *
     * @param type the type of the Pokemon
     * @return the corresponding color
     */
    private Color getTypeColor(PokemonType type) {
        return switch (type) {
            case FIRE -> new Color(255, 100, 100);
            case WATER -> new Color(100, 100, 255);
            case GRASS -> new Color(100, 255, 100);
            case ELECTRIC -> new Color(255, 255, 100);
            default -> new Color(200, 200, 200);
        };
    }
}
