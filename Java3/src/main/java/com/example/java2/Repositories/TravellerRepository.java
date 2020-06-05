package com.example.java2.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.java2.Entities.Traveller;
import com.example.java2.Entities.Business;
import com.example.java2.Entities.City;
import com.example.java2.Entities.Enums.CafeBarRestaur;
import com.example.java2.Entities.Enums.Museums;
import com.example.java2.Entities.Tourist;
@Repository
public class TravellerRepository {
@Autowired MongoTemplate mongoTemplate;
	public List<Traveller> findAll() {
	    return mongoTemplate.findAll(Traveller.class);
	}
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Traveller.class);
    }
	public void deleteByUsername(String username) {
		Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        mongoTemplate.findAllAndRemove(query, Traveller.class);
	}
	public Traveller save(Traveller traveller) {
        mongoTemplate.save(traveller);
        return traveller;
    }
	/*public Business saveBusiness(Business business) {
        mongoTemplate.save(business);
        return business;
    }
	public Tourist saveTourist(Tourist tourist) {
        mongoTemplate.save(tourist);
        return tourist;
    }*/
	public List<Traveller> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
       return mongoTemplate.find(query, Traveller.class);
        
    }
	 public List<Traveller> findTravellerByName(String name){
	        Query query = new Query();
	        query.addCriteria(Criteria.where("name").is(name));
	        return mongoTemplate.find(query, Traveller.class);
	 }
	public Traveller update(Traveller Traveller){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(Traveller.getId()));
        Update update = new Update();
        update.set("name", Traveller.getName());
        update.set("age", Traveller.getAge());
        update.set("city", Traveller.getCity());
        update.set("preferedWeather", Traveller.getPreferedWeather());
        update.set("preferedMuseums", Traveller.getPreferedMuseums());
        update.set("preferedCafesRestaurantsBars", Traveller.getPreferedCafesRestaurantsBars());
        update.set("preferedCities", Traveller.getPreferedCities());
        update.set("currentlon", Traveller.getCurrentlon());
        update.set("currentlon", Traveller.getCurrentlon());
        return mongoTemplate.findAndModify(query, update, Traveller.class);
    }
}
