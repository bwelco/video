package com.elliott.supervideoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elliott.supervideoplayer.utils.ConfigUtil;

public class SettingActivity extends AppCompatActivity {

    EditText ip;
    EditText phone;
    Button config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ip = (EditText) findViewById(R.id.ip_config);
        phone = (EditText) findViewById(R.id.default_police_phone);

        ip.setText(ConfigUtil.getIp(this));
        phone.setText(ConfigUtil.getPhone(this));

        config = (Button) findViewById(R.id.save_config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ip.length() == 0 || phone.length() == 0) {
                    return;
                }
                ConfigUtil.setIp(SettingActivity.this, ip.getText().toString());
                ConfigUtil.setPhone(SettingActivity.this, phone.getText().toString());
                Toast.makeText(SettingActivity.this, "更新设置成功", Toast.LENGTH_LONG).show();
            }
        });

    }
}
