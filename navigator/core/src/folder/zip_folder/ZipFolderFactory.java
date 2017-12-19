package folder.zip_folder;

import folder.IFolder;
import folder.IFolderFactory;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;


public class ZipFolderFactory implements IFolderFactory {

    private ZipFile zipFile;

    public ZipFolderFactory(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        String inZipPath = (String) params.get(INZIPPATHSTRING);
        List<String[]> entries = (List<String[]>) params.get(CHILDENTRIES);
        return new ZipFileFolder(zipFile, inZipPath, entries, this);
    }
}
