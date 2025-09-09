package controller;

import model.Pokemon;
import model.PokemonType;

import java.util.List;

/**
 * Interface for Pokemon Controller defining the contract for
 * mediating between Model and View components.
 */
public interface IPokemonController {

    /**
     * Fetches initial Pokemon data from the model.
     *
     * @param count the number of Pokemon to fetch
     */
    void fetchInitialPokemon(int count);

    /**
     * Returns the current Pokemon collection.
     *
     * @return the current list of Pokemon
     */
    List<Pokemon> getPokemonCollection();

    /**
     * Saves the current Pokemon collection to a file.
     *
     * @param filename the name of the file to save to
     */
    void saveCollection(String filename);

    /**
     * Searches Pokemon by name.
     *
     * @param searchTerm the name to search for
     * @return list of Pokemon matching the search term
     */
    List<Pokemon> searchPokemon(String searchTerm);

    /**
     * Sorts Pokemon by name alphabetically.
     *
     * @return sorted list of Pokemon
     */
    List<Pokemon> sortPokemonByName();

    /**
     * Filters Pokemon by type.
     *
     * @param type the type to filter by
     * @return list of Pokemon of the specified type
     */
    List<Pokemon> filterPokemonByType(PokemonType type);

    /**
     * Gets a Pokemon by its ID.
     *
     * @param id the Pokemon ID
     * @return the Pokemon with the specified ID, or null if not found
     */
    Pokemon getPokemonById(int id);
}