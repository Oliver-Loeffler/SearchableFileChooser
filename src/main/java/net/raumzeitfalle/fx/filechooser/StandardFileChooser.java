package net.raumzeitfalle.fx.filechooser;

import java.awt.Window;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.stage.FileChooser;

public class StandardFileChooser {
    
    /**
     * 
     * The wrapped system dialog from JavaFX.
     *  
     */
	private final FileChooser dialog;

	private final FileSystemDialogAdapter<FileChooser, Window, File> adapter;
	
	private final Consumer<Window> beforeOpen;
	
	private final Consumer<Window> afterClosing;
	
	public StandardFileChooser() {
		this.dialog = new FileChooser();
		this.adapter = new FileSystemDialogAdapter<FileChooser, Window, File>(dialog,
				(FileChooser dialog, Window window)->dialog.showOpenDialog(null));
		
		this.beforeOpen = window -> { 
			window.setFocusableWindowState(false);
			window.setEnabled(false);	
		};
		
		this.afterClosing = window -> {
			SwingUtilities.invokeLater(() -> {
				window.setEnabled(true);
				window.setFocusableWindowState(true);
				window.toFront();
			});
		};
		
		this.adapter
		    .beforeOpenDialog(beforeOpen)
		    .afterClosingDialog(afterClosing);
		
	}
	
	public FileChooser getDialog() {
		return dialog;
	}
	
	public int showOpenDialog(JFrame frame) {
		return this.adapter.runDialog(frame);
	}
	
	public int showSaveDialog(JFrame frame) {
		return this.adapter.runDialog((FileChooser dialog, Window window)->dialog.showSaveDialog(null), frame);
	}
	
	public File getSelectedFile() {
		return this.adapter.getResult();
	}
	
}