package view;

import model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeComboBoxRendererTest {

    private TypeComboBoxRenderer renderer;
    private JList<?> mockList;

    @BeforeEach
    void setUp() {
        renderer = new TypeComboBoxRenderer();
        mockList = mock(JList.class);

        // Set up default JList behavior
        when(mockList.getSelectionBackground()).thenReturn(Color.BLUE);
        when(mockList.getSelectionForeground()).thenReturn(Color.WHITE);
        when(mockList.getBackground()).thenReturn(Color.WHITE);
        when(mockList.getForeground()).thenReturn(Color.BLACK);
    }

    @Test
    void testGetListCellRendererComponent_NullValue() {
        // Test with null (should show "All Types")
        Component result = renderer.getListCellRendererComponent(
                mockList, null, 0, false, false);

        // Verify result
        assertSame(renderer, result, "Should return itself");
        assertEquals("All Types", renderer.getText(),
                "Should show 'All Types' for null value");
    }

    @Test
    void testGetListCellRendererComponent_PokemonTypeValue() {
        // Test with PokemonType value
        Component result = renderer.getListCellRendererComponent(
                mockList, PokemonType.FIRE, 0, false, false);

        // Verify result
        assertSame(renderer, result, "Should return itself");
        assertEquals("FIRE", renderer.getText(),
                "Should show the enum name for PokemonType value");
    }

    @Test
    void testGetListCellRendererComponent_OtherValue() {
        // Test with some other object value
        String otherValue = "Not a Pokemon type";
        Component result = renderer.getListCellRendererComponent(
                mockList, otherValue, 0, false, false);

        // Verify result
        assertSame(renderer, result, "Should return itself");
        assertEquals(otherValue, renderer.getText(),
                "Should show toString() for other values");
    }

    @Test
    void testGetListCellRendererComponent_Selected() {
        // Test with a selected item
        Component result = renderer.getListCellRendererComponent(
                mockList, PokemonType.WATER, 0, true, false);

        // Verify selection properties
        assertSame(renderer, result, "Should return itself");
        assertEquals(mockList.getSelectionBackground(), renderer.getBackground(),
                "Background should be selection background when selected");
        assertEquals(mockList.getSelectionForeground(), renderer.getForeground(),
                "Foreground should be selection foreground when selected");
    }

    @Test
    void testGetListCellRendererComponent_AllPokemonTypes() {
        // Test with all PokemonType values to ensure they all render properly
        for (PokemonType type : PokemonType.values()) {
            Component result = renderer.getListCellRendererComponent(
                    mockList, type, 0, false, false);

            assertEquals(type.name(), renderer.getText(),
                    "Should show the enum name for " + type);
        }
    }

    @Test
    void testGetListCellRendererComponent_CellHasFocus() {
        // Test when cell has focus
        Component result = renderer.getListCellRendererComponent(
                mockList, PokemonType.PSYCHIC, 0, false, true);

        // Result should still be the renderer, with appropriate text
        assertSame(renderer, result, "Should return itself");
        assertEquals("PSYCHIC", renderer.getText(),
                "Should show correct type regardless of focus");
    }

    @Test
    void testGetListCellRendererComponent_DifferentIndex() {
        // Test with different index values
        renderer.getListCellRendererComponent(
                mockList, PokemonType.DRAGON, 5, false, false);

        assertEquals("DRAGON", renderer.getText(),
                "Should show correct type regardless of index");
    }
}
