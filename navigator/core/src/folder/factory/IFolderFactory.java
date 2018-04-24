package folder.factory;

import folder.IFolder;

import java.util.Map;

public interface IFolderFactory {

    String INZIPPATH = "InFolderPath";
    String THISENTRY = "ParentEntry";
    String CHILDENTRIES = "ChildEntries";
    String FILE = "File";
    String FILEPATH = "FilePath";

    IFolder createIFolder(Map<String, Object> params) throws Exception;

}
