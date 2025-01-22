package DeviesIntervju.API.controller;

import DeviesIntervju.API.model.Book;
import DeviesIntervju.API.model.Genre;
import DeviesIntervju.service.BookShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class BookShopController {
    private BookShop bookShop;

    @Autowired
    public BookShopController(BookShop bookShop){
        this.bookShop = bookShop;
    }

    @PutMapping("/BuyBook")
    public boolean buyBook(@RequestParam int bookId,@RequestParam int customerId,@RequestParam int amount){
        return bookShop.buyBook(bookId,customerId,amount);
    }

    @GetMapping("/GetStock")
    public ResponseEntity<Map<Book, Integer>> getStockBalance(){
        return new ResponseEntity<Map<Book, Integer>>(bookShop.getBooks(), HttpStatus.OK);
    }
    @GetMapping("/Ping")
    public boolean ping(){
        return true;
    }
    @GetMapping("/GetStock/Filter")
    public Map<Book, Integer> getStockBalance(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) List<Genre> genres,
            @RequestParam(required = false) Integer minPublishYear,
            @RequestParam(required = false) Integer maxPublishYear,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages) {

        return bookShop.getBooksWithFilter((book, stock) -> {
            boolean matches = true;
            if (title != null) matches &= book.title.toLowerCase().contains(title.toLowerCase());
            if (author != null) matches &= book.author.toLowerCase().contains(author.toLowerCase());
            if (minPages != null) matches &= book.pageCount >= minPages;
            if (maxPages != null) matches &= book.pageCount <= maxPages;
            if (minPrice != null) matches &= book.price >= minPrice;
            if (maxPrice != null) matches &= book.price <= maxPrice;
            if (minPublishYear != null) matches &= book.publishYear >= minPublishYear;
            if (maxPublishYear != null) matches &= book.publishYear <= maxPublishYear;
            if (id != null) matches &= book.id == id;
            if (genres != null) matches &= genres.contains(book.genre);
            return matches;
        });
    }
    @PutMapping("/CreateNewCustomer")
    public int createCustomer(double money){
        return bookShop.getNewCustomer(money).getId();
    }
}
