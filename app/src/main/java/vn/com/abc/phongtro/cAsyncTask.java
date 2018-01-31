package vn.com.abc.phongtro;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by user on 09/01/2018.
 */

public class cAsyncTask extends AsyncTask<String,Void,Object> {
    WebService ws=new WebService();
    ProgressDialog progressDialog;

    public cAsyncTask(MainActivity activity)
    {
        progressDialog=new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Thông Báo");
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
    }

    @Override
    protected Object doInBackground(String... strings) {
        switch (strings[0]) {
            case "Phong":
                return ws.GetDSPhong();
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        progressDialog.dismiss();
    }
}
