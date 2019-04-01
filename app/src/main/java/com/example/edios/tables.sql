CREATE TABLE IF NOT EXISTS events (
 event_id INTEGER PRIMARY KEY,
 event_name TEXT NOT NULL,
 battery_level INTEGER,
 caller_number TEXT,
 call_time NUMERIC,
 location TEXT,
 FOREIGN KEY(event_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE IF NOT EXISTS services (
 service_id INTEGER PRIMARY KEY,
 service_name TEXT NOT NULL,
 wifi_status NUMERIC,
 gps_status NUMERIC,
 sync NUMERIC,
 bluetooth_status NUMERIC,
 ip_address TEXT,
 http_data TEXT,
 date_time NUMERIC,
 alarm_message TEXT,
 ringtone TEXT,
 ringtone_level INTEGER,
 media_level INTEGER,
 alarm_level INTEGER,
 FOREIGN KEY(service_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE IF NOT EXISTS recipe (
 recipe_id INTEGER PRIMARY KEY autoincrement NOT null,
 log_id INTEGER NOT NULL,
 event_list TEXT,
 service_list TEXT
);

CREATE TABLE IF NOT EXISTS logs (
 log_id INTEGER PRIMARY KEY NOT NULL,
 log_title TEXT NOT NULL,
 log_description TEXT,
 FOREIGN KEY(log_id) REFERENCES recipe(recipe_id)
);