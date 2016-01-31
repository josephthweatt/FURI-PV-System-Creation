import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class BestCaseWithAPI {
	final static private String apiSite = "http://api.sunrise-sunset.org/";
	final static private String format = "json?";
	final private static String resultKey = "results";
	final private static String sunriseKey = "sunrise";
	final private static String sunsetKey = "sunset";
	
	private static String latitude, longitude, date; //three JSON variables
	private static String sunrise, sunset;
	private static double sunriseDbl, sunsetDbl;
	private static double daylightHours;
	
	//vars to find the average daylight hours
	private static double totalDaylight;
	private static int year;
	private static int[] daysInMonth = {31,28,31,30,31,30,31,31,30,31,30,31};

	public static void main(String[] args) throws IOException, JSONException {
		getDaylightHours();
		
		System.out.println(daylightHours);
	}

	public static void getDaylightHours() throws IOException, JSONException {
		latitude = "lat=" + 33.45; //az lat
		longitude = "lng=" + 112.0667; //az long
		
		//material used for getting the date
		year = 2016; //lets just assume this, I'll make it changeable if the concept works.
		if ((year % 4 == 0) && (year % 100 == year % 400)) {
			daysInMonth[1]++;
		}
		
		//now the calls
		for (int i = 1; i <= 365; i++) {
			date = "date=" + getDayOfYear(i);
			String url = apiSite + format + latitude + "&"+ longitude + "&" + date;
			String source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
			JSONObject mainObject = new JSONObject(source);
			mainObject = mainObject.getJSONObject(resultKey);
			sunrise = mainObject.getString(sunriseKey);
			sunset = mainObject.getString(sunsetKey);
			
			String[] dubs = sunrise.split(":");	
			sunriseDbl = Double.parseDouble(dubs[0]) + Double.parseDouble(dubs[1]) / 60;
			
			dubs = sunset.split(":");
			sunsetDbl = Double.parseDouble(dubs[0]) + Double.parseDouble(dubs[1]) / 60;
			if (sunsetDbl < 12) {
				sunsetDbl += 12;
			}
			
			daylightHours = sunsetDbl - sunriseDbl;
			totalDaylight += daylightHours;
			System.out.println("finished " + date);
		}
		System.out.println(totalDaylight);
	}
		
	public static String getDayOfYear(int day) {
		for (int i = 0; i < 12; i++) {
			if (day - daysInMonth[i] >= 0) {
				day -= daysInMonth[i];
			} else {
				return year + "-" + (i + 1) + "-" + day;
			}
		}
		return year + "-" + 12 + "-00"; //for december
	}
}
