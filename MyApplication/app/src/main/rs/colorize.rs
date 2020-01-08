#pragma version(1)
#pragma rs java_package_name(com.android.rssample)
#define MODULO(x,y) (x-  ( ( (int)(x/y) ) * y)  )


float h = 250.0;
uchar4 RS_KERNEL colorize(uchar4 in){
   // float h = rsRand(0, 360);
    uchar4 out;
    float R=(float)((float)(in.r)/255.0);
    float G=(float)((float)(in.g)/255.0);
    float B=(float)((float)(in.b)/255.0);
    float Cmax = (float)max(max(R,G), B);
    float Cmin = (float)min(min(R,G), B);
    //rsDebug("%f, %f", Cmax, Cmin, 0);
    float delta = (float)(Cmax-Cmin);





    float s;
    if(Cmax==0){
        s = 0;
     }else{
           s = delta/Cmax;
           }
     float v = Cmax;

     // On a le pixel en HSV, on le convertit en RGB
     float C=v*s;
     float kk=(float)h/60.0;
     float X=C*(1.0 -((MODULO(kk,2.0))-1.0) );
     float m = v-C;
     int R2, G2,B2;

     if(h>=0 && h<60){
        R2=C;
        G2=X;
        B2=0;
     }
     if(h>=60 && h<120){
        R2=X;
        G2=C;
        B2=0;
     }
     if(h>=120 && h<180){
        R2=0;
        G2=C;
        B2=X;
     }
     if(h>=180 && h<240){
        R2=0;
        G2=X;
        B2=C;
     }
     if(h>=240 && h<300){
        R2=X;
        G2=0;
        B2=C;
     }
     if(h>=300 && h<360){
        R2=C;
        G2=0;
        B2=X;
     }

     out.r=255*(R2+m);
     out.g=255*(G2+m);
     out.b=255*(B2+m);
     out.a=in.a;

    return out;

}