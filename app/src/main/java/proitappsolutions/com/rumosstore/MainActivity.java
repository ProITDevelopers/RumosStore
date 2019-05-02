package proitappsolutions.com.rumosstore;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;
import proitappsolutions.com.rumosstore.testeRealmDB.Revistas;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(AppPref.getInstance().getAuthToken())) {
                    Intent intent = new Intent(MainActivity.this, MediaRumoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;

                }
                initData();


            }
        }, 2000);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeInicialActivity.class);
        startActivity(intent);
        finish();
    }

    private void initData() {
        //ARTIGO: MERCADO
        Common.revistasArrayList.add(new Revistas(1,"Mercado: Bodiva","http://www.meiosepublicidade.pt/wp-content/uploads/2015/04/Mercado.jpg","http://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf","mercado"));
        Common.revistasArrayList.add(new Revistas(2,"Mercado: Orçamentos plurianuais","https://thumbs.web.sapo.io/?pic=http://ia.imgs.sapo.pt/01/images/6/6a/10035_6a4bd2af5992c06127cdaa6c653fc13e.png&W=562&H=687&delay_optim=1&tv=2","http://www.africau.edu/images/default/sample.pdf","mercado"));
        Common.revistasArrayList.add(new Revistas(3,"Mercado: Carteira de Crédito","https://thumbs.web.sapo.io/?epic=ZTNhspFkzW05QypyBy2e6sOqiN+xc46eRqGmPGJwELSfWoaGPJeUzV2d9FnnZWHQ2GpuI7kXE/scGBJnFfU1nO59kvGBD3m90mqwSBY4FkV5cx6hysVQuiEw5PqaXjHRALjT&W=1050&H=0&delay_optim=1&tv=1","http://www.africau.edu/images/default/sample.pdf","mercado"));
        Common.revistasArrayList.add(new Revistas(4,"Mercado: Ministro da geologia e minas","https://thumbs.web.sapo.io/?epic=YzMxJA+sW1YQCQvx8WGAAXdKwUL0yC3atRL34wj12lhHUV3K5k8eQfFrL89Y+CEMSQO6nHFHW59ESBRHHrHpv9JdhnDk2FFdni9i8wBsmA6h44wGwx11M9ZwBZRuFeU3o5Mr&W=1050&H=0&delay_optim=1&tv=1","http://www.africau.edu/images/default/sample.pdf","mercado"));
        Common.revistasArrayList.add(new Revistas(5,"Mercado: Novo sistema cambial","https://thumbs.web.sapo.io/?epic=MTdkPzkQXvRUIglnK9fA3CA8LJE1RL/1OjTa+akpBPytCK/0EarlHXUAFscjJDYF8+4fLe0ygG6EHM5HLFeW6bfbM15jG1PkrMe6mA9eJTfXNUJYz227XeyEt91mT4tBMjht&W=1050&H=0&png=1&delay_optim=1&tv=1","http://www.africau.edu/images/default/sample.pdf","mercado"));
        Common.revistasArrayList.add(new Revistas(6,"Mercado: BFA recupera divisas","https://thumbs.web.sapo.io/?epic=Yjgwn6GtlzJekPGMdf8w+gO/VjQANT01rfyeDyFyZUj2c0DbBFNIE3GR/MmnyEP0EYrBfhbjtcd9moSUbLk2yE7q6GY0I4JCi5WuC3qqMZi58sJR3E3i/cEkIUjvrT+vLKV6&W=240&H=0&png=1&delay_optim=1&tv=1","http://www.africau.edu/images/default/sample.pdf","mercado"));
        //ARTIGO: VANGUARDA
        Common.revistasArrayList.add(new Revistas(7,"Vanguarda: Revolução Pacífica ","https://cdn.worldpresstitles.com/image/portugal/default/MDMwNDIwMTglN2N2YW5ndWFyZGE1YWMzMmU4NTUzN2Ux","https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf","vanguarda"));
        Common.revistasArrayList.add(new Revistas(8,"Vanguarda: Custo político","https://scontent-frt3-2.cdninstagram.com/vp/279d9c1a7a12a9f699ce516dbce47b9f/5D1247E0/t51.2885-15/e35/51330807_2555187241220551_4165445216191147393_n.jpg?_nc_ht=instagram.fbne2-1.fna.fbcdn.net","https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf","vanguarda"));
        Common.revistasArrayList.add(new Revistas(9,"Vanguarda: Estado de graça","https://4.bp.blogspot.com/-18kmZLxWybU/WfTFere-PyI/AAAAAAAAasQ/bBGFL11lwdMkYko_aqf-kUlGyyz_JuPggCLcBGAs/s1600/V39_2017Out20_CIRGL_entrevista_capa.jpg","https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf","vanguarda"));
        Common.revistasArrayList.add(new Revistas(10,"Vanguarda: Presidente prudente","https://pbs.twimg.com/media/DClyyr2W0AA7z4M.jpg:large","https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf","vanguarda"));
        Common.revistasArrayList.add(new Revistas(11,"Vanguarda: Separação de poderes","https://scontent-lax3-1.cdninstagram.com/vp/a04b066b9d3a71a78abe43b1f9057926/5CEA4ECE/t51.2885-15/e35/c0.93.750.750/s480x480/49379001_139197417016578_7533300775527334327_n.jpg?_nc_ht=scontent-lax3-1.cdninstagram.com","https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf","vanguarda"));
        //ARTIGO: RUMO
        Common.revistasArrayList.add(new Revistas(12,"Rumo: Gestora dos desafios","https://thumbs.web.sapo.io/?epic=YmI0NVX/QnIv8cIeq/m94SU8Ade3vfIa4DmOhN5TLKNcys+s8FA/CnxRzHbiboyRMf3jM20FQs6Qq+XHbbjDQwCdBfSAsOfxWk9VnAhDNrCHoNE=&W=240&H=0&png=1&delay_optim=1&tv=1","http://www.pdf995.com/samples/pdf.pdf","rumo"));
        Common.revistasArrayList.add(new Revistas(13,"Rumo: Cultura de Mudança","https://thumbs.web.sapo.io/?epic=ZjA2jNEocMhQzAnZgaRmAIws1oOU5ZlTCUlGKPmh8rr2Zv8WdkJ2WCHaj5g85GvqlsqcmOH280uGXOb+DUbFHueWLl/uP2ncB4s16zWHue/iBsY=&W=240&H=0&png=1&delay_optim=1&tv=1","http://www.pdf995.com/samples/pdf.pdf","rumo"));
        Common.revistasArrayList.add(new Revistas(14,"Rumo: Banca Nacional","https://thumbs.web.sapo.io/?epic=ZTUyzf05nwJFJ0JRe8zPxHFZL8rVSme7Az9w0s09JEkffF39+jbXxlX1EIpk1VVGuzdmEd6KdN0A2S/Mm7OaF84FnknPBTDiCr0/4pi6yElVE04=&W=240&H=0&png=1&delay_optim=1&tv=1","http://www.pdf995.com/samples/pdf.pdf","rumo"));
        Common.revistasArrayList.add(new Revistas(15,"Rumo: Empreendedor","https://scontent-iad3-1.cdninstagram.com/vp/5cd76a465303cb09a4c603f430f3960e/5CE22540/t51.2885-15/e35/40304937_166341154294670_2602381243250387311_n.jpg?_nc_ht=scontent-iad3-1.cdninstagram.com","http://www.pdf995.com/samples/pdf.pdf","rumo"));
        Common.revistasArrayList.add(new Revistas(16,"Rumo: Cabeto","http://www.grupocabeto.com/portals/6/imagens/rumo%20ed.%2029%20maro%202012.jpg","http://www.pdf995.com/samples/pdf.pdf","rumo"));
        Common.revistasArrayList.add(new Revistas(17,"Rumo: A Srª ANIP","http://www.meiosepublicidade.pt/wp-content/uploads/2012/02/rumo-2.jpg","http://www.pdf995.com/samples/pdf.pdf","rumo"));


        AppDatabase.saveResvistasList(Common.revistasArrayList);

        launchHomeScreen();



    }
}
