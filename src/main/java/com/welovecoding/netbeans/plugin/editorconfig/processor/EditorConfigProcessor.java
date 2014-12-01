package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter;
import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.editorconfig.core.EditorConfig;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getSimpleName());
  public static final Level OPERATION_LOG_LEVEL = Level.WARNING;
  private final EditorConfig ec;

  public EditorConfigProcessor() {
    ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
  }

  public void applyRulesToFile(DataObject dataObject) throws Exception {
    String filePath = dataObject.getPrimaryFile().getPath();
    MappedEditorConfig mappedConfig = EditorConfigPropertyMapper.createEditorConfig(filePath);

    LOG.log(Level.INFO, "Mapped rules for: {0}", filePath);
    LOG.log(Level.INFO, mappedConfig.toString());
//    MappedEditorConfig editorConfig = parseRulesForFile(dataObject);
  }

  private void flushFile(FileInfo info) {
    if (info.isOpenedInEditor()) {
      updateChangesInEditorWindow(info);
    } else {
      updateChangesInFile(info);
    }
  }

  private void updateChangesInFile(FileInfo info) {
    LOG.log(Level.INFO, "Write content (with all rules applied) to file: {0}", info.getFileObject().getPath());

    WriteStringToFileTask task = new WriteStringToFileTask(info);
    task.run();
  }

  private void updateChangesInEditorWindow(FileInfo info) {
    LOG.log(Level.INFO, "Update changes in Editor window for: {0}", info.getPath());

    EditorCookie cookie = info.getCookie();
    NbDocument.runAtomic(cookie.getDocument(), () -> {
      try {
        StyledDocumentWriter.writeWithEditorKit(info);
      } catch (FileAccessException ex) {
        LOG.log(Level.SEVERE, ex.getMessage());
      }
    });
  }

  /*
   private boolean doCharset(FileObject fileObject, String charset) {
   boolean hasToBeChanged = false;

   Charset currentCharset = FileInfoReader.guessCharset(fileObject);
   Charset requestedCharset = EditorConfigPropertyMapper.mapCharset(charset);

   if (!currentCharset.equals(requestedCharset)) {
   LOG.log(Level.INFO, "Charset change needed from {0} to {1}",
   new Object[]{currentCharset.name(), requestedCharset.name()});
   hasToBeChanged = true;
   }

   return hasToBeChanged;
   }
   */

  /*
   private boolean doEndOfLine(DataObject dataObject, String ecLineEnding) {
   FileObject fileObject = dataObject.getPrimaryFile();
   String javaLineEnding = EditorConfigPropertyMapper.mapLineEnding(ecLineEnding);
   boolean wasChanged = false;

   try {
   StringBuilder content = new StringBuilder(fileObject.asText());
   wasChanged = XLineEndingOperation.doLineEndings(content, javaLineEnding);
   } catch (IOException ex) {
   Exceptions.printStackTrace(ex);
   }

   StyledDocument document = NbDocument.getDocument(dataObject);
   if (document != null && wasChanged) {
   if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(javaLineEnding)) {
   document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, javaLineEnding);
   LOG.log(Level.INFO, "Action: Changed line endings in Document.");

   } else {
   LOG.log(Level.INFO, "Action not needed: Line endings are already set to: {0}", ecLineEnding);
   }
   }

   return wasChanged;
   }
   */
  private void flushStyles(FileObject fileObject) {
    try {
      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
    }
  }

  private EditorCookie getEditorCookie(DataObject dataObject) {
    return dataObject.getLookup().lookup(EditorCookie.class);
  }
}
