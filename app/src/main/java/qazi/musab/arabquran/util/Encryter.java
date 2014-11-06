package qazi.musab.arabquran.util;

import android.util.Log;

/**
 * Created by Musab on 9/13/2014.
 */
public class Encryter {
    /*public static void main (String [] args){
        System.out.println(encode("08/25/2014"));
    }*/
    public static String encode(String date) {

        String encodedDate = "";
        if(date.length()>0){
                char c;
                for(int i = 0;i<date.length();i++)
                {
                    c = date.charAt(i);
                    if(c==10||c==32)
                    {
                        encodedDate+=c;
                    }
                    else  if(c==33||c==34||c==99)
                    {
                        encodedDate+=c+=4;
                    }
                    else{

                        encodedDate+= c-=3;
                    }

                }
        }
        Log.e("encodedDate",encodedDate);
        return encodedDate;
    }

    // convert from internal Java String format -> UTF-8
    public static String decode(String encodedDate) {
        String decodedDate = "";
        if (encodedDate.length() > 0) {
            char temp;
            for (int y = 0; y < encodedDate.length(); y++) {
                temp = encodedDate.charAt(y);
                if (temp == 10 || temp == 32) {
                    decodedDate += temp;
                } else if (temp == 37 || temp == 38 || temp == 103) {
                    decodedDate += temp -= 4;
                } else {
                    decodedDate += temp += 3;
                }
            }
        }
        Log.e("decodedDate",decodedDate);
        return decodedDate;
    }
}
