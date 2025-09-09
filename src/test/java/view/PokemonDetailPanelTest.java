package view;

import model.Pokemon;
import model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonDetailPanelTest {

    private PokemonDetailPanel detailPanel;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JPanel statsPanel;
    private JPanel infoPanel;
    private Pokemon testPokemon;

    @BeforeEach
    public void setUp() {
        detailPanel = new PokemonDetailPanel();

        nameLabel = getPrivateField(detailPanel, "nameLabel");
        imageLabel = getPrivateField(detailPanel, "imageLabel");
        statsPanel = getPrivateField(detailPanel, "statsPanel");
        infoPanel = getPrivateField(detailPanel, "infoPanel");

        testPokemon = createTestPokemon();
    }

    @Test
    public void testInitialization() {
        assertNotNull(nameLabel, "Name label should be initialized");
        assertNotNull(imageLabel, "Image label should be initialized");
        assertNotNull(statsPanel, "Stats panel should be initialized");
        assertNotNull(infoPanel, "Info panel should be initialized");
    }

    @Test
    public void testDisplayPokemonDetails() {
        detailPanel.displayPokemonDetails(testPokemon);

        assertEquals("Bulbasaur", nameLabel.getText(), "Name should be capitalized");
        assertTrue(infoPanel.getComponentCount() > 0, "Info panel should have children");
        assertTrue(statsPanel.getComponentCount() > 0, "Stats panel should have children");
    }

    @Test
    public void testDisplayPokemonDetailsNull() {
        detailPanel.displayPokemonDetails(null);

        assertEquals("Select a Pokemon", nameLabel.getText(),
                "Name should be set to default message");
        assertNull(imageLabel.getIcon(), "Image should be cleared");
        assertEquals(1, infoPanel.getComponentCount(),
                "Info panel should have one component");
    }

    @Test
    public void testCreateStatsChart() throws Exception {
        Method createStatsChartMethod = detailPanel.getClass().getDeclaredMethod(
                "createStatsChart", Pokemon.PokemonStats.class);
        createStatsChartMethod.setAccessible(true);

        JPanel statsChart = (JPanel) createStatsChartMethod.invoke(
                detailPanel, testPokemon.getStats());

        assertEquals(6, statsChart.getComponentCount(),
                "Stats chart should have 6 components (one per stat)");
    }

    @Test
    public void testCreateTypeLabel() throws Exception {
        Method createTypeLabelMethod = detailPanel.getClass().getDeclaredMethod(
                "createTypeLabel", PokemonType.class);
        createTypeLabelMethod.setAccessible(true);

        JLabel typeLabel = (JLabel) createTypeLabelMethod.invoke(
                detailPanel, PokemonType.GRASS);

        assertEquals("Grass", typeLabel.getText(),
                "Type label should have capitalized type name");
        assertTrue(typeLabel.isOpaque(),
                "Type label should have background color");
    }

    @Test
    public void testCapitalizeFirst() throws Exception {
        Method capitalizeFirstMethod = detailPanel.getClass().getDeclaredMethod(
                "capitalizeFirst", String.class);
        capitalizeFirstMethod.setAccessible(true);

        assertEquals("Bulbasaur", capitalizeFirstMethod.invoke(detailPanel, "bulbasaur"),
                "Should capitalize first letter");
        assertEquals("Bulbasaur", capitalizeFirstMethod.invoke(detailPanel, "Bulbasaur"),
                "Should handle already capitalized");
        assertEquals("", capitalizeFirstMethod.invoke(detailPanel, ""),
                "Should handle empty string");
        assertEquals(null, capitalizeFirstMethod.invoke(detailPanel, (String)null),
                "Should handle null");
    }

    @Test
    public void testLoadPokemonImage() throws Exception {
        Method loadPokemonImageMethod = detailPanel.getClass().getDeclaredMethod(
                "loadPokemonImage", String.class);
        loadPokemonImageMethod.setAccessible(true);

        Map<String, ImageIcon> mockCache = new HashMap<>();
        mockCache.put(testPokemon.getImageUrl(), new ImageIcon());
        setPrivateField(detailPanel, "imageCache", mockCache);

        loadPokemonImageMethod.invoke(detailPanel, testPokemon.getImageUrl());

        assertNotNull(imageLabel.getIcon(), "Image label should have an icon set");
    }

    @Test
    public void testDisplayPokemonWithNoTypes() {
        Pokemon noTypesPokemon = new Pokemon(
                999, "missingno", "http://example.com/missingno.png",
                new ArrayList<>(),
                new Pokemon.PokemonStats(33, 33, 33, 33, 33, 33)
        );

        detailPanel.displayPokemonDetails(noTypesPokemon);

        assertEquals("Missingno", nameLabel.getText(), "Name should be displayed correctly");
        assertTrue(statsPanel.getComponentCount() > 0, "Stats panel should have content");
    }

    @Test
    public void testDisplayPokemonWithZeroStats() {
        Pokemon zeroStatsPokemon = new Pokemon(
                998, "nullmon", "http://example.com/nullmon.png",
                Arrays.asList(PokemonType.NORMAL),
                new Pokemon.PokemonStats(0, 0, 0, 0, 0, 0)
        );

        detailPanel.displayPokemonDetails(zeroStatsPokemon);

        assertEquals("Nullmon", nameLabel.getText(), "Name should be displayed correctly");
        assertTrue(statsPanel.getComponentCount() > 0, "Stats panel should have content");
    }

    @Test
    public void testDisplayPokemonWithExtremeStats() {
        Pokemon extremeStatsPokemon = new Pokemon(
                997, "godmon", "http://example.com/godmon.png",
                Arrays.asList(PokemonType.NORMAL),
                new Pokemon.PokemonStats(999, 999, 999, 999, 999, 999)
        );

        detailPanel.displayPokemonDetails(extremeStatsPokemon);

        assertEquals("Godmon", nameLabel.getText(), "Name should be displayed correctly");
        assertTrue(statsPanel.getComponentCount() > 0, "Stats panel should have content");
    }

    @Test
    public void testImageLoadingError() throws Exception {
        SwingWorker<ImageIcon, Void> errorWorker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                throw new IOException("Simulated image loading failure");
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    imageLabel.setText("Error loading image");
                    imageLabel.setIcon(null);
                }
            }
        };
        errorWorker.execute();

        Pokemon brokenImagePokemon = new Pokemon(
                996, "glitchmon", "http://nonexistent.url/image.png",
                Arrays.asList(PokemonType.NORMAL),
                new Pokemon.PokemonStats(50, 50, 50, 50, 50, 50)
        );

        Method loadPokemonImageMethod = detailPanel.getClass().getDeclaredMethod(
                "loadPokemonImage", String.class);
        loadPokemonImageMethod.setAccessible(true);

        Map<String, ImageIcon> emptyCache = new HashMap<>();
        setPrivateField(detailPanel, "imageCache", emptyCache);

        loadPokemonImageMethod.invoke(detailPanel, brokenImagePokemon.getImageUrl());

        assertEquals("Loading...", imageLabel.getText(), "Should display loading message");

        imageLabel.setText("Error loading image");
        imageLabel.setIcon(null);

        assertEquals("Error loading image", imageLabel.getText(), "Should display error message");
        assertNull(imageLabel.getIcon(), "Icon should be null");
    }

    @Test
    public void testCapitalizeFirstWithSpecialCharacters() throws Exception {
        Method capitalizeFirstMethod = detailPanel.getClass().getDeclaredMethod(
                "capitalizeFirst", String.class);
        capitalizeFirstMethod.setAccessible(true);

        assertEquals("123test", capitalizeFirstMethod.invoke(detailPanel, "123test"),
                "Should handle numeric prefix");
        assertEquals("-test", capitalizeFirstMethod.invoke(detailPanel, "-test"),
                "Should handle special character prefix");
        assertEquals("Test", capitalizeFirstMethod.invoke(detailPanel, "TEST"),
                "Should convert to title case regardless of input");
        assertEquals(" test", capitalizeFirstMethod.invoke(detailPanel, " test"),
                "Should handle leading whitespace");
    }

    private Pokemon createTestPokemon() {
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        return new Pokemon(1, "bulbasaur", "http://example.com/bulbasaur.png",
                Arrays.asList(PokemonType.GRASS, PokemonType.POISON), stats);
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
