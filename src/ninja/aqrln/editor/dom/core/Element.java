package ninja.aqrln.editor.dom.core;

import java.awt.Dimension;
import java.util.ListIterator;

/**
 * @author Alexey Orlenko
 */
public interface Element {
    ListIterator<Element> getListIterator();
    Dimension getSize();
    void setStyle(Style style);
    Style getStyle();
}