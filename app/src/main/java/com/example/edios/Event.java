package com.example.edios;

import java.util.ArrayList;
import java.util.HashMap;

abstract class Event {

    public abstract void setAttributes(ArrayList<String> attr);

    public abstract HashMap<String, String> getAttributes();
}
