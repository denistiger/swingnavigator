package ui;

import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;
import folder.file_preview.IFilePreviewListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class LazyIconLoader implements Runnable{

    private class FilePreviewData {
        public IFilePreviewListener filePreviewListener;
        public IFolder folder;

        public FilePreviewData(IFilePreviewListener filePreviewListener, IFolder folder) {
            this.filePreviewListener = filePreviewListener;
            this.folder = folder;
        }
    }

    private List<FilePreviewData> filePreviewDataList = new LinkedList<>();
    private volatile boolean stop = false;

    @Override
    public void run() {
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        for (FilePreviewData filePreviewData : filePreviewDataList) {
            filePreviewData.filePreviewListener.setPreviewIcon(previewGenerator.getFilePreview(filePreviewData.folder));
            if (stop == true) {
                break;
            }
        }
    }

    public void stop() {
        stop = true;
    }

    public void start() {
        stop = false;
        new Thread(this).start();
    }

    public void addListener(IFilePreviewListener filePreviewListener, IFolder folder) {
        filePreviewDataList.add(new FilePreviewData(filePreviewListener, folder));
    }

    public void removeListener(IFilePreviewListener filePreviewListener1) {
        Predicate<FilePreviewData> previewDataPredicate = p -> p.filePreviewListener == filePreviewListener1;
        filePreviewDataList.removeIf(previewDataPredicate);
   }

}
