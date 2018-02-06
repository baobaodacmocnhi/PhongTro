package vn.com.abc.phongtro;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class ActivityPhongDanhSach extends Fragment {
    private View rootView;
    private Button btnXem;
    private SoapObject tbPhong;
    private ListView lstView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_activity_phong_danh_sach, container, false);

        btnXem = (Button) rootView.findViewById(R.id.btnXem);
        lstView = (ListView) rootView.findViewById(R.id.lstView);

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new String[]{"Phong"});


            }
        });

        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, SoapObject, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        WebService ws = new WebService();

        @Override
        protected Void doInBackground(String... strings) {
            switch (strings[0]) {
                case "Phong":
                    publishProgress(ws.GetDSPhong());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(SoapObject... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                tbPhong = values[0];
                ArrayList<lvEntity> list = new ArrayList<lvEntity>();
                for (int i = 0; i < tbPhong.getPropertyCount(); i++) {
                    SoapObject obj = (SoapObject) tbPhong.getProperty(i);
                    lvEntity temp = new lvEntity();
                    temp.setID(obj.getProperty("ID").toString());
                    temp.setName(obj.getProperty("Name").toString());
                    String str = obj.getProperty("GiaTien").toString();
                    str += " - Chỉ Số Điện:" + obj.getProperty("ChiSoDien").toString();
                    str += " - Chỉ Số Nước:" + obj.getProperty("ChiSoNuoc").toString();
                    temp.setContent(str);
                    list.add(temp);
                }

                ListView listView = (ListView) rootView.findViewById(R.id.lstView);
                lvAdapter adapter = new lvAdapter(getActivity(), list);
                listView.setAdapter(adapter);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

}
