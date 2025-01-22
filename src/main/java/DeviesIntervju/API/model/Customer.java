package DeviesIntervju.API.model;

import java.util.HashMap;

public class Customer {
    private int id;
    private double money;
    private HashMap<Book,Integer> books;

    public Customer(double money,int id) {
        this(money,id,new HashMap<>());
    }
    public Customer(double money,int id,HashMap<Book,Integer> books) {
        this.money = money;
        this.books = books;
        this.id = id;
    }

    public boolean hasMoney(double price){
        return money>=price;
    }

    public boolean chargeMoney(double amount){
        if(amount>money) return false;
        money-=amount;
        return true;
    }

    public void addBook(Book b, int amount){
        if(books.containsKey(b))
            books.replace(b,books.get(b)+amount);
        else
            books.put(b,amount);
        if(books.get(b)==0) books.remove(b);
    }

    public double getMoney() {
        return money;
    }
    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public int getId() {
        return id;
    }
}
