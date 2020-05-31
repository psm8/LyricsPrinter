class TextUtils {
    static String songNameToURL(String songName){
        /*example: Selig - Hey, Hey, Hey -> Selig_-_Hey,_Hey,_Hey -> Selig_-_Hey__Hey__Hey -> Selig,Hey__Hey__Hey*/
       return "https://www.tekstowo.pl/piosenka," + songName.replaceAll("&|'|,|/|\\\\|\\(|\\)|\\.", "_").replace(" ", "_")
               .replace("!", "").replace("\u2019", "_8217_").replace("_-_", ",") + ".html";
    }

    static int nthIndexOf(String originalText, String delimiter, int n){
        int index = 0;
        for (int i = 0; i < n; i++) {
            index = originalText.indexOf(delimiter, index + 1);
        }
        index = originalText.indexOf(delimiter, index);
        return index;
    }

    static int nthLastIndexOf(String originalText, String delimiter, int n){
        int index = originalText.length();
        for (int i = 0; i < n; i++) {
            index = originalText.lastIndexOf(delimiter, index - 1);
        }
        index = originalText.lastIndexOf("", index);
        return index;
    }
}