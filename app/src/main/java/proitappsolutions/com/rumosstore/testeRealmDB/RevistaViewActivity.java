package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class RevistaViewActivity extends AppCompatActivity {

    private PDFView pdfView;
    private LinearLayout progressBar;

    private RelativeLayout coordinatorLayout;
    private RelativeLayout errorLayout;
    private TextView btnTentarDeNovo;


    private Toolbar toolbar;
    private TextView txt_toolbar;
    String viewType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorBotaoLogin));
        setContentView(R.layout.activity_revista_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Leitura");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
        pdfView = (PDFView)findViewById(R.id.pdf_viewer);
        progressBar = (LinearLayout) findViewById(R.id.linearProgresso);

        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinatorLayout);
        errorLayout = (RelativeLayout) findViewById(R.id.erroLayout);
        btnTentarDeNovo = (TextView) findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorExemplo));

        if (getIntent() != null){
            viewType = getIntent().getStringExtra("ViewType");
            if (viewType != null || !TextUtils.isEmpty(viewType)){
                verifConecxao(viewType);
            }
        }





    }


    private void verifConecxao(String viewType) {

        if (getBaseContext() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }else{
                carregarPDFView(viewType);
            }
        }

    }

    private void carregarPDFView(String viewType){


                progressBar.setVisibility(View.VISIBLE);

                FileLoader.with(this)
                        .load(viewType)
//                            .fromDirectory("PDFFiles",FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest fileLoadRequest, FileResponse<File> fileResponse) {

                                File pdfFile = fileResponse.getBody();



                                pdfView.fromFile(pdfFile)
                                        .password(null) // If have password
                                        .defaultPage(0) // Open default page, you can remember this value to open from the last time
                                        .enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .enableDoubletap(true) // Double tap to zoom
                                        .onDraw(new OnDrawListener() {
                                            @Override
                                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        })
                                        .onDrawAll(new OnDrawListener() {
                                            @Override
                                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                                                // Code here if you want to do something

                                            }
                                        })
                                        .onPageError(new OnPageErrorListener() {
                                            @Override
                                            public void onPageError(int page, Throwable t) {
                                                Toast.makeText(RevistaViewActivity.this, "Error while open page "+page, Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .onPageChange(new OnPageChangeListener() {
                                            @Override
                                            public void onPageChanged(int page, int pageCount) {
                                                // Code here if you want to do something
                                                txt_toolbar.setText(String.valueOf((page + 1)+ " de "+pageCount));

                                            }
                                        })
                                        .onTap(new OnTapListener() {
                                            @Override
                                            public boolean onTap(MotionEvent e) {

                                                return true;
                                            }
                                        })
                                        .onRender(new OnRenderListener() {
                                            @Override
                                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                                pdfView.fitToWidth(); //Fixed screen size

                                            }
                                        })
                                        .enableAnnotationRendering(true)
                                        .invalidPageColor(Color.WHITE)
                                        .load();
                            }

                            @Override
                            public void onError(FileLoadRequest fileLoadRequest, Throwable throwable) {
                                Toast.makeText(RevistaViewActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });





    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordinatorLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                verifConecxao(viewType);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}