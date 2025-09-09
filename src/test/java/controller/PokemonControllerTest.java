package controller;

import model.IPokemonModel;
import model.Pokemon;
import model.PokemonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PokemonController}.
 * Uses a test implementation of IPokemonModel instead of mocks.
 */
public class PokemonControllerTest {

    private TestPokemonModel testModel;
    private IPokemonController controller;
    private List<Pokemon> testPokemonList;

    /**
     * Test implementation of IPokemonModel for testing the controller
     */
    private static class TestPokemonModel implements IPokemonModel {
        private List<Pokemon> storedPokemon = new ArrayList<>();
        private boolean throwExceptionOnFetch = false;
        private boolean throwExceptionOnSave = false;
        private boolean throwExceptionOnLoad = false;

        public void setThrowExceptionOnFetch(boolean throwException) {
            this.throwExceptionOnFetch = throwException;
        }

        public void setThrowExceptionOnSave(boolean throwException) {
            this.throwExceptionOnSave = throwException;
        }

        public void setThrowExceptionOnLoad(boolean throwException) {
            this.throwExceptionOnLoad = throwException;
        }

        @Override
        public Pokemon fetchPokemonById(int id) throws IOException, InterruptedException {
            if (throwExceptionOnFetch) {
                throw new IOException("Test exception");
            }

            // Simple implementation for testing
            if (id == 1) {
                List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
                Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
                return new Pokemon(1, "bulbasaur", "bulbasaur.png", types, stats);
            } else if (id == 4) {
                List<PokemonType> types = Collections.singletonList(PokemonType.FIRE);
                Pokemon.PokemonStats stats = new Pokemon.PokemonStats(39, 52, 43, 60, 50, 65);
                return new Pokemon(4, "charmander", "charmander.png", types, stats);
            } else if (id == 7) {
                List<PokemonType> types = Collections.singletonList(PokemonType.WATER);
                Pokemon.PokemonStats stats = new Pokemon.PokemonStats(44, 48, 65, 50, 64, 43);
                return new Pokemon(7, "squirtle", "squirtle.png", types, stats);
            }
            return null;
        }

        @Override
        public List<Pokemon> fetchMultiplePokemon(int count) {
            if (throwExceptionOnFetch) {
                throw new RuntimeException("Test exception");
            }

            List<Pokemon> result = new ArrayList<>();
            for (int i = 1; i <= count && i <= 7; i++) {
                if (i == 1 || i == 4 || i == 7) { // Only add the three we defined
                    try {
                        Pokemon pokemon = fetchPokemonById(i);
                        if (pokemon != null) {
                            result.add(pokemon);
                        }
                    } catch (Exception e) {
                        // Skip this one
                    }
                }
            }
            return result;
        }

        @Override
        public void saveCollection(List<Pokemon> collection, String filename) throws IOException {
            if (throwExceptionOnSave) {
                throw new IOException("Test save exception");
            }

            if (collection == null || collection.isEmpty()) {
                throw new IllegalArgumentException("Cannot save empty or null Pokemon collection");
            }

            storedPokemon = new ArrayList<>(collection);
        }

        @Override
        public List<Pokemon> loadCollection(String filename) throws IOException {
            if (throwExceptionOnLoad) {
                throw new IOException("Test load exception");
            }

            if (filename == null || filename.trim().isEmpty()) {
                throw new IllegalArgumentException("Filename cannot be null or empty");
            }

            return new ArrayList<>(storedPokemon);
        }
    }

    @BeforeEach
    void setUp() {
        testModel = new TestPokemonModel();
        controller = new PokemonController(testModel);
        testPokemonList = createTestPokemonList();
    }

