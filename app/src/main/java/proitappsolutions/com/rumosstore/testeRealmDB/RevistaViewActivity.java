package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;


import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;
import android.support.v7.widget.Toolbar;

public class RevistaViewActivity extends AppCompatActivity {

    private PDFView pdfView;
    private LinearLayout progressBar;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorBotaoLogin));
        setContentView(R.layout.activity_revista_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Leitura");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pdfView = (PDFView)findViewById(R.id.pdf_viewer);
        progressBar = (LinearLayout) findViewById(R.id.linearProgresso);




        if (getIntent() != null){
            String viewType = getIntent().getStringExtra("ViewType");
            if (viewType != null || !TextUtils.isEmpty(viewType)){

                progressBar.setVisibility(View.VISIBLE);

                FileLoader.with(this)
                        .load(viewType)
//                            .fromDirectory("PDFFiles",FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest fileLoadRequest, FileResponse<File> fileResponse) {

                                File pdfFile = fileResponse.getBody();

                                progressBar.setVisibility(View.GONE);

                                pdfView.fromFile(pdfFile)
                                        .password(null) // If have password
                                        .defaultPage(0) // Open default page, you can remember this value to open from the last time
                                        .enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .enableDoubletap(true) // Double tap to zoom
                                        .onDraw(new OnDrawListener() {
                                            @Override
                                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

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
        }
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