package view;

import model.Pokemon;
import model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonCheckBoxListRendererTest {

    private PokemonCheckBoxListRenderer renderer;
    private JList<CheckBoxListItem> mockList;
    private CheckBoxListItem testItem;
    private Pokemon testPokemon;

    @BeforeEach
    void setUp() {
        // Create the renderer
        renderer = new PokemonCheckBoxListRenderer();

        // Create mock JList
        mockList = mock(JList.class);

        // Create test Pokemon
        testPokemon = new Pokemon(
                1,
                "bulbasaur",
                "http://example.com/bulbasaur.png",
                Arrays.asList(PokemonType.GRASS),
                new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45)
        );

        // Create test item
        testItem = new CheckBoxListItem(testPokemon);

        // Set up default JList behavior
        when(mockList.getSelectionBackground()).thenReturn(Color.BLUE);
        when(mockList.getSelectionForeground()).thenReturn(Color.WHITE);
        when(mockList.getBackground()).thenReturn(Color.WHITE);
        when(mockList.getForeground()).thenReturn(Color.BLACK);
    }

    @Test
    void testRendererInitialization() {
        // Check that renderer is properly initialized
        assertTrue(renderer.isOpaque(), "Renderer should be opaque");
        assertEquals(new Font("Arial", Font.PLAIN, 14), renderer.getFont(),
                "Renderer should have Arial font");
    }

    @Test
    void testGetListCellRendererComponent_Selected() {
        // Test when the item is selected in the list
        Component result = renderer.getListCellRendererComponent(
                mockList, testItem, 0, true, false);

        // Verify it's the renderer itself
        assertSame(renderer, result, "Should return itself");

        // Verify properties set when selected
        assertEquals(testPokemon.toString(), renderer.getText(),
                "Should display Pokemon toString");
        assertEquals(testItem.isSelected(), renderer.isSelected(),
                "Selection state should match item");
        assertEquals(mockList.getSelectionBackground(), renderer.getBackground(),
                "Background should be selection background");
        assertEquals(mockList.getSelectionForeground(), renderer.getForeground(),
                "Foreground should be selection foreground");
    }

    @Test
    void testGetListCellRendererComponent_NotSelected() {
        // Test when the item is not selected in the list
        Component result = renderer.getListCellRendererComponent(
                mockList, testItem, 0, false, false);

        // Verify properties set when not selected
        assertSame(renderer, result, "Should return itself");
        assertEquals(testPokemon.toString(), renderer.getText(),
                "Should display Pokemon toString");
        assertEquals(testItem.isSelected(), renderer.isSelected(),
                "Selection state should match item");

        // Background should be type-based color, but we can't check the exact color
        // Just verify it's not the default selection color
        assertNotEquals(mockList.getSelectionBackground(), renderer.getBackground(),
                "Background should not be selection background");
        assertEquals(mockList.getForeground(), renderer.getForeground(),
                "Foreground should be default foreground");
    }

    @Test
    void testGetListCellRendererComponent_ItemSelected() {
        // Set the item as selected
        testItem.setSelected(true);

        // Render the component
        renderer.getListCellRendererComponent(mockList, testItem, 0, false, false);

        // Verify checkbox is selected
        assertTrue(renderer.isSelected(), "Checkbox should be selected when item is selected");
    }

    @Test
    void testGetListCellRendererComponent_DifferentTypes() {
        // Test with different Pokemon types
        Pokemon firePokemon = new Pokemon(
                4,
                "charmander",
                "http://example.com/charmander.png",
                Arrays.asList(PokemonType.FIRE),
                new Pokemon.PokemonStats(39, 52, 43, 60, 50, 65)
        );

        CheckBoxListItem fireItem = new CheckBoxListItem(firePokemon);

        // Render fire type
        Component fireResult = renderer.getListCellRendererComponent(
                mockList, fireItem, 0, false, false);
        Color fireColor = renderer.getBackground();

        // Render grass type
        Component grassResult = renderer.getListCellRendererComponent(
                mockList, testItem, 0, false, false);
        Color grassColor = renderer.getBackground();

        // Different types should have different background colors
        assertNotEquals(fireColor, grassColor,
                "Different types should have different background colors");
    }

    @Test
    void testGetTypeColor() throws Exception {
        // Test getTypeColor method using reflection
        java.lang.reflect.Method getTypeColorMethod = PokemonCheckBoxListRenderer.class
                .getDeclaredMethod("getTypeColor", PokemonType.class);
        getTypeColorMethod.setAccessible(true);

        // Test colors for different types
        Color fireColor = (Color) getTypeColorMethod.invoke(renderer, PokemonType.FIRE);
        Color waterColor = (Color) getTypeColorMethod.invoke(renderer, PokemonType.WATER);
        Color grassColor = (Color) getTypeColorMethod.invoke(renderer, PokemonType.GRASS);
        Color electricColor = (Color) getTypeColorMethod.invoke(renderer, PokemonType.ELECTRIC);
        Color normalColor = (Color) getTypeColorMethod.invoke(renderer, PokemonType.NORMAL);

        // Verify each type has a distinct color
        assertNotEquals(fireColor, waterColor, "Fire and Water should have different colors");
        assertNotEquals(fireColor, grassColor, "Fire and Grass should have different colors");
        assertNotEquals(fireColor, electricColor, "Fire and Electric should have different colors");
        assertNotEquals(waterColor, grassColor, "Water and Grass should have different colors");
        assertNotEquals(waterColor, electricColor, "Water and Electric should have different colors");
        assertNotEquals(grassColor, electricColor, "Grass and Electric should have different colors");

        // Default type should return grey
        assertEquals(new Color(200, 200, 200), normalColor, "Normal type should have grey color");
    }
}
