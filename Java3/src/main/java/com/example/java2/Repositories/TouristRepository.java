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
import com.example.java2.Entities.Enums.CafeBarRestaur;
import com.example.java2.Entities.Enums.Museums;
import com.example.java2.Entities.Tourist;
@Repository
public class TouristRepository {
@Autowired MongoTemplate mongoTemplate;
	public List<Tourist> findAll() {
	    return mongoTemplate.findAll(Tourist.class);
	}
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Tourist.class);
    }
	public List<Tourist> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.find(query, Tourist.class);
    }
	public void deleteByUsername(String username) {
		Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        mongoTemplate.findAllAndRemove(query, Tourist.class);
	}
	public Traveller save(Tourist traveller) {
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
	 public List<Tourist> findTravellerByName(String name){
	        Query query = new Query();
	        query.addCriteria(Criteria.where("name").is(name));
	        return mongoTemplate.find(query, Tourist.class);
	 }
	public Traveller update(Tourist Traveller){
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
        return mongoTemplate.findAndModify(query, update, Tourist.class);
    }
}

