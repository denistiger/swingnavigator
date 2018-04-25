package folder;

import folder.factory.FolderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FileSystemEntity implements IFolder, ILevelUp{
    protected File file;

    public class NotALocalFolderException extends Exception {

    }

    public class NullInitializedFolderException extends Exception {

    }

    @Override
    public IFolder levelUp() {
        try {
            return new LocalFolder(file.getParentFile());
        } catch (NotALocalFolderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullInitializedFolderException e) {
            e.printStackTrace();
        }
//        if (System.getProperty("os.name").startsWith("Windows")) {
//            return new LocalFolder()
//        }
        return null;
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public boolean isFileSystemPath() {
        return true;
    }


    private void initFromFile(File file) throws FileNotFoundException, NullInitializedFolderException {
        if (file == null) {
            throw new NullInitializedFolderException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        this.file = file;
    }

    public FileSystemEntity(File file) throws FileNotFoundException, NullInitializedFolderException {
        initFromFile(file);
    }

    public FileSystemEntity(String path) throws FileNotFoundException, NullInitializedFolderException {
        this(new File(path));
    }
    @Override
    public List<IFolder> getItems() {
        File[] files = file.listFiles();
        // TODO fix FolderFactory usage
        return new FolderFactory().createIFolderList(files);
    }

    @Override
    public IFolder.FolderTypes getType() {
        return IFolder.FolderTypes.FOLDER;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

}
