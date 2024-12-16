package pt.andrexdias.cybershield;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<Article> {

    public NewsAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.title);
        TextView descriptionTextView = listItemView.findViewById(R.id.description);

        titleTextView.setText(currentArticle.getTitle());
        descriptionTextView.setText(currentArticle.getDescription());

        listItemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getUrl()));
            getContext().startActivity(intent);
        });

        return listItemView;
    }
}