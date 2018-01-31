package vn.com.abc.phongtro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Set;

public class ActivityBluetooth extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;

    BluetoothAdapter bluetoothAdapter;
    String ID = "";

    private Connection printerConnection;
    private ZebraPrinter printer;
    private EditText txtNoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        txtNoiDung = (EditText) findViewById(R.id.txtNoiDung);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBlueToothState();

        ListView lstView = (ListView) findViewById(R.id.lstView);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ID = ((TextView) view.findViewById(R.id.lvID)).getText().toString();
                Toast.makeText(ActivityBluetooth.this, ID, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnIn = (Button) findViewById(R.id.btnIn);
        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID == "") {
                    return;
                }
                printer = connect();
                if (printer != null) {
                    sendTestLabel();
                } else {
                    disconnect();
                }
            }
        });
    }

    private void CheckBlueToothState() {
        if (bluetoothAdapter == null) {
            Toast.makeText(ActivityBluetooth.this, "Bluetooth NOT support", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    Toast.makeText(ActivityBluetooth.this, "Bluetooth is currently in device discovery process.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityBluetooth.this, "Bluetooth is Enabled.", Toast.LENGTH_SHORT).show();
                    ArrayList<lvEntity> list = new ArrayList<lvEntity>();

                    BluetoothAdapter bluetoothAdapter
                            = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> pairedDevices
                            = bluetoothAdapter.getBondedDevices();

                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            lvEntity temp = new lvEntity();
                            String deviceBTName = device.getName();
                            String deviceBTMAC = device.getAddress();
                            String deviceBTMajorClass
                                    = getBTMajorDeviceClass(device
                                    .getBluetoothClass()
                                    .getMajorDeviceClass());

                            temp.setID(deviceBTMAC);
                            temp.setName(deviceBTName);
                            temp.setContent(deviceBTMajorClass);
                            list.add(temp);
                        }
                    }
                    ListView lstView = (ListView) findViewById(R.id.lstView);
                    lvAdapter adapter = new lvAdapter(this, list);
                    lstView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(ActivityBluetooth.this, "Bluetooth is NOT Enabled!", Toast.LENGTH_SHORT).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
        if (requestCode == REQUEST_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    private String getBTMajorDeviceClass(int major) {
        switch (major) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass.Device.Major.WEARABLE:
                return "AUDIO_VIDEO";
            default:
                return "unknown!";
        }
    }

    public ZebraPrinter connect() {
        printerConnection = null;
        printerConnection = new BluetoothConnection(ID);

        try {
            printerConnection.open();
        } catch (ConnectionException e) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                PrinterLanguage pl = printer.getPrinterControlLanguage();
            } catch (ConnectionException e) {
                printer = null;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                printer = null;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (ConnectionException e) {
        } finally {
        }
    }

    private void sendTestLabel() {
        try {
            byte[] configLabel = getConfigLabel();
            printerConnection.write(configLabel);
            Thread.sleep(1500);
            if (printerConnection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                Thread.sleep(500);
            }
        } catch (ConnectionException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    *
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    *
    *
    */
    private byte[] getConfigLabel() {
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            String str="^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FH^FD"+txtNoiDung.getText()+"^FS^XZ";
            configLabel = str.getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 "+txtNoiDung.getText()+"\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;
    }
}
