package com.example.edios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private GetEventFactory eventFactory = new GetEventFactory();
    private GetServiceFactory serviceFactory = new GetServiceFactory();
    private ArrayList<Event> event_list = new ArrayList<>();
    private ArrayList<Service> service_list = new ArrayList<>();

    public void setEvent(String event_name, ArrayList<String> attr){
        try {
            Event new_event = eventFactory.getEvent(event_name);
            new_event.setAttributes(attr);
            event_list.add(new_event);
        }catch (Exception exe){
            System.out.print(exe);
        }
    }

    public void setService(String service_name, ArrayList<String> attr){
        try {
            Service new_service = serviceFactory.getService(service_name);
            new_service.setAttributes(attr);
            service_list.add(new_service);
        }catch (Exception exe){
            System.out.print(exe);
        }
    }


    public void insertDB(){

        HashMap<String, String> event_map = new HashMap<>();
        for(int i=0;i<event_list.size();i++){
            Event event = event_list.get(i);
            HashMap<String,String> temp_map= event.getAttributes();
            Map tmp = new HashMap(temp_map);
            tmp.keySet().removeAll(event_map.keySet());
            event_map.putAll(tmp);
        }

        HashMap<String, String> service_map = new HashMap<>();
        for(int i=0;i<service_list.size();i++){
            Service service = service_list.get(i);
            HashMap<String,String> temp_map= service.getAttributes();
            Map tmp = new HashMap(temp_map);
            tmp.keySet().removeAll(service_map.keySet());
            service_map.putAll(tmp);
        }

    }
}
