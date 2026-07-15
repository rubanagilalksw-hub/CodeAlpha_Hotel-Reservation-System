package hotel.dao;

import java.io.*;
import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private static final String CONFIG_FILE = "hotel_data/config.properties";
    private Properties props = new Properties();

    private AppConfig() {
        new File("hotel_data").mkdirs();
        load();
    }

    public static AppConfig getInstance() {
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    private void load() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (Exception ignored) {
            // defaults
            props.setProperty("hotel.name",    "Grand Palace Hotel");
            props.setProperty("hotel.phone",   "0300-1234567");
            props.setProperty("hotel.email",   "info@grandpalace.com");
            props.setProperty("hotel.address", "123 Main Street, Karachi");
            props.setProperty("currency",      "PKR - Pakistani Rupee");
            props.setProperty("language",      "English");
            props.setProperty("auto.backup",   "true");
            props.setProperty("notify",        "true");
            props.setProperty("sound",         "false");
            props.setProperty("admin.password","admin");
            props.setProperty("admin.username","admin");
        }
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Hotel Reservation System Config");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public String get(String key) { return props.getProperty(key, ""); }
    public void   set(String key, String value) { props.setProperty(key, value); }

    public String getAdminPassword() { return props.getProperty("admin.password", "admin"); }
    public void   setAdminPassword(String p) { props.setProperty("admin.password", p); save(); }

    public String getAdminUsername() { return props.getProperty("admin.username", "admin"); }
    public void   setAdminUsername(String u) { props.setProperty("admin.username", u); save(); }

    public String getHotelName()    { return get("hotel.name"); }
    public String getHotelPhone()   { return get("hotel.phone"); }
    public String getHotelEmail()   { return get("hotel.email"); }
    public String getHotelAddress() { return get("hotel.address"); }
    public String getCurrency()     { return get("currency"); }
    public String getLanguage()     { return get("language"); }
    public boolean isAutoBackup()   { return "true".equals(get("auto.backup")); }
    public boolean isNotify()       { return "true".equals(get("notify")); }
    public boolean isSound()        { return "true".equals(get("sound")); }
}
