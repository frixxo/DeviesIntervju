package DeviesIntervju.service;

import DeviesIntervju.API.model.Book;
import DeviesIntervju.API.model.Customer;
import DeviesIntervju.API.model.Genre;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class BookShop {

    private HashMap<Book, Integer> stockBalance;
    private double money;

    private List<Customer> customers;

    public BookShop(){
        this(generateMockStockBalance(),10000,new ArrayList<>(Arrays.asList(new Customer(1000,0))));
    }
    public BookShop(HashMap<Book, Integer> stockBalance, double money,List<Customer> customers) {
        this.stockBalance = stockBalance;
        this.money = money;

        this.customers = new ArrayList<>();
        //add all customers but remove duplicate ids
        for (Customer c:customers) {
            if(getCustomerById(c.getId()) == null) this.customers.add(c);
        }
    }
    public BookShop(HashMap<Book, Integer> stockBalance, double money) {
        this(stockBalance,money,new ArrayList<Customer>());
    }


    public boolean buyBook(int bookId, int customerId, int amount) {
        Customer c = customers.get(customerId);
        Book b = getBookById(bookId);

        if(c==null) return false;
        if(!c.hasMoney(b.price*amount)) return false;
        if(!stockBalance.containsKey(b)||stockBalance.get(b)<amount) return false;
        stockBalance.replace(b,stockBalance.get(b)-amount);
        money+= amount*b.price;
        c.chargeMoney(amount*b.price);
        c.addBook(b,amount);
        return true;
    }

    public HashMap<Book,Integer> getBooks() {
        return new HashMap<>(stockBalance);
    }

    public HashMap<Book, Integer> getBooksWithFilter(BiPredicate<Book, Integer> filter) {
        HashMap<Book,Integer> duplicate = new HashMap<>(stockBalance);
        Iterator<HashMap.Entry<Book, Integer>> iterator = duplicate.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Book, Integer> entry = iterator.next();
            if (!filter.test(entry.getKey(), entry.getValue())) {
                iterator.remove();
            }
        }
        return duplicate;
    }
    public HashMap<Book,Integer> getBooksThatCustomerDontHave(int customerId){
        return getBooksWithFilter((book, integer) -> customers.get(customerId).getBooks().containsKey(book));
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

    private Book getBookById(int id){
        List<Book> book = stockBalance.keySet().stream()
                .filter(b -> b.id == id).collect(Collectors.toList());
        return book.get(0); //can only be one with same id
    }
    private Customer getCustomerById(int id){
        List<Customer> customer = customers.stream()
                .filter(b -> b.getId() == id).collect(Collectors.toList());
        if(customer.size()==0) return null;
        return customer.get(0); //can only be one with same id
    }

    private static HashMap<Book,Integer> generateMockStockBalance(){
        HashMap<Book,Integer> data = new HashMap<>();
        Book joel = new Book(0,"Joels Test-bok",1999,"Joel Olausson", Genre.BIOGRAPHY,100,420);
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
}
