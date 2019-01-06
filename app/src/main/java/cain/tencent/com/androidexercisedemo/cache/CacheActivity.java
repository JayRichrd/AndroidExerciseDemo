package cain.tencent.com.androidexercisedemo.cache;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cain.tencent.com.androidexercisedemo.R;
import cain.tencent.com.androidexercisedemo.cache.DiskLruCache;
import cain.tencent.com.androidexercisedemo.databinding.ActivityLuaCacheBinding;
import cain.tencent.com.androidexercisedemo.utils.MyUtils;

public class CacheActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLuaCacheBinding binding;
    DiskLruCache mDiskLruCache;
    public static final String IAMGE_URL = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    public static final String TAG = "CacheActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_lua_cache,
                null, false);
        setContentView(binding.getRoot());
        binding.btnShow.setOnClickListener(this);
        binding.btnCache.setOnClickListener(this);
        initCacheDir();
    }

    void initCacheDir() {
        try {
            File cacheDir = MyUtils.getUniqueCacheDir(this, "bitmap");
            if (cacheDir == null) return;
            if (!cacheDir.exists()) cacheDir.mkdirs();
            mDiskLruCache = DiskLruCache.open(cacheDir, MyUtils.getAppVersion(this), 1, 10 * 1024
                    * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cache:
                cacheImage();
                break;
            case R.id.btn_show:
                showImage();
                break;
        }
    }

    private void cacheImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String key = hashKeyForDisk(IAMGE_URL);
                String key = MyUtils.hashKeyForDisk(IAMGE_URL);
                try {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downImge(outputStream)) {
                            Log.i(TAG, "download image ok");
                            editor.commit();
                        } else {
                            Log.i(TAG, "download image error");
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void showImage() {
        try {
//            String key = hashKeyForDisk(IAMGE_URL);
            String key = MyUtils.hashKeyForDisk(IAMGE_URL);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                binding.ivImageView.setImageBitmap(bitmap);
                Log.i(TAG, "cache size = " + mDiskLruCache.size() / 1024 + "k");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean downImge(OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        final URL url;
        try {
            url = new URL(IAMGE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        final MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;

    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

