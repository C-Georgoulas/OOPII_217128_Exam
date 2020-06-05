 
var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var fetch = require('node-fetch');
const cookieParser = require('cookie-parser');
var localStorage = require('localStorage');
var jwt = require('jwt-simple');
var  middleware=require('./authentication-middleware')
var jwt = require('jsonwebtoken');

const exjwt = require('express-jwt');
// app configurations
app.set("view engine", "ejs");
app.use(express.static("public"));
app.use(express.json());
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser())

// RESTFUL ROUTES
const secret="z97r#s"
var infosuccess;
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', 'http://localhost:8080');
  res.setHeader('Access-Control-Allow-Headers', 'Content-type,Authorization');
  next();
});
app.use(function(req, res, next) {
    if (!req.user)
        res.header('Cache-Control', 'private, no-cache, no-store, must-revalidate');
    next();
});




app.get("/", function(req, res){
    res.render("login.ejs",{message:null});
});
app.post("/login",function(req,res){   //new endpoint  an implementation needed
  // LGTM!
  //console.log(req.body);
 // HERE!!!!!!!!!!!
  var role;
  var name;
  fetch('http://localhost:8080/web/auth/signin',{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
          {
             "username":req.body.username,
              "password":req.body.password
          }

        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then(response => {
        response.json()
          .then(responseJson => {
             var decoded = jwt.decode(responseJson.accessToken, 'z97r#s');
             ;
             if(decoded!=null){
               username=decoded.username
               payload={username};
               const token=jwt.sign(payload,secret);
              res.cookie('username', responseJson.username,{ httpOnly: true});
              res.cookie('role', decoded.role[0]["authority"],{ httpOnly: true});
              res.cookie('token', token, { httpOnly: true});
              res.cookie('accesstoken',responseJson.accessToken,{ httpOnly: true});
              role=decoded.role[0]["authority"];
        
            if( role==="ROLE_USER"){
              res.redirect("/logedin")
              //res.render("index.ejs")
              }
              else if(role==="ROLE_ADMIN"){
                res.redirect("/admin");
                //res.render("adminindex.ejs",{name:responseJson.username})
              }
            else{
                res.render("login.ejs",{message:"Deactivated!"});
              }
            }
            else{
              res.render("login.ejs",{message:"Username or password incorrect!"});
            }
            //localStorage.setItem('token', responseJson.accessToken)
            // set localStorage with your preferred name,..
            // ..say 'my_token', and the value sent by server
         
            //console.log(localStorage);
            
            // you may also want to redirect after you have saved localStorage:
            // window.location.assign("http://www.example.org")
          })
      })
      .then((data) => {
        
      }).catch((error) => {
        console.error('Error:', error);
      });
});


app.get("/logout",middleware  ,function(req,res){
  fetch('http://localhost:8080/web/auth/logout',{
  method: "POST",
  // Adding body or contents to send
  body: JSON.stringify(
    {
       "username":req.body.username,
        "password":req.body.password
    }

  ),
   
  // Adding headers to the request
  headers: {
    'Authorization': 'Bearer '+req.cookies['accesstoken'],
    "Content-type": "application/json; charset=UTF-8"
    
},
})
.then((response) => response.json() //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
     
      }).catch((error) => {
        console.error('Error:', error);
      });
  cookie = req.cookies;
  for (var prop in cookie) {
      if (!cookie.hasOwnProperty(prop)) {
          continue;
      }    
      res.cookie(prop, '', {expires: new Date(0)});
  }
  res.redirect('/');
 // res.render("login.ejs",{message:"Succesfully loged out!"});
});

app.get("/admin",middleware,function(req,res) {
  if(req.cookies.role==="ROLE_ADMIN"){
res.render("adminindex.ejs",{name:req.cookies.username});
  }
  else{
    res.send("Not authorized");
  }
});
app.get("/logedin",middleware, function(req, res) { 

  res.render("index"); 
}); 

app.get("/register",function(req,res){
  infosuccess={
    registrymessage:null
  }
  res.render("register",{message:infosuccess});
});
app.post("/registered",function(req,res){
  console.log("ffjerfuigriuerf"+req.body.username);
  var message;
  fetch('http://localhost:8080/web/auth/signup',{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
          {
             "username":req.body.username,
             "roles":["ROLE_USER"],
             "password":req.body.password,
             "email":req.body.email
          }

        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then(response => {
        response.json()
          .then(responseJson => {
            console.log(responseJson);
            var decoded = jwt.decode(responseJson.accessToken, 'z97r#s');
            console.log(decoded);
            message=responseJson.message;
            infosuccess={
              registrymessage:message
            }
            res.cookie('accesstoken',responseJson.accessToken);
           localStorage.setItem('token', responseJson.accessToken);
           if(message==="User registered successfully!"){
            res.render("index.ejs",{message:message});
             }
           else{
            res.render("register.ejs",{message:infosuccess});
             }
           
            // set localStorage with your preferred name,..
            // ..say 'my_token', and the value sent by server
            
            
            // you may also want to redirect after you have saved localStorage:
            // window.location.assign("http://www.example.org")
          })
      })
      .then((data) => {
      
      }).catch((error) => {
        console.error('Error:', error);
      });
});


app.get("/information",function(req,res){});


app.get("/offers", middleware  ,function(req, res){
    res.render("offers");
});
 
app.get("/results",middleware , function(req, res){
  console.log(req.cookies['token']);
  if(req.query.rainChoice==="notinterested"){
    fetch('http://localhost:8080/web/api/Save'+req.query.category ,{
       
        method: "POST",
        withCredentials: true,
        credentials: 'include',
         
        // Adding body or contents to send
        body: JSON.stringify(
            {
                "name":req.query.name,
                 "age":req.query.age,
                 "city":req.query.currentCity,
                 "preferedWeather":"Sun",
                 "preferedMuseums":req.query.museums,
                 "preferedCafesRestaurantsBars":req.query.cafeBarResto,
                 "preferedCities":req.query.preferedCities,
                 "username":req.cookies.username
             }
        ),
         
        // Adding headers to the request
        headers: {
            'Authorization': 'Bearer '+req.cookies['accesstoken'],
            "Content-type": "application/json; charset=UTF-8"
            
        }
      })
      .then((response) => response.json() //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.render("results",{name:data.name,cafeBarResto:data.cafesRestaurantsBars,museums:data.museums});
     
      }).catch((error) => {
          res.render("index");
        console.error('Error:', error);
      });}
      else{
        console.log("edw");
        fetch('http://localhost:8080/web/api/Save'+req.query.category+'BasedOnWeather' ,{
       
        method: "POST",
         
        // Adding body or contents to send
        body: JSON.stringify(
            {
                 
                "name":req.query.name,
                 "age":req.query.age,
                 "city":req.query.currentCity,
                 "preferedWeather":"Sun",
                 "preferedMuseums":req.query.museums,
                 "preferedCafesRestaurantsBars":req.query.cafeBarResto,
                 "preferedCities":req.query.preferedCities,
                 "username":req.cookies.username
             },
        ),
         
        // Adding headers to the request
        headers: {
          'Authorization': 'Bearer '+req.cookies['accesstoken'],
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then((response) => response.json() //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.render("results",{name:data.name,cafeBarResto:data.cafesRestaurantsBars,museums:data.museums});
     
      }).catch((error) => {
          res.render("index");
        console.error('Error:', error);
      });
      }
   
});
 //Admin routes
app.get("/administration", middleware,function(req, res){
  if(req.cookies.role==="ROLE_ADMIN"){
    fetch('http://localhost:8080/web/api/All'+req.query.choice, {
      headers: {
      "Content-type": "application/json; charset=UTF-8",
      "Authorization": 'Bearer '+req.cookies['accesstoken']
      }
    }) //req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("travellers",{data:data,typeoftraveller:req.query.choice});
  });
}
else{
  res.send("Not authorized");
}
});
app.get("/freeticket", middleware ,function(req,res){
  fetch('http://localhost:8080/web/api/AnyTraveller',{
    headers: {
    "Content-type": "application/json; charset=UTF-8",
    'Authorization': 'Bearer '+req.cookies['accesstoken'],
    }
  })//req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("freeticket",{data:data});
  })
})
app.post("/freeticketwinner", middleware ,function(req,res){   //new endpoint  an implementation needed
  // LGTM!
  console.log(req.body.candidateCity);
 // HERE!!!!!!!!!!!
  console.log(req.body.type);
  var response;if(req.cookies.role==="ROLE_ADMIN");
  if(req.cookies.role==="ROLE_ADMIN"){
  fetch('http://localhost:8080/web/api/FreeTicket?city='+req.body.candidateCity,{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
             req.body.type
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8",
            'Authorization': 'Bearer '+req.cookies['accesstoken']
        }
      })
      .then((response) => response.json()  //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
 
      res.render("freeticketwinner",{data:data})
      }).catch((error) => {
        console.error('Error:', error);
      });
    }
    else{
      res.send("Not authorized");
    }
    
   
});

