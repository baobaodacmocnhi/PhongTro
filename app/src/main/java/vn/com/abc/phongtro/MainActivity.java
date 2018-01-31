package vn.com.abc.phongtro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnKhachHang = (Button) findViewById(R.id.btnKhachHang);
        btnKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityKhachHang.class);
                startActivity(intent);
            }
        });

        Button btnPhong = (Button) findViewById(R.id.btnPhong);
        btnPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityPhong.class);
                startActivity(intent);
            }
        });

        Button btnGiaDien = (Button) findViewById(R.id.btnGiaDien);
        btnGiaDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityGiaDien.class);
                startActivity(intent);
            }
        });

        Button btnGiaNuoc = (Button) findViewById(R.id.btnGiaNuoc);
        btnGiaNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityGiaNuoc.class);
                startActivity(intent);
            }
        });

        Button btnHoaDon = (Button) findViewById(R.id.btnHoaDon);
        btnHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityHoaDon.class);
                startActivity(intent);
            }
        });

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityBluetooth.class);
                startActivity(intent);
            }
        });

    }
}
