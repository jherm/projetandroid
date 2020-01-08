#pragma version(1)
#pragma rs java_package_name(com.android.rssample)



int minimum = 300;
int maximum = -1;
int LUT[256];


uchar4 RS_KERNEL dynamique(uchar4 in){
    int grey = in.r;
    if (grey<minimum){
        minimum=grey;
    }
    if (grey>maximum){
        maximum=grey;
    }


uchar4 out;

out=in;
return out;
}