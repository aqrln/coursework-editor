package ninja.aqrln.editor.ui.frames;

import ninja.aqrln.editor.component.EditorPane;
import ninja.aqrln.editor.dom.Document;
import ninja.aqrln.editor.ui.ApplicationUI;
import ninja.aqrln.editor.util.OperatingSystem;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

/**
 * @author Alexey Orlenko
 */
public class DocumentWindow extends JFrame implements WindowListener, Comparable {
    private Document document;

    private String filename = null;

    private long timestamp = new Date().getTime();
    private EditorPane editorPane;

    public DocumentWindow(Document document) {
        super();
        addWindowListener(this);

        this.document = document;
        updateTitle();

        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        setJMenuBar(ApplicationUI.getInstance().getApplicationMenu());

        // If we are under OS X, the menu bar has to be set twice:
        // globally and for each window made invisible, since otherwise
        // Swing windows steal accelerators and none of them except
        // Quit and About work
        if (OperatingSystem.getOS() == OperatingSystem.OS_X) {
            Dimension invisible = new Dimension(0, 0);
            getJMenuBar().setSize(invisible);
            getJMenuBar().setPreferredSize(invisible);
            getJMenuBar().setMaximumSize(invisible);
        }

        initializeComponents();
    }

    private void initializeComponents() {
        editorPane = new EditorPane(document);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        editorPane.setScrollPane(scrollPane);
        addKeyListener(editorPane);

        getContentPane().add(scrollPane);
        pack();
        repaint();
    }

    public void updateTitle() {
        setTitle(document.getName() + " — Editor");
        ApplicationUI.getInstance().rebuildWindowMenu();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        ApplicationUI.getInstance().notifyWindowOpen(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ApplicationUI.getInstance().notifyWindowClose(this);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {
        setJMenuBar(ApplicationUI.getInstance().getApplicationMenu());
        ApplicationUI.getInstance().notifyWindowActivation(this);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public int compareTo(Object o) {
        DocumentWindow other = (DocumentWindow) o;
        return Long.signum(this.timestamp - other.timestamp);
    }

    public EditorPane getEditorPane() {
        return editorPane;
    }
}
