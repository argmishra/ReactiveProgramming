package com.demo.util;

import com.github.javafaker.Book;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BookOrder {

  private final String title;
  private final String author;
  private final String category;
  private final double price;

  public BookOrder() {
    Book book = Utils.faker().book();
    this.title = book.title();
    this.author = book.author();
    this.category = book.genre();
    this.price = Double.parseDouble(Utils.faker().commerce().price());
  }
}