    /**
     * Helper method to create a test Pokemon list
     */
    private List<Pokemon> createTestPokemonList() {
        List<Pokemon> pokemonList = new ArrayList<>();

        // Create Bulbasaur
        List<PokemonType> bulbasaurTypes = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats bulbasaurStats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        Pokemon bulbasaur = new Pokemon(1, "bulbasaur", "bulbasaur.png", bulbasaurTypes, bulbasaurStats);

        // Create Charmander
        List<PokemonType> charmanderTypes = Collections.singletonList(PokemonType.FIRE);
        Pokemon.PokemonStats charmanderStats = new Pokemon.PokemonStats(39, 52, 43, 60, 50, 65);
        Pokemon charmander = new Pokemon(4, "charmander", "charmander.png", charmanderTypes, charmanderStats);

        // Create Squirtle
        List<PokemonType> squirtleTypes = Collections.singletonList(PokemonType.WATER);
        Pokemon.PokemonStats squirtleStats = new Pokemon.PokemonStats(44, 48, 65, 50, 64, 43);
        Pokemon squirtle = new Pokemon(7, "squirtle", "squirtle.png", squirtleTypes, squirtleStats);

        pokemonList.add(bulbasaur);
        pokemonList.add(charmander);
        pokemonList.add(squirtle);

        return pokemonList;
    }

    // =============== fetchInitialPokemon Tests ===============

    @Test
    void fetchInitialPokemon_Success() {
        // Act
        controller.fetchInitialPokemon(3);

        // Assert
        List<Pokemon> result = controller.getPokemonCollection();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("bulbasaur", result.get(0).getName());
    }

    @Test
    void fetchInitialPokemon_ZeroCount() {
        // Act
        controller.fetchInitialPokemon(0);

        // Assert
        List<Pokemon> result = controller.getPokemonCollection();
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchInitialPokemon_NegativeCount() {
        // Act
        controller.fetchInitialPokemon(-5);

        // Assert
        List<Pokemon> result = controller.getPokemonCollection();
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchInitialPokemon_ModelThrowsException() {
        // Arrange
        testModel.setThrowExceptionOnFetch(true);

        // Act
        controller.fetchInitialPokemon(10);

        // Assert
        List<Pokemon> result = controller.getPokemonCollection();
        assertTrue(result.isEmpty());

        // Reset for other tests
        testModel.setThrowExceptionOnFetch(false);
    }

    // =============== getPokemonCollection Tests ===============

    @Test
    void getPokemonCollection_ReturnsDefensiveCopy() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> returnedList = controller.getPokemonCollection();

        // Assert
        assertEquals(1, returnedList.size());

        // Verify that modifying the returned list doesn't affect the controller's internal list
        returnedList.clear();
        List<Pokemon> secondCall = controller.getPokemonCollection();
        assertEquals(1, secondCall.size());
    }

    @Test
    void getPokemonCollection_EmptyWhenNothingFetched() {
        // Act
        List<Pokemon> returnedList = controller.getPokemonCollection();

        // Assert
        assertNotNull(returnedList);
        assertTrue(returnedList.isEmpty());
    }

    // =============== saveCollection Tests ===============

    @Test
    void saveCollection_Success() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        controller.saveCollection("test.json");

        // No exception means success
    }


