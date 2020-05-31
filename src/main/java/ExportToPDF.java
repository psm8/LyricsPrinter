import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.ILeafElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class ExportToPDF {
    final static Map<String, Color> COLORS = new HashMap<String, Color>(){{
        put("BLACK", Color.BLACK);
        put("BLUE", Color.BLUE);
        put("GREEN", Color.GREEN);
        put("RED", Color.RED);
        put("CYAN", Color.CYAN);
        put("DARK_GRAY", Color.DARK_GRAY);
        put("GRAY", Color.GRAY);
        put("LIGHT_GRAY", Color.LIGHT_GRAY);
        put("MAGENTA", Color.MAGENTA);
        put("ORANGE", Color.ORANGE);
        put("PINK", Color.PINK);
        put("WHITE", Color.WHITE);
        put("YELLOW", Color.YELLOW);
    }};
    private Color titleColor;
    private Color textColor;
    private float titleSize;
    private float textSize;
    private int pageNum;
    private float leading;

    void setTilteColor(String titleColor) {
        this.titleColor = COLORS.get(titleColor.toUpperCase());
    }

    void setTextColor(String textColor) {
        this.textColor = COLORS.get(textColor.toUpperCase());
    }

    void setTitleSize(String titleSize) {
        this.titleSize = Float.parseFloat(titleSize);
    }

    void setTextSize(String textSize) {
        this.textSize = Float.parseFloat(textSize);
    }

    void setPageNum(String pageNum) {
        this.pageNum = Integer.parseInt(pageNum);
    }

    void setLeading(String leading) {
        this.leading = Float.parseFloat(leading);
    }

    static void setTilteColorProperty(String titleColor) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("titleColor", titleColor);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void setTextColorProperty(String textColor) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("textColor", textColor);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void setTitleSizeProperty(String titleSize) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("titleSize", titleSize);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void setTextSizeProperty(String textSize) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("textSize", textSize);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void setPageNumProperty(String pageNum) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("pageNum", pageNum);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void setLeadingProperty(String leading) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));
            prop.setProperty("leading", leading);
            prop.store(new FileOutputStream("ExportToPDF.properties"), null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    ExportToPDF(){
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("ExportToPDF.properties"));

            titleColor = COLORS.get(prop.getProperty("titleColor", "RED"));
            textColor = COLORS.get(prop.getProperty("textColor", "BLACK"));
            titleSize = Float.parseFloat(prop.getProperty("titleSize", "30"));
            textSize = Float.parseFloat(prop.getProperty("textSize", "17"));
            pageNum = Integer.parseInt(prop.getProperty("pageNum", "2"));
            leading = Float.parseFloat(prop.getProperty("leading", "1.6"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    void songToPDF(String text){
        try {
            String[] lines = text.split("\n");

            // Creating a PdfWriter object
            String auhorAndTitle = lines[0];

            // Creating a PdfWriter object
            String dest = auhorAndTitle.replaceAll("/|\\\\|\\?|%|\\*|:|\\||<|>", "") + ".pdf";
            PdfWriter writer = new PdfWriter(dest);

            // Creating a PdfDocument object
            PdfDocument pdf = new PdfDocument(writer);

            // Creating a Document object
            Document doc = new Document(pdf);

            // Creating text object
            Text title = new Text(auhorAndTitle  + "\n\n");

            // Setting font of the title
            PdfFont titleFont = PdfFontFactory.createFont(System.getenv("SystemDrive") + "\\Windows\\Fonts\\calibrib.ttf", PdfEncodings.IDENTITY_H);
            title.setFont(titleFont);

            // Setting font color and size
            title.setFontColor(titleColor);
            title.setFontSize(titleSize);

            // Creating Title Paragraph
            Paragraph paragraphTitle = new Paragraph().setMultipliedLeading(0.8f);
            paragraphTitle.add(title);

            List<Paragraph> paragraphs = new ArrayList<>();

            paragraphs.add(new Paragraph().setMultipliedLeading(leading).setKeepTogether(true));
            int paragraphNumber = 0;

            PdfFont textFont = PdfFontFactory.createFont(System.getenv("SystemDrive") + "\\Windows\\Fonts\\calibri.ttf", PdfEncodings.IDENTITY_H);

            for (int i = 1; i < lines.length - 1; i++) {
                /*2 spaces for lyrics; 3 for translation*/
                if (!lines[i].equals("  ") && !lines[i].equals("   ")) {
                    Text textParagraph = new Text(lines[i] + "\n");
                    // Setting font of the text
                    textParagraph.setFont(textFont);

                    // Setting font color and size
                    textParagraph.setFontColor(textColor);
                    textParagraph.setFontSize(textSize);

                    paragraphs.get(paragraphNumber).add(textParagraph);
                } else {
                    paragraphs.add(new Paragraph().setMultipliedLeading(leading).setKeepTogether(true));
                    paragraphNumber++;
                    paragraphs.get(paragraphNumber).add(new Text("\n"));
                }
            }

            // Adding paragraphs to the document
            doc.add(paragraphTitle);

            paragraphs = replaceSameParagraphs(paragraphs, leading);

            for(Paragraph paragraph : paragraphs){
                doc.add(paragraph);
                if(doc.getPdfDocument().getNumberOfPages() >= pageNum + 1){
                    doc.getPdfDocument().removePage(pageNum + 1);
                    break;
                }
            }
            doc.close();

            System.out.println("Text added to pdf ..");
    }catch(IOException e){
            System.out.println("file not found");
        }
    }

    private static List<Paragraph> replaceSameParagraphs(List<Paragraph> paragraphs, float leading){
        for (int i = 0; i < paragraphs.size(); i++) {
            for (int j = i + 1; j < paragraphs.size(); j++) {
                if(paragraphs.get(i).getChildren().size() > 3){
                    if(paragraphs.get(i).getChildren().size() == paragraphs.get(j).getChildren().size()) {
                        if(compareTextChildren(paragraphs.get(i).getChildren(), paragraphs.get(j).getChildren())){
                            paragraphs.set(j, new Paragraph().addAll(
                                    removeRepetitions(paragraphs.get(j).getChildren()))
                                    .setMultipliedLeading(leading));
                        }
                    }
                }
            }
        }
        return paragraphs;
    }

    private static boolean compareTextChildren(List<IElement> children1, List<IElement> children2) {
        for (int i = 0; i < children1.size(); i++) {
            try {
                Text text1 = (Text) children1.get(i);
                Text text2 = (Text) children2.get(i);
                if (!text1.getText().toLowerCase().replace(" ","")
                        .equals(text2.getText().toLowerCase().replace(" ",""))) {
                    return false;
                }
            }catch(Throwable e ){
                return false;
            }
        }
        return true;
    }

    private static List<ILeafElement> removeRepetitions(List<IElement> children) {
        List<ILeafElement> newChildren = new ArrayList<>();
        Text text = (Text) children.get(1);
        /*remove and add new line*/
        text.setText(text.getText().substring(0, text.getText().length() - 1) + "... \n");
        newChildren.add((ILeafElement) children.get(0));
        newChildren.add((ILeafElement) children.get(1));
        return newChildren;
    }
}