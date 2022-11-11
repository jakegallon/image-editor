package frame;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public enum FileFormat {
    PNG("png", ".png"),
    JPG("jpg", ".jpg"),
    BMP("bmp", ".bmp"),
    TIFF("tiff", ".tiff");

    private final String name;
    private final String suffix;

    FileFormat(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public FileFormatFilter getFileFormatFilter() {
        return new FileFormatFilter(this);
    }

    public static class FileFormatFilter extends FileFilter {

        private final FileFormat fileFormat;

        private FileFormatFilter(FileFormat fileFormat) {
            this.fileFormat = fileFormat;
        }

        public FileFormat getFileFormat() {
            return fileFormat;
        }

        @Override
        public boolean accept(File f) {
            return f.getName().endsWith("." + fileFormat.getName());
        }

        @Override
        public String getDescription() {
            return fileFormat.name() + " (*." + fileFormat.getName() + ")";
        }
    }
}


