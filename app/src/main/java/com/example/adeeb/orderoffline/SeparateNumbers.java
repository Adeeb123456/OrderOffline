package com.example.adeeb.orderoffline;

/**
 * Created by AdeeB on 11/15/2016.
 */
public class SeparateNumbers {

    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58 || c==46){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
