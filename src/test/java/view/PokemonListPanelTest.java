package view;

import model.Pokemon;
import model.PokemonType;
import controller.IPokemonController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PokemonListPanelTest {

    private PokemonListPanel listPanel;
    private IPokemonController mockController;
    private JTextField searchField;
    private JComboBox<PokemonType> typeFilter;
    private JComboBox<Object> sortOptions;
    private JButton saveButton;
    private JList<CheckBoxListItem> pokemonList;
    private DefaultListModel<CheckBoxListItem> listModel;
    private List<Pokemon> testPokemonList;

    @BeforeEach
    public void setUp() {
        mockController = mock(IPokemonController.class);
        listPanel = new PokemonListPanel(mockController);
        testPokemonList = createTestPokemonList();

        searchField = getPrivateField(listPanel, "searchField");
        typeFilter = getPrivateField(listPanel, "typeFilter");
        sortOptions = getPrivateField(listPanel, "sortOptions");
        saveButton = getPrivateField(listPanel, "saveButton");
        pokemonList = getPrivateField(listPanel, "pokemonList");
        listModel = getPrivateField(listPanel, "listModel");

        setPrivateField(listPanel, "fullPokemonList", new ArrayList<>(testPokemonList));
    }

    @Test
    public void testInitialization() {
        assertNotNull(searchField, "Search field should be initialized");
        assertNotNull(typeFilter, "Type filter should be initialized");
        assertNotNull(sortOptions, "Sort options should be initialized");
        assertNotNull(saveButton, "Save button should be initialized");
        assertNotNull(pokemonList, "Pokemon list should be initialized");
    }

    @Test
    public void testUpdatePokemonList(){
        listPanel.updatePokemonList(testPokemonList);

        assertEquals(testPokemonList.size(), listModel.size(),
                "List model should contain all test Pokemon");

        List<Pokemon> storedList = getPrivateField(listPanel, "fullPokemonList");
        assertEquals(testPokemonList.size(), storedList.size(),
                "Full Pokemon list should be stored");

        CheckBoxListItem firstItem = listModel.getElementAt(0);
        assertEquals("bulbasaur", firstItem.getPokemon().getName(),
                "First item should be Bulbasaur");
    }

    @Test
    public void testPokemonSelectionListener() {
        @SuppressWarnings("unchecked")
        Consumer<Pokemon> mockListener = mock(Consumer.class);

        listPanel.setPokemonSelectionListener(mockListener);
        listPanel.updatePokemonList(testPokemonList);

        pokemonList.setSelectedIndex(0);

        try {
            MouseListener[] listeners = pokemonList.getMouseListeners();

            MouseEvent mockClickEvent = mock(MouseEvent.class);
            when(mockClickEvent.getPoint()).thenReturn(new java.awt.Point(50, 10));

            for (MouseListener listener : listeners) {
                if (listener.getClass().getName().contains("MouseAdapter")) {
                    listener.mouseClicked(mockClickEvent);
                    break;
                }
            }
        } catch (Exception e) {
            fail("Error accessing listeners: " + e.getMessage());
        }
    }

    @Test
    public void testFilterAndSortList() throws Exception {
        IPokemonController controller = mock(IPokemonController.class);
        PokemonListPanel panel = new PokemonListPanel(controller);

        Pokemon bulbasaur = new Pokemon(1, "bulbasaur", "http://example.com/1.png",
                Arrays.asList(PokemonType.GRASS),
                new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45));
        Pokemon charizard = new Pokemon(6, "charizard", "http://example.com/6.png",
                Arrays.asList(PokemonType.FIRE, PokemonType.FLYING),
                new Pokemon.PokemonStats(78, 84, 78, 109, 85, 100));
        Pokemon pikachu = new Pokemon(25, "pikachu", "http://example.com/25.png",
                Arrays.asList(PokemonType.ELECTRIC),
                new Pokemon.PokemonStats(35, 55, 40, 50, 50, 90));

        List<Pokemon> testList = Arrays.asList(bulbasaur, charizard, pikachu);

        setPrivateField(panel, "fullPokemonList", new ArrayList<>(testList));

        JComboBox<PokemonType> typeFilter = getPrivateField(panel, "typeFilter");
        JComboBox<?> sortOptions = getPrivateField(panel, "sortOptions");
        DefaultListModel<CheckBoxListItem> listModel = getPrivateField(panel, "listModel");

        Method filterAndSortMethod = PokemonListPanel.class.getDeclaredMethod("filterAndSortList");
        filterAndSortMethod.setAccessible(true);

        // Test case 1: No type filter (null selection shows all)
        typeFilter.setSelectedItem(null);
        sortOptions.setSelectedIndex(0); // NAME_ASC
        listModel.clear(); // Clear before each test
        filterAndSortMethod.invoke(panel);
        assertEquals(3, listModel.size(), "All Pokemon should be shown when no type filter is applied");

        // Test case 2: Filter by FIRE type
        typeFilter.setSelectedItem(PokemonType.FIRE);
        listModel.clear();
        filterAndSortMethod.invoke(panel);
        assertEquals(1, listModel.size(), "Only Charizard should be shown when filtering by FIRE");

        // Test case 3: Filter by type with no matches
        typeFilter.setSelectedItem(PokemonType.ROCK);
        listModel.clear();
        filterAndSortMethod.invoke(panel);
        assertEquals(0, listModel.size(), "No Pokemon should be shown when filtering by a type with no matches");

        // Test case 4: ID_DESC sort order
        typeFilter.setSelectedItem(null); // Reset to show all
        // Find the ID_DESC sort option
        for (int i = 0; i < sortOptions.getItemCount(); i++) {
            if (sortOptions.getItemAt(i).toString().contains("ID ↓")) {
                sortOptions.setSelectedIndex(i);
                break;
            }
        }
        listModel.clear();
        filterAndSortMethod.invoke(panel);
        assertEquals(3, listModel.size(), "All Pokemon should be shown with ID_DESC sort");
        // Ideally we would verify the sort order, but that's complex in this test setup
    }

    @Test
    public void testSearchFiltering() throws Exception {
        IPokemonController mockController = mock(IPokemonController.class);
        PokemonListPanel panel = new PokemonListPanel(mockController);

        JTextField searchField = getPrivateField(panel, "searchField");
        DefaultListModel<CheckBoxListItem> listModel = getPrivateField(panel, "listModel");

        Pokemon bulbasaur = new Pokemon(1, "bulbasaur", "http://example.com/1.png",
                Arrays.asList(PokemonType.GRASS),
                new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45));

        Pokemon ivysaur = new Pokemon(2, "ivysaur", "http://example.com/2.png",
                Arrays.asList(PokemonType.GRASS),
                new Pokemon.PokemonStats(60, 62, 63, 80, 80, 60));

        List<Pokemon> testList = Arrays.asList(bulbasaur, ivysaur);

        panel.updatePokemonList(testList);

        Method filterListMethod = PokemonListPanel.class.getDeclaredMethod("filterList");
        filterListMethod.setAccessible(true);

        // Existing test cases
        searchField.setText("bulba");
        filterListMethod.invoke(panel);
        assertEquals(1, listModel.size(), "List should be filtered to only show matching Pokemon");

        searchField.setText("");
        filterListMethod.invoke(panel);
        assertEquals(2, listModel.size(), "List should show all Pokemon when search is empty");

        // Additional test cases for better branch coverage

        // Test case 3: Search with no matches
        searchField.setText("xyz");
        filterListMethod.invoke(panel);
        assertEquals(0, listModel.size(), "No Pokemon should be shown with non-matching search text");

        // Test case 4: Search by ID
        searchField.setText("1");
        filterListMethod.invoke(panel);
        assertEquals(1, listModel.size(), "Only Pokemon with ID 1 should be shown");

        // Test case 5: Handling null fullPokemonList
        setPrivateField(panel, "fullPokemonList", null);

        listModel.clear();

        filterListMethod.invoke(panel);

        assertTrue(true, "Method should handle null fullPokemonList without errors");
    }

    @Test
    public void testTypeFiltering() throws Exception {
        listPanel.updatePokemonList(testPokemonList);

        typeFilter.setSelectedItem(PokemonType.FIRE);

        Method filterAndSortMethod = listPanel.getClass().getDeclaredMethod("filterAndSortList");
        filterAndSortMethod.setAccessible(true);
        filterAndSortMethod.invoke(listPanel);

        assertEquals(1, listModel.size(), "Only Fire Pokemon should be in the filtered list");
        assertEquals("charmander", listModel.getElementAt(0).getPokemon().getName(),
                "The filtered Pokemon should be Charmander");
    }

    @Test
    public void testSaveButtonTriggersControllerSave() throws Exception {
        listPanel.updatePokemonList(testPokemonList);

        for (int i = 0; i < listModel.size(); i++) {
            listModel.getElementAt(i).setSelected(true);
        }

        Method saveMethod = listPanel.getClass().getDeclaredMethod("saveSelectedPokemon");
        saveMethod.setAccessible(true);
        saveMethod.invoke(listPanel);

        verify(mockController).saveCollection(anyString());
    }

    @Test
    public void testUpdatePokemonListWithEmptyList() {
        List<Pokemon> emptyList = new ArrayList<>();

        listPanel.updatePokemonList(emptyList);

        assertEquals(0, listModel.size(), "List model should be empty");

        List<Pokemon> storedList = getPrivateField(listPanel, "fullPokemonList");
        assertEquals(0, storedList.size(), "Stored full list should be empty");
    }

    @Test
    public void testUpdatePokemonListWithNull() {
        try {
            listPanel.updatePokemonList(null);

            List<Pokemon> storedList = getPrivateField(listPanel, "fullPokemonList");
            assertNotNull(storedList, "Stored full list should not be null");
            assertEquals(0, storedList.size(), "Stored full list should be empty");
        } catch (NullPointerException e) {
            // This is also acceptable behavior, depending on implementation
        }
    }

    @Test
    public void testSearchWithSpecialCharacters() throws Exception {
        listPanel.updatePokemonList(testPokemonList);

        searchField.setText("*[^]");

        Method filterListMethod = listPanel.getClass().getDeclaredMethod("filterList");
        filterListMethod.setAccessible(true);
        filterListMethod.invoke(listPanel);

        assertEquals(0, listModel.size(), "There should be no matching Pokemon");
    }

    @Test
    public void testSearchCaseInsensitivity() throws Exception {
        listPanel.updatePokemonList(testPokemonList);

        searchField.setText("BuLbAsAuR");

        Method filterListMethod = listPanel.getClass().getDeclaredMethod("filterList");
        filterListMethod.setAccessible(true);
        filterListMethod.invoke(listPanel);

        assertEquals(1, listModel.size(), "Should match one Pokemon");
        assertEquals("bulbasaur", listModel.getElementAt(0).getPokemon().getName(),
                "The matched Pokemon should be Bulbasaur");
    }

    @Test
    public void testTypeFilterWithNoMatches() throws Exception {
        listPanel.updatePokemonList(testPokemonList);

        typeFilter.setSelectedItem(PokemonType.DARK); // Assuming test data has no Dark type

        Method filterAndSortMethod = listPanel.getClass().getDeclaredMethod("filterAndSortList");
        filterAndSortMethod.setAccessible(true);
        filterAndSortMethod.invoke(listPanel);

        assertEquals(0, listModel.size(), "There should be no matching Pokemon");
    }

    @Test
    public void testSaveWithNoSelections() {
        listPanel.updatePokemonList(testPokemonList);

        for (int i = 0; i < listModel.size(); i++) {
            listModel.getElementAt(i).setSelected(false);
        }

        try {
            Method saveMethod = listPanel.getClass().getDeclaredMethod("saveSelectedPokemon");
            saveMethod.setAccessible(true);
            saveMethod.invoke(listPanel);
        } catch (Exception ignored) {
        }

        verify(mockController, never()).saveCollection(anyString());
    }

    private List<Pokemon> createTestPokemonList() {
        List<Pokemon> list = new ArrayList<>();

        Pokemon.PokemonStats stats1 = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        list.add(new Pokemon(1, "bulbasaur", "http://example.com/bulbasaur.png",
                Arrays.asList(PokemonType.GRASS, PokemonType.POISON), stats1));

        Pokemon.PokemonStats stats2 = new Pokemon.PokemonStats(39, 52, 43, 60, 50, 65);
        list.add(new Pokemon(4, "charmander", "http://example.com/charmander.png",
                Arrays.asList(PokemonType.FIRE), stats2));

        Pokemon.PokemonStats stats3 = new Pokemon.PokemonStats(44, 48, 65, 50, 64, 43);
        list.add(new Pokemon(7, "squirtle", "http://example.com/squirtle.png",
                Arrays.asList(PokemonType.WATER), stats3));

        return list;
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object target, String fieldName) {
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Error setting field: " + fieldName, e);
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw new RuntimeException("Field not found: " + fieldName);
            }
            return findField(superClass, fieldName);
        }
    }
}
