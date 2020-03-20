import axios from "axios";
//https://opencagedata.com/dashboard
const API_KEY = "8560513c8f074064b922cbd111448802";
const GOOGlE_API_KEY = "AIzaSyBf3ARbM-XLuujYWpzqx-7a7284t7VCCaU";
const API_URL = "https://api.opencagedata.com/geocode/v1/json";

const GOOGlE_API_URL ="https://maps.googleapis.com/maps/api/geocode/json";
//const GOOGlE_API_URL_SAMPLE ="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY";


 export class GeocodeService {

     getAdress(geoposition){

         console.log(" geoposition ====> "+JSON.stringify(geoposition));
         let latitude =  geoposition.lat;
         let longitude = geoposition.lng;


         let request_url =API_URL
             + '?'
             + 'key=' + API_KEY
             + '&q=' + encodeURIComponent(latitude + ',' + longitude)
             + '&pretty=1'
             + '&no_annotations=1';

         return  axios.get(request_url,'GET');
     }

     getGeocodeFromAdresse(addresse){
         console.log(" addresse ====> "+JSON.stringify(addresse));

         let request_url =API_URL
             + '?'
             + 'key=' + API_KEY
             + '&q=' + encodeURIComponent(addresse)
             + '&pretty=1';

         return  axios.get(request_url, 'GET');

     }

     getGoogleGeocodeFromAdresse(addresse){
         console.log(" addresse ====> "+JSON.stringify(addresse));

         let request_url =GOOGlE_API_URL
             + '?'
             + 'address=' +encodeURIComponent(addresse) + GOOGlE_API_KEY;

         console.log(`${JSON.stringify(request_url)}`);

         let url = `${GOOGlE_API_URL}?address=${addresse}&key=${GOOGlE_API_KEY}`;

         return  axios.get(url, 'GET');

     }

}

export default new GeocodeService();