package PortfolioApiCall.Record;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*
 * As part of GraphQl learning.
 * Outside of requirement.
 */
public record Author(Integer Id, String name) {
    public static List<Author> authors = Arrays.asList(
            new Author (1, "Paulo Coelho"),
            new Author(2, "JK Rowling"),
            new Author(3,   "Charles Dickens")
            );

    public static Optional<Author> getAuthorById(Integer Id) {
        return authors.stream()
                .filter(b -> b.Id.equals(Id))
                .findFirst();
    }

}
