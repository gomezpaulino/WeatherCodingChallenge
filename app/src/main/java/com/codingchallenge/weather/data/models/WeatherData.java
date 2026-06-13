package com.codingchallenge.weather.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherData {
    @SerializedName("name") private String cityName;
    @SerializedName("state") private String state;
    @SerializedName("main") private Main main;
    @SerializedName("weather") private List<Weather> weather;
    @SerializedName("wind") private Wind wind;
    @SerializedName("sys") private Sys sys;
    @SerializedName("visibility") private int visibility;
    @SerializedName("dt") private long timestamp;

    public String getCityName() {
        return cityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public int getVisibility() {
        return visibility;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public WeatherData() {}

    public WeatherData(String cityName, Main main, List<Weather> weather) {
        this.cityName = cityName;
        this.main = main;
        this.weather = weather;
    }

    public static class Main {
        @SerializedName("temp") private double temperature;
        @SerializedName("feels_like") private double feelsLike;
        @SerializedName("temp_min") private double tempMin;
        @SerializedName("temp_max") private double tempMax;
        @SerializedName("humidity") private int humidity;

        public Main() {}

        public Main(double temperature, double feelsLike, double tempMin, double tempMax, int humidity) {
            this.temperature = temperature;
            this.feelsLike = feelsLike;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.humidity = humidity;
        }

        public double getTemperature() {
            return temperature;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public double getTempMin() {
            return tempMin;
        }

        public double getTempMax() {
            return tempMax;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Wind {
        @SerializedName("speed") private double speed;

        public Wind() {}

        public Wind(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    public static class Sys {
        @SerializedName("sunrise") private long sunrise;
        @SerializedName("sunset") private long sunset;

        public Sys() {}

        public Sys(long sunrise, long sunset) {
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }
    }

    public static class Weather {
        @SerializedName("main") private String main;

        @SerializedName("description") private String description;

        @SerializedName("icon") private String icon;

        public Weather() {}

        public Weather(String main, String description, String icon) {
            this.main = main;
            this.description = description;
            this.icon = icon;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}
