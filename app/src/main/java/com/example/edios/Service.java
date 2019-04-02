package com.example.edios;

import java.util.ArrayList;
import java.util.HashMap;

abstract class Service {

    public abstract void setAttributes(ArrayList<String> attr);

    public abstract HashMap<String, String> getAttributes();
}
