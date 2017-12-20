package folder.zip_folder;

import folder.FileTypeGetter;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipInZipFolder extends ZipStreamFolder {

    private ZipFile file;
    private ZipEntry entry;

    public ZipInZipFolder(ZipFile file, ZipEntry entry) {
        this.zipEntryData = new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName()));
        this.file = file;
        this.entry = entry;
    }

    @Override
    void resetStream() throws IOException {
        zipStream = new ZipInputStream(file.getInputStream(entry));
    }
}
