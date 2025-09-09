package view;

import model.Pokemon;
import model.PokemonType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;

/**
 * Panel for displaying detailed information about a selected Pokemon.
 * Shows the Pokemon's image, types, and stats with graphical representation.
 */
public class PokemonDetailPanel extends JPanel {

    // UI Components as per UML
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JPanel statsPanel;
    private JPanel infoPanel;

    // Cache for Pokemon images to avoid reloading
    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    // Color mapping for Pokemon types
    private final Map<PokemonType, Color> typeColors = new HashMap<>();

    /**
     * Constructor initializes the panel with a reference to the controller.
     */
    public PokemonDetailPanel() {
        initTypeColors();
        initComponents();
    }

    /**
     * Initializes the type-to-color mapping for Pokemon type badges.
     */
    private void initTypeColors() {
        typeColors.put(PokemonType.NORMAL, new Color(168, 168, 120));
        typeColors.put(PokemonType.FIRE, new Color(240, 128, 48));
        typeColors.put(PokemonType.WATER, new Color(104, 144, 240));
        typeColors.put(PokemonType.GRASS, new Color(120, 200, 80));
        typeColors.put(PokemonType.ELECTRIC, new Color(248, 208, 48));
        typeColors.put(PokemonType.ICE, new Color(152, 216, 216));
        typeColors.put(PokemonType.FIGHTING, new Color(192, 48, 40));
        typeColors.put(PokemonType.POISON, new Color(160, 64, 160));
        typeColors.put(PokemonType.GROUND, new Color(224, 192, 104));
        typeColors.put(PokemonType.FLYING, new Color(168, 144, 240));
        typeColors.put(PokemonType.PSYCHIC, new Color(248, 88, 136));
        typeColors.put(PokemonType.BUG, new Color(168, 184, 32));
        typeColors.put(PokemonType.ROCK, new Color(184, 160, 56));
        typeColors.put(PokemonType.GHOST, new Color(112, 88, 152));
        typeColors.put(PokemonType.DRAGON, new Color(112, 56, 248));
        typeColors.put(PokemonType.DARK, new Color(112, 88, 72));
        typeColors.put(PokemonType.STEEL, new Color(184, 184, 208));
        typeColors.put(PokemonType.FAIRY, new Color(238, 153, 172));
    }

