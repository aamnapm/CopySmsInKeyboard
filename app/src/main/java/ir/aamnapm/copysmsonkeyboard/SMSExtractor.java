package ir.aamnapm.copysmsonkeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSExtractor {

    private Intent intent;

    public SMSExtractor(Intent intent) {
        this.intent = intent;
    }

    public String build() {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        return findCode(msgs[i].getMessageBody());
                    }
                } catch (Exception e) {
                    return null;
                }
            } else return null;
        }
        return null;
    }

    private String findCode(String sms) {
        if (sms.contains("ملي")) {
            return melli(sms);
        } else if (sms.contains("پاسارگاد")) {
            return pasargad(sms);
        } else if (sms.contains("پارسیان")) {
            return parsian(sms);
        } else if (sms.contains("محرمانه\n" + "رمز برداشت وجه از کارت شما")) {
            return mellat(sms);
        } else return null;
    }

    private String pasargad(String sms) {
        return sms.split("code:")[1].replaceAll("\\s+", "");
    }

    private String melli(String sms) {
        return sms.split("رمزدوم:")[1].split("اعتبارتا:")[0].replaceAll("\\s+", "");
    }

    private String mellat(String sms) {
        return sms.split("محرمانه\n" + "رمز برداشت وجه از کارت شما:")[1].split("اعتبار:")[0].replaceAll("\\s+", "");
    }

    private String parsian(String sms) {
        return sms.split("رمز یکبارمصرف:")[1].split("مهلت")[0].replaceAll("\\s+", "");
    }
}
