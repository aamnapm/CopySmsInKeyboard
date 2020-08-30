package ir.aamnapm.copysmsonkeyboard

import android.content.*
import androidx.appcompat.app.AppCompatActivity


class SMSListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var msg: String? = SMSExtractor(intent).build();

        if (msg != null) {

            var clipboardManager: ClipboardManager =
                context!!.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager

            var clip: ClipData = ClipData.newPlainText("label", msg)
            clipboardManager.setPrimaryClip(clip)


            var intent = Intent()
            intent.putExtra("DATA", msg)
            intent.action = "SMS"; context.sendBroadcast(intent);
        }

    }
}