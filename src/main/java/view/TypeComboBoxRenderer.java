package view;

import model.PokemonType;

import javax.swing.*;
import java.awt.*;

/**
 * A custom renderer for displaying items in the Pokemon type JComboBox.
 * <p>
 * This renderer displays "All Types" when the value is null,
 * and shows the name of the PokemonType otherwise.
 */
public class TypeComboBoxRenderer extends DefaultListCellRenderer {

    /**
     * Overrides how each item in the combo box is displayed.
     *
     * @param list the JList we're rendering
     * @param value the value to display (can be null or PokemonType)
     * @param index the index of the item
     * @param isSelected whether the item is selected
     * @param cellHasFocus whether the cell has focus
     * @return the component used for rendering
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null) {
            setText("All Types");
        } else if (value instanceof PokemonType) {
            setText(((PokemonType) value).name());
        }

        return this;
    }
}
