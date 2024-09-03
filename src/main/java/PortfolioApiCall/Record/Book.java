package PortfolioApiCall.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * As part of GraphQl learning.
 * Outside of requirement.
 */
public record Book(Integer id, String name, Integer pageCount, Integer authorId) {
    public static List<Book> books = new ArrayList<>(List.of (
            new Book(1, "A Tale of Two Cities", 201, 3),
            new Book(2, "Harry Potter", 300, 2),
            new Book(3, "Alchemist", 300, 1),
            new Book(4, "Eleven Minute", 45, 1)
    ));

    public static Optional<Book> getBookById(Integer id) {
        return books.stream()
                .filter(b -> b.id.equals(id))
                .findFirst();
    }

    public static Book addBook(String name,  Integer authorId, Integer pageCount){
        int currentSize = Book.books.size();
        Book newBook = new Book(currentSize + 1 , name, pageCount, authorId);
        Book.books.add( newBook);
        return  newBook;

    }

}
