package com.example.edios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Long.*;

public class EventTime extends Event {
    private Date date = new Date();

    @Override
    public void setAttributes(ArrayList<String> attr) {
        date.setTime(parseLong(attr.get(0)));
    }

    @Override
    public HashMap<String, String> getAttributes() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("date_time", String.valueOf(date));
        return mp;
    }
}
