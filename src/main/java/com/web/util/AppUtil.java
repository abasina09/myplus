/**
 * 
 */
package com.web.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persistence.model.Geolocation;
import com.service.IGeoLocationService;

/**
 * @author sabbasi
 *
 */
@Component
public class AppUtil {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
	
	@Autowired
	IGeoLocationService geoLocationService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static Map<String,String> countryMap=null;


    @NonNull
    public String toJson(@org.springframework.lang.Nullable Object object) {
        try {
            return object == null ? "null" : OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(@NonNull String json, @NonNull Type type) {
        JavaType javaType = OBJECT_MAPPER.constructType(type);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
   	//Get current date time
    public static String todayDateStr() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(AppUtil.dateformatter);
    }

   	//Get current date time
    public static String getDateStr(LocalDateTime date) {
    	if(date==null)
    		return "";
    	return dateformatter.format(date);
    }

   	//Get current date time
    public static String getLoaclDateStr(LocalDate date) {
    	if(date==null)
    		return "";
    	return dateformatter.format(date);
    }

    //Get current date time
    public static String getLocalDateStr(LocalDate date) {
    	if(date==null)
    		return "";
    	return dateformatter.format(date);
    }

   	//Get current date time
    public static Date getDate(String date) throws ParseException {
    	if(date==null)
    		return new Date();
    	return dateFormat.parse(date);
    }

   	//Get current date time
    public static LocalDateTime getDateTime(String dateStr) throws ParseException {
    	if(dateStr==null)
    		return LocalDateTime.now();
    	LocalDateTime dateTime = LocalDateTime.parse(dateStr, dateformatter);
    	return dateTime;
    }
   	//Get current date
    public static LocalDate getLocalDate(String dateStr) throws ParseException {
    	if(dateStr==null)
    		return LocalDate.now();
    	LocalDate dateTime = LocalDate.parse(dateStr, dateformatter);
    	return dateTime;
    }
   	//Get date time str
    public static String getDateTimeStr(LocalDateTime dateTime){
    	if(dateTime==null)
    		return "";
        return dateTimeFormatter.format(dateTime);
    }

    //Get current date time
    public static String todayDateTimeStr() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(AppUtil.dateTimeFormatter);
    }

    @PostConstruct
    void init() {
    	try {
			List<Geolocation> countries =  geoLocationService.loadCountries();
			if(countries==null)
				return;
			LOGGER.info("Contries fetched "+countries.size());
			countryMap = new HashMap<String, String>();
			countries.forEach(c ->{
				countryMap.put(c.getCountryCode(),c.getCountry());
			});
			LOGGER.info("Contries availabe in map  "+countryMap.size());
    	}catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.info("Contries fetching issues  "+this.getClass().getName()+" : "+e.getCause());
    	}
    }

  
	public static boolean isEmptyOrNull( Collection<?> collection ){
		if( collection == null || collection.isEmpty() ){
			return true;
		}
		return false;
	}

	/**
	 * This method returns true of the map is null or is empty.
	 * @param map
	 * @return true | false
	 */
	public boolean isEmptyOrNull( Map<?, ?> map ){
		if( map == null || map.isEmpty() ){
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the objet is null.
	 * @param object
	 * @return true | false
	 */
	public static boolean isEmptyOrNull( Object object ){
		if( object == null ){
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the input array is null or its length is zero.
	 * @param array
	 * @return true | false
	 */
	public boolean isEmptyOrNull( Object[] array ){
		if( array == null || array.length == 0 ){
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the input string is null or its length is zero.
	 * @param string
	 * @return true | false
	 */
	public static boolean isEmptyOrNull( String string ){
		if( string == null || string.trim().length() == 0 ){
			return true;
		}
		return false;
	}    
   
}
