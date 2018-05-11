package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.factory.IFolderFactory;

import java.util.*;


public abstract class AbstractZipFolder implements IFolder {

    ZipEntryData zipEntryData;
    protected IFolderFactory factory = null;
    private List<IFolder> children = null;
    boolean initialized = false;

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

    void initChildren(List<ZipEntryData> entries) throws Exception {
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
        ArrayList<IFolder> sortedChildren = new ArrayList<>();
        sortedChildren.ensureCapacity(children.size());
        for (IFolder folder : children) {
            if (folder.getType() == FolderTypes.FOLDER) {
                sortedChildren.add(folder);
            }
        }
        for (IFolder folder : children) {
            if (folder.getType() != FolderTypes.FOLDER) {
                sortedChildren.add(folder);
            }
        }
        children = sortedChildren;
    }

    @Override
    public Character getSeparator() {
        return '/';
    }
}
