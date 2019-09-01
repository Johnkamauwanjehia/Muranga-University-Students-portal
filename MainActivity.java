package com.example.paddy.pro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webView;
    ProgressDialog  progressBar;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressBar = new ProgressDialog(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {if (webView!=null){
                webView.loadUrl("http://portal.mut.ac.ke/Messages/Index");
            }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        String logged = getString(R.string.filneme);
        File log = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                + logged + ".xml");
        if (log.exists()){
            webView.loadUrl("http://portal.mut.ac.ke/Home/Index");
        }else {
            webView.loadUrl("http://portal.mut.ac.ke/");
        }

        if ( webView.getVisibility() == View.VISIBLE&&webView!=null){
            webView.setVisibility(View.INVISIBLE);
        }

        webView.setWebViewClient(new WebViewClient() {
            public void onReceievedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.requestFocus();
                        webView.setWebViewClient(new WebViewClient() {
                            boolean loadingFinished = true;
                            boolean redirect = false;
                            boolean getur = false;

                            long last_page_start;
                            long now;

                            // Load the url
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                if (!loadingFinished) {
                                    redirect = true;
                                }

                                loadingFinished = false;
                                view.loadUrl(url);
                                return false;
                            }

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                Log.i("p", "pagestart");
                                loadingFinished = false;
                                last_page_start = System.nanoTime();
                                show_splash();
                                if (!isNetworkAvailable()) {
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                                                }
                                            },
                                            10);
                                } else {

                                }


                            }
                            String fileName=getString(R.string.filnem);
                            SharedPreferences pre = getSharedPreferences(fileName, MODE_PRIVATE);
                            String ema = pre.getString("user_Name", "");
                            String pas = pre.getString("pass_Word", "");

                            @Override
                            public void onLoadResource(WebView view, String url) {
                                super.onLoadResource(view, url);
                            }

                            // When finish loading page
                            public void onPageFinished(WebView view, String url) {
                                getur = true;


                                Log.i("p", "pagefinish");
                                if (!redirect) {
                                    loadingFinished = true;
                                }
                                //call remove_splash in 100 miSec
                                if (loadingFinished && !redirect) {
                                    now = System.nanoTime();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    remove_splash();
                                                }
                                            },
                                            100);

                                } else {
                                    redirect = false;
                                }
                                if (isNetworkAvailable()&&loadingFinished && !redirect) {
                                    String urll;

                                    if (webView!=null){
                                        urll = webView.getUrl();
                                    }else {
                                        urll="about.blank";
                                    }
                                    if (urll.contains("http://portal.mut.ac.ke/Account/ForgotPassword")) {
                                        getb();

                                    }else if (progressBar.isShowing()&&urll.contentEquals("http://portal.mut.ac.ke/")|| progressBar.isShowing()&&urll.contentEquals("http://portal.mut.ac.ke/")||progressBar.isShowing() &&urll.contains("http://portal.mut.ac.ke/Account/Login?ReturnUrl")) {

                                                        int i;
                                                        for (i=1;i<=1;i++){

                                                            String js = "javascript: var x = document.getElementById('UserName').value = '" + ema + "';" +
                                                                    "var y = document.getElementById('Password').value='" + pas + "';";
                                                            webView.loadUrl(js);
                                                            webView.loadUrl("javascript:document.querySelector(\"input[type=submit]\").click();");
                                                            progressBar.setMessage("loging you in...");
                                                        }


                                    }else if(urll.contentEquals("http://portal.mut.ac.ke/Home/Index")){
                                        String logged = getString(R.string.filneme);
                                        File file = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                                                + logged + ".xml");
                                        if (file.exists()){
                                            //do nothing
                                        }else {
                                            SharedPreferences preferences = getSharedPreferences(logged, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("logged", "logged");
                                            editor.commit();
                                        }
                                    }
                                }
                                if (!isNetworkAvailable()) {

                                    now = System.nanoTime();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    progressBar.setMessage("Network error..Retrying");
                                                }
                                            },
                                            2000);

                                    new android.os.Handler().postDelayed(

                                            new Runnable() {
                                                public void run() {
                                                    moveTaskToBack(true);
                                                    finish();
                                                }
                                            },
                                            9000);

                                }


                            }

                            private void show_splash() {

                                    if (!progressBar.isShowing()) {

                                        progressBar.setMessage("Please wait...");
                                        progressBar.setCancelable(false);
                                        progressBar.setCanceledOnTouchOutside(false);
                                        progressBar.show();
                                    }
                            }

                            //if a new "page start" was fired dont remove splash screen
                            private void remove_splash() {
                                if (last_page_start < now && progressBar.isShowing()) {
                                    if (webView.getVisibility() == View.INVISIBLE){
                                        webView.setVisibility(View.VISIBLE);
                                    }
                                    webView.setVisibility(View.VISIBLE);
                                    progressBar.dismiss();
                                }
                            }
                        });
    }
    private void getb(){
        String fileName=getString(R.string.filnem);
        File file = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                + fileName + ".xml");
        if (file.exists()){
            file.delete();

        }
        Toast.makeText(getApplicationContext(),"Wrong Username or Password", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//when the back button is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (webView.canGoBack()){
                webView.goBack();
                webView.requestFocus();
            }else {
                moveTaskToBack(true);
            }

            super.onBackPressed();
        }
    }
