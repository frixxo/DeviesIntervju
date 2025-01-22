package DeviesIntervju;

import DeviesIntervju.API.model.*;
import DeviesIntervju.service.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson", Genre.BIOGRAPHY,100);
        StockEntry s = new StockEntry(joel,100,100);

        BookShop shop = new BookShop(new ArrayList<>(Arrays.asList(s)),1000);
        Customer c = shop.getNewCustomer(1000);
        shop.buyBook(0,c.getId(),5);
        Assert.assertEquals(shop.getMoney(),1500,0.01);
        Assert.assertEquals(c.getMoney(),500,0.01);
    }
    @Test
    public void moveStockWhenBuyingTest(){
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson", Genre.BIOGRAPHY,100);
        StockEntry s = new StockEntry(joel,100,100);
        BookShop shop = new BookShop(new ArrayList<>(Arrays.asList(s)),1000);
        Customer c = shop.getNewCustomer(1000);
        shop.buyBook(0,c.getId(),5);
        int balance = shop.getStockById(joel.id).getStock();
        int cBooks = c.getBooks().get(joel);
        Assert.assertEquals(balance,95);
        Assert.assertEquals(cBooks,5);
    }
    @Test
    public void testPriceFilter(){
        Book test1 = new Book(1,"1",1999,"Joel Olausson",Genre.BIOGRAPHY,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100);
        List<StockEntry> data = new ArrayList<>();
        StockEntry s1 = new StockEntry(test1,100,100);
        StockEntry s2 = new StockEntry(test2,100,200);
        data.add(s1);
        data.add(s2);
        BookShop shop = new BookShop(data,1000);

        List<StockEntry> stockEntries = shop.getBooksWithFilter((stockEntry) ->stockEntry.getPrice()<=150);

        Assert.assertTrue(stockEntries.contains(s1));
        Assert.assertFalse(stockEntries.contains(s2));
    }
    @Test
    public void testGenreFilter(){
        Book test1 = new Book(1,"1",1999,"Joel Olausson",Genre.ADVENTURE,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100);
        Book test3 = new Book(3,"3",1999,"Joel Olausson",Genre.SCI_FI,100);
        List<StockEntry> data = new ArrayList<>();
        StockEntry s1=new StockEntry(test1,100,100);
        StockEntry s2=new StockEntry(test2,100,100);
        StockEntry s3=new StockEntry(test3,100,100);
        data.add(s1);
        data.add(s2);
        data.add(s3);
        BookShop shop = new BookShop(data,1000);

        List<Genre> genreList = new ArrayList<>(Arrays.asList(Genre.SCI_FI,Genre.BIOGRAPHY));
        List<StockEntry> stockEntries = shop.getBooksWithFilter((stockEntry) -> genreList.contains(stockEntry.getBook().genre));

        Assert.assertFalse(stockEntries.contains(s1));
        Assert.assertTrue(stockEntries.contains(s2));
        Assert.assertTrue(stockEntries.contains(s3));
    }
    @Test
    public void testYearFilter(){
        Book test1 = new Book(1,"1",1998,"Joel Olausson",Genre.ADVENTURE,100);
        Book test2 = new Book(2,"2",1999,"Joel Olausson",Genre.BIOGRAPHY,100);
        Book test3 = new Book(3,"3",2000,"Joel Olausson",Genre.SCI_FI,100);
        List<StockEntry> data = new ArrayList<>();
        StockEntry s1 = new StockEntry(test1,100,100);
        StockEntry s2 = new StockEntry(test2,100,100);
        StockEntry s3 = new StockEntry(test3,100,100);
        data.add(s1);
        data.add(s2);
        data.add(s3);
        BookShop shop = new BookShop(data,1000);

        List<StockEntry> stockEntries = shop.getBooksWithFilter((stockEntry) -> stockEntry.getBook().publishYear>=0 && stockEntry.getBook().publishYear<=1999);
        Assert.assertTrue(stockEntries.contains(s1));
        Assert.assertTrue(stockEntries.contains(s2));
        Assert.assertFalse(stockEntries.contains(s3));
    }

}
