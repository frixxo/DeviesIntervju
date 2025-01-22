package DeviesIntervju.service;

import DeviesIntervju.API.model.Book;
import DeviesIntervju.API.model.Customer;
import DeviesIntervju.API.model.Genre;
import DeviesIntervju.API.model.StockEntry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class BookShop {

    private List<StockEntry> stockBalance;
    private double money;

    private List<Customer> customers;

    public BookShop(){
        this(generateMockStockBalance(),10000,new ArrayList<>(List.of(new Customer(1000, 0))));
    }
    public BookShop(List<StockEntry> stockBalance, double money,List<Customer> customers) {
        this.stockBalance = stockBalance;
        this.money = money;

        this.customers = new ArrayList<>();
        //add all customers but remove duplicate ids
        for (Customer c:customers) {
            if(getCustomerById(c.getId()) == null) this.customers.add(c);
        }
    }
    public BookShop(List<StockEntry> stockBalance, double money) {
        this(stockBalance,money,new ArrayList<>());
    }


    public boolean buyBook(int bookId, int customerId, int amount){
        if(amount<=0) return false;

        Customer c = customers.get(customerId);
        StockEntry s = getStockById(bookId);

        if(c==null) return false;
        if(s==null) return false;
        if(!c.hasMoney(s.getPrice()*amount)) return false;
        if(s.getStock()<amount) return false;

        return transferBook(bookId,customerId,amount);
    }
    public boolean sellBook(int bookId, int customerId, int amount){
        if(amount<=0) return false;

        Customer c = customers.get(customerId);
        StockEntry s = getStockById(bookId);

        if(c==null) return false;
        if(s==null) return false;

        if(money<amount) return false;
        if(c.getBooks().get(s.getBook())<amount) return false;

        return transferBook(bookId,customerId,-amount);
    }

    public boolean transferBook(int bookId, int customerId, int amount) {
        Customer c = customers.get(customerId);
        StockEntry s = getStockById(bookId);

        s.setStock(s.getStock()-amount);
        money+= amount*s.getPrice();
        c.chargeMoney(amount*s.getPrice());
        c.addBook(s.getBook(),amount);
        return true;
    }

    public List<StockEntry> getBooks() {
        return new ArrayList<>(stockBalance);
    }

    public List<StockEntry> getBooksWithFilter(Predicate<StockEntry> filter) {
        List<StockEntry> duplicate = new ArrayList<>(stockBalance);
        duplicate.removeIf(entry -> !filter.test(entry));
        return duplicate;
    }

    public double getMoney(){
        return money;
    }

    public Customer getNewCustomer(double money){
        int id = 0;
        while(getCustomerById(id)!=null){
            Random random = new Random();
            id = random.nextInt();
        }
        Customer c = new Customer(money,id);
        customers.add(c);
        return c;
    }

    public StockEntry getStockById(int id){
        List<StockEntry> s = stockBalance.stream().filter(b -> b.getBook().id == id).toList();
        if(s.size()==1) return s.get(0); //can only be one with same id
        return null;
    }

    public Customer getCustomerById(int id){
        List<Customer> customer = customers.stream()
                .filter(b -> b.getId() == id).toList();
        if(customer.size()==0) return null;
        return customer.get(0); //can only be one with same id
    }

    private static List<StockEntry> generateMockStockBalance(){
        List<StockEntry> data = new ArrayList<>();
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson", Genre.BIOGRAPHY,100);
        Book prideAndPrejudice = new Book(1,"Pride and Justice",1813, "Jane Austen", Genre.ROMANCE, 432);
        Book mobyDick = new Book(2,"Moby Dick", 1851, "Herman Melville", Genre.ADVENTURE, 635);
        Book greatExpectations = new Book(3,"Great Expectations",1861, "Charles Dickens", Genre.LITERARY_FICTION, 505);
        Book crimeAndPunishment = new Book(4,"Crime and Punishment",1866, "Fyodor Dostoevsky", Genre.MYSTERY, 671);
        Book warAndPeace = new Book(5,"War and Peace",1869, "Leo Tolstoy", Genre.HISTORICAL_FICTION, 1225);
        Book huckleberryFinn = new Book(6,"Huckleberry Finn",1884, "Mark Twain", Genre.ADVENTURE, 366);
        Book dracula = new Book(7,"Dracula",1897, "Bram Stoker", Genre.HORROR, 418);
        Book ulysses = new Book(8,"Ulysses",1922, "James Joyce", Genre.LITERARY_FICTION, 730);
        Book theGreatGatsby = new Book(9,"The Great Gatsby",1925, "F. Scott Fitzgerald", Genre.LITERARY_FICTION, 180);
        Book b1984 = new Book(10,"1984",1949, "George Orwell", Genre.DYSTOPIAN, 328);
        data.add(new StockEntry(joel,40,420));
        data.add(new StockEntry(prideAndPrejudice, 24, 320));
        data.add(new StockEntry(mobyDick, 5, 150));
        data.add(new StockEntry(greatExpectations, 26, 280));
        data.add(new StockEntry(crimeAndPunishment, 56, 450));
        data.add(new StockEntry(warAndPeace, 14, 600));
        data.add(new StockEntry(huckleberryFinn, 10, 200));
        data.add(new StockEntry(dracula, 38, 370));
        data.add(new StockEntry(ulysses, 8, 410));
        data.add(new StockEntry(theGreatGatsby, 45, 340));
        data.add(new StockEntry(b1984, 84, 500));
        return data;
    }
}
