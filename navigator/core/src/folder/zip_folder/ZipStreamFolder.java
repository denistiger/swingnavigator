package folder.zip_folder;

import folder.IFolder;
import folder.UnimplementedFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ZipStreamFolder extends AbstractZipfolder {
    ZipInputStream zipStream = null;

    abstract void resetStream() throws IOException;

    @Override
    public List<IFolder> getItems() {
        try {
            resetStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        List<IFolder> list = new ArrayList<>();
        List<String> listNames = new ArrayList<>();
        try {
            while (zipStream.available() == 1) {
                ZipEntry entry = zipStream.getNextEntry();
                if (entry == null) {
                    break;
                }
                listNames.add(entry.getName());
                list.add(new UnimplementedFolder(entry.getName()));
            }
            listNames.sort(Comparator.naturalOrder());
        } catch (IOException er) {
            System.out.println("folder.zip_folder.ZipStreamFolder getItems error");
            er.printStackTrace();
        }
        return list;
    }

    @Override
    public IFolder.FolderTypes getType() {
        return IFolder.FolderTypes.ZIP_FILE;
    }

    @Override
    public String getName() {
        return name;
    }

}
