package com.example.library.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 書籍の登録・編集フォーム
 */
public class BookForm {

    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    private String title;

    @NotBlank(message = "著者を入力してください")
    @Size(max = 50, message = "著者は50文字以内で入力してください")
    private String author;

    @NotBlank(message = "ISBNを入力してください")
    @Pattern(regexp = "\\d{13}", message = "ISBNは13桁の数字で入力してください")
    private String isbn;

    @Min(value = 1900, message = "出版年は1900以降で入力してください")
    @Max(value = 2100, message = "出版年は2100以前で入力してください")
    private Integer publishedYear;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
}
