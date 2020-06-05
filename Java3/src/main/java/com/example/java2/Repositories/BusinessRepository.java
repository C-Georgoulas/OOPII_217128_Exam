package com.example.java2.Repositories;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.java2.Entities.Business;
import com.example.java2.Entities.City;
import com.example.java2.Entities.Traveller;
@Repository
public class BusinessRepository {
	@Autowired MongoTemplate mongoTemplate;
	public List<Business> findAll() {
	    return mongoTemplate.findAll(Business.class);
	}
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Business.class);
    }
	public Traveller save(Business business) {
        mongoTemplate.save(business);
        return business;
    }
	public void deleteByUsername(String username) {
		Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        mongoTemplate.findAllAndRemove(query, Business.class);
	}
	public List<Business> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
         return mongoTemplate.find(query, Business.class);
    }
	/*public Business saveBusiness(Business business) {
        mongoTemplate.save(business);
        return business;
    }
	public Tourist saveTourist(Tourist tourist) {
        mongoTemplate.save(tourist);
        return tourist;
    }*/
	 public List<Business> findTravellerByName(String name){
	        Query query = new Query();
	        query.addCriteria(Criteria.where("name").is(name));
	        return mongoTemplate.find(query, Business.class);
	 }
	public Traveller update(Business business){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(business.getId()));
        Update update = new Update();
        update.set("name", business.getName());
        update.set("age", business.getAge());
        update.set("city", business.getCity());
        update.set("preferedWeather", business.getPreferedWeather());
        update.set("preferedMuseums", business.getPreferedMuseums());
        update.set("currentlon", business.getCurrentlon());
        update.set("currentlon", business.getCurrentlon());
        return mongoTemplate.findAndModify(query, update, Business.class);
    }
}
