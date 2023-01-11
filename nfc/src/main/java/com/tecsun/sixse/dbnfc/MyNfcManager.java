package com.tecsun.sixse.dbnfc;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

/**
 * 用于NFC配置
 */
public class MyNfcManager {
    public static NfcAdapter enableReaderMode(final Activity activity, final NfcListener listener) {
        try {
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
            if (nfcAdapter == null) {
                if (listener != null) {
                    listener.onNfcError(false);
                }
                return null;
            }
            if (!nfcAdapter.isEnabled()) {
                if (listener != null) {
                    listener.onNfcError(true);
                }
                return null;
            }
            Bundle options = new Bundle();
            //对卡片的检测延迟300ms
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 300);
            int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_NFC_V | NfcAdapter.FLAG_READER_NFC_F
                    | NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_BARCODE;
            nfcAdapter.enableReaderMode(activity, new NfcAdapter.ReaderCallback() {

                @Override
                public void onTagDiscovered(final Tag tag) {
                    if (listener != null) {
                        listener.onNfcEvent(tag);
                    }
                }
            }, READER_FLAGS, options);
            return nfcAdapter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NfcAdapter enableReaderMode(final Activity activity) {
        return enableReaderMode(activity, null);
    }


    public static void disableReaderMode(Activity activity, NfcAdapter nfcAdapter) {
        try {
            if (nfcAdapter != null) {
                nfcAdapter.disableReaderMode(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
