package com.example.edios;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

class GetEventFactory {
    public Event getEvent(String event_name) throws Exception{
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader("Events.json"));

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        /* getting the classname of the required class */
        String event_class = (String) jo.get(event_name);
        Class clazz = Class.forName(event_class);
        return (Event) clazz.newInstance();
    }
}
