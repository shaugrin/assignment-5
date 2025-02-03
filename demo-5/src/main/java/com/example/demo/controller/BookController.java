package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController //@Controller+ @ResponseBody(trigger the parser to return json)
@RequestMapping(path = "api")
public class BookController {

    private final BookService bookService;
    //@Autowired is optional to write on single ctr
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Value("${app.welcome.message}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    @GetMapping(path = "books")
    public ResponseEntity<CollectionModel<Book>> getAll(){
        List<Book> books = bookService.getAllBooks();
        CollectionModel<Book> collectionModel = CollectionModel.of(books);
        collectionModel.add(linkTo(methodOn(BookController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    //get by id
    @GetMapping(path = "books/{id}")
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable int id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Book> bookModel = EntityModel.of(book);
        bookModel.add(linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel());
        bookModel.add(linkTo(methodOn(BookController.class).getAll()).withRel("books"));
        bookModel.add(linkTo(methodOn(BookController.class).updateProduct(id, new Book())).withRel("update"));
        bookModel.add(linkTo(methodOn(BookController.class).removeProduct(id)).withRel("delete"));

        return ResponseEntity.ok(bookModel);
    }

    @PostMapping(path = "books")
    public ResponseEntity<Book> addProduct( @RequestBody @Valid Book product){
        Book productToReturn = bookService.addBook(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productToReturn);
    }

    //Exception hainding in controller layer.. 100 of controller, try catch... AOP
    //Ex hainding is a ccc, using aop
    //update
    @PutMapping(path = "books/{id}")
    public ResponseEntity<Book> updateProduct(@PathVariable int id, @RequestBody Book product){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(id, product));
    }

    //delete
    @DeleteMapping(path = "books/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable int id){
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
