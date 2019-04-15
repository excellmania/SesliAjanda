package com.excelmania.sesliajanda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Agenta extends Activity {
    int TotalHeight = -1;
    Activity cx;
    boolean durmus = false;
    private String filePath = "";
    private int kaydirma_sure;
    public boolean mHasToRestoreState = true;
    private boolean oldu = false;
    private ProgressDialog pDialog;
    private WebView web;
    Handler yardimci;
    Timer zamanlayici;

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Agenta.this.pDialog.show();
        }

        /* Access modifiers changed, original: protected|varargs */
        public String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/" + Agenta.this.filePath);
                byte[] data = new byte[1024];
                long total = 0;
                while (true) {
                    int count = input.read(data);
                    if (count == -1) {
                        break;
                    }
                    total += (long) count;
                    publishProgress(new String[]{"" + ((int) ((100 * total) / ((long) lenghtOfFile)))});
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        /* Access modifiers changed, original: protected|varargs */
        public void onProgressUpdate(String... progress) {
            Agenta.this.pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String file_url) {
            Agenta.this.pDialog.dismiss();
            Agenta.this.message("Resim indirildi.");
        }
    }

    public Agenta(Activity c) {
        this.cx = c;
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenSola(Class<?> cls) {
        Open(cls);
        animasyonSola();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenSaga(Class<?> cls) {
        Open(cls);
        animasyonSaga();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgsSola(Class<?> cls, String[] args, String[] vals) {
        OpenWithArgs(cls, args, vals);
        animasyonSola();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgsSaga(Class<?> cls, String[] args, String[] vals) {
        OpenWithArgs(cls, args, vals);
        animasyonSaga();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgsSola(Class<?> cls, String arg, String val) {
        OpenWithArgs(cls, new String[]{arg}, new String[]{val});
        animasyonSola();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgsSaga(Class<?> cls, String arg, String val) {
        OpenWithArgs(cls, new String[]{arg}, new String[]{val});
        animasyonSaga();
    }

    public void Open(Class<?> cls) {
        this.cx.startActivity(new Intent(this.cx, cls));
        this.cx.finish();
    }

    /* Access modifiers changed, original: 0000 */
    //@SuppressLint("WrongConstant")
    public void OpenClear( Class<?> cls) {
        Intent intent = new Intent(this.cx, cls);
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        startActivity(intent);
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgs(Class<?> cls, String[] args, String[] vals) {
        OpenArgs(cls, args, vals);
        this.cx.finish();
    }

    /* Access modifiers changed, original: 0000 */
    public void OpenWithArgsNotFinish(Class<?> cls, String[] args, String[] vals) {
        OpenArgs(cls, args, vals);
    }

    private void OpenArgs(Class<?> cls, String[] args, String[] vals) {
        Intent intent = new Intent(this.cx, cls);
        for (int i = 0; i < args.length; i++) {
            intent.putExtra(args[i], vals[i]);
        }
        this.cx.startActivity(intent);
    }

    /* Access modifiers changed, original: 0000 */
    public String getAttr(String name) {
        try {
            Bundle extras = this.cx.getIntent().getExtras();
            if (extras.containsKey(name)) {
                return extras.getString(name);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Bundle extra() {
        return this.cx.getIntent().getExtras();
    }

    /* Access modifiers changed, original: 0000 */
    public void SendEmail(String emailTo, String subject) {
        Intent email = new Intent("android.intent.action.SEND");
        email.putExtra("android.intent.extra.EMAIL", new String[]{emailTo});
        email.putExtra("android.intent.extra.SUBJECT", subject);
        email.putExtra("android.intent.extra.TEXT", "Konu : ");
        email.setType("message/rfc822");
        this.cx.startActivity(Intent.createChooser(email, "Email gönderme aracı :"));
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isNet() {
        try {
            @SuppressLint("WrongConstant") ConnectivityManager connectivity = (ConnectivityManager) this.cx.getSystemService("connectivity");
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info == null) {
                return false;
            }
            for (NetworkInfo state : info) {
                if (state.getState() == State.CONNECTED) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap BitmapSmall(String path, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /* Access modifiers changed, original: 0000 */
    public String ReadFile(String name) {
        try {
            InputStream stream = this.cx.getAssets().open(name);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* Access modifiers changed, original: 0000 */
    public String GetAssetPath() {
        return "file:///android_asset/";
    }

    /* Access modifiers changed, original: 0000 */
    public TextView textView(int i) {
        return (TextView) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public RelativeLayout relativeLayout(int i) {
        return (RelativeLayout) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public LinearLayout linearLayout(int i) {
        return (LinearLayout) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public ImageView imageview(int i) {
        return (ImageView) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public Button button(int i) {
        return (Button) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public EditText textbox(int i) {
        return (EditText) view(i);
    }

    /* Access modifiers changed, original: 0000 */
    public WebView webview(int i) {
        WebView engine = (WebView) view(i);
        engine.getSettings().setJavaScriptEnabled(true);
        engine.setWebViewClient(new WebViewClient());
        engine.getSettings().setBuiltInZoomControls(true);
        return engine;
    }

    /* Access modifiers changed, original: 0000 */
    public WebView webviewNoZoom(int i) {
        WebView w = webview(i);
        w.getSettings().setBuiltInZoomControls(false);
        return w;
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaSet(int i, String s) {
        textView(i).setText(s);
    }

    /* Access modifiers changed, original: 0000 */
    public void SetImageButton(View v, int i) {
        ((ImageButton) v).setImageResource(i);
    }

    /* Access modifiers changed, original: 0000 */
    public void SetImageView(View v, int i) {
        ((ImageView) v).setImageResource(i);
    }

    /* Access modifiers changed, original: 0000 */
    public View view(int i) {
        return this.cx.findViewById(i);
    }

    /* Access modifiers changed, original: 0000 */
    public void invisible(View v) {
        v.setVisibility(8);
    }

    /* Access modifiers changed, original: 0000 */
    public void invisible(int i) {
        view(i).setVisibility(8);
    }

    /* Access modifiers changed, original: 0000 */
    public void visible(View v) {
        v.setVisibility(0);
    }

    /* Access modifiers changed, original: 0000 */
    public void visible(int i) {
        view(i).setVisibility(0);
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackground(View v, int color) {
        v.setBackgroundColor(color);
    }

    /* Access modifiers changed, original: 0000 */
    public void webLoadTR(WebView web, String htmlSource) {
        web.loadDataWithBaseURL("", htmlSource, "text/html", "UTF-8", null);
    }

    /* Access modifiers changed, original: 0000 */
    public void webLoadHistory(WebView web) {
        WebBackForwardList mWebBackForwardList = web.copyBackForwardList();
        web.loadUrl(mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl());
    }

    /* Access modifiers changed, original: 0000 */
    public void LoadImageFromAsset(ImageView img, String fileName) {
        try {
            img.setImageDrawable(Drawable.createFromStream(getAssets().open(fileName), null));
        } catch (IOException e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void share(String title, String mes) {
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("text/plain");
        shareIntent.putExtra("android.intent.extra.SUBJECT", title);
        shareIntent.putExtra("android.intent.extra.TEXT", mes);
        this.cx.startActivity(Intent.createChooser(shareIntent, "Paylaş..."));
    }

    /* Access modifiers changed, original: 0000 */
    @SuppressLint("ResourceType")
    public void confirm( String title, String mes, final Callable func) {
        new Builder(this.cx).setTitle(title).setMessage(mes).setIcon(17301543).setPositiveButton(17039379, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(17039369, null).show();
    }

    /* Access modifiers changed, original: 0000 */
    public void message(String m) {
        Toast.makeText(this.cx, m, 3).show();
    }

    public ProgressDialog LoadDialog(String title) {
        return LoadDialog(title, "Yï¿½kleniyor...");
    }

    public ProgressDialog LoadDialog(String title, String mes) {
        ProgressDialog pDialog = ProgressDialog.show(this.cx, title, mes, true);
        pDialog.setCancelable(false);
        return pDialog;
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaWebClient(WebView engine, String title, String mes) {
        final ProgressDialog pDialog = LoadDialog(title, mes);
        engine.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                WebView webView = view;
                webView.loadDataWithBaseURL("", " <span style = \" font-weight:bold; font-size:20px; color:#c60000;  \" > İnternet bağlantısı yok !</span>   ", "text/html", "UTF-8", null);
                Agenta.this.MessageBox("Hata oluştu !");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pDialog.show();
            }

            public void onPageFinished(WebView view, String url) {
                pDialog.cancel();
                pDialog.dismiss();
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaWebClientScroll(WebView engine, String title, String mes, final int mProgressToRestore) {
        final ProgressDialog pDialog = LoadDialog(title, mes);
        engine.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Agenta.this.MessageBox("Hata Oluştu !");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pDialog.show();
            }

            public void onPageFinished(final WebView view, String url) {
                pDialog.cancel();
                pDialog.dismiss();
                if (Agenta.this.mHasToRestoreState) {
                    Agenta.this.mHasToRestoreState = false;
                    view.postDelayed(new Runnable() {
                        public void run() {
                            view.scrollTo(0, mProgressToRestore);
                        }
                    }, 300);
                }
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void WebError(WebView engine) {
        engine.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                WebView webView = view;
                webView.loadDataWithBaseURL("", " <span style = \" font-weight:bold; font-size:20px; color:#c60000;  \" >  İnternet bağlantısı yok!</span>   ", "text/html", "UTF-8", null);
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaWebFinishTimer(WebView web, final Callable<Void> func) {
        final ProgressDialog pDialog = LoadDialog("", "Yükleniyor...");
        web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Agenta.this.MessageBox("Hata Oluştu !");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pDialog.show();
            }

            public void onPageFinished(WebView view, String url) {
                view.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            pDialog.cancel();
                            func.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaWebLinkClick(WebView web, final Callable<Void> func) {
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void WebFindAll(WebView web, String kelime) {
        web.findAll(kelime);
        try {
            for (Method m : WebView.class.getDeclaredMethods()) {
                if (m.getName().equals("setFindIsUp")) {
                    m.setAccessible(true);
                    m.invoke(web, new Object[]{Boolean.valueOf(true)});
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void MessageBox(String s) {
        Builder builder = new Builder(this.cx);
        builder.setMessage(s);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    /* Access modifiers changed, original: 0000 */
    public void PlayMusic(int i) {
        try {
            MediaPlayer mp = MediaPlayer.create(this.cx.getBaseContext(), i);
            mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mp.start();
        } catch (Exception e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public MediaPlayer mediaPlayer(int i) {
        MediaPlayer mp = MediaPlayer.create(this.cx.getBaseContext(), i);
        mp.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        return mp;
    }

    /* Access modifiers changed, original: 0000 */
    public void showKeyboard(EditText txt) {
        ((InputMethodManager) this.cx.getSystemService("input_method")).toggleSoftInputFromWindow(txt.getApplicationWindowToken(), 2, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void hideKeyboard(EditText txt) {
        ((InputMethodManager) this.cx.getSystemService("input_method")).hideSoftInputFromWindow(txt.getWindowToken(), 0);
    }

    /* Access modifiers changed, original: 0000 */
    public String aralikBul(String source, String start, String end) {
        return source.substring(source.indexOf(start) + start.length(), source.indexOf(end) + end.length());
    }

    /* Access modifiers changed, original: 0000 */
    public String[] getLines(String source) {
        return source.split("\n");
    }

    /* Access modifiers changed, original: 0000 */
    public String getStarKey(String source, int index) {
        return source.split("\\*")[index];
    }

    /* Access modifiers changed, original: 0000 */
    public void EnterKey(EditText txt, final Callable<Void> funct) {
        txt.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 66) {
                    return false;
                }
                try {
                    funct.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void Download(String url, String downloadPath) {
        this.filePath = downloadPath;
        this.pDialog = new ProgressDialog(this.cx);
        this.pDialog.setMessage("Resim indiriliyor... Lütfen bekleyin...");
        this.pDialog.setIndeterminate(false);
        this.pDialog.setMax(100);
        this.pDialog.setProgressStyle(1);
        this.pDialog.setCancelable(true);
        new DownloadFileFromURL().execute(new String[]{url});
    }

    /* Access modifiers changed, original: 0000 */
    public String[] GetAssetFiles(String path) {
        String[] dosya = null;
        try {
            return this.cx.getAssets().list(path);
        } catch (IOException e) {
            e.printStackTrace();
            return dosya;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<String> GetAssetHtmlFiles(String path) {
        String[] dosya = GetAssetFiles(path);
        ArrayList<String> htmlDosya = new ArrayList();
        for (String x : dosya) {
            if (x.contains(".htm")) {
                htmlDosya.add(x);
            }
        }
        return htmlDosya;
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<String> GetAssetFolders(String path) {
        String[] dosya = GetAssetFiles(path);
        ArrayList<String> htmlDosya = new ArrayList();
        for (String x : dosya) {
            if (!x.contains(".")) {
                htmlDosya.add(x);
            }
        }
        return htmlDosya;
    }

    /* Access modifiers changed, original: 0000 */
    public File createFileFromAsset(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = this.cx.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File("/sdcard/AgentaTemp/").mkdirs();
        try {
            File file = new File("/sdcard/AgentaTemp/" + fileName);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = inputStream.read(buffer);
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                } else {
                    outputStream.close();
                    inputStream.close();
                    return file;
                }
            }
        } catch (IOException e2) {
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public File createFileFromAsset(String fileName, String output) {
        InputStream inputStream = null;
        try {
            inputStream = this.cx.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File("/sdcard/AgentaTemp/").mkdirs();
        try {
            File file = new File("/sdcard/AgentaTemp/" + output);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = inputStream.read(buffer);
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                } else {
                    outputStream.close();
                    inputStream.close();
                    return file;
                }
            }
        } catch (IOException e2) {
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void HtmlOku(Class<?> cls, String url, String scroll) {
        OpenWithArgsSola((Class) cls, new String[]{"url", "son"}, new String[]{url, scroll});
    }

    /* Access modifiers changed, original: 0000 */
    public void HtmlOku(Class<?> cls, String url) {
        OpenWithArgsSola((Class) cls, new String[]{"url"}, new String[]{url});
    }

    /* Access modifiers changed, original: 0000 */
    public void animasyonSola() {
        this.cx.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    /* Access modifiers changed, original: 0000 */
    public void animasyonSaga() {
        this.cx.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    /* Access modifiers changed, original: 0000 */
    public void animasyonCevir() {
        this.cx.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /* Access modifiers changed, original: 0000 */
    public SharedPreferences Ayar() {
        return PreferenceManager.getDefaultSharedPreferences(this.cx.getApplicationContext());
    }

    /* Access modifiers changed, original: 0000 */
    public String AyarString(String name) {
        return Ayar().getString(name, "");
    }

    /* Access modifiers changed, original: 0000 */
    public int AyarInt(String name) {
        return Ayar().getInt(name, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean AyarBool(String name) {
        return Ayar().getBoolean(name, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void AyarYap(String name, String val) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.cx.getApplicationContext()).edit();
        editor.putString(name, val);
        editor.commit();
    }

    /* Access modifiers changed, original: 0000 */
    public void AyarGizle(PreferenceActivity ac, String name) {
        ac.getPreferenceScreen().removePreference(ac.getPreferenceManager().findPreference(name));
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaKaydir(WebView w, int ksure) {
        if (this.web == null) {
            this.web = w;
        }
        this.kaydirma_sure = ksure;
        switch (this.kaydirma_sure) {
            case 2:
                AgentaTimer(3000, 100);
                return;
            case 3:
                AgentaTimer(5000, 100);
                return;
            case 4:
                AgentaTimer(5000, 200);
                return;
            case 5:
                AgentaTimer(7000, 200);
                return;
            case 6:
                AgentaTimer(10000, 300);
                return;
            case 7:
                AgentaTimer(10000, 500);
                return;
            case 8:
                AgentaTimer(60, 1);
                return;
            default:
                AgentaTimer(30, 1);
                return;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaDur() {
        if (this.durmus) {
            AgentaKaydir (this.web, this.kaydirma_sure);
        } else {
            this.zamanlayici.cancel();
        }
        this.durmus = !this.durmus;
    }

    /* Access modifiers changed, original: 0000 */
    public void kay_bitir() {
        if (this.TotalHeight != 0 && this.web.getScrollY() + this.web.getHeight() >= this.TotalHeight) {
            this.zamanlayici.cancel();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void AgentaTimer(int sure, final int kaydir) {
        try {
            this.zamanlayici = new Timer();
            this.yardimci = new Handler(Looper.getMainLooper());
            this.zamanlayici.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    Agenta.this.yardimci.post(new Runnable() {
                        public void run() {
                            Agenta.this.TotalHeight = (int) Math.floor((double) (((float) Agenta.this.web.getContentHeight()) * Agenta.this.web.getScale()));
                            if (Agenta.this.web.getScrollY() != 0) {
                                Agenta.this.web.scrollTo(0, Agenta.this.web.getScrollY() + kaydir);
                            } else if (Agenta.this.web.getScrollY() < Agenta.this.TotalHeight) {
                                Agenta.this.web.scrollTo(0, Agenta.this.web.getScrollY() + kaydir);
                            }
                            Agenta.this.kay_bitir();
                        }
                    });
                }
            }, (long) sure, (long) sure);
        } catch (Exception e) {
        }
    }
}
