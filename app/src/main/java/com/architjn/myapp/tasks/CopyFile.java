package com.architjn.myapp.tasks;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by architjn on 11/16/2016.
 */

public class CopyFile extends AsyncTask<File, Void, Void> {

    private File des;
    private OnProcessComplete callback;

    public CopyFile(OnProcessComplete callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
        if (callback != null && des != null)
            callback.fileCopied(des);
    }

    @Override
    protected Void doInBackground(File... files) {
        try {
            des = files[1];
            copy(files[0], files[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public interface OnProcessComplete {
        void fileCopied(File des);
    }

}
