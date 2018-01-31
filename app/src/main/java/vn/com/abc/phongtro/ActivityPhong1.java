package vn.com.abc.phongtro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityPhong1 extends AppCompatActivity {

    private WebService ws = new WebService();
    private SoapObject tbPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // permits to make a HttpURLConnection
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong1);

        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditText txtNgayThue = (EditText) findViewById(R.id.txtNgayThue);
                        txtNgayThue.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                //Hiển thị ra Dialog
                DatePickerDialog pic = new DatePickerDialog(
                        ActivityPhong1.this,
                        callback, year, month, day);
                //pic.setTitle("Chọn ngày hoàn thành");
                pic.show();
            }
        });

        Button btnSua = (Button) findViewById(R.id.btnSua);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText txtID = (EditText) findViewById(R.id.txtID);
                    if (txtID.getText().toString().matches("")) {
                        Toast.makeText(ActivityPhong1.this, "Chưa chọn Phòng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EditText txtName = (EditText) findViewById(R.id.txtName);
                    EditText txtGiaTien = (EditText) findViewById(R.id.txtGiaTien);
                    EditText txtSoNKNuoc = (EditText) findViewById(R.id.txtSoNKNuoc);
                    EditText txtChiSoDien = (EditText) findViewById(R.id.txtChiSoDien);
                    EditText txtChiSoNuoc = (EditText) findViewById(R.id.txtChiSoNuoc);
                    EditText txtNgayThue = (EditText) findViewById(R.id.txtNgayThue);
                    CheckBox chkThue = (CheckBox) findViewById(R.id.chkThue);
                    String Thue = "0";
                    if (chkThue.isChecked())
                        Thue = "1";

                    String resp = ws.SuaPhong(txtID.getText().toString(), txtName.getText().toString(), txtGiaTien.getText().toString(), txtSoNKNuoc.getText().toString(), txtChiSoDien.getText().toString(), txtChiSoNuoc.getText().toString(), txtNgayThue.getText().toString(), Thue);
                    Toast.makeText(ActivityPhong1.this, resp.toString(), Toast.LENGTH_SHORT).show();
                    onStart();
                } catch (Exception ex) {
                    Toast.makeText(ActivityPhong1.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText txtID = (EditText) findViewById(R.id.txtID);
                EditText txtName = (EditText) findViewById(R.id.txtName);
                EditText txtGiaTien = (EditText) findViewById(R.id.txtGiaTien);
                EditText txtSoNKNuoc = (EditText) findViewById(R.id.txtSoNKNuoc);
                EditText txtChiSoDien = (EditText) findViewById(R.id.txtChiSoDien);
                EditText txtChiSoNuoc = (EditText) findViewById(R.id.txtChiSoNuoc);
                EditText txtNgayThue = (EditText) findViewById(R.id.txtNgayThue);
                CheckBox chkThue = (CheckBox) findViewById(R.id.chkThue);

                String ID = ((TextView) view.findViewById(R.id.lvID)).getText().toString();
                if (tbPhong != null) {
                    for (int i = 0; i < tbPhong.getPropertyCount(); i++) {
                        SoapObject obj = (SoapObject) tbPhong.getProperty(i);
                        if (obj.getProperty("ID").toString() == ID) {
                            txtID.setText(obj.getProperty("ID").toString());
                            txtName.setText(obj.getProperty("Name").toString());
                            txtGiaTien.setText(obj.getProperty("GiaTien").toString());
                            txtSoNKNuoc.setText(obj.getProperty("SoNKNuoc").toString());
                            txtChiSoDien.setText(obj.getProperty("ChiSoDien").toString());
                            txtChiSoNuoc.setText(obj.getProperty("ChiSoNuoc").toString());
                            if (obj.getProperty("NgayThue").toString() != "anyType{}")
                                txtNgayThue.setText(obj.getProperty("NgayThue").toString());
                            if (Boolean.parseBoolean(obj.getProperty("Thue").toString()) == true)
                                chkThue.setChecked(true);
                            else
                                chkThue.setChecked(false);
                            break;
                        }

                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
//        EditText txtID = (EditText) findViewById(R.id.txtID);
//        txtID.setText("");
//        EditText txtName = (EditText) findViewById(R.id.txtName);
//        txtName.setText("");
//        EditText txtGiaTien = (EditText) findViewById(R.id.txtGiaTien);
//        txtGiaTien.setText("");

        tbPhong = ws.GetDSPhong();
        ArrayList<lvEntity> list = new ArrayList<lvEntity>();

        if (tbPhong != null) {
            for (int i = 0; i < tbPhong.getPropertyCount(); i++) {
                SoapObject obj = (SoapObject) tbPhong.getProperty(i);
                lvEntity temp = new lvEntity();
                temp.setID(obj.getProperty("ID").toString());
                temp.setName(obj.getProperty("Name").toString());
                String str = obj.getProperty("GiaTien").toString();
                str += " - Chỉ Số Điện:" + obj.getProperty("ChiSoDien").toString();
                str += " - Chỉ Số Nước:" + obj.getProperty("ChiSoNuoc").toString();
                list.add(temp);
            }

            ListView listView = (ListView) findViewById(R.id.listView);
            lvAdapter adapter = new lvAdapter(this, list);
            listView.setAdapter(adapter);
        }
        super.onStart();
    }
}
