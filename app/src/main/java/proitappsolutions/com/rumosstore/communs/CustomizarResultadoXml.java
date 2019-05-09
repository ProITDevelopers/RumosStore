package proitappsolutions.com.rumosstore.communs;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomizarResultadoXml {

    private String tag;
    private String xml;
    private static final String TAG = "ExtraindoXml";

    public CustomizarResultadoXml(String tag, String xml) {
        this.tag = tag;
        this.xml = xml;
    }

    public String comecar(){
        List<String> resultado = new ArrayList<>();
        String[] cortaXml = xml.split(tag + "\"");
        String temp2;
        int count = cortaXml.length;

        for (int i = 1; i<count;i++){
            String temp = cortaXml[i];
            int index = temp.indexOf("\"");
            temp = temp.substring(0,index);
            Log.d("imagem","CORTANDO " + temp);
            resultado.add(temp);
            Log.d("resultado","CORTANDO " + resultado);
        }

        return resultado.get(0);
    }

    public List<String> conteudo(){
        List<String> resultado = new ArrayList<>();
        String[] cortaXml = xml.split(tag);
        String conteudo = null;
        int count = cortaXml.length;

        for (int i = 1; i<count;i++){
            String temp = cortaXml[i];
            int index = temp.indexOf("</p>");
            temp = temp.substring(0,index);
            Log.d("conteudo","CORTANDO " + temp);
            resultado.add(temp);
            resultado.add(".");
            Log.d("resultadoConteudo","CORTANDO " + resultado);
        }
        return resultado;
    }
}
