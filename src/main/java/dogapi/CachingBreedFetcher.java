package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // If we already have it cached, return it directly.
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            // ✅ Record that we are making a call to the underlying fetcher.
            callsMade++;

            // Call the underlying fetcher
            List<String> subBreeds = fetcher.getSubBreeds(breed);

            // Cache the successful result
            cache.put(breed, subBreeds);

            return subBreeds;

        } catch (BreedNotFoundException e) {
            // ✅ Still counts as a call, but do NOT cache failures.
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}