package br.com.opba.backservitakepic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import br.com.opba.serv.OpbaTakePic;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int WRITE_EXTERNAL_REQUEST = 2;
    private static final int READ_EXTERNAL_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }

        startService(new Intent(this, OpbaTakePic.class));

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_REQUEST);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Aplicativo nao funcionara", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case READ_EXTERNAL_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Aplicativo nao funcionara", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case WRITE_EXTERNAL_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Aplicativo nao funcionara", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}