//when the activity is killed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressBar.isShowing()){
            progressBar.dismiss();;
        }

    }
//when moving to another activity
    @Override
    protected void onPause() {
        super.onPause();
        if (progressBar.isShowing()){
            progressBar.dismiss();;
        }
    }
//after onpause
    @Override
    protected void onResume() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getd();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.t1) {
            // Handle the camera action
            webView.loadUrl("http://portal.mut.ac.ke/Home/Index");

        } else if (id == R.id.t2) {
            webView.loadUrl("http://portal.mut.ac.ke/Programme/LoadUnits");
        } else if (id == R.id.t4) {
            webView.loadUrl("http://portal.mut.ac.ke/Programme/Getregistered");
        } else if (id == R.id.t5) {
            webView.loadUrl("http://portal.mut.ac.ke/Student/LoadResultslip");
        } else if (id == R.id.t7) {
            webView.loadUrl("http://portal.mut.ac.ke/HostelBooking/Index");
        } else if (id == R.id.t8) {
            webView.loadUrl("http://portal.mut.ac.ke/Student/LoadFeestructure");
        } else if (id == R.id.t9) {
            webView.loadUrl("http://portal.mut.ac.ke/Student/Generatefees");
        } else if (id == R.id.t11) {
            webView.loadUrl("http://portal.mut.ac.ke/HostelBooking/Create");
        } else if (id == R.id.t12) {
            webView.loadUrl("http://portal.mut.ac.ke/Onlinereporting/Index");
        }else if (id == R.id.t13){
            Intent intent = new Intent(this, Regards.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Uri uri = Uri.parse("mailto:paddykiprop@gmail.com ");
            Intent sendmail = new Intent(Intent.ACTION_SENDTO, uri);
            sendmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback on the app");
            startActivity(sendmail);
        }
        else if (id == R.id.nav_share){
            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
                File srcFile = new File(ai.publicSourceDir);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/vnd.android.package-archive");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
                startActivity(Intent.createChooser(share, "Share MUT app with"));
            } catch (Exception e) {
                Log.e("ShareApp", e.getMessage());
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getd(){
        String fileName=getString(R.string.filnem);
        String logged = getString(R.string.filneme);
        File log = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                + logged + ".xml");
        if (log.exists()){
            log.delete();
        }
        File file= new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                + fileName + ".xml");
        if (file.exists()) {
            file.delete();
        };
        String js = "javascript:document.getElementById('logoutForm').submit();";
        webView.loadUrl(js);

        new android.os.Handler().postDelayed(

                new Runnable() {
                    public void run() {
                        webView.loadUrl("about.blank");
                        if (webView!=null){
                            webView.stopLoading();
                        }
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                500);


    }

}
