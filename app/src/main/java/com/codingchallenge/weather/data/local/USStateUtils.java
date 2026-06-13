package com.codingchallenge.weather.data.local;

import java.util.HashMap;
import java.util.Map;

public class USStateUtils {
    private static final Map<String, String> STATE_MAP = new HashMap<>();

    static {
        STATE_MAP.put("Alabama", "AL");
        STATE_MAP.put("Alaska", "AK");
        STATE_MAP.put("Arizona", "AZ");
        STATE_MAP.put("Arkansas", "AR");
        STATE_MAP.put("California", "CA");
        STATE_MAP.put("Colorado", "CO");
        STATE_MAP.put("Connecticut", "CT");
        STATE_MAP.put("Delaware", "DE");
        STATE_MAP.put("Florida", "FL");
        STATE_MAP.put("Georgia", "GA");
        STATE_MAP.put("Hawaii", "HI");
        STATE_MAP.put("Idaho", "ID");
        STATE_MAP.put("Illinois", "IL");
        STATE_MAP.put("Indiana", "IN");
        STATE_MAP.put("Iowa", "IA");
        STATE_MAP.put("Kansas", "KS");
        STATE_MAP.put("Kentucky", "KY");
        STATE_MAP.put("Louisiana", "LA");
        STATE_MAP.put("Maine", "ME");
        STATE_MAP.put("Maryland", "MD");
        STATE_MAP.put("Massachusetts", "MA");
        STATE_MAP.put("Michigan", "MI");
        STATE_MAP.put("Minnesota", "MN");
        STATE_MAP.put("Mississippi", "MS");
        STATE_MAP.put("Missouri", "MO");
        STATE_MAP.put("Montana", "MT");
        STATE_MAP.put("Nebraska", "NE");
        STATE_MAP.put("Nevada", "NV");
        STATE_MAP.put("New Hampshire", "NH");
        STATE_MAP.put("New Jersey", "NJ");
        STATE_MAP.put("New Mexico", "NM");
        STATE_MAP.put("New York", "NY");
        STATE_MAP.put("North Carolina", "NC");
        STATE_MAP.put("North Dakota", "ND");
        STATE_MAP.put("Ohio", "OH");
        STATE_MAP.put("Oklahoma", "OK");
        STATE_MAP.put("Oregon", "OR");
        STATE_MAP.put("Pennsylvania", "PA");
        STATE_MAP.put("Rhode Island", "RI");
        STATE_MAP.put("South Carolina", "SC");
        STATE_MAP.put("South Dakota", "SD");
        STATE_MAP.put("Tennessee", "TN");
        STATE_MAP.put("Texas", "TX");
        STATE_MAP.put("Utah", "UT");
        STATE_MAP.put("Vermont", "VT");
        STATE_MAP.put("Virginia", "VA");
        STATE_MAP.put("Washington", "WA");
        STATE_MAP.put("West Virginia", "WV");
        STATE_MAP.put("Wisconsin", "WI");
        STATE_MAP.put("Wyoming", "WY");
    }

    public static String getAbbreviation(String stateName) {
        if (stateName == null) return null;
        String abbrev = STATE_MAP.get(stateName);
        return abbrev != null ? abbrev : stateName;
    }
}
