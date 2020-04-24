package com.android.deport.utils;

import java.util.Date;

public class OrderIDUtil {

    public static String getOrderNum(){
        String num = "";
        String stringDate = DateUtil.getStringDate(new Date());
        num = stringDate+getChar();
        return num;
    }


        public static String getChar(){
            String word = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String tmp = "";
            for(int i = 0; i < 2; i++){
                int rand = (int)(Math.random() * word.length());
                char c = word.charAt(rand);
                if(!tmp.contains(c+"")){
                    tmp += c;
                }else{
                    i--;
                }
            }
           return tmp;
        }

}
