package file_preview;

import folder.IFolder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
    private volatile boolean backgroundMode = false;
    private Executor pool = Executors.newFixedThreadPool(4);

    @Override
    public void run() {
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        for (FilePreviewData filePreviewData : filePreviewDataList) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    if (stop == false) {
                        if (backgroundMode) {
                            try {
                                // Use less resources.
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        filePreviewData.filePreviewListener.setPreviewIcon(previewGenerator.getFilePreviewSmall(filePreviewData.folder));
                    }
                }
            });
        }
    }

    public void stop() {
        stop = true;
    }

    public void setBackgroundMode(boolean backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public void start() {
        stop = false;
        setBackgroundMode(false);
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
