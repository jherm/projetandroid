package com.example.myapplication;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.alpha;
import static android.graphics.Color.argb;
import static android.graphics.Color.blue;
import static android.graphics.Color.colorToHSV;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;
import android.renderscript.Allocation;

import com.android.rssample.ScriptC_dynamique;
import com.android.rssample.ScriptC_gray;
import com.android.rssample.ScriptC_conserver;
import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_rgbtohsv;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int image = R.mipmap.highres;
    String record = "";

    void reset(){
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.txtHello);
        ImageView imgView = (ImageView) findViewById(R.id.imgView);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fonctions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        Bitmap bm=BitmapFactory.decodeResource(getResources(), image);
        final Bitmap output=bm.copy(bm.getConfig(), true);


        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java_apply(output);
            }
        });

        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rs_apply(output);
            }
        });

        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });


        imgView.setImageBitmap(output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reset();

        System.out.println("Hello console !");
        Log.i("CV", "onCreate executed");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        record = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CV", "onStart executed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CV", "onStop executed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CV", "onDestroy executed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CV", "onPause executed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CV", "onResume executed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CV", "onRestart executed");
    }

    void java_apply(Bitmap bmp){
        if (record.equals("Noir et blanc")){
            toGray2(bmp);
        }
        if (record.equals("Coloriser (aleatoire)")){
            colorize(bmp);
        }
        if (record.equals("Conserver le rouge")){
            conserver(bmp);
        }
        if (record.equals("Extension de dynamique (noir et blanc)")){
            contraste(bmp);
        }
        if (record.equals("Egalisation histogramme (noir et blanc)")){
            contraste2(bmp);
        }
    }

    void rs_apply(Bitmap bmp){
        if (record.equals("Noir et blanc")){
            toGrayRS(bmp);
        }
        if (record.equals("Coloriser (aleatoire)")){
            colorizeRS(bmp);
        }
        if (record.equals("Conserver le rouge")){
            conserverRS(bmp);
        }
        if (record.equals("Extension de dynamique (noir et blanc)")){
            //contrasteRS(bmp);
        }
        if (record.equals("Egalisation histogramme (noir et blanc)")){
            //contraste2RS(bmp);
        }
    }


    //JAVA//


    void toGray(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int inpixel, outpixel;
        int grey;
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel = bmp.getPixel(i,j);
                grey=(int)((red(inpixel)+green(inpixel)+blue(inpixel))/3);
                outpixel = argb((int)(alpha(inpixel)), grey, grey, grey);
                bmp.setPixel(i, j, outpixel);
            }
        }
    }

    void toGray2(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int grey;
        int inpixel, outpixel;
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
        int[] outpixels = new int[x*y];
        for(int i=0; i<outpixels.length; i++){
            inpixel=inpixels[i];
            grey=(int)((red(inpixel)+green(inpixel)+blue(inpixel))/3);
            outpixel = argb((int)(alpha(inpixel)), grey, grey, grey);
            outpixels[i]=outpixel;
        }
        bmp.setPixels(outpixels, 0, x, 0, 0, x, y);

        }

        void conserver(Bitmap bmp){
            int x = bmp.getWidth();
            int y = bmp.getHeight();
            int r,g,b,grey;
            float hsv[] = new float[3];
            int inpixel, outpixel;
            int[] inpixels = new int[x*y];
            bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
            int[] outpixels = new int[x*y];
            for(int i=0; i<outpixels.length; i++){
                inpixel=inpixels[i];
                colorToHSV(inpixel, hsv);
                r=(int)red(inpixel);
                g=(int)green(inpixel);
                b=(int)blue(inpixel);
                if(hsv[0]>30 && hsv[0]<330){
                    grey=(int)((r+g+b)/3);
                    outpixel=argb((int)(alpha(inpixel)), grey, grey, grey);
                } else{
                    outpixel=inpixel;
                }

                outpixels[i]=outpixel;
            }
            bmp.setPixels(outpixels, 0, x, 0, 0, x, y);
        }


    void colorize(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int h = (int)(361*Math.random());
        float hsv[] = new float[3];
        int inpixel, outpixel;
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
        int[] outpixels = new int[x*y];
        for(int i=0; i<outpixels.length; i++){
            inpixel=inpixels[i];
            colorToHSV(inpixel, hsv);
            hsv[0]=h;
            outpixel = HSVToColor(hsv);
            outpixels[i]=outpixel;
        }
        bmp.setPixels(outpixels, 0, x, 0, 0, x, y);
    }



    float[] RGBtoHSV(int inpixel){
        float R2,G2,B2;
        float Cmax,Cmin,Delta;
        float H, S, V;
        H=-1;
        R2=(red(inpixel)/255);
        G2=(green(inpixel)/255);
        B2=(blue(inpixel)/255);
        Cmax=Math.max(Math.max(R2,G2),B2);
        Cmin=Math.min(Math.min(R2,G2),B2);
        Delta=Cmax-Cmin;
        V= Cmax;
            if (Cmax==0){
                S=0;
            }
            else{
                S=Delta/Cmax;
            }
            if (Delta==0){
                H=0;
            }
            else if(Cmax==R2) {
                H = 60 * (((G2 - B2) / Delta) % 6);
            }
            else if(Cmax==G2) {
                H = 60 * (((B2 - R2) / Delta) + 2);
            }
            else if(Cmax==B2){
                H=60*(((R2-G2)/Delta)+1);
            }
            float[] output = new float[3];
            output[0]=H;
            output[1]=S;
            output[2]=V;

            return output;
        }



    void conserver2(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int r,g,b,grey;
        float hsv[] = new float[3];
        int inpixel, outpixel;
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
        int[] outpixels = new int[x*y];
        for(int i=0; i<outpixels.length; i++){
            inpixel=inpixels[i];
            r=(int)red(inpixel);
            g=(int)green(inpixel);
            b=(int)blue(inpixel);
            hsv=RGBtoHSV(inpixel);
            if(hsv[0]>30 && hsv[0]<330){
                grey=(int)((r+g+b)/3);
                outpixel=argb((int)(alpha(inpixel)), grey, grey, grey);
            } else{
                outpixel=inpixel;
            }

            outpixels[i]=outpixel;
        }
        bmp.setPixels(outpixels, 0, x, 0, 0, x, y);
    }

    //Extension de Dynamique
    void contraste(Bitmap bmp){
            int x = bmp.getWidth();
            int y = bmp.getHeight();
            int inpixel, outpixel;
            int[] inpixels = new int[x*y];
            bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
            int[] outpixels = new int[x*y];
            int min = 300;
            int max = -1;
            for (int i=0; i<inpixels.length; i++){
                if (inpixels[i]<min){
                    min=inpixels[i];
                }
                if (inpixels[i]>max){
                    max=inpixels[i];
                }
            }
            int[] LUT = new int[256];
            for(int ng=0; ng<256; ng++){
                LUT[ng]=(255*(ng-red(min)))/(red(max)-red(min));

            }
            for(int i=0; i<x; i++){
                for(int j=0; j<y; j++){
                    inpixel = bmp.getPixel(i,j);
                    int ng_out = LUT[red(inpixel)];
                    outpixel = rgb(ng_out, ng_out, ng_out);
                    bmp.setPixel(i, j, outpixel);
                }
            }


    }


    // Egalisation d'histogramme
    void contraste2(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int inpixel, outpixel;
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
        int[] outpixels = new int[x*y];

        int[] histo = new int[256];
        for(int i=0; i<256; i++){
            histo[i]=0;
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel=bmp.getPixel(i,j);
                histo[red(inpixel)]+=1;
            }
        }

        int[] histo_cumule = new int[256];
        for(int i=0; i<256; i++) {
            histo_cumule[i]=0;
            for (int j=0; j<=i; j++) {
                histo_cumule[i]+=histo[j];
            }
        }

        int[] LUT = new int[256];
        for(int ng=0; ng<256; ng++){
            LUT[ng]=(histo_cumule[ng]*255)/(x*y);
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel = bmp.getPixel(i,j);
                int ng_out = LUT[red(inpixel)];
                outpixel = rgb(ng_out, ng_out, ng_out);
                bmp.setPixel(i, j, outpixel);
            }
        }


    }


    // Egalisation d'Histogramme COULEURS, non fonctionnel
    void contraste3(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int inpixel, outpixel;
        float[] inpixelHSV;
        float[] outpixelHSV = new float[3];
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);

        int[] outpixels = new int[x*y];

        int[] histo = new int[101];
        for(int i=0; i<101; i++){
            histo[i]=0;
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel=bmp.getPixel(i,j);
                inpixelHSV=RGBtoHSV(inpixel);
                histo[(int)(inpixelHSV[2]*100)]+=1;
            }
        }

        int[] histo_cumule = new int[101];
        for(int i=0; i<101; i++) {
            histo_cumule[i]=0;
            for (int j=0; j<=i; j++) {
                histo_cumule[i]+=histo[j];
            }
        }

        int[] LUT = new int[101];
        for(int ng=0; ng<101; ng++){
            LUT[ng]=(histo_cumule[ng]*100)/(x*y);
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel = bmp.getPixel(i,j);
                inpixelHSV=RGBtoHSV(inpixel);
                float h = inpixelHSV[0];
                float s = inpixelHSV[1];
                float v = LUT[(int)(inpixelHSV[2]*100)];
                outpixelHSV[0]=h;
                outpixelHSV[1]=s;
                outpixelHSV[2]=v;
                outpixel = HSVToColor(outpixelHSV);
                bmp.setPixel(i, j, outpixel);
            }
        }


    }


    // Egalisation d'histogramme COULEURS, non fonctionnel
    void contraste4(Bitmap bmp){
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        int inpixel, outpixel;
        int[] inpixels = new int[x*y];
        bmp.getPixels(inpixels, 0, x, 0, 0, x, y);
        int[] outpixels = new int[x*y];
//R
        int[] histoR = new int[256];
        for(int i=0; i<256; i++){
            histoR[i]=0;
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel=bmp.getPixel(i,j);
                histoR[red(inpixel)]+=1;
            }
        }

        int[] histo_cumuleR = new int[256];
        for(int i=0; i<256; i++) {
            histo_cumuleR[i]=0;
            for (int j=0; j<=i; j++) {
                histo_cumuleR[i]+=histoR[j];
            }
        }
