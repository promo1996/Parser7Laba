package sisairis7;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XMLFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);

        return extension != null && extension.equals(Utils.xml);

    }

    public String getDescription() {
        return "Emm XML File";
    }

}