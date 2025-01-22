package DeviesIntervju.API.controller;

import DeviesIntervju.API.model.Customer;
import DeviesIntervju.API.model.Genre;
import DeviesIntervju.API.model.StockEntry;
import DeviesIntervju.service.BookShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookShopController {
    private BookShop bookShop;

    @Autowired
    public BookShopController(BookShop bookShop){
        this.bookShop = bookShop;
    }

    @PutMapping("/BuyBook")
    public ResponseEntity<String> buyBook(@RequestParam int bookId, @RequestParam int customerId, @RequestParam int amount){
        boolean success = bookShop.buyBook(bookId,customerId,amount);
        if(success) return new ResponseEntity<>("Book bought", HttpStatus.OK);
        else return new ResponseEntity<>("Buy failed",HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @PutMapping("/SellBook")
    public ResponseEntity<String> SellBook(@RequestParam int bookId, @RequestParam int customerId, @RequestParam int amount){
        boolean success = bookShop.sellBook(bookId,customerId,amount);
        if(success) return new ResponseEntity<>("Book sold", HttpStatus.OK);
        else return new ResponseEntity<>("Sell failed",HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @GetMapping("/GetCustomer")
    public Customer getCustomer(int id){
        return bookShop.getCustomerById(id);
    }

    @GetMapping("/GetStock")
    public List<StockEntry> getStockBalance(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) List<Genre> genres,
            @RequestParam(required = false) Integer minPublishYear,
            @RequestParam(required = false) Integer maxPublishYear,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Integer notOwnedByUserId) {

        return bookShop.getBooksWithFilter((stockEntry) -> {
            boolean matches = true;
            if (title != null) matches &= stockEntry.getBook().title.toLowerCase().contains(title.toLowerCase());
            if (author != null) matches &= stockEntry.getBook().author.toLowerCase().contains(author.toLowerCase());
            if (minPages != null) matches &= stockEntry.getBook().pageCount >= minPages;
            if (maxPages != null) matches &= stockEntry.getBook().pageCount <= maxPages;
            if (minPrice != null) matches &= stockEntry.getPrice() >= minPrice;
            if (maxPrice != null) matches &= stockEntry.getPrice() <= maxPrice;
            if (minPublishYear != null) matches &= stockEntry.getBook().publishYear >= minPublishYear;
            if (maxPublishYear != null) matches &= stockEntry.getBook().publishYear <= maxPublishYear;
            if (id != null) matches &= stockEntry.getBook().id == id;
            if (genres != null) matches &= genres.contains(stockEntry.getBook().genre);
            if  (notOwnedByUserId != null) matches &= !bookShop.getCustomerById(notOwnedByUserId).getBooks().containsKey(stockEntry.getBook());
            return matches;
        });
    }
    @PutMapping("/CreateNewCustomer")
    public ResponseEntity<String> createCustomer(@RequestParam double money){
        return new ResponseEntity<>("Customer with id "+ bookShop.getNewCustomer(money).getId() + " created", HttpStatus.OK);
    }
}
