package ninja.aqrln.editor.ui.platform;

import ninja.aqrln.editor.ui.dialogs.FilePicker;
import ninja.aqrln.editor.ui.dialogs.UnixFilePicker;
import ninja.aqrln.editor.ui.menu.*;

/**
 * @author Alexey Orlenko
 */
public class OSXUIFactory extends UIFactory {
    private static OSXUIFactory instance;

    public static OSXUIFactory getInstance() {
        if (instance == null) {
            instance = new OSXUIFactory();
        }

        return instance;
    }

    @Override
    public MenuBuilder createMenuBuilder() {
        MenuBuilder builder = new MenuBuilder();
        builder.setMnemonicAdditionStrategy(OSXMnemonicAdditionStrategy.getInstance());

        return builder;
    }

    @Override
    public MenuBarBuilder createMenuBarBuilder() {
        MenuBarBuilder builder = new MenuBarBuilder();
        builder.setMnemonicAdditionStrategy(OSXMnemonicAdditionStrategy.getInstance());

        return builder;
    }

    @Override
    public ApplicationMenuDirector createApplicationMenuDirector(ApplicationMenuListener listener) {
        return new OSXApplicationMenuDirector(listener);
    }

    @Override
    public FilePicker getFilePicker() {
        return UnixFilePicker.getInstance();
    }

    private OSXUIFactory() {
    }
}