    @Test
    void saveCollection_EmptyList() {
        // Act & Assert - expect IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            controller.saveCollection("empty.json");
        });
    }


    @Test
    void saveCollection_ModelThrowsException() {
        // Arrange
        controller.fetchInitialPokemon(3);
        testModel.setThrowExceptionOnSave(true);

        // Act & Assert - The controller should catch the exception
        assertDoesNotThrow(() -> controller.saveCollection("error.json"));

        // Reset for other tests
        testModel.setThrowExceptionOnSave(false);
    }

    @Test
    void saveCollection_NullFilename() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act & Assert - no exception expected as controller handles it
        controller.saveCollection(null);
    }

    // =============== searchPokemon Tests ===============

    @Test
    void searchPokemon_ValidTerm() {
        // Arrange
        controller.fetchInitialPokemon(4);

        // Act
        List<Pokemon> results = controller.searchPokemon("char");

        // Assert
        assertEquals(1, results.size());
        assertEquals("charmander", results.get(0).getName());
    }

    @Test
    void searchPokemon_CaseInsensitive() {
        // Arrange
        controller.fetchInitialPokemon(7);

        // Act
        List<Pokemon> results = controller.searchPokemon("SQUIRTLE");

        // Assert
        assertEquals(1, results.size());
        assertEquals("squirtle", results.get(0).getName());
    }

    @Test
    void searchPokemon_NoMatch() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> results = controller.searchPokemon("pikachu");

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    void searchPokemon_EmptyString() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> results = controller.searchPokemon("");

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    void searchPokemon_NullSearchTerm() {
        // Arrange
        controller.fetchInitialPokemon(7);

        // Act
        List<Pokemon> results = controller.searchPokemon(null);
        System.out.println(results);

        // Assert
        assertEquals(3, results.size());
    }

    @Test
    void searchPokemon_EmptyCollection() {
        // Act
        List<Pokemon> results = controller.searchPokemon("bulbasaur");

        // Assert
        assertTrue(results.isEmpty());
    }

    // =============== sortPokemonByName Tests ===============

    @Test
    void sortPokemonByName_Success() {
        // Arrange
        controller.fetchInitialPokemon(7);

        // Act
        List<Pokemon> sortedList = controller.sortPokemonByName();

        // Assert
        assertEquals(3, sortedList.size());
        assertEquals("bulbasaur", sortedList.get(0).getName());
        assertEquals("charmander", sortedList.get(1).getName());
        assertEquals("squirtle", sortedList.get(2).getName());
    }

    @Test
    void sortPokemonByName_EmptyCollection() {
        // Act
        List<Pokemon> sortedList = controller.sortPokemonByName();

        // Assert
        assertTrue(sortedList.isEmpty());
    }

    @Test
    void sortPokemonByName_ReturnsDefensiveCopy() {
        // Arrange
        controller.fetchInitialPokemon(4);

        // Act
        List<Pokemon> sortedList = controller.sortPokemonByName();
        sortedList.clear();

        // Assert
        assertEquals(2, controller.getPokemonCollection().size());
    }

    // =============== filterPokemonByType Tests ===============

    @Test
    void filterPokemonByType_ValidType() {
        // Arrange
        controller.fetchInitialPokemon(7);

        // Act
        List<Pokemon> fireTypes = controller.filterPokemonByType(PokemonType.FIRE);

        // Assert
        assertEquals(1, fireTypes.size());
        assertEquals("charmander", fireTypes.get(0).getName());
    }

    @Test
    void filterPokemonByType_TypeWithMultipleMatches() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> poisonTypes = controller.filterPokemonByType(PokemonType.POISON);

        // Assert
        assertEquals(1, poisonTypes.size());
        assertEquals("bulbasaur", poisonTypes.get(0).getName());
    }

    @Test
    void filterPokemonByType_NoMatches() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> electricTypes = controller.filterPokemonByType(PokemonType.ELECTRIC);

        // Assert
        assertTrue(electricTypes.isEmpty());
    }

    @Test
    void filterPokemonByType_NullType() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        List<Pokemon> allPokemon = controller.filterPokemonByType(null);

        // Assert
        assertEquals(1, allPokemon.size());
    }

    @Test
    void filterPokemonByType_EmptyCollection() {
        // Act
        List<Pokemon> filteredList = controller.filterPokemonByType(PokemonType.WATER);

        // Assert
        assertTrue(filteredList.isEmpty());
    }

    // =============== getPokemonById Tests ===============

    @Test
    void getPokemonById_ValidId() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        Pokemon pokemon = controller.getPokemonById(1);

        // Assert
        assertNotNull(pokemon);
        assertEquals(1, pokemon.getId());
        assertEquals("bulbasaur", pokemon.getName());
    }

    @Test
    void getPokemonById_InvalidId() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        Pokemon pokemon = controller.getPokemonById(999);

        // Assert
        assertNull(pokemon);
    }

    @Test
    void getPokemonById_EmptyCollection() {
        // Act
        Pokemon pokemon = controller.getPokemonById(1);

        // Assert
        assertNull(pokemon);
    }

    @Test
    void getPokemonById_NegativeId() {
        // Arrange
        controller.fetchInitialPokemon(3);

        // Act
        Pokemon pokemon = controller.getPokemonById(-1);

        // Assert
        assertNull(pokemon);
    }
}