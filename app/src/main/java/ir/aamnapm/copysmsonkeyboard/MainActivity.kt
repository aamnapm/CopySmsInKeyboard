package ir.aamnapm.copysmsonkeyboard

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PermissionListener {

    val TAG_PERMISSION = "Permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnStart = findViewById<Button>(R.id.btnStartService)
        var btnStop = findViewById<Button>(R.id.btnStopService)
        var txt = findViewById<TextView>(R.id.txt)

        btnStart.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.RECEIVE_SMS)
                .withListener(this)
                .check();
        }

        btnStop.setOnClickListener {
            Intent(this, ForegroundService::class.java).also { intent ->
                stopService(intent)
                txt.text = getString(R.string.service_is_not_running)
            }
        }

    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        Log.i(TAG_PERMISSION, "You have given the necessary permission")
        Toast.makeText(this, "You have given the necessary permission", Toast.LENGTH_LONG).show()
        Intent(this, ForegroundService::class.java).also { intent ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else startService(intent)

            txt.text = getString(R.string.service_is_running)
        }
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        Log.i(TAG_PERMISSION, "Give the necessary permission")
        Toast.makeText(this, "Give the necessary permission", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Log.i(TAG_PERMISSION, "Give the necessary permission")
        Toast.makeText(this, "Give the necessary permission", Toast.LENGTH_LONG).show()
    }
}