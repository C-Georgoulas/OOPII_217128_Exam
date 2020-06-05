package com.example.java2.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.java2.Entities.City;
import com.example.java2.Entities.Tourist;
import com.example.java2.Entities.Traveller;
@Repository
public class CityRepository {
	@Autowired MongoTemplate mongoTemplate;
	
	public void deleteById(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        mongoTemplate.remove(query, City.class);
    }
	public City save(City name) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("name").is(name));
	    if(mongoTemplate.findById(name.getName(), City.class)==null) {
        mongoTemplate.save(name);
        return name;
	    }
	    else {
	    	name=this.update(name);
	    	return name;
	    }      
    }
	public List<City> findAll() {
	    return mongoTemplate.findAll(City.class);

    }
	public List<City> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(id));
        return mongoTemplate.find(query, City.class);
    }
	public City update(City city){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(city.getName()));
        Update update = new Update();
        update.set("museums", city.getMuseums());
        update.set("cafesRestaurantsBars", city.getCafesRestaurantsBars());
        update.set("weather", city.getWeather());
        update.set("lat", city.getLat());
        update.set("lon", city.getLon());
        update.set("timesvisited",city.getTimesvisited());
        update.set("timeslived",city.getTimeslived());
        return mongoTemplate.findAndModify(query, update, City.class);
    }
}
