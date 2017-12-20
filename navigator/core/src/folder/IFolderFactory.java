package folder;

import java.util.HashMap;
import java.util.Map;

public interface IFolderFactory {

    String INZIPPATHSTRING = "InFolderPath";
    String THISENTRY = "ParentEntry";
    String CHILDENTRIES = "ChildEntries";
    String FILESTRING = "File";

    IFolder createIFolder(Map<String, Object> params) throws Exception;

}
