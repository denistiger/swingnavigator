package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.factory.IFolderFactory;

import java.util.*;


public abstract class AbstractZipFolder implements IFolder {

    protected ZipEntryData zipEntryData;
    protected IFolderFactory factory = null;
    protected List<IFolder> children = null;
    protected boolean initialized = false;

    protected abstract void init() throws Exception;

    @Override
    public List<IFolder> getItems() {
        if (!initialized) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (!FileTypeGetter.isFolderType(getType())) {
            assert children == null || children.size() == 0;
            return null;
        }
        return children;
    }

    @Override
    public String getName() {
        return zipEntryData.getName();
    }

    @Override
    public IFolder.FolderTypes getType() {
        return zipEntryData.getType();
    }

    protected void initChildren(List<ZipEntryData> entries) throws Exception {
        children = new ArrayList<>();
        if (entries.isEmpty()) {
            return;
        }
        Iterator<ZipEntryData> iter = entries.iterator();
        ZipEntryData zipEntry = iter.next();
        do {
            if (zipEntry.getInZipSplitPath().length != 1 + zipEntryData.getInZipSplitPath().length) {
                throw new Exception("init Children - lead name not a parent!");
            }
            List<ZipEntryData> localChildren = new ArrayList<>();
            ZipEntryData currentZipEntry = null;
            while (iter.hasNext()) {
                currentZipEntry = iter.next();
                if (currentZipEntry.getInZipSplitPath().length == zipEntry.getInZipSplitPath().length) {
                    break;
                }
                localChildren.add(currentZipEntry);
            }
            Map<String, Object> params = new HashMap<>();
            params.put(IFolderFactory.THISENTRY, zipEntry);
            params.put(IFolderFactory.CHILDENTRIES, localChildren);
            children.add(factory.createIFolder(params));
            if (!iter.hasNext() && currentZipEntry != null && currentZipEntry.getInZipSplitPath().length == zipEntry.getInZipSplitPath().length) {
                params.put(IFolderFactory.THISENTRY, currentZipEntry);
                params.put(IFolderFactory.CHILDENTRIES, new ArrayList<ZipEntryData>());
                children.add(factory.createIFolder(params));
            }
            zipEntry = currentZipEntry;
        } while (iter.hasNext());
    }

    @Override
    public Character getSeparator() {
        return '/';
    }
}
