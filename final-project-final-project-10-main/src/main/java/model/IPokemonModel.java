package model;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining operations for fetching, storing, and managing Pokemon data.
 */
public interface IPokemonModel {

    /**
     * Fetches a Pokemon by its ID from the API.
     *
     * @param id the Pokemon ID
     * @return the Pokemon object
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    Pokemon fetchPokemonById(int id) throws IOException, InterruptedException;

    /**
     * Fetches multiple Pokemon by ID range.
     *
     * @param count the number of Pokemon to fetch starting from ID 1
     * @return list of fetched Pokemon
     */
    List<Pokemon> fetchMultiplePokemon(int count);

    /**
     * Saves a collection of Pokemon to a JSON file.
     *
     * @param collection the collection to save
     * @param filename the file to save to
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the collection is null or empty
     */
    void saveCollection(List<Pokemon> collection, String filename) throws IOException;

    /**
     * Loads a collection of Pokemon from a JSON file.
     *
     * @param filename the file to load from
     * @return list of loaded Pokemon
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the filename is invalid
     */
    List<Pokemon> loadCollection(String filename) throws IOException;
}