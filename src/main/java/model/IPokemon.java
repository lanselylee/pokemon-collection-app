package model;

import java.util.List;

/**
 * Interface representing a Pokemon with essential properties.
 */
public interface IPokemon {
    /**
     * Gets the Pokemon's ID number.
     * @return the Pokemon ID
     */
    int getId();

    /**
     * Gets the Pokemon's name.
     * @return the Pokemon name
     */
    String getName();

    /**
     * Gets the URL to the Pokemon's image.
     * @return the image URL
     */
    String getImageUrl();

    /**
     * Gets the Pokemon's types.
     * @return list of Pokemon types
     */
    List<PokemonType> getTypes();

    /**
     * Gets the Pokemon's stats.
     * @return the Pokemon's stats
     */
    Pokemon.PokemonStats getStats();
}