//G
        int[] histoG = new int[256];
        for(int i=0; i<256; i++){
            histoG[i]=0;
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel=bmp.getPixel(i,j);
                histoG[green(inpixel)]+=1;
            }
        }

        int[] histo_cumuleG = new int[256];
        for(int i=0; i<256; i++) {
            histo_cumuleG[i]=0;
            for (int j=0; j<=i; j++) {
                histo_cumuleG[i]+=histoG[j];
            }
        }
//B
        int[] histoB = new int[256];
        for(int i=0; i<256; i++){
            histoB[i]=0;
        }
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel=bmp.getPixel(i,j);
                histoB[blue(inpixel)]+=1;
            }
        }

        int[] histo_cumuleB = new int[256];
        for(int i=0; i<256; i++) {
            histo_cumuleB[i]=0;
            for (int j=0; j<=i; j++) {
                histo_cumuleB[i]+=histoB[j];
            }
        }
//LUT R
        int[] LUTr = new int[256];
        for(int r=0; r<256; r++){
            LUTr[r]=(histo_cumuleR[r]*255)/(x*y);
        }
//LUT G
        int[] LUTg = new int[256];
        for(int g=0; g<256; g++){
            LUTg[g]=(histo_cumuleG[g]*255)/(x*y);
        }
//LUT B
        int[] LUTb = new int[256];
        for(int b=0; b<256; b++){
            LUTb[b]=(histo_cumuleB[b]*255)/(x*y);
        }

        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                inpixel = bmp.getPixel(i,j);
                int r = LUTr[red(inpixel)];
                int g = LUTg[green(inpixel)];
                int b = LUTb[blue(inpixel)];
                outpixel = rgb(r, g, b);
                bmp.setPixel(i, j, outpixel);
            }
        }


    }

    //RENDERSCRIPT//

    private void toGrayRS(Bitmap bmp){
// 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
// 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
// 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
// 4) Copier les donnees dans les Allocations
// ...
// 5) Initialiser les variables globales potentielles
// ...
// 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
// 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
// 8) Detruire le context , les Allocation (s) et le script
        input.destroy(); output.destroy();
        grayScript.destroy(); rs.destroy();
    }

    private void RGBtoHSV_RS(Bitmap bmp){
// 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
// 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
// 3) Creer le script
        ScriptC_rgbtohsv rgbtohsvScript = new ScriptC_rgbtohsv(rs);
// 4) Copier les donnees dans les Allocations
// ...
// 5) Initialiser les variables globales potentielles
// ...
// 6) Lancer le noyau
        rgbtohsvScript.forEach_rgbtohsv(input, output);
// 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
// 8) Detruire le context , les Allocation (s) et le script
        input.destroy(); output.destroy();
        rgbtohsvScript.destroy(); rs.destroy();

    }

    private void conserverRS(Bitmap bmp){
// 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
// 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
// 3) Creer le script
        ScriptC_conserver conserverScript = new ScriptC_conserver(rs);
// 4) Copier les donnees dans les Allocations
// ...
// 5) Initialiser les variables globales potentielles
// ...
// 6) Lancer le noyau
        conserverScript.forEach_conserver(input, output);
// 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
// 8) Detruire le context , les Allocation (s) et le script
        input.destroy(); output.destroy();
        conserverScript.destroy(); rs.destroy();
    }

    private void colorizeRS(Bitmap bmp){
// 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
// 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
// 3) Creer le script
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);
// 4) Copier les donnees dans les Allocations
// ...
// 5) Initialiser les variables globales potentielles
       int h = (int)(361*Math.random());
// 6) Lancer le noyau
        colorizeScript.forEach_colorize(input, output);
// 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
// 8) Detruire le context , les Allocation (s) et le script
        input.destroy(); output.destroy();
        colorizeScript.destroy(); rs.destroy();
    }

    private void dynamiqueRS(Bitmap bmp){
// 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
// 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
// 3) Creer le script
        ScriptC_dynamique dynamiqueScript = new ScriptC_dynamique(rs);
// 4) Copier les donnees dans les Allocations
// ...
// 5) Initialiser les variables globales potentielles
    int min=300;
// 6) Lancer le noyau
        dynamiqueScript.forEach_dynamique(input, output);
// 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
// 8) Detruire le context , les Allocation (s) et le script
        input.destroy(); output.destroy();
        dynamiqueScript.destroy(); rs.destroy();
    }









    }

