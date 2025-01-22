package DeviesIntervju.API.model;

public class StockEntry {
    private Book book;
    private int stock;

    private double price;

    public StockEntry(Book book, int stock, double price) {
        this.book = book;
        this.stock = stock;
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
