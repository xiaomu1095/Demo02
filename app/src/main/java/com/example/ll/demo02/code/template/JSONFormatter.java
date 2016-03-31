package com.example.ll.demo02.code.template;

/**
 * Created by Administrator on 2016/3/31.
 */
public class JSONFormatter extends Formatter {

    @Override
    protected String formating(Book book) {
        String result = "";
        result += "{\n";
        result += "\"book_name\" : \"" + book.getBookName() + "\",\n";
        result += "\"pages\" : \"" + book.getPages() + "\",\n";
        result += "\"price\" : \"" + book.getPrice() + "\",\n";
        result += "\"author\" : \"" + book.getAuthor() + "\",\n";
        result += "\"isbn\" : \"" + book.getIsbn() + "\",\n";
        result += "}";
        return result;
    }

}
