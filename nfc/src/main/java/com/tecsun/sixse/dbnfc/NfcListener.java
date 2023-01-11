package com.tecsun.sixse.dbnfc;

import android.nfc.Tag;

public interface NfcListener {
    void onNfcEvent(Tag tag);

    void onNfcError(boolean has);
}
