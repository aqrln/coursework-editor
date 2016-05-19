package ninja.aqrln.editor.ui.platform;

import ninja.aqrln.editor.dom.Document;
import ninja.aqrln.editor.io.DocumentSerializer;
import ninja.aqrln.editor.ui.frames.AboutDialog;
import ninja.aqrln.editor.ui.frames.DocumentWindow;
import ninja.aqrln.editor.ui.menu.ApplicationMenuListener;
import ninja.aqrln.editor.util.OSXExtensions;
import ninja.aqrln.editor.util.OperatingSystem;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

/**
 * @author Alexey Orlenko
 */
public class ApplicationUI implements ApplicationMenuListener {
    private static ApplicationUI instance;

    private DocumentWindow activeWindow;

    public static ApplicationUI getInstance() {
        if (instance == null) {
            instance = new ApplicationUI();
        }

        return instance;
    }

    private JMenuBar applicationMenu;

    private ApplicationUI() {
        applicationMenu = UIFactory.getInstance().createApplicationMenuDirector(this).buildApplicationMenu();
    }

    public void run() {
        if (OperatingSystem.getOS() == OperatingSystem.OS_X) {
            OSXExtensions.getInstance().setMenuBar(applicationMenu);
        }

        onFileNew();
    }

    public JMenuBar getApplicationMenu() {
        return applicationMenu;
    }

    private void newDocumentWindow(Document document, String filename) {
        SwingUtilities.invokeLater(() -> {
            DocumentWindow window = new DocumentWindow(document);
            window.setFilename(filename);
            window.setVisible(true);
        });
    }

    public void notifyWindowActivation(DocumentWindow window) {
        activeWindow = window;
    }

    @Override
    public void onFileNew() {
        newDocumentWindow(new Document(), null);
    }

    @Override
    public void onFileOpen() {
        String filename = UIFactory.getInstance().getFilePicker().showLoadDialog();
        if (filename == null) {
            return;
        }

        Document document = DocumentSerializer.load(filename);
        newDocumentWindow(document, filename);
    }

    @Override
    public void onFileSave() {
        DocumentWindow window = activeWindow;
        if (window == null) {
            return;
        }

        String filename = window.getFilename();

        if (filename == null) {
            onFileSaveAs();
        } else {
            Document document = window.getDocument();
            DocumentSerializer.save(document, filename);
        }
    }

    @Override
    public void onFileSaveAs() {
        DocumentWindow window = activeWindow;
        if (window == null) {
            return;
        }

        String filename = UIFactory.getInstance().getFilePicker().showSaveDialog();
        if (filename == null) {
            return;
        }

        Document document = window.getDocument();
        DocumentSerializer.save(document, filename);

        window.setFilename(filename);
        window.updateTitle();
    }

    @Override
    public void onFileSaveAll() {

    }

    @Override
    public void onFileClose() {

    }

    @Override
    public void onQuit() {
        System.out.println("quit");
        System.exit(0);
    }

    @Override
    public void onExportToHTML() {

    }

    @Override
    public void onExportToLaTeX() {

    }

    @Override
    public void onUndo() {

    }

    @Override
    public void onRedo() {

    }

    @Override
    public void onCut() {

    }

    @Override
    public void onCopy() {

    }

    @Override
    public void onPaste() {

    }

    @Override
    public void onAbout() {
        JDialog about = new AboutDialog();
        about.setVisible(true);
    }
}