app.post("/removeTravellers",middleware,function(req,res){
  console.log(req.body.id);
  console.log(req.body.typeo);
  if(req.cookies.role==="ROLE_ADMIN"){
  fetch('http://localhost:8080/web/api/Delete'+req.body.typeoftraveller ,{
       
        method: "POST",
         
        // Adding body or contents to send
        body: JSON.stringify(
          req.body.id //this is a json array
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8",
            'Authorization': 'Bearer '+req.cookies['accesstoken']
        }
      })
      .then((response) => response.json()  //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.send("Users Deleted");
     
      }).catch((error) => {
        res.send("Fault");
      });
    }else{
      res.send("Not authorized");
    }
})
//User Routes
app.get("/freelotterysubmissionform",middleware,function(req,res){
  fetch('http://localhost:8080/web/api/GetSessions',{
    headers: {
    "Content-type": "application/json; charset=UTF-8",
    "Authorization": 'Bearer '+req.cookies['accesstoken']
    }
  })//req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("UserLotteryDestinationSubmission.ejs",{data:data});
  }).catch((error) => {
        res.send("Fault");
        console.error('Error:', error);
      });
})
app.post("/ticketsubmission",function(req,res){
  fetch('http://localhost:8080/web/api/FreeTicketPosting',{
    method: "POST",
         
    // Adding body or contents to send
    body:JSON.stringify(req.body.id)
        //this is a json array
    ,
    headers: {
    "Content-type": "application/json; charset=UTF-8",
    "Authorization": 'Bearer '+req.cookies['accesstoken']
    }
  }).then((response) => {
   // return JSON.stringify(response.json());
  })
  .then((data) => {
    console.log(JSON.stringify(data));
    res.send({data:JSON.stringify(data)});
  }).catch((error) => {
    res.send("Fault");
    console.error('Error:', error);
  });
})

