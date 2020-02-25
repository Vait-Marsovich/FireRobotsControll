package ru.karelia.rovesnik.firerobotcontoll;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "contoll";

    public static final String APP_PREFERENCES_GUN = "AddrGun";
    public static final String APP_PREFERENCES_MDL = "AddrMdl";
    public static final String APP_PREFERENCES_ADR = "MdlAddr";

    private SharedPreferences mSettings;

    private SocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.fireAddress);
        numberPicker.setMaxValue(255);
        numberPicker.setMinValue(0);
        numberPicker.setValue(mSettings.getInt(APP_PREFERENCES_GUN, 0));
        NumberPicker numberPicker2 = (NumberPicker) findViewById(R.id.controlAddress);
        numberPicker2.setMaxValue(255);
        numberPicker2.setMinValue(0);
        numberPicker.setValue(mSettings.getInt(APP_PREFERENCES_MDL, 80));
    }

    public void connect(View view) {
        TextView edtext = (TextView)findViewById(R.id.inetStatus);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            edtext.setText("ONLINE");
        }
        else {
            edtext.setText("OFFLINE");
        }

        EditText ip = (EditText) findViewById(R.id.ipDevice);
        client = new SocketClient(ip.getText().toString(), 3487);
        client.updater = new ScreenData() {
            @Override
            public void update(Byte a) {
                String b = String.valueOf((char)a.byteValue());

                TextView edtext = (TextView)findViewById(R.id.answer);
                edtext.setText(b);
            }
        };
        client.run();

        setContentView(R.layout.gamepad_control);
    }

    public void pressContoll(View v) {
        String command = v.getTag().toString();
        client.sendData(command.getBytes());
    }
}
