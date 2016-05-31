package br.com.opba.serv;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class OpbaTakePic extends Service {


    public static int MAKE_TEN_PIC_AFTER_STOP = 10;

    private String TAG = "OPBA";

    private final String FOLDER = "OPPHOTO";

    private final Boolean GENERATOR_UNIQUE_NAME = Boolean.TRUE;

    private final String NAME_PHOTO = "OpbaPhoto_";
    private final String PHOTO_EXTENSION = ".jpg";

    @Override
    public void onCreate() {
        super.onCreate();
        PhotoDecodeRunnable myThread = new PhotoDecodeRunnable();
        myThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "BIND");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "DESTROY");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onSTARTCOMAND");
        return START_STICKY_COMPATIBILITY;
    }

    private void takePhoto() {

        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (RuntimeException e) {
            Log.i(TAG, "Camera not available");
            camera = null;
        }
        try {
            if (null == camera) {
                Log.i(TAG, "Could not get camera instance");
            } else {
                Log.i(TAG, "Got the camera, creating the dummy surface texture");
                try {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();
                } catch (Exception e) {
                    Log.i(TAG, "Could not set the surface preview texture");
                    e.printStackTrace();
                }
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                        File pictureFileDir = new File(path + File.separator + FOLDER + File.separator);
                        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                            return;
                        }

                        String photoFile;
                        if( GENERATOR_UNIQUE_NAME ) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                            String date = dateFormat.format(new Date());
                            photoFile = NAME_PHOTO + "_" + date + PHOTO_EXTENSION;
                        } else {
                            photoFile = NAME_PHOTO + PHOTO_EXTENSION;
                        }

                        //Unique name - Replace the arquive
                        String filename = pictureFileDir.getPath() + File.separator + photoFile;
                        File mainPicture = new File(filename);

                        try {
                            FileOutputStream fos = new FileOutputStream(mainPicture);
                            fos.write(data);
                            fos.close();
                            Log.i(TAG, "image saved");
                        } catch (Exception error) {
                            Log.i(TAG, "Image could not be saved");
                        }
                        camera.release();
                    }
                });
            }
        } catch (Exception e) {
            camera.release();
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(TAG, "REBIND");
    }

    public class PhotoDecodeRunnable extends Thread {
        @Override
        public void run() {
            int i = 0;
            while(true){
                //Taking Photo HERE
                if( i <= MAKE_TEN_PIC_AFTER_STOP ) {
                    takePhoto();
                }
                if( i >= 10000 ) {
                    i=0;
                }
                i++;
                SystemClock.sleep(3000);
            }
        }
    }
}
