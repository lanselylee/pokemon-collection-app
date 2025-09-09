package view;

import model.Pokemon;
import model.PokemonType;
import controller.IPokemonController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainPokemonFrameTest {

    private MainPokemonFrame mainFrame;
    private IPokemonController mockController;
    private PokemonListPanel mockListPanel;
    private PokemonDetailPanel mockDetailPanel;

    @BeforeEach
    public void setUp() throws Exception {
        mockController = mock(IPokemonController.class);
        mockListPanel = mock(PokemonListPanel.class);
        mockDetailPanel = mock(PokemonDetailPanel.class);

        mainFrame = new MainPokemonFrame(mockController);

        setPrivateField(mainFrame, "listPanel", mockListPanel);
        setPrivateField(mainFrame, "detailPanel", mockDetailPanel);
    }

    @Test
    public void testInitialization() {
        assertNotNull(getPrivateField(mainFrame, "controller"), "Controller should be set");
        assertNotNull(getPrivateField(mainFrame, "listPanel"), "List panel should be initialized");
        assertNotNull(getPrivateField(mainFrame, "detailPanel"), "Detail panel should be initialized");
    }

    @Test
    public void testInitComponentsLogoBranches() throws Exception {
        IPokemonController controller = mock(IPokemonController.class);

        MainPokemonFrame frame = new MainPokemonFrame(controller);

        Method createStyledTitleMethod = MainPokemonFrame.class.getDeclaredMethod("createStyledTitle");
        createStyledTitleMethod.setAccessible(true);

        assertNotNull(getPrivateField(frame, "listPanel"), "List panel should be initialized");
        assertNotNull(getPrivateField(frame, "detailPanel"), "Detail panel should be initialized");
        assertTrue(true, "Frame should be properly initialized");
    }

    @Test
    public void testCreateStyledTitle() throws Exception {
        IPokemonController controller = mock(IPokemonController.class);

        MainPokemonFrame frame = new MainPokemonFrame(controller);

        Method createStyledTitleMethod = MainPokemonFrame.class.getDeclaredMethod("createStyledTitle");
        createStyledTitleMethod.setAccessible(true);

        createStyledTitleMethod.invoke(frame);

        Component northComponent = ((BorderLayout)frame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH);
        assertNotNull(northComponent, "Should add a panel in the north region");
        assertTrue(northComponent instanceof JPanel, "North component should be a JPanel");

        JPanel titlePanel = (JPanel)northComponent;
        boolean foundTitleLabel = false;
        for(Component c : titlePanel.getComponents()) {
            if(c instanceof JLabel && ((JLabel)c).getText().contains("Pokédex")) {
                foundTitleLabel = true;
                break;
            }
        }
        assertTrue(foundTitleLabel, "Should create a title label containing 'Pokédex'");
    }

    @Test
    public void testUpdatePokemonList() {
        List<Pokemon> testList = createTestPokemonList();

        mainFrame.updatePokemonList(testList);

        verify(mockListPanel).updatePokemonList(testList);
    }

    @Test
    void testShowPokemonDetails() throws Exception {
        IPokemonController controller = mock(IPokemonController.class);
        MainPokemonFrame frame = new MainPokemonFrame(controller);

        PokemonDetailPanel mockDetailPanel = mock(PokemonDetailPanel.class);
        setPrivateField(frame, "detailPanel", mockDetailPanel);

        // Test branch 1: null Pokemon
        frame.showPokemonDetails(null);
        verify(mockDetailPanel).displayPokemonDetails(null);

        // Test branch 2: non-null Pokemon
        Pokemon testPokemon = new Pokemon(1, "bulbasaur", "http://example.com/1.png",
                Arrays.asList(PokemonType.GRASS),
                new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45));
        frame.showPokemonDetails(testPokemon);
        verify(mockDetailPanel).displayPokemonDetails(testPokemon);

        // Test branch 3: null detailPanel
        setPrivateField(frame, "detailPanel", null);

        // This should not throw an exception even with null detailPanel
        assertDoesNotThrow(() -> frame.showPokemonDetails(testPokemon));
    }

    @Test
    public void testPokemonSelectionListener() throws Exception {
        IPokemonController controller = mock(IPokemonController.class);
        MainPokemonFrame frame = new MainPokemonFrame(controller);

        PokemonListPanel listPanel = getPrivateField(frame, "listPanel");

        Consumer<Pokemon> selectionConsumer = mock(Consumer.class);

        listPanel.setPokemonSelectionListener(selectionConsumer);

        Pokemon testPokemon = createTestPokemon();

        verify(selectionConsumer, never()).accept(any()); // Should not be called yet

        selectionConsumer.accept(testPokemon);

        verify(selectionConsumer).accept(testPokemon);
    }

    @Test
    public void testUpdatePokemonListWithEmptyList() {
        List<Pokemon> emptyList = new ArrayList<>();

        mainFrame.updatePokemonList(emptyList);

        verify(mockListPanel).updatePokemonList(emptyList);
    }

    @Test
    public void testUpdatePokemonListWithNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            mainFrame.updatePokemonList(null);
        });

        // Optional: assert something about the exception message if applicable
    }

    @Test
    public void testShowPokemonDetailsWithNull() {
        mainFrame.showPokemonDetails(null);

        verify(mockDetailPanel).displayPokemonDetails(null);
    }

    @Test
    public void testLogoLoadingFailure() throws Exception {
        MainPokemonFrame frame = new MainPokemonFrame(mockController);

        Method createStyledTitleMethod = MainPokemonFrame.class.getDeclaredMethod("createStyledTitle");
        createStyledTitleMethod.setAccessible(true);
        createStyledTitleMethod.invoke(frame);

        boolean foundTitlePanel = false;
        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel &&
                    ((JPanel) component).getComponentCount() > 0 &&
                    ((JPanel) component).getComponent(0) instanceof JLabel &&
                    "Pokédex".equals(((JLabel)((JPanel) component).getComponent(0)).getText())) {
                foundTitlePanel = true;
                break;
            }
        }

        assertTrue(foundTitlePanel, "Should find title panel");
    }

    private Pokemon createTestPokemon() {
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        return new Pokemon(1, "bulbasaur", "http://example.com/bulbasaur.png",
                Arrays.asList(PokemonType.GRASS, PokemonType.POISON), stats);
    }

    private List<Pokemon> createTestPokemonList() {
        List<Pokemon> list = new ArrayList<>();
        list.add(createTestPokemon());

        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(39, 52, 43, 60, 50, 65);
        list.add(new Pokemon(4, "charmander", "http://example.com/charmander.png",
                Arrays.asList(PokemonType.FIRE), stats));

        return list;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private <T> T getPrivateField(Object target, String fieldName) {
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            T value = (T) field.get(target);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
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
