package ninja.aqrln.editor.ui;

import ninja.aqrln.editor.dom.Document;
import ninja.aqrln.editor.io.DocumentSerializer;
import ninja.aqrln.editor.ui.frames.AboutDialog;
import ninja.aqrln.editor.ui.frames.DocumentWindow;
import ninja.aqrln.editor.ui.menu.ApplicationMenuListener;
import ninja.aqrln.editor.ui.platform.UIFactory;
import ninja.aqrln.editor.util.OSXExtensions;
import ninja.aqrln.editor.util.OperatingSystem;

import javax.swing.*;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Alexey Orlenko
 */
public class ApplicationUI implements ApplicationMenuListener {
    private static ApplicationUI instance;

    private DocumentWindow activeWindow;

    private SortedSet<DocumentWindow> windowsPool;

    private JMenu windowMenu;
    private JCheckBoxMenuItem checkedWindowMenuItem;

    public static ApplicationUI getInstance() {
        if (instance == null) {
            instance = new ApplicationUI();
        }

        return instance;
    }

    private JMenuBar applicationMenu;

    private ApplicationUI() {
        applicationMenu = UIFactory.getInstance().createApplicationMenuDirector(this).buildApplicationMenu();
        windowsPool = Collections.synchronizedSortedSet(new TreeSet<>());
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
        rebuildWindowMenu();
    }

    public void notifyWindowOpen(DocumentWindow window) {
        windowsPool.add(window);
        rebuildWindowMenu();
    }

    public void notifyWindowClose(DocumentWindow window) {
        windowsPool.remove(window);

        if (windowsPool.size() == 0) {
            activeWindow = null;

            if (OperatingSystem.getOS() != OperatingSystem.OS_X) {
                onQuit();
            }
        }

        rebuildWindowMenu();
    }

    public void rebuildWindowMenu() {
        windowMenu.removeAll();

        for (DocumentWindow window : windowsPool) {
            String title = window.getDocument().getName();
            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(title);

            menuItem.addActionListener(e -> {
                checkedWindowMenuItem.setState(false);
                checkedWindowMenuItem = menuItem;
                window.toFront();
            });

            if (window == activeWindow) {
                menuItem.setState(true);
                checkedWindowMenuItem = menuItem;
            }

            windowMenu.add(menuItem);
        }
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

    private void saveDocumentWindow(DocumentWindow window) {
        String filename = window.getFilename();

        if (filename == null) {
            onFileSaveAs();
        } else {
            Document document = window.getDocument();
            DocumentSerializer.save(document, filename);
        }
    }

    @Override
    public void onFileSave() {
        DocumentWindow window = activeWindow;
        if (window == null) {
            return;
        }

        saveDocumentWindow(window);
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
        windowsPool.forEach(this::saveDocumentWindow);
    }

    @Override
    public void onFileClose() {

    }

    @Override
    public void onQuit() {
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

    @Override
    public void onToggleFirstLineIndent() {

    }

    @Override
    public void notifyWindowMenuReference(JMenu windowMenu) {
        this.windowMenu = windowMenu;
    }
}