app.get("/SeeSessions", middleware ,function(req,res){
  if(req.cookies.role==="ROLE_USER"){
    fetch('http://localhost:8080/web/api/'+req.query.choice,{
      headers: {
      "Content-type": "application/text; charset=UTF-8",
      "Authorization": 'Bearer '+req.cookies['accesstoken']
      }
    })//req.query.choice should be Traveller,Tourist or Business.
    .then((response) => {
      return response.text();
    })
    .then((data) => {
      var winsession;
       winsession=data;
       res.render("sessions.ejs",{winses:winsession});
       //console.log(winsession);
    }).catch((error) => {
      var winsession;
          winsession="Not Found"
          console.error('Error:', error);
          res.render("sessions.ejs",{winses:winsession});
        });
      }
      else{
        res.status(401).send("Not allowed!");
      }
});
app.get("/countries",middleware,function(req,res){
  if(req.cookies.role==="ROLE_ADMIN"){
  fetch('http://localhost:8080/web/api/AllCountries',{
    headers: {
    "Content-type": "application/json; charset=UTF-8",
    "Authorization": 'Bearer '+req.cookies['accesstoken']
    }
  })//req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.text();
  })
  .then((data) => {
    console.log(data);
    res.render("stats.ejs",{data:data});
  }).catch((error) => {
        res.send("Fault");
        console.error('Error:', error);
      });
    }
    else{
      res.status(401).send("Not allowed!");
    }
});
app.get('/checkToken', middleware, function(req, res) {
  res.sendStatus(200);
});

app.listen(5000, function() {
    console.log('Server up and running.');
  });