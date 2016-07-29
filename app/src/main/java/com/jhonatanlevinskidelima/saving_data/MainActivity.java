package com.jhonatanlevinskidelima.saving_data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

public class MainActivity extends Activity {

    String HELLO_NAME = "nameDefaultHello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void storeFile(File file, String value) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(value);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFile(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            int i;

            i = inputStream.read();
            while (i != -1) {
                byteArray.write(i);
                i = inputStream.read();
            }
            inputStream.close();
            setMessage(byteArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMessage(String name) {
        TextView msg = (TextView) findViewById(R.id.helloLabel);
        msg.setText("Hello fucking " + name);
    }

    public void changeMessage(View view) {
        EditText editMsg = (EditText) findViewById(R.id.helloField);
        setMessage(editMsg.getText().toString());
    }


    public void storeSharedPreferences(View view) {
        EditText name = (EditText) findViewById(R.id.helloField);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(HELLO_NAME, name.getText().toString());
        editor.commit();
    }

    public void readSharedPreferences(View view) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String namePref = sharedPref.getString(HELLO_NAME, null);

        if (namePref != null) {
            setMessage(namePref.toString());
        }
    }

    public void deleteSharedPreferences(View view) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();
        editor.commit();
    }


    public void storeFileInternally(View view) {
        EditText name = (EditText) findViewById(R.id.helloField);
        File file = new File(getBaseContext().getFilesDir(), HELLO_NAME);
        storeFile(file, name.getText().toString());
    }

    public void readFileInternally(View view) {
        File file = new File(getBaseContext().getFilesDir(), HELLO_NAME);
        readFile(file);
    }

    public void deleteFileInternally(View view) {
        File file = new File(getBaseContext().getFilesDir(), HELLO_NAME);
        file.delete();
    }


    public void storeFileExternally(View view) {
        if (isExternalStorageWritable()) {
            EditText name = (EditText) findViewById(R.id.helloField);
            File file = new File(getExternalFilesDir(null), HELLO_NAME);
            storeFile(file, name.getText().toString());
        }
    }

    public void readFileExternally(View view) {
        if (isExternalStorageReadable()) {
            File file = new File(getExternalFilesDir(null), HELLO_NAME);
            readFile(file);
        }
    }

    public void deleteFileExternally(View view) {
        if (isExternalStorageWritable()) {
            File file = new File(getExternalFilesDir(null), HELLO_NAME);
            file.delete();
        }
    }


    public void storeCacheInternally(View view) {
        EditText name = (EditText) findViewById(R.id.helloField);
        File file = new File(getCacheDir(), HELLO_NAME);
        storeFile(file, name.getText().toString());
    }

    public void readCacheInternally(View view) {
        File file = new File(getCacheDir(), HELLO_NAME);
        readFile(file);
    }

    public void deleteCacheInternally(View view) {
        File file = new File(getCacheDir(), HELLO_NAME);
        file.delete();
    }


    public void storeCacheExternally(View view) {
        if (isExternalStorageWritable()) {
            EditText name = (EditText) findViewById(R.id.helloField);
            File file = new File(getExternalCacheDir(), HELLO_NAME);
            storeFile(file, name.getText().toString());
        }
    }

    public void readCacheExternally(View view) {
        if (isExternalStorageReadable()) {
            File file = new File(getExternalCacheDir(), HELLO_NAME);
            readFile(file);
        }
    }

    public void deleteCacheExternally(View view) {
        if (isExternalStorageReadable()) {
            File file = new File(getExternalCacheDir(), HELLO_NAME);
            file.delete();
        }
    }

    public void storeFirebase(View view) {
        EditText name = (EditText) findViewById(R.id.helloField);

        Firebase ref = new Firebase("https://nowrite-93cdf.firebaseio.com/");

        ref.child(HELLO_NAME).setValue(name.getText().toString());
    }

    public void readFirebase(View view) {
        Firebase ref = new Firebase("https://nowrite-93cdf.firebaseio.com/" + HELLO_NAME);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    setMessage(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void deleteFirebase(View view) {
        Firebase ref = new Firebase("https://nowrite-93cdf.firebaseio.com/" + HELLO_NAME);
        ref.removeValue();
    }
}
