package DeviesIntervju;

import DeviesIntervju.API.model.*;
import DeviesIntervju.service.*;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class Tests {
    @Test
    public void moveMoneyWhenBuyingTest(){
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson", Genre.BIOGRAPHY,100,100);
        HashMap<Book,Integer> data = new HashMap<>();
        data.put(joel,100);
        BookShop shop = new BookShop(data,1000);
        Customer c = shop.getNewCustomer(1000);
        shop.buyBook(0,c.getId(),5);
        Assert.assertEquals(shop.getMoney(),1500,0.01);
        Assert.assertEquals(c.getMoney(),500,0.01);
    }
    @Test
    public void moveStockWhenBuyingTest(){
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson",Genre.BIOGRAPHY,100,100);
        HashMap<Book,Integer> data = new HashMap<>();
        data.put(joel,100);
        BookShop shop = new BookShop(data,1000);
        Customer c = shop.getNewCustomer(1000);
        shop.buyBook(0,c.getId(),5);
        int balance = shop.getBooks().get(joel);
        int cBooks = c.getBooks().get(joel);
        Assert.assertEquals(balance,95);
        Assert.assertEquals(cBooks,5);
    }
    @Test
    public void testPriceFilter(){
        Book test1 = new Book(1,"1",1999,"Joel Olausson",Genre.BIOGRAPHY,100,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100,200);
        HashMap<Book,Integer> data = new HashMap<>();
        data.put(test1,100);
        data.put(test2,100);
        BookShop shop = new BookShop(data,1000);

        HashMap<Book,Integer> books = shop.getBooksWithFilter((book, integer) ->book.price<=150);
        Assert.assertTrue(books.containsKey(test1));
        Assert.assertFalse(books.containsKey(test2));
    }
    @Test
    public void testGenreFilter(){
        Book test1 = new Book(1,"1",1999,"Joel Olausson",Genre.ADVENTURE,100,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100,200);
        Book test3 = new Book(3,"3",1999,"Joel Olausson",Genre.SCI_FI,100,200);
        HashMap<Book,Integer> data = new HashMap<>();
        data.put(test1,100);
        data.put(test2,100);
        data.put(test3,100);
        BookShop shop = new BookShop(data,1000);

        List<Genre> genreList = new ArrayList<>(Arrays.asList(Genre.SCI_FI,Genre.BIOGRAPHY));
        HashMap<Book,Integer> books = shop.getBooksWithFilter((book, integer) -> genreList.contains(book.genre));
        Assert.assertFalse(books.containsKey(test1));
        Assert.assertTrue(books.containsKey(test2));
        Assert.assertTrue(books.containsKey(test3));
    }
    @Test
    public void testYearFilter(){
        Book test1 = new Book(1,"1",1998,"Joel Olausson",Genre.ADVENTURE,100,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100,200);
        Book test3 = new Book(3,"3",2000,"Joel Olausson",Genre.SCI_FI,100,200);
        HashMap<Book,Integer> data = new HashMap<>();
        data.put(test1,100);
        data.put(test2,100);
        data.put(test3,100);
        BookShop shop = new BookShop(data,1000);

        HashMap<Book,Integer> books = shop.getBooksWithFilter((book, integer) -> book.publishYear>=0 && book.publishYear<=1999);
        Assert.assertTrue(books.containsKey(test1));
        Assert.assertTrue(books.containsKey(test2));
        Assert.assertFalse(books.containsKey(test3));
    }

    private static HashMap<Book,Integer> generateMockStockBalance(){
        HashMap<Book,Integer> data = new HashMap<>();
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson",Genre.BIOGRAPHY,100,420);
        Book prideAndPrejudice = new Book(1,"Pride and Justice",1813, "Jane Austen", Genre.ROMANCE, 432, 159.90);
        Book mobyDick = new Book(2,"Moby Dick", 1851, "Herman Melville", Genre.ADVENTURE, 635, 189.90);
        Book greatExpectations = new Book(3,"Great Expectations",1861, "Charles Dickens", Genre.LITERARY_FICTION, 505, 149.90);
        Book crimeAndPunishment = new Book(4,"Crime and Punishment",1866, "Fyodor Dostoevsky", Genre.MYSTERY, 671, 199.90);
        Book warAndPeace = new Book(5,"War and Peace",1869, "Leo Tolstoy", Genre.HISTORICAL_FICTION, 1225, 259.90);
        Book huckleberryFinn = new Book(6,"Huckleberry Finn",1884, "Mark Twain", Genre.ADVENTURE, 366, 129.9);
        Book dracula = new Book(7,"Dracula",1897, "Bram Stoker", Genre.HORROR, 418, 169.9);
        Book ulysses = new Book(8,"Ulysses",1922, "James Joyce", Genre.LITERARY_FICTION, 730, 209.9);
        Book theGreatGatsby = new Book(9,"The Great Gatsby",1925, "F. Scott Fitzgerald", Genre.LITERARY_FICTION, 180, 109.9);
        Book b1984 = new Book(10,"1984",1949, "George Orwell", Genre.DYSTOPIAN, 328, 149.9);
        data.put(joel,40);
        data.put(prideAndPrejudice,24);
        data.put(mobyDick,5);
        data.put(greatExpectations,26);
        data.put(crimeAndPunishment,56);
        data.put(warAndPeace,14);
        data.put(huckleberryFinn,10);
        data.put(dracula,38);
        data.put(ulysses,8);
        data.put(theGreatGatsby,45);
        data.put(b1984,84);
        return data;
    }

    private static BookShop generateMockShop(){
        return new BookShop(generateMockStockBalance(),1000);
    }
}
