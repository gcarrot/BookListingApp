package si.gcarrot.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Urban on 7/5/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView authorView = (TextView) listItemView.findViewById(R.id.book_author);
        TextView titleView = (TextView) listItemView.findViewById(R.id.book_title);
        TextView dateView = (TextView) listItemView.findViewById(R.id.book_date);

        authorView.setText(currentBook.getAuthor());
        titleView.setText(currentBook.getTitle());
        dateView.setText(currentBook.getPublishedDate());

        return listItemView;
    }
}