package com.example.java2.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  private 	List<Traveller> travellers;
  
  private Traveller selectedsession;
  
  private Traveller WinnerSession;
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.travellers=new ArrayList<Traveller>();
    this.WinnerSession=null;
    this.selectedsession=null;
  }

  public String getId() {
    return id;
  }

  public Traveller getSelectedsession() {
	return selectedsession;
}

public void setSelectedsession(Traveller selectedsession) {
	this.selectedsession = selectedsession;
}

public Traveller getWinnerSession() {
	return WinnerSession;
}

public void setWinnerSession(Traveller winnerSession) {
	WinnerSession = winnerSession;
}

public void setTravellers(List<Traveller> travellers) {
	this.travellers = travellers;
}

public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

public List<Traveller> getTravellers() {
	return travellers;
}

public void setTravellers(Traveller traveller) {
	this.travellers.add(traveller);
}
}
