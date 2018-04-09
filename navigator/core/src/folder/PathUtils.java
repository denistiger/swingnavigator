package folder;

public class PathUtils {
    private String path, pathPrefix;
    private Character separator;
    private int pathBegin;

    public PathUtils(String path1) {
        this.path = path1;
        separator = '/';
        if (!path.contains("/") && path.contains("\\")) {
            separator = '\\';
        }
        if (path.lastIndexOf(separator) != path.length() - 1) {
            path += separator;
        }
        pathBegin = path.indexOf("//") + 2;
        pathPrefix = path.substring(0, pathBegin);
        if (path.lastIndexOf(separator) == path.length() - 1 && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }
    }

    public void push(String name) {
        path += name + separator;
    }

    public String pop() {
        int sepPos = path.lastIndexOf(separator);
        if (sepPos == -1) {
            String out = path;
            path = "";
            return path;
        }
        String res = path.substring(sepPos + 1);
        path = path.substring(0, sepPos);
        return res;
    }

    public String getPath() {
        return path;
    }
}
