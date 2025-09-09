package model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    @Test
    void testPokemonConstructor() {
        // Arrange
        int id = 25;
        String name = "Pikachu";
        String imageUrl = "https://example.com/pikachu.png";
        List<PokemonType> types = Arrays.asList(PokemonType.ELECTRIC);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(35, 55, 40, 50, 50, 90);

        // Act
        Pokemon pokemon = new Pokemon(id, name, imageUrl, types, stats);

        // Assert
        assertEquals(id, pokemon.getId());
        assertEquals(name, pokemon.getName());
        assertEquals(imageUrl, pokemon.getImageUrl());
        assertEquals(types, pokemon.getTypes());
        assertEquals(stats, pokemon.getStats());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        Pokemon pokemon = new Pokemon();

        // Assert
        assertEquals(0, pokemon.getId());
        assertNull(pokemon.getName());
        assertNull(pokemon.getImageUrl());
        assertNull(pokemon.getTypes());
        assertNull(pokemon.getStats());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Pokemon pokemon = new Pokemon();
        int id = 1;
        String name = "Bulbasaur";
        String imageUrl = "https://example.com/bulbasaur.png";
        List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);

        // Act
        pokemon.setId(id);
        pokemon.setName(name);
        pokemon.setImageUrl(imageUrl);
        pokemon.setTypes(types);
        pokemon.setStats(stats);

        // Assert
        assertEquals(id, pokemon.getId());
        assertEquals(name, pokemon.getName());
        assertEquals(imageUrl, pokemon.getImageUrl());
        assertEquals(types, pokemon.getTypes());
        assertEquals(stats, pokemon.getStats());
    }

    @Test
    void testToString() {
        // Arrange
        Pokemon pokemon = new Pokemon(7, "squirtle", "url", Collections.emptyList(), null);

        // Act
        String result = pokemon.toString();

        // Assert
        assertEquals("#007 - SQUIRTLE", result);
    }

    @Test
    void testStatsConstructor() {
        // Arrange
        int hp = 100;
        int attack = 110;
        int defense = 120;
        int specialAttack = 130;
        int specialDefense = 140;
        int speed = 150;

        // Act
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(hp, attack, defense,
                specialAttack, specialDefense, speed);

        // Assert
        assertEquals(hp, stats.getHp());
        assertEquals(attack, stats.getAttack());
        assertEquals(defense, stats.getDefense());
        assertEquals(specialAttack, stats.getSpecialAttack());
        assertEquals(specialDefense, stats.getSpecialDefense());
        assertEquals(speed, stats.getSpeed());
    }

    @Test
    void testStatsDefaultConstructor() {
        // Act
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats();

        // Assert
        assertEquals(0, stats.getHp());
        assertEquals(0, stats.getAttack());
        assertEquals(0, stats.getDefense());
        assertEquals(0, stats.getSpecialAttack());
        assertEquals(0, stats.getSpecialDefense());
        assertEquals(0, stats.getSpeed());
    }

    @Test
    void testStatsGettersAndSetters() {
        // Arrange
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats();
        int hp = 78;
        int attack = 84;
        int defense = 78;
        int specialAttack = 109;
        int specialDefense = 85;
        int speed = 100;

        // Act
        stats.setHp(hp);
        stats.setAttack(attack);
        stats.setDefense(defense);
        stats.setSpecialAttack(specialAttack);
        stats.setSpecialDefense(specialDefense);
        stats.setSpeed(speed);

        // Assert
        assertEquals(hp, stats.getHp());
        assertEquals(attack, stats.getAttack());
        assertEquals(defense, stats.getDefense());
        assertEquals(specialAttack, stats.getSpecialAttack());
        assertEquals(specialDefense, stats.getSpecialDefense());
        assertEquals(speed, stats.getSpeed());
    }

    @Test
    void testStatsToString() {
        // Arrange
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);

        // Act
        String result = stats.toString();

        // Assert
        assertTrue(result.contains("hp=45"));
        assertTrue(result.contains("attack=49"));
        assertTrue(result.contains("defense=49"));
        assertTrue(result.contains("specialAttack=65"));
        assertTrue(result.contains("specialDefense=65"));
        assertTrue(result.contains("speed=45"));
    }

    @Test
    void testMultipleTypes() {
        // Arrange
        List<PokemonType> types = Arrays.asList(PokemonType.WATER, PokemonType.ICE);
        Pokemon pokemon = new Pokemon(131, "Lapras", "url", types, null);

        // Act
        List<PokemonType> returnedTypes = pokemon.getTypes();

        // Assert
        assertEquals(2, returnedTypes.size());
        assertTrue(returnedTypes.contains(PokemonType.WATER));
        assertTrue(returnedTypes.contains(PokemonType.ICE));
    }

    @Test
    void testEmptyTypes() {
        // Arrange
        List<PokemonType> types = Collections.emptyList();
        Pokemon pokemon = new Pokemon(132, "Ditto", "url", types, null);

        // Act
        List<PokemonType> returnedTypes = pokemon.getTypes();

        // Assert
        assertTrue(returnedTypes.isEmpty());
    }
}