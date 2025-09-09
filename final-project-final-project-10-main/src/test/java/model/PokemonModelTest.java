package model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PokemonModel.
 * This test suite uses reflection to access private methods for testing.
 */
public class PokemonModelTest {

    private PokemonModel pokemonModel;
    private ObjectMapper objectMapper;
    private Method parseApiResponseMethod;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        // Create test instance and set up reflection to access private method
        pokemonModel = new TestPokemonModel();
        objectMapper = new ObjectMapper();

        // Get access to the private parseApiResponse method using reflection
        parseApiResponseMethod = PokemonModel.class.getDeclaredMethod("parseApiResponse", String.class);
        parseApiResponseMethod.setAccessible(true);
    }

    /**
     * Test implementation of PokemonModel that overrides network-dependent methods.
     */
    private static class TestPokemonModel extends PokemonModel {
        @Override
        public Pokemon fetchPokemonById(int id) throws IOException, InterruptedException {
            // For testing, return predefined Pokemon without making HTTP calls
            if (id == 1) {
                return createTestBulbasaur();
            } else if (id == 2) {
                return createTestIvysaur();
            } else {
                throw new IOException("Pokemon not found with id: " + id);
            }
        }
    }

    /**
     * Helper method to invoke the private parseApiResponse method using reflection.
     */
    private Pokemon invokeParseApiResponse(String jsonResponse) throws Exception {
        return (Pokemon) parseApiResponseMethod.invoke(pokemonModel, jsonResponse);
    }

    /**
     * Creates a test Bulbasaur Pokemon object.
     */
    private static Pokemon createTestBulbasaur() {
        List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        return new Pokemon(1, "bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", types, stats);
    }

    /**
     * Creates a test Ivysaur Pokemon object.
     */
    private static Pokemon createTestIvysaur() {
        List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(60, 62, 63, 80, 80, 60);
        return new Pokemon(2, "ivysaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png", types, stats);
    }

    @Test
    void testFetchPokemonById() throws IOException, InterruptedException {
        // Test fetching a Pokemon by ID
        Pokemon bulbasaur = pokemonModel.fetchPokemonById(1);

        // Verify the Pokemon properties
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", bulbasaur.getImageUrl());
        assertEquals(2, bulbasaur.getTypes().size());
        assertTrue(bulbasaur.getTypes().contains(PokemonType.GRASS));
        assertTrue(bulbasaur.getTypes().contains(PokemonType.POISON));

        // Verify stats
        assertEquals(45, bulbasaur.getStats().getHp());
        assertEquals(49, bulbasaur.getStats().getAttack());
        assertEquals(49, bulbasaur.getStats().getDefense());
        assertEquals(65, bulbasaur.getStats().getSpecialAttack());
        assertEquals(65, bulbasaur.getStats().getSpecialDefense());
        assertEquals(45, bulbasaur.getStats().getSpeed());
    }

    @Test
    void testFetchPokemonByIdNotFound() {
        // Test fetching a Pokemon with an invalid ID
        Exception exception = assertThrows(IOException.class, () -> {
            pokemonModel.fetchPokemonById(999);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testFetchMultiplePokemon() throws IOException, InterruptedException {
        // Test fetching multiple Pokemon
        List<Pokemon> pokemon = pokemonModel.fetchMultiplePokemon(2);

        // Verify the list size and Pokemon properties
        assertEquals(2, pokemon.size());

        // Verify first Pokemon (Bulbasaur)
        Pokemon bulbasaur = pokemon.get(0);
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());

        // Verify second Pokemon (Ivysaur)
        Pokemon ivysaur = pokemon.get(1);
        assertEquals(2, ivysaur.getId());
        assertEquals("ivysaur", ivysaur.getName());
    }

    @Test
    void testParseApiResponseBasic() throws Exception {
        // Test parsing a basic API response
        String jsonResponse = "{\n" +
                "  \"id\": 25,\n" +
                "  \"name\": \"pikachu\",\n" +
                "  \"sprites\": {\n" +
                "    \"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png\"\n" +
                "  },\n" +
                "  \"types\": [\n" +
                "    {\n" +
                "      \"type\": {\n" +
                "        \"name\": \"electric\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"stats\": [\n" +
                "    {\n" +
                "      \"base_stat\": 35,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"hp\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 55,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"attack\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 40,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"defense\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 50,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"special-attack\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 50,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"special-defense\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 90,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"speed\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Pokemon pikachu = invokeParseApiResponse(jsonResponse);

        // Verify the Pokemon properties
        assertEquals(25, pikachu.getId());
        assertEquals("pikachu", pikachu.getName());
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png", pikachu.getImageUrl());
        assertEquals(1, pikachu.getTypes().size());
        assertTrue(pikachu.getTypes().contains(PokemonType.ELECTRIC));

        // Verify stats
        assertEquals(35, pikachu.getStats().getHp());
        assertEquals(55, pikachu.getStats().getAttack());
        assertEquals(40, pikachu.getStats().getDefense());
        assertEquals(50, pikachu.getStats().getSpecialAttack());
        assertEquals(50, pikachu.getStats().getSpecialDefense());
        assertEquals(90, pikachu.getStats().getSpeed());
    }

    @Test
    void testParseApiResponseMultipleTypes() throws Exception {
        // Test parsing a Pokemon with multiple types
        String jsonResponse = "{\n" +
                "  \"id\": 6,\n" +
                "  \"name\": \"charizard\",\n" +
                "  \"sprites\": {\n" +
                "    \"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png\"\n" +
                "  },\n" +
                "  \"types\": [\n" +
                "    {\n" +
                "      \"type\": {\n" +
                "        \"name\": \"fire\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": {\n" +
                "        \"name\": \"flying\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"stats\": [\n" +
                "    {\n" +
                "      \"base_stat\": 78,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"hp\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 84,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"attack\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 78,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"defense\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 109,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"special-attack\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 85,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"special-defense\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 100,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"speed\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Pokemon charizard = invokeParseApiResponse(jsonResponse);

        // Verify the Pokemon properties
        assertEquals(6, charizard.getId());
        assertEquals("charizard", charizard.getName());
        assertEquals(2, charizard.getTypes().size());
        assertTrue(charizard.getTypes().contains(PokemonType.FIRE));
        assertTrue(charizard.getTypes().contains(PokemonType.FLYING));

        // Verify stats
        assertEquals(78, charizard.getStats().getHp());
        assertEquals(84, charizard.getStats().getAttack());
        assertEquals(78, charizard.getStats().getDefense());
        assertEquals(109, charizard.getStats().getSpecialAttack());
        assertEquals(85, charizard.getStats().getSpecialDefense());
        assertEquals(100, charizard.getStats().getSpeed());
    }

    @Test
    void testParseApiResponseMissingStats() throws Exception {
        // Test parsing a response with missing stats
        String jsonResponse = "{\n" +
                "  \"id\": 132,\n" +
                "  \"name\": \"ditto\",\n" +
                "  \"sprites\": {\n" +
                "    \"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png\"\n" +
                "  },\n" +
                "  \"types\": [\n" +
                "    {\n" +
                "      \"type\": {\n" +
                "        \"name\": \"normal\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"stats\": [\n" +
                "    {\n" +
                "      \"base_stat\": 48,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"hp\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 48,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"attack\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"base_stat\": 48,\n" +
                "      \"stat\": {\n" +
                "        \"name\": \"defense\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Pokemon ditto = invokeParseApiResponse(jsonResponse);

        // Verify the Pokemon properties
        assertEquals(132, ditto.getId());
        assertEquals("ditto", ditto.getName());
        assertEquals(1, ditto.getTypes().size());
        assertTrue(ditto.getTypes().contains(PokemonType.NORMAL));

        // Verify stats (missing stats should be 0)
        assertEquals(48, ditto.getStats().getHp());
        assertEquals(48, ditto.getStats().getAttack());
        assertEquals(48, ditto.getStats().getDefense());
        assertEquals(0, ditto.getStats().getSpecialAttack());
        assertEquals(0, ditto.getStats().getSpecialDefense());
        assertEquals(0, ditto.getStats().getSpeed());
    }

    @Test
    void testParseApiResponseInvalidJson() {
        // Test parsing invalid JSON (should throw an exception)
        String invalidJson = "{ this is not valid JSON }";

        Exception exception = assertThrows(Exception.class, () -> {
            invokeParseApiResponse(invalidJson);
        });

        // Verify that an exception was thrown
        assertNotNull(exception);
    }

    @Test
    void testSaveAndLoadCollection() throws IOException {
        // Create some test Pokemon
        List<Pokemon> pokemonList = Arrays.asList(createTestBulbasaur(), createTestIvysaur());

        // Create a temporary file for testing
        File tempFile = tempDir.resolve("testCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Save the collection
        pokemonModel.saveCollection(pokemonList, filename);

        // Verify the file exists
        assertTrue(tempFile.exists());

        // Load the collection back
        List<Pokemon> loadedPokemon = pokemonModel.loadCollection(filename);

        // Verify the loaded collection
        assertEquals(2, loadedPokemon.size());

        // Check first Pokemon
        Pokemon bulbasaur = loadedPokemon.get(0);
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());
        assertEquals(2, bulbasaur.getTypes().size());

        // Check second Pokemon
        Pokemon ivysaur = loadedPokemon.get(1);
        assertEquals(2, ivysaur.getId());
        assertEquals("ivysaur", ivysaur.getName());
    }

    @Test
    void testSaveCollectionWithoutJsonExtension() throws IOException {
        // Test saving with a filename that doesn't have .json extension
        List<Pokemon> pokemonList = Arrays.asList(createTestBulbasaur());

        // Create a filename without .json extension
        File tempFile = tempDir.resolve("testCollection").toFile();
        String filename = tempFile.getAbsolutePath();

        // Save the collection
        pokemonModel.saveCollection(pokemonList, filename);

        // Verify that the file was created with .json extension
        File expectedFile = new File(filename + ".json");
        assertTrue(expectedFile.exists());

        // Load the file to verify content
        List<Pokemon> loadedPokemon = pokemonModel.loadCollection(filename);
        assertEquals(1, loadedPokemon.size());
        assertEquals("bulbasaur", loadedPokemon.get(0).getName());
    }

    @Test
    void testSaveEmptyCollection() {
        // Test saving an empty collection
        List<Pokemon> emptyList = new ArrayList<>();

        // Create a temporary file
        File tempFile = tempDir.resolve("emptyCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Attempt to save an empty collection should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.saveCollection(emptyList, filename);
        });

        assertTrue(exception.getMessage().contains("Cannot save empty or null"));
    }

    @Test
    void testSaveNullCollection() {
        // Test saving a null collection
        List<Pokemon> nullList = null;

        // Create a temporary file
        File tempFile = tempDir.resolve("nullCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Attempt to save a null collection should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.saveCollection(nullList, filename);
        });

        assertTrue(exception.getMessage().contains("Cannot save empty or null"));
    }

    @Test
    void testLoadNonExistentFile() {
        // Test loading a file that doesn't exist
        String nonExistentFile = tempDir.resolve("nonExistent.json").toString();

        // Attempt to load a non-existent file should throw an exception
        Exception exception = assertThrows(IOException.class, () -> {
            pokemonModel.loadCollection(nonExistentFile);
        });

        assertTrue(exception.getMessage().contains("File does not exist"));
    }

    @Test
    void testLoadWithEmptyFilename() {
        // Test loading with an empty filename
        String emptyFilename = "";

        // Attempt to load with an empty filename should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.loadCollection(emptyFilename);
        });

        assertTrue(exception.getMessage().contains("Filename cannot be null or empty"));
    }

    @Test
    void testLoadWithNullFilename() {
        // Test loading with a null filename
        String nullFilename = null;

        // Attempt to load with a null filename should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.loadCollection(nullFilename);
        });

        assertTrue(exception.getMessage().contains("Filename cannot be null or empty"));
    }
}