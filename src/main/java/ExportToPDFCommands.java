import java.io.PrintStream;

class ExportToPDFCommands {
    static void export(PrintStream out, PrintStream err, String[] argv, String text) throws Exception {
        if(argv.length == 0) {
            ExportToPDF exportToPDF = new ExportToPDF();
            exportToPDF.songToPDF(text);

        /*need to have at least 1 pair*/
        }else if(argv.length >= 2 && argv.length%2 == 0){
            ExportToPDF exportToPDF = new ExportToPDF();

            int validOptionsCounter = 0;
            for (int i = 0; i < argv.length; i = i + 2)  {
                if("-p".equals(argv[i]) || "--pages".equals(argv[i])) {
                    exportToPDF.setPageNum(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-l".equals(argv[i]) || "--leading".equals(argv[i])) {
                    exportToPDF.setLeading(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-ttlc".equals(argv[i]) || "--titlecolor".equals(argv[i])) {
                    exportToPDF.setTilteColor(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-txtc".equals(argv[i]) || "--textcolor".equals(argv[i])) {
                    exportToPDF.setTextColor(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-ttls".equals(argv[i]) || "--titlesize".equals(argv[i])) {
                    exportToPDF.setTitleSize(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-txts".equals(argv[i]) || "--textsize".equals(argv[i])) {
                    exportToPDF.setTextSize(argv[i + 1]);
                    validOptionsCounter++;
                }
            }
            if(validOptionsCounter != argv.length/2){
                exportOptions(out);
                return;
            }
            exportToPDF.songToPDF(text);
        }else{
            exportOptions(out);
        }
    }

    static void exportDefaults(PrintStream out, PrintStream err, String[] argv) throws Exception {
        if(argv.length >= 2 && argv.length%2 == 0){
            int validOptionsCounter = 0;
            for (int i = 0; i < argv.length; i = i + 2)  {
                if("-p".equals(argv[i]) || "--pages".equals(argv[i])) {
                    ExportToPDF.setPageNumProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-l".equals(argv[i]) || "--leading".equals(argv[i])) {
                    ExportToPDF.setLeadingProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-ttlc".equals(argv[i]) || "--titlecolor".equals(argv[i])) {
                    ExportToPDF.setTilteColorProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-txtc".equals(argv[i]) || "--textcolor".equals(argv[i])) {
                    ExportToPDF.setTextColorProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-ttls".equals(argv[i]) || "--titlesize".equals(argv[i])) {
                    ExportToPDF.setTitleSizeProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
                if("-txts".equals(argv[i]) || "--textsize".equals(argv[i])) {
                    ExportToPDF.setTextSizeProperty(argv[i + 1]);
                    validOptionsCounter++;
                }
            }
            /*need to restore defaults*/
            if(validOptionsCounter != argv.length/2){
                exportOptions(out);
            }
        }else{
            exportdefaultsOptions(out);
        }
    }

    private static void exportOptions(PrintStream out) {
        out.println("Usage: export                       -        export chosen song to PDF");
        out.println("Usage: export -p --pages            -        export to PDF and set number of pages");
        out.println("Usage: export -l --leading          -        export to PDF and set leading");
        out.println("Usage: export -ttlc --titlecolor    -        export to PDF and set title's color");
        out.println("Usage: export -txtc --textcolor     -        export to PDF and set text's color");
        out.println("Usage: export -ttls --titlesize     -        export to PDF and set title's size");
        out.println("Usage: export -txts --textsize      -        export to PDF and set text's size");
    }

    private static void exportdefaultsOptions(PrintStream out) {
        out.println("Usage: exportdefaults                       -        export chosen song to PDF");
        out.println("Usage: exportdefaults -p --pages            -        export to PDF and set number of pages");
        out.println("Usage: exportdefaults -l --leading          -        export to PDF and set leading");
        out.println("Usage: exportdefaults -ttlc --titlecolor    -        export to PDF and set title's color");
        out.println("Usage: exportdefaults -txtc --textcolor     -        export to PDF and set text's color");
        out.println("Usage: exportdefaults -ttls --titlesize     -        export to PDF and set title's size");
        out.println("Usage: exportdefaults -txts --textsize      -        export to PDF and set text's size");
    }
}
