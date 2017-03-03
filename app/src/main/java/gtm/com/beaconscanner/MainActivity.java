package gtm.com.beaconscanner;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconData;
import org.altbeacon.beacon.BeaconDataNotifier;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.client.DataProviderException;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends Activity implements BeaconDataNotifier, BeaconConsumer, RangeNotifier, MonitorNotifier {


    private BeaconManager mBeaconManager;
    String TAG = MainActivity.class.getName();
    ListView mScannedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Button mBtnScan = (Button) findViewById(R.id.id_scan_button);
        mScannedDevices = (ListView) findViewById(R.id.id_devices_lv);


        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
       /* mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));*/

        mBeaconManager.bind(this);
    }

    @Override
    public void beaconDataUpdate(Beacon beacon, BeaconData beaconData, DataProviderException e) {


    }

    @Override
    public void onBeaconServiceConnect() {

        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addRangeNotifier(this);
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.i("Main", "I detected a beacon in the region with namespace id " + region.getId1() +
                " and instance id: " + region.getId2());
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {


        Log.i("Main", "didDetermineStateForRegion: " + region.getBluetoothAddress());
    }


    @Override
    protected void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        final ArrayList mlist = new ArrayList();
        mlist.add(collection);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter mAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mlist);
                mScannedDevices.setAdapter(mAdapter);

            }
        });

        for (Beacon beacon : collection) {


           // Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getId1() + "----" + beacon.getId2() + "---" + beacon.getId3());
            Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getServiceUuid());
           /* Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getBluetoothName());
            Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getDistance());
            Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getManufacturer());
            Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getParserIdentifier());
            Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getServiceUuid());*/
           /* if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                Log.d(TAG, "I see a beacon transmitting namespace id: " + namespaceId +
                        " and instance id: " + instanceId +
                        " approximately " + beacon.getDistance() + " meters away.");

                // Do we have telemetry data?
                if (beacon.getExtraDataFields().size() > 0) {
                    long telemetryVersion = beacon.getExtraDataFields().get(0);
                    long batteryMilliVolts = beacon.getExtraDataFields().get(1);
                    long pduCount = beacon.getExtraDataFields().get(3);
                    long uptime = beacon.getExtraDataFields().get(4);

                    Log.d(TAG, "The above beacon is sending telemetry version " + telemetryVersion +
                            ", has been up for : " + uptime + " seconds" +
                            ", has a battery level of " + batteryMilliVolts + " mV" +
                            ", and has transmitted " + pduCount + " advertisements.");
                }
            } else {
                Log.i(TAG, "didRangeBeaconsInRegion: " + beacon.getManufacturer());
            }*/
        }
    }
}
