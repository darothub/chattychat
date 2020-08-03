package com.peacedude.chattychat;


public class Algo {
    public static int shiftedDiff(String first, String second) {
//        int len = first.length();
//        String placeholder = first;
//        int counter = 0;
//        if(first == second){
//            return counter;
//        }
//        for(int i=0; i<len;i++){
//            String str = "";
//            str += String.valueOf(placeholder.charAt(len-1));
//            str += placeholder.substring(0, len-1);
//            counter += 1;
//            if(str.equals(second)){
//                return counter;
//            }
//            else {
//                placeholder = str;
//            }
//        }
        if(first.length() != second.length()){
            return -1;
        }else{
            return (second.concat(second)).indexOf(first);
        }

    }
}