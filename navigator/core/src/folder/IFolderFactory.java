package folder;

import java.util.Map;

public interface IFolderFactory {

    String INZIPPATHSTRING = "InFolderPath";
    String ENTRIESLISTSTRING = "EntriesStrings";
    String FILESTRING = "File";

    IFolder createIFolder(Map<String, Object> params) throws Exception;
}
