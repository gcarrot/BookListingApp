package si.gcarrot.booklistingapp;

/**
 * Created by Urban on 7/5/17.
 */

public class Book {

    /** Book author **/
    private String mAuthor;

    /** Book title **/
    private String mTitle;

    /** Book published date **/
    private String mPublishedDate;

    public Book(String author, String title, String publishedDate) {
        mAuthor = author;
        mTitle = title;
        mPublishedDate = publishedDate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }


}
