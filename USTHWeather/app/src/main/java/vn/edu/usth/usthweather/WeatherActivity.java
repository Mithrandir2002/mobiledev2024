package vn.edu.usth.usthweather;

import static android.app.PendingIntent.getActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "Weather Activity";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        WeatherAndForecastFragmentPagerAdapter adapter = new WeatherAndForecastFragmentPagerAdapter(this);
        adapter.addFragment(new WeatherAndForecastFragment(), getApplicationContext().getString(R.string.location_1));
        adapter.addFragment(new WeatherAndForecastFragment(), getApplicationContext().getString(R.string.location_2));
        adapter.addFragment(new WeatherAndForecastFragment(), getApplicationContext().getString(R.string.location_3));

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
            tab.setText(adapter.getTitle(position));
        })).attach();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            saveMP3ToMediaStore();
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu resource (defined below in Step 2)
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            final Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    String content = msg.getData().getString("server_response");
                    Toast.makeText(WeatherActivity.this, content, Toast.LENGTH_SHORT).show();
                }
            };

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        Bundle bundle = new Bundle();
                        bundle.putString("server_response", "some json here");
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            return true;
        }
        if (id == R.id.action_settings) {
                // Start the new PrefActivity when Settings is clicked
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void saveMP3ToMediaStore() {
//        ContentResolver resolver = getContentResolver();
//        ContentValues contentValues = new ContentValues();
//
//        // Setting details for the new file to be created in the MediaStore
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "mysong.mp3");
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC); // Path: Music directory
//
//        // Insert the content values into the MediaStore
//        Uri uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//        if (uri != null) {
//            try (OutputStream outputStream = resolver.openOutputStream(uri);
//                 InputStream inputStream = getResources().openRawResource(R.raw.song)) {
//
//                // Copy the data from the raw resource file to the MediaStore location
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//                outputStream.flush();
//                System.out.println("MP3 file successfully copied to MediaStore.");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Failed to create a new MediaStore entry.");
//        }
//    }
}