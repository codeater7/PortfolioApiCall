package PortfolioApiCall.contoller;

import PortfolioApiCall.Record.Author;
import PortfolioApiCall.Record.Book;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

/*
* Just for being familiar with graphql this was scribbled quickly.
* Outside of requirement.
*/

@Controller
public class GraphqlController {

    @QueryMapping
    public ResponseEntity<String> heartbeat() {
        return ResponseEntity.ok("Beep Beep");
    }

    @QueryMapping
    public List<Book> books() {
        return Book.books;
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument Integer id) {
        return Book.getBookById(id);
    }

    @SchemaMapping
    public Optional<Author> author( Book book){
        return Author.getAuthorById(book.authorId());
    }

    @MutationMapping
    public Book addBook(@Argument String name, @Argument Integer authorId, @Argument Integer pageCount){
        return Book.addBook(name, authorId, pageCount);
    }
}
