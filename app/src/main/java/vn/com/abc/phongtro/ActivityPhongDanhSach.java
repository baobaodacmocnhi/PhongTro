package vn.com.abc.phongtro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ActivityPhongDanhSach extends Fragment {
    private View rootView;
private Button btnXem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_activity_phong_danh_sach, container, false);
        return  rootView;
    }

@Override
public void onStart() {
    super.onStart();

    btnXem=(Button)rootView.findViewById(R.id.btnXem);

    btnXem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        }
    });
}



}
