package net.raumzeitfalle.fx.filechooser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class FileChooserModelApplicationTest extends ApplicationTest {
	
	private FileChooserModel classUnderTest;
	
	private Path testRoot = Paths.get("./TestData/SomeFiles");
	
	private PathFilter txtFiles = PathFilter.forFileExtension("TXT", "txt");

	@Override
	public void init() throws Exception {
		classUnderTest = FileChooserModel.startingIn(testRoot);
	}

	
	@Test
	public void performFilterOperation() throws InterruptedException, ExecutionException {
				
		assertEquals(testRoot.toAbsolutePath(), 
					 classUnderTest.currentSearchPath().get().toAbsolutePath(),
					 "search path after initialization");
					
		assertEquals(11, classUnderTest.getFilteredPaths().size());

		// search all files with extension ".txt"	
		classUnderTest.updateFilterCriterion(txtFiles, "");
		assertEquals(5, classUnderTest.getFilteredPaths().size());

		// search all files with extension ".txt" and string "File3" in name
		classUnderTest.updateFilterCriterion(txtFiles, "File3");
		assertEquals(1, classUnderTest.getFilteredPaths().size());
		
		// search all files with extension ".txt" and string "File3" in name
		classUnderTest.updateFilterCriterion(txtFiles, "File3");
		assertEquals(1, classUnderTest.getFilteredPaths().size());
		
		PathFilter allFiles = PathFilter.create(p->true);
		
		// search all files with a horrible name
		classUnderTest.updateFilterCriterion(allFiles, "Horrbible");
		assertEquals(2, classUnderTest.getFilteredPaths().size());
		
		// no selection yet performed
		assertTrue(classUnderTest.invalidSelectionProperty().get());
		
		// now mimic a selection by user
		IndexedPath selection = classUnderTest.getFilteredPaths().get(0);
		classUnderTest.setSelectedFile(selection);
		
		assertFalse(classUnderTest.invalidSelectionProperty().get());
		
		Path selectedFile = classUnderTest.getSelectedFile();
		assertEquals(selection.asPath().getFileName(), selectedFile.getFileName());
		
	}
	
}
