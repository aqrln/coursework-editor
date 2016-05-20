package ninja.aqrln.editor.export;

import ninja.aqrln.editor.dom.Document;
import ninja.aqrln.editor.dom.core.Element;
import ninja.aqrln.editor.dom.core.Style;
import ninja.aqrln.editor.dom.model.*;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Alexey Orlenko
 */
public class HTMLExporter implements DocumentModelVisitor {
    private StringBuilder stringBuilder;
    private String title;

    public HTMLExporter(String title) {
        stringBuilder = new StringBuilder();
        this.title = title;
    }

    public String getString() {
        return stringBuilder.toString();
    }

    public static String toHTML(Document document) {
        HTMLExporter exporter = new HTMLExporter(document.getName());
        document.getRootElement().accept(exporter);
        return exporter.getString();
    }

    private void writeColorComponent(int colorComponent) {
        String string = Integer.toHexString(colorComponent);
        if (string.length() < 2) {
            stringBuilder.append('0');
        }
        stringBuilder.append(string);
    }

    private void writeColor(Color color) {
        stringBuilder.append('#');
        writeColorComponent(color.getRed());
        writeColorComponent(color.getGreen());
        writeColorComponent(color.getBlue());
    }

    private void writeFontFamily(String family) {
        switch (family) {
            case "Serif":
                stringBuilder.append("serif");
                break;
            case "Monospaced":
                stringBuilder.append("monospace");
                break;
            default:
                stringBuilder.append("sans-serif");
                break;
        }
    }

    @Override
    public void visitCharacterElement(CharacterElement element) {
        Style style = element.getStyle();
        stringBuilder.append("<span style=\"");

        stringBuilder.append("color:");
        writeColor(style.getForegroundColor());
        stringBuilder.append(";background-color:");
        writeColor(style.getBackgroundColor());

        Font font = style.getFont();

        stringBuilder.append(";font-family:");
        writeFontFamily(font.getFamily());
        stringBuilder.append(";font-size:");
        stringBuilder.append(font.getSize());
        stringBuilder.append("px;");

        if (font.isBold()) {
            stringBuilder.append("font-weight:bold;");
        }

        if (font.isItalic()) {
            stringBuilder.append("font-style:italic;");
        }

        stringBuilder.append("\">");

        stringBuilder.append(htmlEscape(element.getCharacter()));

        stringBuilder.append("</span>");
    }

    @Override
    public void visitParagraphElement(ParagraphElement element) {
        String alignment = "";
        switch (element.getAlignment()) {
            case LEFT:
                alignment = "left";
                break;
            case RIGHT:
                alignment = "right";
                break;
            case CENTER:
                alignment = "center";
                break;
            case JUSTIFY:
                alignment = "justify";
                break;
        }

        stringBuilder.append("<p style=\"text-align: ");
        stringBuilder.append(alignment);
        stringBuilder.append(";\">");

        for (Element character : element.getChildren()) {
            ((DocumentModelElement) character).accept(this);
        }

        stringBuilder.append("</p>");
    }

    @Override
    public void visitRootElement(RootElement element) {
        String preamble = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>" +
                htmlEscape(title) + "</title></head><body>";
        stringBuilder.append(preamble);

        for (Element paragraph : element.getChildren()) {
            ((DocumentModelElement) paragraph).accept(this);
        }

        stringBuilder.append("</body></html>");
    }

    private String htmlEscape(String string) {
        StringBuilder out = new StringBuilder();
        for (char c : string.toCharArray()) {
            out.append(htmlEscape(c));
        }
        return out.toString();
    }

    private String htmlEscape(char character) {
        if ("\"<>&".indexOf(character) != -1) {
            return "&#" + (int) character + ";";
        } else {
            return "" + character;
        }
    }
}
