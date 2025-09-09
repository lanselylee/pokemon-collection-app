package view;

import model.Pokemon;
import model.PokemonType;
import controller.IPokemonController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.io.File;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * Panel for displaying a list of Pokemon.
 * Uses a JList to show Pokemon names and allows for selection.
 */
public class PokemonListPanel extends JPanel {
    private final IPokemonController controller;
    private final DefaultListModel<CheckBoxListItem> listModel;
    private final JList<CheckBoxListItem> pokemonList;
    private Consumer<Pokemon> selectionListener;
    private List<Pokemon> fullPokemonList; // Store the complete list
    private JTextField searchField;
    private JComboBox<PokemonType> typeFilter;
    private JComboBox<SortOption> sortOptions;
    private JButton saveButton;
    private int nextTeamNumber = 1;  // For auto-incrementing file names
    private JLabel viewingLabel;

    /**
     * Enum for sort options.
     */ 
    private enum SortOption {
        NAME_ASC("Name A-Z"),
        NAME_DESC("Name Z-A"),
        ID_ASC("ID ↑"),
        ID_DESC("ID ↓"),
        HP_DESC("HP ↓");

        private final String label;

        SortOption(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    /**
     * Constructs a new PokemonListPanel.
     *
     * @param controller the Pokemon controller
     */
    public PokemonListPanel(IPokemonController controller) {
        this.controller = controller;
        this.listModel = new DefaultListModel<>();
        this.pokemonList = new JList<>(listModel);

        initializeComponents();
        setupListeners();
    }

    /**
     * Initializes the panel components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 255)); // Light blue-tinted background

        // Top control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Configure the JList with checkbox renderer
        configureList();

        // Bottom panel with only save button
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        // Title and search panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Stylized title
        JLabel titleLabel = new JLabel("Pokédex", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Add viewing label
        viewingLabel = new JLabel("Currently Viewing: All Pokémon", SwingConstants.CENTER);
        viewingLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        viewingLabel.setForeground(new Color(100, 100, 100));
        topPanel.add(viewingLabel, BorderLayout.SOUTH);

        // Search panel with icon
        JPanel searchPanel = createSearchPanel();
        topPanel.add(searchPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Filter and sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filterSortPanel.setOpaque(false);

        // Type filter
        typeFilter = new JComboBox<>();
        typeFilter.addItem(null); // "All" option
        for (PokemonType type : PokemonType.values()) {
            typeFilter.addItem(type);
        }
        typeFilter.setRenderer(new TypeComboBoxRenderer());
        filterSortPanel.add(new JLabel("Type:"));
        filterSortPanel.add(typeFilter);

        // Sort options
        sortOptions = new JComboBox<>(SortOption.values());
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortOptions);

        panel.add(filterSortPanel, BorderLayout.CENTER);
        return panel;
    }
    /** 
     * Configures the JList with the custom cell renderer and styling.
     */
    private void configureList() {
        // Use custom cell renderer with checkboxes
        pokemonList.setCellRenderer(new PokemonCheckBoxListRenderer());
        pokemonList.setFixedCellHeight(40); // Smaller height

        // Enable multiple selection
        pokemonList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pokemonList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add custom styling to the scroll pane
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the bottom panel with the save button.
     *
     * @return The JPanel containing the save button.
     */ 
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);

        saveButton = createStyledButton("Save Team", "💾");
        panel.add(saveButton);// Add save button to the panel

        return panel;
    }

