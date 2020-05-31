import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

class Scraper {
    String title;
    String author;
    Elements rows;

    Scraper(String _title){
        title = _title;
        author = "";
    }

    Scraper(String _title, String _author){
        author = _author;
        title = _title;
    }

    void getSongsList() throws Exception {

        final Document document = Jsoup.connect("https://www.tekstowo.pl/szukaj,wykonawca," + author + ",tytul," + title + ".html").get();

        rows = document.select(".content > .box-przeboje");

        listSongs();
    }

    void listSongs(){
        System.out.println("Available songs: \n");
        for(Element row : rows){
            System.out.println(row.text());
        }
    }

    String getLyrics(int songNumber) throws Exception{
        String songName = rows.get(songNumber).text().substring(rows.get(songNumber).text().indexOf(".") + 2);

        final String textURL = TextUtils.songNameToURL(songName);

        final Document document = Jsoup.connect(textURL).get();

        final Elements rawText = document.select(".song-text");

        String text = br2nl(rawText.outerHtml());
        text = text.substring(TextUtils.nthIndexOf(text,"\n", 2), TextUtils.nthLastIndexOf(text,"\n", 5));

        System.out.println(songName + text);

        return songName + text;
    }

    String getTranslation(int songNumber) throws Exception{
        String songName = rows.get(songNumber).text().substring(rows.get(songNumber).text().indexOf(".") + 2);

        final String textURL = TextUtils.songNameToURL(songName);

        final Document document = Jsoup.connect(textURL).get();

        final Elements rawText = document.select(".tlumaczenie");

        String text = br2nl(rawText.outerHtml());
        text = text.substring(TextUtils.nthIndexOf(text,"\n", 5), TextUtils.nthLastIndexOf(text,"\n", 20));

        System.out.println(songName + text);

        return songName + text;
    }

        private static String br2nl(String html) {
            if(html==null) {
                return html;
            }
            Document document = Jsoup.parse(html);
            document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
            /*document.select("br").append("\\n");*/
            document.select("p").prepend("\\n\\n");
            String s = document.html().replaceAll("\\\\n", "\n");
            return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        }
}