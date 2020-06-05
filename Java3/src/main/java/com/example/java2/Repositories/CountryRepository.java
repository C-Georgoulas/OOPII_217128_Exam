package com.example.java2.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.java2.Entities.City;
import com.example.java2.Entities.Country;
@Repository
public class CountryRepository {
	@Autowired MongoTemplate mongoTemplate;
	public void deleteById(String country) {
        Query query = new Query();
        query.addCriteria(Criteria.where("CountryName").is(country));
        mongoTemplate.remove(query, Country.class);
    }
	public Country save(Country country) {
        mongoTemplate.save(country);
        return country ;
    }
	public List<Country> findAll() {
	    return mongoTemplate.findAll(Country.class);

    }
	public List<Country> findById(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(name));
       return  mongoTemplate.find(query, Country.class);
    }
	public Country update(Country country){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(country.getCountryName()));
        Update update = new Update();
        update.set("Cities", country.getCities());
        update.set("timeslived", country.getTimeslived());
        update.set("timesvisited", country.getTimesvisited());
        return mongoTemplate.findAndModify(query, update, Country.class);
    }
}
