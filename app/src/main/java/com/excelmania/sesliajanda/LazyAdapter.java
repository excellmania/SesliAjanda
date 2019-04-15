package com.excelmania.sesliajanda;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class LazyAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

        public LazyAdapter( Activity a, ArrayList<HashMap<String, String>> d) {
        this.activity = a;
        this.data = d;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_row, null);
        }
        TextView title = vi.findViewById(R.id.title);
        TextView artist = vi.findViewById(R.id.artist);
        ImageView thumb_image = vi.findViewById(R.id.list_image);
        HashMap<String, String> song = new HashMap();
        song = this.data.get(position);
        title.setText( song.get("title") );
        artist.setText( song.get("artist") );
        return vi;
    }
}