    /**
     * Creates a styled button with an icon and text.
     *
     * @param text The text to display on the button.
     * @param icon The icon to display on the button.
     * @return The JButton created.
     */  
    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(51, 51, 51));
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(5, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Sets up event listeners for the panel.
     */
    private void setupListeners() {
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }
        });
        typeFilter.addActionListener(e -> filterAndSortList());
        sortOptions.addActionListener(e -> filterAndSortList());
        saveButton.addActionListener(e -> saveSelectedPokemon());

        // Handle checkbox clicks
        pokemonList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = pokemonList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    CheckBoxListItem item = listModel.getElementAt(index);
                    Rectangle cellBounds = pokemonList.getCellBounds(index, index);

                    // Calculate checkbox bounds (assuming 20px width from left edge)
                    Rectangle checkBoxBounds = new Rectangle(
                            cellBounds.x + 4,  // Left padding
                            cellBounds.y + (cellBounds.height - 16) / 2,  // Vertically centered
                            16,  // Checkbox width
                            16   // Checkbox height
                    );

                    // If click is within checkbox bounds, toggle selection
                    if (checkBoxBounds.contains(e.getPoint())) {
                        item.setSelected(!item.isSelected());
                        pokemonList.repaint();
                    } else {
                        // If clicked outside checkbox, just trigger detail view
                        if (selectionListener != null) {
                            selectionListener.accept(item.getPokemon());
                        }
                    }
                }
            }
        });
    }

    /**
     * Filters and sorts the list of Pokemon based on the selected type and sort option.
     */
    private void filterAndSortList() {
        if (fullPokemonList == null) return;

        List<Pokemon> filtered = fullPokemonList.stream()
                .filter(pokemon -> typeFilter.getSelectedItem() == null ||
                        pokemon.getTypes().contains(typeFilter.getSelectedItem()))
                .collect(Collectors.toList());

        SortOption selectedSort = (SortOption) sortOptions.getSelectedItem();
        switch (selectedSort) {
            case NAME_ASC -> filtered.sort(Comparator.comparing(Pokemon::getName));
            case NAME_DESC -> filtered.sort(Comparator.comparing(Pokemon::getName).reversed());
            case ID_ASC -> filtered.sort(Comparator.comparingInt(Pokemon::getId));
            case ID_DESC -> filtered.sort(Comparator.comparingInt(Pokemon::getId).reversed());
            case HP_DESC -> filtered.sort(Comparator.comparingInt((Pokemon p) -> p.getStats().getHp()).reversed());
        }

        updateListContent(filtered);
    }
    /** 
     * Sets up the search functionality.
     */
    private void setupSearch() {
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }
        });
    }

    /**
     * Filters the list based on the search text.
     */ 
    private void filterList() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (fullPokemonList != null) {
            listModel.clear();

            if (searchText.isEmpty()) {
                fullPokemonList.forEach(pokemon -> listModel.addElement(new CheckBoxListItem(pokemon)));
            } else {
                fullPokemonList.stream()
                        .filter(pokemon -> pokemon.getName().toLowerCase().contains(searchText) ||
                                String.valueOf(pokemon.getId()).contains(searchText))
                        .forEach(pokemon -> listModel.addElement(new CheckBoxListItem(pokemon)));
            }

            // Select first item if list is not empty
            if (listModel.size() > 0) {
                pokemonList.setSelectedIndex(0);
            }
        }
    }

    /**
     * Updates the list with new Pokemon data.
     *
     * @param pokemonList the list of Pokemon to display
     */
    public void updatePokemonList(List<Pokemon> pokemonList) {
        fullPokemonList = new ArrayList<>(pokemonList);
        updateListContent(pokemonList);
        viewingLabel.setText("Currently Viewing: All Pokémon");
    }

    /**
     * Sets up a listener for Pokemon selection events.
     *
     * @param listener the consumer that will handle the selected Pokemon
     */
    public void setPokemonSelectionListener(Consumer<Pokemon> listener) {
        this.selectionListener = listener;
        // Update the list selection listener
        pokemonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                CheckBoxListItem selectedItem = pokemonList.getSelectedValue();
                if (selectedItem != null && selectionListener != null) {
                    selectionListener.accept(selectedItem.getPokemon());
                }
            }
        });
    }

    /**
     * Creates the search panel with an icon and text field.
     *
     * @return The JPanel containing the search panel.
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);

        // Search icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchPanel.add(searchIcon, BorderLayout.WEST);

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search Pokémon...");
        searchPanel.add(searchField, BorderLayout.CENTER);

        setupSearch(); // Initialize search functionality
        return searchPanel;
    }

    /**
     * Saves the selected Pokémon to a file.
     */
    private void saveSelectedPokemon() {
        List<Pokemon> selectedPokemon = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            CheckBoxListItem item = listModel.getElementAt(i);
            if (item.isSelected()) {
                selectedPokemon.add(item.getPokemon());
            }
        }

        if (selectedPokemon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one Pokémon to save.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Find next available file name
        String fileName;
        do {
            fileName = String.format("team%d.json", nextTeamNumber++);
        } while (new File(fileName).exists());

        controller.saveCollection(fileName);
        JOptionPane.showMessageDialog(this,
                "Team saved successfully to " + fileName,
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
    * Updates the content of the displayed JList based on a given filtered list of Pokemon.
    * <p>
    * Note: This does not change the fullPokemonList data source.
    *
    * @param filtered The filtered list of Pokemon to display.
    */
    private void updateListContent(List<Pokemon> filtered) {
        listModel.clear();
        filtered.forEach(pokemon -> listModel.addElement(new CheckBoxListItem(pokemon)));
    }
}
