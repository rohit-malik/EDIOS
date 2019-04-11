package com.example.edios;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceAlarm extends Service {
    private String alarm_message = new String();
    private String ringtone = new String();
    private int ringtone_volume;

    @Override
    public void setAttributes(ArrayList<String> attr) {
        alarm_message = attr.get(0);
        ringtone = attr.get(1);
        ringtone_volume = Integer.parseInt(attr.get(2));
    }

    @Override
    public HashMap<String, String> getAttributes() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("alarm_message" ,alarm_message);
        mp.put("ringtone" ,ringtone);
        mp.put("ringtone_volume", String.valueOf(ringtone_volume));
        return mp;
    }
}
