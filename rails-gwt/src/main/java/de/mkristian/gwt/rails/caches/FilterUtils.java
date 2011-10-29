package de.mkristian.gwt.rails.caches;

public class FilterUtils {
    // from http://stackoverflow.com/questions/990904/javascript-remove-accents-in-strings
    static final String STRIP_STRING = 
            "AAAAAAACEEEEIIII" +
            "DNOOOOO.OUUUUY.." +
            "aaaaaaaceeeeiiii" +
            "dnooooo.ouuuuy.y" +
            "AaAaAaCcCcCcCcDd" +
            "DdEeEeEeEeEeGgGg" +
            "GgGgHhHhIiIiIiIi" +
            "IiIiJjKkkLlLlLlL" +
            "lJlNnNnNnnNnOoOo" +
            "OoOoRrRrRrSsSsSs" +
            "SsTtTtTtUuUuUuUu" +
            "UuUuWwYyYZzZzZz.";
    
    public static String stripAccents(String str){
        StringBuilder answer = new StringBuilder();
        for(int i=0; i < str.length(); i++){
            char ch= str.charAt(i);
            int chindex = ch - 192;   // Index of character code in the strip string
            if(chindex >= 0 && chindex < STRIP_STRING.length()){
                // Character is within our table, so we can strip the accent...
                char outch = STRIP_STRING.charAt(chindex);
                // ...unless it was shown as a '.'
                if(outch!='.') ch = outch;
            }
            answer.append(ch);
        }
        return answer.toString();
    }
    
    public static String normalize(String val){
        return stripAccents(val).toLowerCase().replaceAll("(.)\\1+", "$1")
                .replace("ue", "u").replace("ae", "a").replace("oe", "o")
                .replace("ß", "s");
    }
    
}