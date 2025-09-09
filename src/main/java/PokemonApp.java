import controller.IPokemonController;
import controller.PokemonController;
import model.IPokemonModel;
import model.PokemonModel;
import view.IPokemonView;
import view.MainPokemonFrame;

import javax.swing.*;


/**
 * Main entry point for the Pokemon application.
 * Creates and connects the Model, View, and Controller components.
 */
public class PokemonApp {

    /**
     * Main method that launches the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PokemonApp app = new PokemonApp();
            app.initializeApplication();
        });
    }

    /**
     * Initializes the components of the application and starts it.
     * Creates Model, Controller, and View instances and connects them.
     * Loads initial Pokemon data.
     */
    private void initializeApplication() {
        // Create model
        IPokemonModel model = new PokemonModel();

        // Create controller with reference to model
        IPokemonController controller = new PokemonController(model);

        // Create view with reference to controller
        IPokemonView view = new MainPokemonFrame(controller);

        // Display the view
        view.display();

        // Load data in a background thread to keep UI responsive
        new Thread(() -> {
            // Fetch initial data
            System.out.println("Loading Pokemon data, please wait...");
            controller.fetchInitialPokemon(100); // Load first 100 Pokemon

            // Use SwingUtilities.invokeLater to update UI components
            // This ensures UI updates happen on the EDT for thread safety
            SwingUtilities.invokeLater(() -> {
                // Update the list panel with all loaded Pokemon
                view.updatePokemonList(controller.getPokemonCollection());

                // If Pokemon were successfully loaded, show the first one in the details panel
                if (!controller.getPokemonCollection().isEmpty()) {
                    view.showPokemonDetails(controller.getPokemonCollection().get(0));
                }

                System.out.println("Application initialized successfully!");
            });
        }).start(); // The start() method begins execution of the new thread
    }
}

