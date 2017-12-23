package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.IFolderFactory;

import java.util.*;


public abstract class AbstractZipFolder implements IFolder {

    protected ZipEntryData zipEntryData;
    protected IFolderFactory factory = null;
    protected List<IFolder> children = null;


    @Override
    public List<IFolder> getItems() {
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
        while (iter.hasNext()) {
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
            if (!iter.hasNext() && currentZipEntry.getInZipSplitPath().length == zipEntry.getInZipSplitPath().length) {
                params.put(IFolderFactory.THISENTRY, currentZipEntry);
                params.put(IFolderFactory.CHILDENTRIES, new ArrayList<ZipEntryData>());
                children.add(factory.createIFolder(params));
            }
            zipEntry = currentZipEntry;
        }
    }


}
