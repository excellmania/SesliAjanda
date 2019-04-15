package com.excelmania.sesliajanda;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Write extends Activity {
    int check = 1000;
    String detay;
    String id;
    Agenta m;
    ArrayList<String> results;
    String tarih;
    EditText tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_layout);
        this.m = new Agenta(this);
        this.tv = this.m.textbox(R.id.tv);
        this.id = this.m.getAttr("id");
        this.detay = this.m.getAttr("detay");
        this.tarih = this.m.getAttr("tarih");
        this.tv.setText(this.detay);
        if (isDetay()) {
            this.m.AgentaSet(R.id.txtBaslik, this.detay);
        }
        char[] chars = new char[40];
        Arrays.fill(chars, ( char ) 10 );
        this.tv.setText(this.tv.getText() + new String(chars));
        this.tv.setSelection(this.detay.length());
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isDetay() {
        return !this.detay.replace ( "\n", "" ).trim ( ).equals ( "" );
    }

    public void konus(View v) {
        Intent i = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        i.putExtra("android.speech.extra.LANGUAGE", "tr-TR");
        i.putExtra("android.speech.extra.PROMPT", "Şimdi konuşun!");
        startActivityForResult(i, this.check);
    }

    public void sil(View v) {
        if (!this.id.equals("")) {
            final Veritabani vt = new Veritabani(this);
            this.m.confirm("Sil", "Notu sil ?", new Callable() {
                public Object call() {
                    vt.getWritableDatabase().delete("notlar", " id = " + Write.this.id, null);
                    Write.this.m.OpenSaga(CustomizeListView.class);
                    return null;
                }
            });
        }
    }

    public void paylas(View v) {
        this.m.share("Not", this.tv.getText() + "");
    }

    public void kaydet(View v) {
        if (!this.tv.getText().toString().replace("\n", "").trim().equals("")) {
            SQLiteDatabase db = new Veritabani(this).getWritableDatabase();
            ContentValues veriler = new ContentValues();
            veriler.put("detay", this.tv.getText() + "");
            if (this.id.equals("")) {
                db.insertOrThrow("notlar", null, veriler);
                KayitGetir();
            } else {
                db.update("notlar", veriler, " id = " + this.id, null);
            }
            this.m.hideKeyboard(this.tv);
            this.m.message("Notunuz Kaydedildi !");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void KayitGetir() {
        String[] alanlar = new String[]{"id", "detay", "tarih"};
        String[] strArr = null;
        String str = null;
        String str2 = null;
        Cursor kayitlar = new Veritabani(this).getReadableDatabase().query("notlar", alanlar, null, strArr, str, str2, "id DESC");
        if (kayitlar.moveToNext()) {
            long i = kayitlar.getLong(kayitlar.getColumnIndex("id"));
            String d = kayitlar.getString(kayitlar.getColumnIndex("detay"));
            String t = kayitlar.getString(kayitlar.getColumnIndex("tarih"));
            d = this.detay.trim();
            this.id = i + "";
            this.detay = d;
            this.tarih = t;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == this.check && resultCode == -1) {
                ArrayList<String> text = data.getStringArrayListExtra("android.speech.extra.RESULTS");
                this.results = text;
                String gelen = text.get(0);
                String topla = "";
                int i = this.tv.getSelectionStart();
                String ilk = this.tv.getText() + "";
                int sel = i + gelen.length();
                if (i != 0) {
                    String bosluk = "";
                    if (ilk.charAt(i - 1) != ' ') {
                        bosluk = " ";
                        sel++;
                    }
                    topla = ilk.substring(0, i) + bosluk + gelen + ilk.substring(i);
                } else {
                    topla = gelen + ilk;
                }
                this.tv.setText(topla + "  ");
                this.tv.setSelection(sel);
            }
        } catch (Exception e) {
        }
    }

    public void onBackPressed() {
        this.m.OpenSaga(CustomizeListView.class);
        super.onBackPressed();
    }
}
