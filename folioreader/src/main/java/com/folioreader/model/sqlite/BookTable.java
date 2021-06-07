package com.folioreader.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.folioreader.model.BookImpl;
import com.folioreader.model.HighlightImpl;

import java.util.ArrayList;

public class BookTable {
    public static final String TABLE_NAME = "book_table";

    public static final String ID = "_id";
    public static final String COL_BOOK_ID = "bookId";
    private static final String COL_LOC = "loc";
    private static final String COL_BOOK_NAME = "bookName";
    private static final String COL_BOOK_PATH = "bookPath";


    public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
            + COL_BOOK_ID + " TEXT" + ","
            + COL_LOC + " TEXT" + ","
            + COL_BOOK_PATH + " TEXT" + ","
            + COL_BOOK_NAME + " TEXT" + ")";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static ArrayList<BookImpl> getAllBook() {
        ArrayList<BookImpl> book = new ArrayList<>();
        Cursor bookCursor = DbAdapter.getAllBook();
        while (bookCursor.moveToNext()) {
            book.add(new BookImpl(bookCursor.getInt(bookCursor.getColumnIndex(ID)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_ID)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_LOC)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_PATH)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_NAME))));
        }
        return book;
    }

    public static void addBook(BookImpl book) {
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID, book.getBookId());
        values.put(COL_LOC, book.getLoc());
        values.put(COL_BOOK_PATH, book.getBookPath());
        values.put(COL_BOOK_NAME, book.getBookName());
        DbAdapter.insert(TABLE_NAME,values);
    }
    public static BookImpl getBookById(int id) {
        Cursor bookCursor = DbAdapter.getBookById(id);
        BookImpl book = new BookImpl();
        while (bookCursor.moveToNext()) {
            book = new BookImpl(bookCursor.getInt(bookCursor.getColumnIndex(ID)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_ID)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_LOC)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_PATH)),
                    bookCursor.getString(bookCursor.getColumnIndex(COL_BOOK_NAME)));
        }
        return book;
    }

    public static boolean updateLoc(BookImpl book) {
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID, book.getBookId());
        values.put(COL_LOC, book.getLoc());
        values.put(COL_BOOK_PATH, book.getBookPath());
        values.put(COL_BOOK_NAME, book.getBookName());

        return DbAdapter.update(TABLE_NAME,ID, String.valueOf(book.getID()),values);
    }
}
