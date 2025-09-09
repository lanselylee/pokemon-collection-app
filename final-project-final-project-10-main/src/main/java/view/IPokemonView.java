package view;

import model.Pokemon;

import java.util.List;

/**
 * Interface for the Pokemon View defining the contract for displaying Pokemon data to the user.
 */
public interface IPokemonView {

    /**
     * Displays the main application user interface.
     */
    void display();

    /**
     * Updates the Pokemon list displayed in the view.
     *
     * @param pokemonList the list of Pokemon to display
     */
    void updatePokemonList(List<Pokemon> pokemonList);

    /**
     * Shows detailed information about a specific Pokemon.
     *
     * @param pokemon the Pokemon to display details for
     */
    void showPokemonDetails(Pokemon pokemon);
}