    /**
     * Initializes UI components and sets up layout.
     */
    private void initComponents() {
        // Set panel properties
        setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 255));

        // Create name label at the top
        nameLabel = new JLabel("Loading Pokemon data...");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel, BorderLayout.NORTH);

        // Create center panel with image and info side by side
        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setBackground(new Color(245, 245, 255));

        // Image panel on the left
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        imagePanel.setBackground(Color.WHITE);

        // Add a label for image panel to display the Pokemon image
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        centerPanel.add(imagePanel, BorderLayout.CENTER);

        // Info panel on the right
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        infoPanel.setBackground(new Color(245, 245, 255));

        // Add placeholder content for info panel
        JLabel placeholderLabel = new JLabel("No Pokemon selected");
        placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(placeholderLabel);

        centerPanel.add(infoPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Create stats panel at the bottom
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Stats");
        titledBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 16));
        statsPanel.setBorder(titledBorder);
        statsPanel.setMinimumSize(new Dimension(0, 200));
        statsPanel.setPreferredSize(new Dimension(0, 200));
        statsPanel.setBackground(new Color(245, 245, 255));
        add(statsPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays detailed information about a Pokemon.
     *
     * @param pokemon the Pokemon to display
     */
    public void displayPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            clearDisplay();
            return;
        }

        // Update the name
        nameLabel.setText(capitalizeFirst(pokemon.getName()));

        // Load and display the image asynchronously
        loadPokemonImage(pokemon.getImageUrl());

        // Update info panel with Pokemon details
        updateInfoPanel(pokemon);

        // Create and display stats panel
        statsPanel.removeAll();
        JPanel statsChart = createStatsChart(pokemon.getStats());
        statsPanel.add(statsChart);

        // Refresh UI
        revalidate();
        repaint();
    }

    /**
     * Creates a graphical representation of Pokemon stats.
     *
     * @param stats the Pokemon's stats
     * @return a panel containing the stats chart
     */
    private JPanel createStatsChart(Pokemon.PokemonStats stats) {
        JPanel chartPanel = new JPanel(new GridLayout(6, 1, 0, 8));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartPanel.setBackground(new Color(245, 245, 255));

        // Max stat value for scaling (255 is the max possible in Pokemon games)
        final int MAX_STAT = 255;

        // Create bar for each stat
        addStatBar(chartPanel, "HP", stats.getHp(), MAX_STAT, new Color(255, 50, 50));
        addStatBar(chartPanel, "Attack", stats.getAttack(), MAX_STAT, new Color(240, 128, 48));
        addStatBar(chartPanel, "Defense", stats.getDefense(), MAX_STAT, new Color(120, 200, 88));
        addStatBar(chartPanel, "Special Attack", stats.getSpecialAttack(), MAX_STAT, new Color(170, 85, 170));
        addStatBar(chartPanel, "Special Defense", stats.getSpecialDefense(), MAX_STAT, new Color(248, 208, 48));
        addStatBar(chartPanel, "Speed", stats.getSpeed(), MAX_STAT, new Color(104, 144, 240));

        return chartPanel;
    }

    /**
     * Adds a stat bar to the chart.
     *
     * @param panel     the panel to add the bar to
     * @param statName  the name of the stat
     * @param statValue the value of the stat
     * @param maxValue  the maximum value for scaling
     * @param barColor  the color of the bar
     */
    private void addStatBar(JPanel panel, String statName, int statValue, int maxValue, Color barColor) {
        JPanel statPanel = new JPanel(new BorderLayout(10, 0));
        statPanel.setBackground(new Color(245, 245, 255));
        statPanel.setOpaque(true);

        // Add stat name label
        JLabel nameLabel = new JLabel(statName);
        nameLabel.setPreferredSize(new Dimension(110, 20));
        nameLabel.setBackground(new Color(245, 245, 255));
        nameLabel.setOpaque(true);
        statPanel.add(nameLabel, BorderLayout.WEST);

        // Add stat value label
        JLabel valueLabel = new JLabel(String.valueOf(statValue));
        valueLabel.setPreferredSize(new Dimension(30, 20));
        valueLabel.setOpaque(true);
        valueLabel.setBackground(new Color(245, 245, 255));
        statPanel.add(valueLabel, BorderLayout.EAST);

        // Draw bar for visualization
        JPanel barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.WHITE);
                int width = (int) ((double) statValue / maxValue * getWidth());
                g.setColor(barColor);
                g.fillRect(0, 0, width, getHeight());
            }
        };
        barPanel.setPreferredSize(new Dimension(300, 20));
        barPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        statPanel.add(barPanel, BorderLayout.CENTER);
        panel.add(statPanel);
    }

    /**
     * Updates the info panel with Pokemon details.
     *
     * @param pokemon the Pokemon to display info for
     */
    private void updateInfoPanel(Pokemon pokemon) {
        infoPanel.removeAll();

        // Add Pokemon ID
        JLabel idLabel = new JLabel("Pokédex ID: #" + pokemon.getId());
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        // Add Pokemon types
        JLabel typesLabel = new JLabel("Types:");
        typesLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        typesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(typesLabel);

        // Create types panel to hold the type tags
        JPanel typesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        typesPanel.setBackground(new Color(245, 245, 255));

        List<PokemonType> types = pokemon.getTypes();
        for (PokemonType type : types) {
            JLabel typeLabel = createTypeLabel(type);
            typesPanel.add(typeLabel);
        }

        infoPanel.add(typesPanel);
        infoPanel.add(Box.createVerticalStrut(15));

        // Add basic stats summary
        Pokemon.PokemonStats stats = pokemon.getStats();
        JLabel statsSummaryLabel = new JLabel("Base Stats Total: " +
                (stats.getHp() + stats.getAttack() + stats.getDefense() +
                        stats.getSpecialAttack() + stats.getSpecialDefense() + stats.getSpeed()));
        statsSummaryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statsSummaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(statsSummaryLabel);
    }

    /**
     * Creates a styled label for a Pokemon type.
     *
     * @param type the Pokemon type
     * @return a styled label
     */
    private JLabel createTypeLabel(PokemonType type) {
        JLabel label = new JLabel(capitalizeFirst(type.name()));
        label.setOpaque(true);
        label.setBackground(typeColors.getOrDefault(type, Color.GRAY));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    /**
     * Loads a Pokemon image asynchronously.
     *
     * @param imageUrl the URL of the image to load
     */
    private void loadPokemonImage(String imageUrl) {
        // Use cached image if available
        if (imageCache.containsKey(imageUrl)) {
            imageLabel.setIcon(imageCache.get(imageUrl));
            return;
        }

        // Set a loading placeholder
        imageLabel.setIcon(null);
        imageLabel.setText("Loading...");

        // Use SwingWorker to load image asynchronously
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                URL url = new URL(imageUrl);
                BufferedImage image = ImageIO.read(url);

                // Scale the image to fit better
                int newWidth = 200;
                int newHeight = 200;
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                return new ImageIcon(scaledImage);
            }

            // Updates the UI with the loaded image or shows error message
            // - runs on EDT when background task completes
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    imageLabel.setText(null);
                    imageLabel.setIcon(icon);
                    // Add to cache for future use
                    imageCache.put(imageUrl, icon);
                } catch (InterruptedException | ExecutionException e) {
                    imageLabel.setText("Error loading image");
                    imageLabel.setIcon(null);
                }
            }
        };

        worker.execute();
    }

    /**
     * Clears the display when no Pokemon is selected.
     */
    private void clearDisplay() {
        nameLabel.setText("Select a Pokemon");
        imageLabel.setIcon(null);
        imageLabel.setText("No image");

        infoPanel.removeAll();
        JLabel placeholderLabel = new JLabel("No Pokemon selected");
        placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(placeholderLabel);

        statsPanel.removeAll();

        revalidate();
        repaint();
    }

    /**
     * Utility method to capitalize the first letter of a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
