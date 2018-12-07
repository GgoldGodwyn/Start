package com.app.fit;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.lang.ref.WeakReference;
import java.util.Set;

import github.chenupt.springindicator.SpringIndicator;
import me.drakeet.materialdialog.MaterialDialog;


public class SplashScreen extends AppCompatActivity {



    //USB CONNECTIONN START
    /*
     * Notifications from UsbService will be received here.
     */

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "FIT Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "FIT Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No FIT connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "FIT disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private UsbService usbService;

    R305 fitModule = new R305();


    //private MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
//            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };


    //USB CONNECTION ENDS


    String bVNPhoneString;


    CustomViewPager viewPager;
    MaterialDialog mMaterialDialog;
    MaterialEditText bVNPhoneEdit ;

    String TAG = "Result";

    ImageView imageViewConnect, imageViewDisconnect;
    TextView textViewConnect;
    boolean fingerPrint ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //mHandler = new MyHandler(this);

        viewPager = findViewById(R.id.view_pager);
        SpringIndicator springIndicator = findViewById(R.id.indicator);
        springIndicator.setOnTabClickListener(null);


        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        viewPager.fixScrollSpeed();



        // just set viewPager
        springIndicator.setViewPager(viewPager);


        View v = getLayoutInflater().inflate(R.layout.alert_pop, (RelativeLayout)findViewById(R.id.activity_dashboard_relative_alert_root), false);
        bVNPhoneEdit = v.findViewById(R.id.activity_pending_material_job_token);

