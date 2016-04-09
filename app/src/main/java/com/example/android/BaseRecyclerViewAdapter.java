package com.example.android;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 4/8/2016.
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    List<T> items;

    public BaseRecyclerViewAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(), parent, false);
        return inflateViewHolder(view);
    }

    public void addAll(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void swap(List<T> items) {
        this.items.clear();
        notifyDataSetChanged();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(T item) {
        items.add(item);
        notifyItemChanged(items.size() - 1);
    }

    public List<T> getItems() {
        return items;
    }

    public void removeItem(T item) {
        int pos = items.indexOf(item);
        items.remove(item);
        //notifyItemChanged(pos); //Causes crashes
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public int size() {
        return items.size();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract int getLayoutResource();

    protected abstract VH inflateViewHolder(View view);
}
