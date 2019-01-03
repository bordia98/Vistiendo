const functions = require('firebase-functions');

var admin = require('firebase-admin');

admin.initializeApp({
  credential: admin.credential.cert({
 	// add your admin secret credentials  
}),
  databaseURL: 'https://vistiendo-deecb.firebaseio.com/'
});

exports.getUID = functions.https.onRequest((request,response) =>{ 
	let another_record ;
   const record =  (admin.auth().getUserByEmail(request.query.email)
  	.then(function(userRecord) {
  		console.log("Successfully fetched user data:", userRecord.toJSON());
  		another_record = userRecord.uid;
  		return userRecord;
  	})
  	.catch(function(error) {
    	console.log("Error fetching user data:", error);
    	throw new Error("No user found");
 	})	
 	);

   return Promise.all([record]).then(()=>{
   		console.log("promise triggered");
   		response.send(another_record);
   		return another_record;
   });
});
