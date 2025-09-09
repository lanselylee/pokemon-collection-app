package model;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Pokemon entity with its properties.
 * Implements IPokemon interface and Serializable for JSON serialization.
 */
public class Pokemon implements IPokemon, Serializable {
    private int id;
    private String name;
    private String imageUrl;
    private List<PokemonType> types;
    private PokemonStats stats;

    /**
     * Nested Stats Class to represent Pokemon's base stats
     */
    public static class PokemonStats implements Serializable {
        private int hp;
        private int attack;
        private int defense;
        private int specialAttack;
        private int specialDefense;
        private int speed;

        /**
         * Constructor for Pokemon stats
         * @param hp hit points
         * @param attack attack stat
         * @param defense defense stat
         * @param specialAttack special attack stat
         * @param specialDefense special defense stat
         * @param speed speed stat
         */
        public PokemonStats(int hp, int attack, int defense,
                            int specialAttack, int specialDefense, int speed) {
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.specialAttack = specialAttack;
            this.specialDefense = specialDefense;
            this.speed = speed;
        }

        /**
         * Default constructor for Jackson deserialization
         */
        public PokemonStats() {
            // Required for Jackson
        }

        // Getters
        public int getHp() { return hp; }
        public int getAttack() { return attack; }
        public int getDefense() { return defense; }
        public int getSpecialAttack() { return specialAttack; }
        public int getSpecialDefense() { return specialDefense; }
        public int getSpeed() { return speed; }

        // Setters for Jackson deserialization
        public void setHp(int hp) { this.hp = hp; }
        public void setAttack(int attack) { this.attack = attack; }
        public void setDefense(int defense) { this.defense = defense; }
        public void setSpecialAttack(int specialAttack) { this.specialAttack = specialAttack; }
        public void setSpecialDefense(int specialDefense) { this.specialDefense = specialDefense; }
        public void setSpeed(int speed) { this.speed = speed; }

        @Override
        public String toString() {
            return "PokemonStats{" +
                    "hp=" + hp +
                    ", attack=" + attack +
                    ", defense=" + defense +
                    ", specialAttack=" + specialAttack +
                    ", specialDefense=" + specialDefense +
                    ", speed=" + speed +
                    '}';
        }
    }

    /**
     * Constructs a Pokemon with all required properties.
     *
     * @param id the Pokemon's ID number
     * @param name the Pokemon's name
     * @param imageUrl the URL to the Pokemon's image
     * @param types list of Pokemon types
     * @param stats Pokemon's base stats
     */
    public Pokemon(int id, String name, String imageUrl, List<PokemonType> types, PokemonStats stats) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.types = types;
        this.stats = stats;
    }

    /**
     * Default constructor for Jackson deserialization.
     */
    public Pokemon() {
        // Required for Jackson
    }

    // Getters from IPokemon interface
    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getImageUrl() { return imageUrl; }

    @Override
    public List<PokemonType> getTypes() { return types; }

    @Override
    public PokemonStats getStats() { return stats; }

    // Setters for Jackson deserialization
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setTypes(List<PokemonType> types) { this.types = types; }
    public void setStats(PokemonStats stats) { this.stats = stats; }

    /**
     * Returns a formatted string representation of the Pokemon.
     * Used for display in UI components.
     *
     * @return the Pokemon's formatted ID and name
     */
    @Override
    public String toString() {
        return String.format("#%03d - %s", getId(), getName().toUpperCase());
    }
}