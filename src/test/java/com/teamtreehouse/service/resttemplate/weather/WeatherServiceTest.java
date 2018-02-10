package com.teamtreehouse.service.resttemplate.weather;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import com.teamtreehouse.config.AppConfig;
import com.teamtreehouse.service.WeatherService;
import com.teamtreehouse.service.dto.geocoding.Location;
import com.teamtreehouse.service.dto.weather.Weather;
import java.time.Duration;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class WeatherServiceTest {

  public static final double LATITUDE = 41.940375;
  public static final double LONGITUDE = -87.653180499999;
  @Autowired
  private WeatherService service;

  private Location location;
  private Weather weather;

  private static final double ERROR_GEO = 0.000001;
  private static final double ERROR_TIME = 5000;

  @Before
  public void setup() {
    location = new Location(LATITUDE, LONGITUDE);
    weather = service.findByLocation(location);
  }

  @Test
  public void findByLocation_ShouldReturnSameCoords() throws Exception {
    assertThat(weather.getLatitude(), closeTo(location.getLatitude(), ERROR_GEO));
    assertThat(weather.getLongitude(), closeTo(location.getLongitude(), ERROR_GEO));
  }

  @Test
  public void findByLocation_ShouldReturn8DaysForecastData() throws Exception {
    assertThat(weather.getDaily().getData(), hasSize(8));
  }

  @Test
  public void findByLocation_ShouldReturnCurrentConditions() throws Exception {
    Instant now = Instant.now();
    double duration = Duration.between(now, weather.getCurrently().getTime()).toMillis();
    assertThat(duration, closeTo(0, ERROR_TIME));
  }

  @Configuration
  @PropertySource("api.properties")
  public static class TestConfig {
    @Autowired
    private Environment environment;

    @Bean
    public RestTemplate restTemplate() {
      return AppConfig.defaultRestTemplate();
    }

    @Bean
    public WeatherService weatherService() {
      WeatherService service = new WeatherServiceImpl(
          environment.getProperty("weather.api.name"),
          environment.getProperty("weather.api.key"),
          environment.getProperty("weather.api.host")
      );
      return service;
    }
  }
}