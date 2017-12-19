package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.IFolderFactory;

import java.util.*;

public abstract class AbstractZipFolder implements IFolder {

    protected ZipEntryData zipEntryData;

//    protected String name = null;
//    protected String inZipPath = null;
    protected IFolderFactory factory = null;
    protected List<IFolder> children = null;
//    protected FolderTypes type;


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

    protected List<String[]> prepareEntriesList(List<String> entriesStr){
        entriesStr.sort(Comparator.naturalOrder());
        List<String[]> entriesNames = new ArrayList<>();
        for (String st : entriesStr) {
            entriesNames.add(splitPath(st));
        }
        return entriesNames;
    }

    protected void initChildren(List<ZipEntryData> entries) throws Exception {
        children = new ArrayList<>();
        if (entries.isEmpty()) {
            return;
        }
        Iterator<ZipEntryData> iter = entries.iterator();
        ZipEntryData zipEntry = iter.next();
        while (iter.hasNext()) {
            if (zipEntry.getInZipSplitPath().length > 1 + zipEntryData.getInZipSplitPath().length) {
                throw new Exception("init Children - lead name not a parent!");
            }
            List<ZipEntryData> localChildren = new ArrayList<>();
            ZipEntryData currentZipEntry = null;
            while (iter.hasNext()) {
                currentZipEntry = iter.next();
                if (currentZipEntry.getInZipSplitPath().length == 1 + zipEntryData.getInZipSplitPath().length) {
                    break;
                }
                localChildren.add(currentZipEntry);
            }
            Map<String, Object> params = new HashMap<>();
            params.put(IFolderFactory.PARENTENTRY, zipEntry);
            params.put(IFolderFactory.CHILDENTRIES, localChildren);
            children.add(factory.createIFolder(params));
            if (!iter.hasNext() && currentZipEntry.getInZipSplitPath().length == 1 + zipEntryData.getInZipSplitPath().length) {
                params.put(IFolderFactory.PARENTENTRY, currentZipEntry);
                params.put(IFolderFactory.CHILDENTRIES, new ArrayList<ZipEntryData>());
                children.add(factory.createIFolder(params));
            }
            zipEntry = currentZipEntry;
        }
    }


}