        mMaterialDialog = new MaterialDialog(this)
                .setView(v)
                .setPositiveButton("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bVNPhoneString = bVNPhoneEdit.getText().toString().trim();
                        if (bVNPhoneString.isEmpty()){
                            showToastInMiddle("BVN cannot be empty");
                        } else {

                            if (isInternetAvailable()){
                                mMaterialDialog.dismiss();
                            } else {
                                showToastInMiddle("Check your network connectivity and try again");
                            }
                            mMaterialDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();

    }


    public void captureFinger(View v) {
        verifyPassword();
        GenImg();
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        int page = viewPager.getCurrentItem();
        View v = getLayoutInflater().inflate(R.layout.enter, (RelativeLayout)findViewById(R.id.enter_parent), false);
        MaterialEditText descriptionEdt = v.findViewById(R.id.activity_sign_up_material_name);
        MaterialEditText amountEdt = v.findViewById(R.id.activity_sign_up_material_password);

        savedInstanceState.putInt("page", page);
        savedInstanceState.putString("description", descriptionEdt.getText().toString());
        savedInstanceState.putString("amount", amountEdt.getText().toString());


        // Call the superclass to save the state of all the other controls in the view hierarchy
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the superclass to restore the state of all the other controls in the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore value of counters from saved stat
        View v = getLayoutInflater().inflate(R.layout.enter, (RelativeLayout)findViewById(R.id.enter_parent), false);
        MaterialEditText descriptionEdt = v.findViewById(R.id.activity_sign_up_material_name);
        MaterialEditText amountEdt = v.findViewById(R.id.activity_sign_up_material_password);

        viewPager.setCurrentItem(savedInstanceState.getInt("page"));
        descriptionEdt.setText(savedInstanceState.getString("description"));
        amountEdt.setText(savedInstanceState.getString("amount"));

    }


    public void showToastInMiddle(String message){
        Toast toast = Toast.makeText(SplashScreen.this, message, Toast.LENGTH_LONG);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public boolean isInternetAvailable(){

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }


    public void setConnected(boolean toggle){

        imageViewConnect =  findViewById(R.id.status_connected);
        imageViewDisconnect = findViewById(R.id.status_disconnected);
        textViewConnect = findViewById(R.id.status_indicator);

        if (toggle){

            textViewConnect.setText("Connected");
            imageViewConnect.setVisibility(View.VISIBLE);
            imageViewDisconnect.setVisibility(View.GONE);

        } else {
            textViewConnect.setText("Disconnected, Kindly connect a FIT fingerprint");
            imageViewConnect.setVisibility(View.GONE);
            imageViewDisconnect.setVisibility(View.VISIBLE);
    }
    }

    //FIT DEVICE COMMS START
    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
//    private static class MyHandler extends Handler {
//        private final WeakReference<SplashScreen> mActivity;
//
//        public MyHandler(SplashScreen activity) {
//            mActivity = new WeakReference<>(activity);
//        }
//
//        //String hex = String.format("%04x", (int) ch);
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UsbService.MESSAGE_FROM_SERIAL_PORT:
//                   // String data = (String) msg.obj;
//                    //mActivity.get().details.append(data);
//                    break;
//                case UsbService.CTS_CHANGE:
//                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
//                    break;
//                case UsbService.DSR_CHANGE:
//                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    }




    ////////////fingerprint
    public void verifyPassword() {
        if (usbService != null) {

            int[] packet = {R305.FINGERPRINT_VERIFYPASSWORD, R305.thePassword, 0, 0, 0};

            writePacket(R305.theAddress, R305.FINGERPRINT_COMMANDPACKET, 7, packet);// FINGERPRINT_COMMANDPACKET is indentifier

            int idx = 0;
            int[] freply = new int[100];

        }
    }
    boolean datRecieved;
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {

        @Override
        public void onReceivedData(byte[] arg0)
        {
            // Code here :)
            datRecieved=true;
        }

    };

    public byte GenImg() {
        if (usbService != null)
        {
            int[] packet = {R305.FINGERPRINT_GETIMAGE};
            writePacket(R305.theAddress, R305.FINGERPRINT_COMMANDPACKET, 3, packet);//
            int idx = 0;
            byte[] freply = new byte[250];


            while (usbService.serialPort.read(mCallback) < 1) ;
            while (usbService.serialPort.read(mCallback) > 0) {
                //serialPort1.ReadTimeout != 0 ||
                freply[idx] = (byte)usbService.serialPort.read(mCallback);
                idx++;
            }
            if(freply[0]!=(((R305.FINGERPRINT_STARTCODE)>>8)&0XFF) || freply[1]!=((R305.FINGERPRINT_STARTCODE)))
            {
                return 0; // BAD PACKET
            }
            if(freply[9]==(int)R305.FINGERPRINT_OK)
            {
                return 1;
            }
            else
                return 0 ; // BAD PACKET
        }

        return 0 ;
    }

    public  byte []UpImg() {
        if (usbService != null) {
            int[] packet = {R305.FINGERPRINT_BUFFER};
            writePacket(R305.theAddress, R305.FINGERPRINT_COMMANDPACKET, 3, packet);
            int idx = 0;
            byte[] freply = new byte[250];
            byte[] Template = new byte[289];
            //readTimeout= R305.FINGERPRINT_TIMEOUT;
            while (usbService.serialPort.read(mCallback) < 1) ;
            while (usbService.serialPort.read(mCallback) > 0) {
                while (idx != 16) {
                    freply[idx] = (byte) usbService.serialPort.read(mCallback);
                    idx++;
                }
                Template[idx-16] = (byte) usbService.serialPort.read(mCallback);
                idx++;
            }
            return Template;
        }
        return null;
    }




    public void writePacket(int Addr, int PacketCMD,int LEN, int[]packet){

        byte [] sc={((byte)((R305.FINGERPRINT_STARTCODE>>8)&0xff)),(byte)(R305.FINGERPRINT_STARTCODE & 0xff)};
        byte [] address={(byte)((Addr>>24) &0xff),(byte)((Addr>>16) &0xff),(byte)((Addr>>8) &0xff),(byte)(Addr&0xff)};//
        byte [] packetType ={(byte)(PacketCMD & 0xFF) };//
        byte [] len ={(byte)((LEN>>8) &0xff),(byte)(LEN &0xff)};//
        usbService.write(sc);
        usbService.write(address);
        usbService.write(packetType);
        usbService.write(len);
        int sum = PacketCMD;
        sum += LEN;
        for(int i= 0; i<LEN-2; i++){
            //dat8 = (int)packet[i];
            byte []sca = {(byte)packet[i]};    //sc = BitConverter.GetBytes(dat8);
            usbService.write(sca);             //serialPort1.write(sc,0,1);
            sum += (int)packet[i];             //sum += (ushort)packet[i];
        }

        byte []arr = {(byte)((sum>> 8) & 0xff),(byte)(sum & 0xff)};
        usbService.write(arr);
    }
    //FIT COMMS ENDS




}
