package com.example.ll.demo02.code.template;

/**
 * Created by Administrator on 2016/3/31.
 */
public class Book {
    private String bookName;

    private int pages;

    private double price;

    private String author;

    private String isbn;

    public Book(String bookName, int pages, double price, String author, String isbn) {
        this.bookName = bookName;
        this.pages = pages;
        this.price = price;
        this.author = author;
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
