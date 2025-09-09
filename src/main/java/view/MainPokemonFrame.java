package view;

import model.Pokemon;
import controller.IPokemonController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main frame for the Pokemon application.
 * Implements the IPokemonView interface and acts as the primary UI container.
 */
public class MainPokemonFrame extends JFrame implements IPokemonView {

    private IPokemonController controller;
    private PokemonListPanel listPanel;
    private PokemonDetailPanel detailPanel;

    /**
     * Constructor that initializes the frame with a controller reference.
     *
     * @param controller the Pokemon controller
     */
    public MainPokemonFrame(IPokemonController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Initializes UI components and sets up layout.
     */
    private void initComponents() {
        // Set frame properties
        setTitle("Pokéllection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Set up the main layout
        setLayout(new BorderLayout());

        // Add logo with error handling
        try {
            // Use absolute path to resources directory
            ImageIcon logoIcon = new ImageIcon("src/main/resources/pokemon_logo.png");
            if (logoIcon.getImage().getWidth(null) == -1) {
                System.err.println("Failed to load pokemon_logo.png - using text fallback");
                createStyledTitle();
            } else {
                // Scale the image to a reasonable size (adjust width as needed)
                Image scaledImage = logoIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                JPanel logoPanel = new JPanel(new BorderLayout());
                logoPanel.setBackground(new Color(245, 245, 255));
                logoPanel.add(logoLabel, BorderLayout.CENTER);
                add(logoPanel, BorderLayout.NORTH);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            createStyledTitle();
        }

        // Create split pane for list and detail panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3); // Give 30% space to list panel
        splitPane.setDividerLocation(500); // Set initial divider position

        // Initialize panels
        listPanel = new PokemonListPanel(controller);
        detailPanel = new PokemonDetailPanel();

        // Add panels to split pane
        splitPane.setLeftComponent(listPanel);
        splitPane.setRightComponent(detailPanel);

        // Add split pane to frame
        add(splitPane, BorderLayout.CENTER);

        // Set up Pokemon selection listener
        listPanel.setPokemonSelectionListener(this::showPokemonDetails);
    }

    /**
     * Creates a styled title as a fallback when logo can't be loaded.
     */
    private void createStyledTitle() {
        JLabel titleLabel = new JLabel("Pokédex", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 55, 153));  // Pokemon blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 255));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Displays the main application window.
     */
    @Override
    public void display() {
        setVisible(true);
    }

    /**
     * Updates the Pokemon list displayed in the view.
     *
     * @param pokemonList the list of Pokemon to display
     */
    @Override
    public void updatePokemonList(List<Pokemon> pokemonList) {
        System.out.println("Pokemon list updated with " + pokemonList.size() + " Pokemon.");
        listPanel.updatePokemonList(pokemonList);
    }

    /**
     * Shows detailed information about a specific Pokemon.
     *
     * @param pokemon the Pokemon to display details for
     */
    @Override
    public void showPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            System.out.println("Cannot show details for null Pokemon");
        } else {
            System.out.println("Showing details for Pokemon: " + pokemon.getName());
        }

        if (detailPanel != null) {
            detailPanel.displayPokemonDetails(pokemon);
        }
    }
}
