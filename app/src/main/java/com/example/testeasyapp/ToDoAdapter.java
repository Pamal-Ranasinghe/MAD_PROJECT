package com.example.testeasyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<Currency> implements Filterable {
    private Context context;
    private int resource;
    private List<Currency> todos;
    private List<Currency> todosFull;

    ToDoAdapter(Context context, int resource, List<Currency> todos){
        super(context,resource,todos);
        this.context = context;
        this.resource = resource;
        this.todos = todos;
        todosFull = new ArrayList<>(todos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource,parent,false);

        TextView title = row.findViewById(R.id.title);
        TextView description = row.findViewById(R.id.description);
        TextView amount = row.findViewById(R.id.amount);
        ImageView imageView = row.findViewById(R.id.onGoing);


        Currency currency = todos.get(position);
        title.setText(currency.getTitle());
        description.setText(currency.getDescription());
        amount.setText(Double.toString(currency.getAmount()));
        imageView.setVisibility(row.INVISIBLE);

        if(currency.getFinished() > 0){
            imageView.setVisibility(View.VISIBLE);
        }

        return row;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return taksFilter;
    }

    private Filter taksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Currency> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(todosFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Currency item : todosFull){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            todos.clear();;
            todos.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
