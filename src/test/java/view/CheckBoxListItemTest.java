package view;

import model.Pokemon;
import model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CheckBoxListItemTest {

    private Pokemon testPokemon;
    private CheckBoxListItem item;

    @BeforeEach
    void setUp() {
        // Create a test Pokemon
        testPokemon = new Pokemon(
                1,
                "bulbasaur",
                "http://example.com/bulbasaur.png",
                Arrays.asList(PokemonType.GRASS, PokemonType.POISON),
                new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45)
        );

        // Create the item to test
        item = new CheckBoxListItem(testPokemon);
    }

    @Test
    void getPokemon() {
        // Test that getPokemon returns the correct Pokemon
        assertSame(testPokemon, item.getPokemon(), "Should return the Pokemon that was set in the constructor");
    }

    @Test
    void isSelected_initialState() {
        // Test initial state is unselected
        assertFalse(item.isSelected(), "Initial state should be unselected");
    }

    @Test
    void setSelected_true() {
        // Test setting to selected
        item.setSelected(true);
        assertTrue(item.isSelected(), "Should be selected after setting to true");
    }

    @Test
    void setSelected_false() {
        // First set to true
        item.setSelected(true);

        // Then set back to false
        item.setSelected(false);
        assertFalse(item.isSelected(), "Should be unselected after setting to false");
    }

    @Test
    void setSelected_multipleChanges() {
        // Test multiple state changes
        item.setSelected(true);
        assertTrue(item.isSelected());

        item.setSelected(false);
        assertFalse(item.isSelected());

        item.setSelected(true);
        assertTrue(item.isSelected());
    }
}
