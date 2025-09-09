package controller;

import model.IPokemonModel;
import model.Pokemon;
import model.PokemonType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class that mediates between the Pokemon Model and View.
 * Implements IPokemonController interface and manages the business logic.
 */
public class PokemonController implements IPokemonController {

    // Instance variables as per UML
    private final IPokemonModel model;
    private List<Pokemon> currentPokemonList;

    /**
     * Constructor initializes the controller with a reference to the model.
     *
     * @param model the Pokemon data model
     */
    public PokemonController(IPokemonModel model) {
        this.model = model;
        this.currentPokemonList = new ArrayList<>();
    }

    /**
     * Fetches initial Pokemon data from the model.
     *
     * @param count the number of Pokemon to fetch
     */
    @Override
    public void fetchInitialPokemon(int count) {
        try {
            currentPokemonList = model.fetchMultiplePokemon(count);
        } catch (Exception e) {
            System.err.println("Error fetching initial Pokemon: " + e.getMessage());
            e.printStackTrace();
            currentPokemonList = new ArrayList<>(); // Initialize with empty list if fetch fails
        }
    }

    /**
     * Returns the current Pokemon collection.
     *
     * @return the current list of Pokemon
     */
    @Override
    public List<Pokemon> getPokemonCollection() {
        return new ArrayList<>(currentPokemonList); // Return a copy to avoid external modification
    }

    /**
     * Saves the current Pokemon collection to a file.
     *
     * @param filename the name of the file to save to
     */
    @Override
    public void saveCollection(String filename) {
        try {
            model.saveCollection(currentPokemonList, filename);
        } catch (IOException e) {
            System.err.println("Error saving Pokemon collection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads a Pokemon collection from a file.
     *
     * @param filename the name of the file to load from
     * @return the loaded Pokemon collection
     */


    /**
     * Searches Pokemon by name.
     *
     * @param searchTerm the name to search for
     * @return list of Pokemon matching the search term
     */
    @Override
    public List<Pokemon> searchPokemon(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return new ArrayList<>(currentPokemonList);
        }

        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        return currentPokemonList.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Sorts Pokemon by name alphabetically.
     *
     * @return sorted list of Pokemon
     */
    @Override
    public List<Pokemon> sortPokemonByName() {
        List<Pokemon> sortedList = new ArrayList<>(currentPokemonList);
        sortedList.sort(Comparator.comparing(Pokemon::getName));
        return sortedList;
    }

    /**
     * Filters Pokemon by type.
     *
     * @param type the type to filter by
     * @return list of Pokemon of the specified type
     */
    @Override
    public List<Pokemon> filterPokemonByType(PokemonType type) {
        if (type == null) {
            return new ArrayList<>(currentPokemonList);
        }

        return currentPokemonList.stream()
                .filter(pokemon -> pokemon.getTypes().contains(type))
                .collect(Collectors.toList());
    }

    /**
     * Gets a Pokemon by its ID.
     *
     * @param id the Pokemon ID
     * @return the Pokemon with the specified ID, or null if not found
     */
    @Override
    public Pokemon getPokemonById(int id) {
        return currentPokemonList.stream()
                .filter(pokemon -> pokemon.getId() == id)
                .findFirst()
                .orElse(null);
    }
}