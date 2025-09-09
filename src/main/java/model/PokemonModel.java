package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for fetching Pokemon data from PokeAPI and managing Pokemon collections.
 * Implements the PokemonService interface.
 */
public class PokemonModel implements IPokemonModel {
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";

    // Instance variables as per UML
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private List<Pokemon> pokemonCache;

    /**
     * Constructor initializes the HTTP client, object mapper, and Pokemon cache.
     */
    public PokemonModel() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.pokemonCache = new ArrayList<>();
    }

    /**
     * Fetches a Pokemon by its ID from the PokeAPI.
     *
     * @param id the Pokemon ID
     * @return the Pokemon object
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Override
    public Pokemon fetchPokemonById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(POKEAPI_BASE_URL + id))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Pokemon pokemon = parseApiResponse(response.body());
        if (!pokemonCache.contains(pokemon)) {
            pokemonCache.add(pokemon);
        }

        return pokemon;
    }

    /**
     * Fetches multiple Pokemon by ID range.
     *
     * @param count the number of Pokemon to fetch starting from ID 1
     * @return list of fetched Pokemon
     */
    @Override
    public List<Pokemon> fetchMultiplePokemon(int count) {
        List<Pokemon> result = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            try {
                Pokemon pokemon = fetchPokemonById(i);
                result.add(pokemon);
            } catch (Exception e) {
                System.err.println("Error fetching Pokemon " + i + ": " + e.getMessage());
            }
        }
        return result;
    }

    /**
     * Parses API response to create a Pokemon object.
     *
     * @param jsonResponse the JSON response from the API
     * @return the parsed Pokemon object
     * @throws IOException if parsing fails
     */
    private Pokemon parseApiResponse(String jsonResponse) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        // Extract id
        int id = jsonNode.get("id").asInt();

        // Extract name
        String name = jsonNode.get("name").asText();

        // Extract image URL
        String imageUrl = jsonNode.get("sprites").get("front_default").asText();

        // Extract types
        List<PokemonType> types = new ArrayList<>();
        jsonNode.get("types").forEach(type -> {
            String typeName = type.get("type").get("name").asText();
            types.add(PokemonType.fromApiName(typeName));
        });

        // Extract stats
        int hp = 0, attack = 0, defense = 0, specialAttack = 0, specialDefense = 0, speed = 0;

        JsonNode statsNode = jsonNode.get("stats");
        for (JsonNode statNode : statsNode) {
            String statName = statNode.get("stat").get("name").asText();
            int value = statNode.get("base_stat").asInt();

            switch (statName) {
                case "hp":
                    hp = value;
                    break;
                case "attack":
                    attack = value;
                    break;
                case "defense":
                    defense = value;
                    break;
                case "special-attack":
                    specialAttack = value;
                    break;
                case "special-defense":
                    specialDefense = value;
                    break;
                case "speed":
                    speed = value;
                    break;
            }
        }

        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(
                hp, attack, defense, specialAttack, specialDefense, speed
        );

        return new Pokemon(id, name, imageUrl, types, stats);
    }

    /**
     * High-level method for saving a collection of Pokemon to a JSON file.
     * Acts as a public API for saving Pokémon data to disk.
     * Handles necessary logic before saving like validation and filename formatting.
     *
     * @param collection the collection to save
     * @param filename the file to save to
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the collection is null or empty
     */
    @Override
    public void saveCollection(List<Pokemon> collection, String filename) throws IOException {
        // Validation logic
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Cannot save empty or null Pokemon collection");
        }

        // Format filename if needed
        String formattedFilename = formatFilename(filename);

        // Update cache with any new Pokemon in the collection
        updateCacheFromCollection(collection);

        // Delegate actual file writing to the helper method
        writeJsonToFile(collection, formattedFilename);

        System.out.println("Successfully saved " + collection.size() + " Pokemon to " + formattedFilename);
    }

    /**
     * High-level method for loading a collection of Pokemon from a JSON file.
     * Provides a clean API for loading Pokemon collections.
     * Handles validation and processing of loaded data before returning.
     *
     * @param filename the file to load from
     * @return list of loaded Pokemon
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the filename is invalid
     */
    @Override
    public List<Pokemon> loadCollection(String filename) throws IOException {
        // Validation logic
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        // Format filename if needed
        String formattedFilename = formatFilename(filename);

        // Check if file exists
        File file = new File(formattedFilename);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File does not exist: " + formattedFilename);
        }

        // Delegate actual file reading to the helper method
        List<Pokemon> loadedPokemon = readJsonFromFile(formattedFilename);

        // Update cache with loaded Pokemon
        updateCacheFromCollection(loadedPokemon);

        System.out.println("Successfully loaded " + loadedPokemon.size() + " Pokemon from " + formattedFilename);

        return loadedPokemon;
    }

    /**
     * Low-level helper method that handles the technical part of writing to file.
     * Focuses only on converting the list to JSON and writing it to the file.
     *
     * @param collection the collection to write
     * @param filename the file to write to
     * @throws IOException if an I/O error occurs
     */
    private void writeJsonToFile(List<Pokemon> collection, String filename) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), collection);
    }

    /**
     * Low-level helper method that handles the technical part of reading from file.
     * Focuses only on reading from the file and converting JSON to objects.
     *
     * @param filename the file to read from
     * @return list of read Pokemon
     * @throws IOException if an I/O error occurs
     */
    private List<Pokemon> readJsonFromFile(String filename) throws IOException {
        return objectMapper.readValue(
                new File(filename),
                new TypeReference<List<Pokemon>>() {}
        );
    }

    /**
     * Helper method to ensure filename has the proper extension.
     *
     * @param filename the original filename
     * @return the formatted filename
     */
    private String formatFilename(String filename) {
        if (!filename.endsWith(".json")) {
            return filename + ".json";
        }
        return filename;
    }

    /**
     * Updates the cache with Pokemon from a collection if they don't already exist.
     *
     * @param collection the collection to update the cache from
     */
    private void updateCacheFromCollection(List<Pokemon> collection) {
        for (Pokemon pokemon : collection) {
            if (!pokemonCache.contains(pokemon)) {
                pokemonCache.add(pokemon);
            }
        }
    }
}