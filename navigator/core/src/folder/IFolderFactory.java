package folder;

import java.util.Map;

public interface IFolderFactory {

    IFolder createIFolder(Map<String, Object> params);
}
