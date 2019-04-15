package com.excelmania.sesliajanda;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomizeListView extends Activity {
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_ID = "id";
    static final String KEY_SONG = "song";
    static final String KEY_THUMB_URL = "thumb_url";
    static final String KEY_TITLE = "title";
    static final String URL = "http://api.androidhive.info/music/music.xml";
    LazyAdapter adapter;
    ArrayList<mNot> dbNotlar = new ArrayList <> ();
    ArrayList<HashMap<String, String>> itemList = new ArrayList <> ();
    ListView list;
    Agenta m;

    class mNot {
        public String detay;
        public String id;
        public String tarih;

        mNot() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        this.m = new Agenta (this);
        try {
            KayitGetir();
            for (mNot n : this.dbNotlar) {
                HashMap <String, String> map = new HashMap <> ( );
                map.put ( KEY_ID, n.id );
                map.put ( KEY_TITLE, n.detay );
                map.put ( KEY_ARTIST, n.tarih );
                this.itemList.add ( map );
            }
            this.list = findViewById(R.id.list);
            this.adapter = new LazyAdapter(this, this.itemList);
            this.list.setAdapter(this.adapter);
            this.list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    mNot n = CustomizeListView.this.dbNotlar.get(position);
                    CustomizeListView.this.m.OpenWithArgsSola(Write.class, new String[]{CustomizeListView.KEY_ID, "detay", "tarih"}, new String[]{n.id, n.detay, n.tarih});
                }
            });
        } catch (Exception e) {
        }
    }

    public void yeni(View v) {
        this.m.OpenSola(Write.class);
    }

    /* Access modifiers changed, original: 0000 */
    public void KayitGetir() {
        String[] alanlar = new String[]{KEY_ID, "detay", "tarih"};
        String[] strArr = null;
        String str = null;
        String str2 = null;
        Cursor kayitlar = new Veritabani(this).getReadableDatabase().query("notlar", alanlar, null, strArr, str, str2, "id DESC");
        while (kayitlar.moveToNext()) {
            long id = kayitlar.getLong(kayitlar.getColumnIndex(KEY_ID));
            String detay = kayitlar.getString(kayitlar.getColumnIndex("detay"));
            String tarih = kayitlar.getString(kayitlar.getColumnIndex("tarih"));
            detay = detay.trim();
            mNot n = new mNot();
            n.id = id + "";
            n.detay = detay;
            n.tarih = tarih;
            this.dbNotlar.add(n);
        }
    }
}