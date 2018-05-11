package file_preview;

import folder.IFolder;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class LazyIconLoader {

    private class FilePreviewData {
        IFilePreviewListener filePreviewListener;
        public IFolder folder;

        FilePreviewData(IFilePreviewListener filePreviewListener, IFolder folder) {
            this.filePreviewListener = filePreviewListener;
            this.folder = folder;
        }
    }

    private Deque<FilePreviewData> filePreviewDataList = new LinkedList<>();
    private volatile boolean stop = false;
    private volatile boolean backgroundMode = false;
    private FilePreviewGenerator filePreviewGenerator;
    private Semaphore semaphore = new Semaphore(1);

    public LazyIconLoader(FilePreviewGenerator filePreviewGenerator) {
        this.filePreviewGenerator = filePreviewGenerator;
        ArrayList<Thread> threadPull = new ArrayList<>();
        for (int i = 0; i < 4; ++i){
            threadPull.add(new Thread(new PreviewLoader()));
        }
        for (Thread thread : threadPull) {
            thread.start();
        }
    }

    private class PreviewLoader implements Runnable {

        @Override
        public void run() {
            // Technical solution - needs to have the same threads during the whole time program is up.
            // This is needed to maintain the same ftp clients one for each thread.
            // This allows safely use their InputStreams.
            while (true) {
                if (stop) {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if (backgroundMode) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                FilePreviewData filePreviewData = null;
                try {
                    semaphore.acquire();
                    if (!filePreviewDataList.isEmpty()) {
                        filePreviewData = filePreviewDataList.pollFirst();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                if (filePreviewData != null) {
                    filePreviewData.filePreviewListener.setPreviewIcon(
                            filePreviewGenerator.getFilePreviewSmall(filePreviewData.folder));
                }
                else {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void stop() {
        stop = true;
        try {
            semaphore.acquire();
            filePreviewDataList.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
    }

    public void setBackgroundMode(boolean backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public void start() {
        stop = false;
        setBackgroundMode(false);
    }

    public void addListener(IFilePreviewListener filePreviewListener, IFolder folder) {
        try {
            semaphore.acquire();
            filePreviewDataList.add(new FilePreviewData(filePreviewListener, folder));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
    }

}
