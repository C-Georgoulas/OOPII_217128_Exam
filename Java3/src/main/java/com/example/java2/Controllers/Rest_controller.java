package com.example.java2.Controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Handler.LocationsHandler;
import com.example.Handler.TravellerRecordsJson;
import com.example.java2.Entities.Business;
import com.example.java2.Entities.City;
import com.example.java2.Entities.Country;
import com.example.java2.Entities.Enums;
import com.example.java2.Entities.Tourist;
import com.example.java2.Entities.Traveller;
import com.example.java2.Entities.User;
import com.example.java2.Repositories.BusinessRepository;
import com.example.java2.Repositories.CityRepository;
import com.example.java2.Repositories.CountryRepository;
import com.example.java2.Repositories.TouristRepository;
import com.example.java2.Repositories.TravellerRepository;
import com.example.java2.Repositories.UserRepository;
import com.example.java2.RetrieveData.OpenData;
import com.example.java2.security.JwtUtils;
import com.example.java2.security.WebSecurityConfig;

@CrossOrigin(origins = "*", maxAge = 3600)

@Import(WebSecurityConfig.class)
@RestController
@RequestMapping("/web/api")
public class Rest_controller {
	public static List<City> listofcities;
	@Autowired
	CityRepository cr;
	@Autowired
	UserRepository userrepo;
	@Autowired
	CountryRepository countryr;
	@Autowired
	TravellerRepository tr;
	@Autowired
	BusinessRepository br;
	@Autowired
	TouristRepository tour;

