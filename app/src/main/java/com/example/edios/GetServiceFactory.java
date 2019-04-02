package com.example.edios;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class GetServiceFactory {
    public Service getService(String service_name) throws Exception{
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader("Services.json"));

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        /* getting the classname of the required class */
        String service_class = (String) jo.get(service_name);
        Class clazz = Class.forName(service_class);
        return (Service) clazz.newInstance();
    }
}
