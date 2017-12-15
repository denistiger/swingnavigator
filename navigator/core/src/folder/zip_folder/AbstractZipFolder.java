package folder.zip_folder;

import folder.IFolder;
import folder.IFolderFactory;

import java.util.*;

public abstract class AbstractZipFolder implements IFolder {

    // TODO remove
    static final Character zipPathSeparator = '/';

    protected String name = null;
    protected String inZipPath = null;
    protected IFolderFactory factory = null;
    protected List<IFolder> children = null;
    protected FolderTypes type;

    protected String[] splitPath(String path) {
        return path.split(String.valueOf(zipPathSeparator));
    }

    protected String getLastName(String path) {
        String[] list = splitPath(path);
        if (list.length > 0) {
            return list[list.length - 1];
        }
        return "";
    }

    @Override
    public List<IFolder> getItems() {
        if (getType() == IFolder.FolderTypes.FILE) {
            assert children == null || children.size() == 0;
            return null;
        }
        return children;
    }

    @Override
    public String getName() {
        if (name != null)
            return name;
        return getLastName(inZipPath);
    }

    @Override
    public IFolder.FolderTypes getType() {
        return type;
    }

    protected List<String[]> prepareEntriesList(List<String> entriesStr){
        entriesStr.sort(Comparator.naturalOrder());
        List<String[]> entriesNames = new ArrayList<>();
        for (String st : entriesStr) {
            entriesNames.add(splitPath(st));
        }
        return entriesNames;
    }

    protected void initChildren(List<String[]> entries) throws Exception {
        children = new ArrayList<>();
        if (entries.isEmpty()) {
            return;
        }
        Iterator<String[]> iter = entries.iterator();
        String[] itemName = iter.next();
        while (iter.hasNext()) {
            if (itemName.length > 1) {
                throw new Exception("init Children - lead name not a parent!");
            }
            List<String[]> localChildren = new ArrayList<>();
            String[] currentItemName = null;
            while (iter.hasNext()) {
                currentItemName = iter.next();
                if (currentItemName.length == 1) {
                    break;
                }
                String[] cutPath = new String[currentItemName.length - 1];
                for (int i = 1; i < currentItemName.length; ++i) {
                    cutPath[i - 1] = currentItemName[i];
                }
                localChildren.add(cutPath);
            }
            Map<String, Object> params = new HashMap<>();
            params.put(IFolderFactory.INZIPPATHSTRING, inZipPath == "" ? itemName[0] : inZipPath + String.valueOf(zipPathSeparator) + itemName[0]);
            params.put(IFolderFactory.ENTRIESLISTSTRING, localChildren);
            children.add(factory.createIFolder(params));
            if (!iter.hasNext() && currentItemName.length == 1) {
                params.put(IFolderFactory.INZIPPATHSTRING, inZipPath == "" ? itemName[0] : inZipPath + String.valueOf(zipPathSeparator) + currentItemName[0]);
                params.put(IFolderFactory.ENTRIESLISTSTRING, new ArrayList<String[]>());
                children.add(factory.createIFolder(params));
            }
            itemName = currentItemName;
        }
    }


}