	@RequestMapping(value = "/SaveTraveller", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	@PreAuthorize("hasRole('USER')")
	public City CreateTraveller(@RequestBody Traveller traveller) throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();

		/*
		 * for (int i=0;i<cities.size(); i++) {
		 * LocationsHandler.CityCheck(cities.get(i).getName(),
		 * cr.findById(cities.get(i).getName()).size()>0, cr,
		 * Enums.LiveorVisitorNeither.neither);
		 * LocationsHandler.CountryCheck(cities.get(i),
		 * countryr.findById(cities.get(i).getCountryName()).size()>0, countryr,
		 * Enums.LiveorVisitorNeither.neither); }
		 */
		// City Visiting DB update
		String visit = traveller.CompareCities(cities);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		LocationsHandler.CountryCheck(visitCity, countryr.findById(visitCity.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving, countryr.findById(traveller.getCountryName()).size() > 0,
				countryr, Enums.LiveorVisitorNeither.live);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTravellerBasedOnWeather", method = RequestMethod.POST, produces = {
			"application/json", "application/xml" })
	public City CreateTravellerBasedOnWeather(HttpServletRequest request, @RequestBody Traveller traveller)
			throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();
		// City Visiting DB update
		String visit = traveller.CompareCities(cities, true);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving,
				countryr.findById(CityCurrentlyLiving.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.live);
		// tr.save(traveller);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveBusiness", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public City CreateBusiness(@RequestBody Business traveller) throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();
		// City Visiting DB update
		String visit = traveller.CompareCities(cities);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving,
				countryr.findById(CityCurrentlyLiving.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.live);
		// tr.save(traveller);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveBusinessBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public City CreateBusinessBasedOnWeather(@RequestBody Business traveller) throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();
		// City Visiting DB update
		String visit = traveller.CompareCities(cities, true);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving,
				countryr.findById(CityCurrentlyLiving.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.live);
		// tr.save(traveller);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTourist", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public City CreateTourist(@RequestBody Tourist traveller) throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();
		// City Visiting DB update
		String visit = traveller.CompareCities(cities);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving,
				countryr.findById(CityCurrentlyLiving.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.live);
		// tr.save(traveller);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTouristBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public City CreateTouristBasedOnWeather(@RequestBody Tourist traveller) throws IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < traveller.preferedCities.size(); i++) {
			String city = traveller.preferedCities.get(i);
			String[] arrofStr = city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0], 0, 0));
		}
		Country countryFound = null;
		ArrayList<City> citytmp = new ArrayList<>();
		Runnable runcities = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CityCheck(cities.get(i).getCityname(),
							cr.findById(cities.get(i).getName()).size() > 0, cr, Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runcountries = () -> {
			for (int i = 0; i < cities.size(); i++) {
				try {
					LocationsHandler.CountryCheck(cities.get(i),
							countryr.findById(cities.get(i).getCountryName()).size() > 0, countryr,
							Enums.LiveorVisitorNeither.neither);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread threadcities = new Thread(runcities);
		Thread threadcountries = new Thread(runcountries);
		threadcities.start();
		threadcountries.start();
		// City Visiting DB update
		String visit = traveller.CompareCities(cities, true);
		City visitCity = null;
		visitCity = LocationsHandler.CityCheck(visit, cr.findById(visit).size() > 0, cr,
				Enums.LiveorVisitorNeither.visit);
		// City Living
		traveller.setVisit(visit);
		String CityCurrentlyLivingString = traveller.getCity();
		City CityCurrentlyLiving = LocationsHandler.CityCheck(CityCurrentlyLivingString,
				cr.findById(CityCurrentlyLivingString).size() > 0, cr, Enums.LiveorVisitorNeither.live);
		// Country DB insert
		Country CountryCurrentlyLiving = null;
		String CountryCurrentlyLivingString = traveller.getCountryName();
		LocationsHandler.CountryCheck(CityCurrentlyLiving,
				countryr.findById(CityCurrentlyLiving.getCountryName()).size() > 0, countryr,
				Enums.LiveorVisitorNeither.live);
		// tr.save(traveller);
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		currentuser.setTravellers(traveller);
		userrepo.save(currentuser);
		return visitCity;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/GetSessions", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Traveller> GetSearchSessions() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		// JwtUtils JWT=new JwtUtils();
		// JWT.getUserNameFromJwtToken(o);
		return currentuser.getTravellers();
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/GetSelectedsession", method = RequestMethod.GET, produces = { "text/plain" })
	public String GetSelectedSession() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		if (currentuser.getSelectedsession() == null) {
			return "No session saved!";
		} else {
			long longTimeSet = Long.parseLong(currentuser.getSelectedsession().getTimestamp());
			Date DateMade = new Date(longTimeSet);
			DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
			return "Name:   " + currentuser.getSelectedsession().getName() + "   Visit City: "
					+ currentuser.getSelectedsession().visit + "   Session creation time:   " + df.format(DateMade)
					+ "   Currently Living in:" + currentuser.getSelectedsession().getCity() + "  ";
		}
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/GetWinningsession", method = RequestMethod.GET, produces = { "text/plain" })
	public String GetWinningSession() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		if (currentuser.getWinnerSession() == null) {
			return "You did not win any prize!";
		} else {
			long longTimeSet = Long.parseLong(currentuser.getWinnerSession().getTimestamp());
			Date DateMade = new Date(longTimeSet);
			DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
			return "You won a prize:" + currentuser.getWinnerSession().visit + "\n" + df.format(DateMade);
		}
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/FreeTicketPosting", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public String FreeTicketPosting(@RequestBody String travellerid) {
		travellerid = travellerid.substring(1, travellerid.length() - 1);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User currentuser = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		Traveller travellerfound;
		Tourist touristfound;
		Business bussinessfound;
		if (currentuser.getSelectedsession() != null) {

			try {
				travellerfound = tr.findById(travellerid).get(0);
				tr.deleteByUsername(travellerfound.getUsername());
				br.deleteByUsername(travellerfound.getUsername());
				tour.deleteByUsername(travellerfound.getUsername());
				tr.save(travellerfound);
				System.out.println(travellerfound.toString());
			     currentuser.setSelectedsession(travellerfound);
			     userrepo.save(currentuser);
			     return "Saved";
			} catch (IndexOutOfBoundsException e) {
				travellerfound = null;
				e.printStackTrace();
			}

			try {
				touristfound = tour.findById(travellerid).get(0);
				tr.deleteByUsername(travellerfound.getUsername());
				br.deleteByUsername(travellerfound.getUsername());
				tour.deleteByUsername(travellerfound.getUsername());
				tr.save(touristfound);

				System.out.println(touristfound.toString());
			     currentuser.setSelectedsession(touristfound);
			     userrepo.save(currentuser);
			     return "Saved";
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				touristfound = null;
			}

			try {
				bussinessfound = br.findById(travellerid).get(0);
				tr.deleteByUsername(travellerfound.getUsername());
				br.deleteByUsername(travellerfound.getUsername());
				tour.deleteByUsername(travellerfound.getUsername());
				tr.save(bussinessfound);

				System.out.println(bussinessfound.toString());
			     currentuser.setSelectedsession(bussinessfound);
			     userrepo.save(currentuser);
			     return "Saved";
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				bussinessfound = null;
			}
			for (Traveller traveller : currentuser.getTravellers()) {
				System.out.println(traveller.getId());
				if (travellerid.equals(traveller.getId())) {
					currentuser.setSelectedsession(traveller);
					userrepo.save(currentuser);
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Traveller")) {
						tr.deleteByUsername(travellerfound.getUsername());
						br.deleteByUsername(travellerfound.getUsername());
						tour.deleteByUsername(travellerfound.getUsername());
						tr.save(traveller);
						System.out.println("Session saved! Tra");
						return "Session saved!";
					}
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Tourist")) {
						tr.deleteByUsername(travellerfound.getUsername());
						br.deleteByUsername(travellerfound.getUsername());
						tour.deleteByUsername(travellerfound.getUsername());
						tour.save((Tourist) traveller);
						System.out.println("Session saved! Tou");
						return "Session saved!";
					}
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Business")) {
						tr.deleteByUsername(travellerfound.getUsername());
						br.deleteByUsername(travellerfound.getUsername());
						tour.deleteByUsername(travellerfound.getUsername());
						br.save((Business) traveller);
						System.out.println("Session saved! Bus");
						return "Session saved!";
					}
				}
			}
		} else {
			for (Traveller traveller : currentuser.getTravellers()) {
				System.out.println(traveller.getId());
				if (travellerid.equals(traveller.getId())) {
					currentuser.setSelectedsession(traveller);
					userrepo.save(currentuser);
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Traveller")) {
						tr.deleteByUsername(traveller.getUsername());
						br.deleteByUsername(traveller.getUsername());
						tour.deleteByUsername(traveller.getUsername());
						tr.save(traveller);
						System.out.println("Session saved! Tra");
						return "Session saved!";
					}
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Tourist")) {
						tr.deleteByUsername(traveller.getUsername());
						br.deleteByUsername(traveller.getUsername());
						tour.deleteByUsername(traveller.getUsername());
						tour.save((Tourist) traveller);
						System.out.println("Session saved! Tou");
						return "Session saved!";
					}
					if (traveller.getClass().toString().equals("class com.example.java2.Entities.Business")) {
						tr.deleteByUsername(traveller.getUsername());
						br.deleteByUsername(traveller.getUsername());
						tour.deleteByUsername(traveller.getUsername());
						br.save((Business) traveller);
						System.out.println("Session saved! Bus");
						return "Session saved!";
					}
				}
			}}
		return "Not saved";
		}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/FreeTicket", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public Traveller FreeTicket(@RequestBody String[] ids,
			@RequestParam(name = "city", required = false) String CityString) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		City city = null;
		try {
			city = OpenData.RetrieveData(CityString, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Traveller> TravellerstoBeCompared = new ArrayList<Traveller>();
		Traveller temp;
		List<Traveller> traveller;
		List<Tourist> tourists;
		List<Business> business;
		for (String id : ids) {
			if ((traveller = tr.findById(id)).size() > 0) {
				temp = traveller.get(0);
				System.out.println(temp);
				TravellerstoBeCompared.add(temp);
			} else if ((tourists = tour.findById(id)).size() > 0) {
				temp = tourists.get(0);
				TravellerstoBeCompared.add(temp);
			} else if ((business = br.findById(id)).size() > 0) {
				temp = business.get(0);
				TravellerstoBeCompared.add(temp);
			} else {

			}
		}
		System.out.println("hey" + TravellerstoBeCompared);
		Traveller travellerfound = city.FreeTicket(TravellerstoBeCompared);
		User userwinning = userrepo.findByUsername(securityContext.getAuthentication().getName()).get();
		userwinning.setWinnerSession(travellerfound);
		userrepo.save(userwinning);
		return travellerfound;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteTraveller/{id}", method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	public String DeleteTraveller(@PathVariable("id") String id) {
		tr.deleteById(id);
		return "User Deleted";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteBusiness/{id}", method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	public String DeleteBusiness(@PathVariable("id") String id) {
		tr.deleteById(id);

		return "User Deleted";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteTourist/{id}", method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	public String DeleteTourist(@PathVariable("id") String id) {
		tr.deleteById(id);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteCity/{id}", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public String DeleteCityById(@PathVariable("id") String id) {

		System.out.println(id);
		cr.deleteById(id);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteTravellers", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public String DeleteTravellers(@RequestBody String[] ids) {
		System.out.println(ids);
		for (String string : ids) {
			tr.deleteById(string);
		}
		return "Items Deleted";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteTourists", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public String DeleteTourists(@RequestBody String[] ids) {
		System.out.println(ids);
		for (String string : ids) {
			tour.deleteById(string);
		}
		return "Items Deleted";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/DeleteBusiness", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	public String DeleteBusiness(@RequestBody String[] ids) {
		System.out.println(ids);
		for (String string : ids) {
			br.deleteById(string);
		}
		return "Items Deleted";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/UpdateCity/{id}", method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	public String UpdateCity(@PathVariable("name") String id, @RequestBody City city) {
		cr.update(city);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/UpdateTraveller/{id}", method = RequestMethod.PUT, produces = { "application/json",
			"application/xml" })
	public String UpdateTraveller(@PathVariable("id") String id, @RequestBody Traveller traveller) {
		traveller.setId(id);
		tr.update(traveller);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/UpdateBusiness/{id}", method = RequestMethod.PUT, produces = { "application/json",
			"application/xml" })
	public String UpdateBusiness(@PathVariable("id") String id, @RequestBody Business traveller) {
		traveller.setId(id);
		br.update(traveller);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/UpdateTourist/{id}", method = RequestMethod.PUT, produces = { "application/json",
			"application/xml" })
	public String UpdateBusiness(@PathVariable("id") String id, @RequestBody Tourist traveller) {
		traveller.setId(id);
		tour.update(traveller);
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AllTravellers", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Traveller> AllTravellers() {
		return tr.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AllBusiness", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Business> AllBusiness() {
		return br.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AllTourists", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Tourist> AllTourist() {
		return tour.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AnyTraveller", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Traveller> AnyTraveller() {
		List<Traveller> trl = new ArrayList();
		trl.addAll(tr.findAll());
		trl.addAll((List<Traveller>) ((List<?>) br.findAll()));
		trl.addAll((List<Traveller>) ((List<?>) tour.findAll()));
		return trl;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AllCountries", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Country> GetCountries() {
		return countryr.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/AllCities", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<City> AllCities() {
		return cr.findAll();
	}
}
