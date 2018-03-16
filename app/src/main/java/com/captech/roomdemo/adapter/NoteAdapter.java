package com.captech.roomdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.captech.roomdemo.R;
import com.captech.roomdemo.domain.CategoryNote;

import java.util.ArrayList;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * @author acampbell
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {

    private List<CategoryNote> notes = new ArrayList<>();
    private List<CategoryNote> noteListFiltered = new ArrayList<>();
    private final ActionCallback callback;

    public interface ContactsAdapterListener {
        void onContactSelected(CategoryNote note);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    noteListFiltered = notes;
                } else {
                    List<CategoryNote> filteredList = new ArrayList<>();
                    for (CategoryNote row : notes) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    noteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = noteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteListFiltered = (ArrayList<CategoryNote>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public NoteAdapter(List<CategoryNote> notes, @NonNull ActionCallback callback) {
        this.notes = notes;
        this.noteListFiltered = notes;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onEdit(notes.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryNote note = noteListFiltered.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return noteListFiltered.size();
    }

    public CategoryNote getNote(int position) {
        return notes.get(position);
    }

    public void setNotes(@NonNull List<CategoryNote> notes) {
        this.notes = notes;
        this.noteListFiltered = notes;
        notifyDataSetChanged();
    }

    public interface ActionCallback {
        void onEdit(CategoryNote note);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, category;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

        }
    }
}
