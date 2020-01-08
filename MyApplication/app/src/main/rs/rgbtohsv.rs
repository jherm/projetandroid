#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.android.rssample)

// RenderScript kernel that performs RGB2HSV conversion
ushort3 RS_KERNEL rgbtohsv(uchar4 rgb){
    ushort3 hsv;
    hsv.x=0;
    hsv.y=0;
    hsv.z=0;

    return hsv;
}