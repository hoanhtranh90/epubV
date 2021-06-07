package com.folioreader.model;

public class BookImpl {
    private int ID;
    private String bookId;
    private String loc;
    private String bookName;
    private String bookPath;

    public BookImpl() {
    }

    public BookImpl(int ID, String bookId, String loc, String bookName, String bookPath) {
        this.ID = ID;
        this.bookId = bookId;
        this.loc = loc;
        this.bookName = bookName;
        this.bookPath = bookPath;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
