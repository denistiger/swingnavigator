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
    private final Object lock = new Object();

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
                synchronized (lock) {
                    if (stop || filePreviewDataList.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                boolean background;
                synchronized (lock) {
                    background = backgroundMode;
                }
                if (background) {
                    try {
                        // Minimizing resource load.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                FilePreviewData filePreviewData = null;
                synchronized (lock) {
                    if (!filePreviewDataList.isEmpty()) {
                        filePreviewData = filePreviewDataList.pollFirst();
                    }
                }
                if (filePreviewData != null) {
                    filePreviewData.filePreviewListener.setPreviewIcon(
                            filePreviewGenerator.getFilePreviewSmall(filePreviewData.folder));
                }
            }
        }
    }

    public void stop() {
        synchronized (lock) {
            stop = true;
            filePreviewDataList.clear();
        }
    }

    public void setBackgroundMode(boolean backgroundMode) {
        synchronized (lock) {
            this.backgroundMode = backgroundMode;
        }
    }

    public void start() {
        synchronized (lock) {
            stop = false;
            lock.notifyAll();
        }
        setBackgroundMode(false);
    }

    public void addListener(IFilePreviewListener filePreviewListener, IFolder folder) {
        synchronized (lock) {
            filePreviewDataList.add(new FilePreviewData(filePreviewListener, folder));
            lock.notify();
        }
    }

}
