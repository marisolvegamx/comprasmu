package com.example.comprasmu.data.remote;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.work.ProgressUpdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody  extends RequestBody {
    private File mFile;
    private String mPath;
    private ImageUploadCallback mListener;
    private String content_type;
    private String upload;
    // checks when the function is called second time
    private int writeToCall = 0;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    public ProgressRequestBody(final File file, String content_type, final  ImageUploadCallback listener) {
        this.content_type = content_type;
        mFile = file;
        mListener = listener; //callback passed from the activity
    }
    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type+"/jpeg");
    }
    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }
    @Override
    public void writeTo( BufferedSink sink) throws IOException {
        writeToCall++; // update the counter
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long uploaded = 0;
        try (FileInputStream in = new FileInputStream(mFile)) {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                uploaded += read;
                sink.write(buffer, 0, read);
                if (writeToCall == 2) { // updating the progress
                    //  handler.post(new ProgressUpdater(uploaded, fileLength));
                }
            }
        }
    }
    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }
        @Override
        public void run() {
            if(mListener!=null)
                mListener.onProgressUpdate((int)(100 * mUploaded / mTotal)); //updating the UI of the progress
        }
    }


}
