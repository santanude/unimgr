import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { HttpHeaders } from "@angular/common/http";
import { Observable, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Console } from '@angular/core/src/console';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http:HttpClient) { }

  getUsers(){

 console.log("******-port Details is-******"+window.location.port);
const  headers = new  HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
                                    .set("Content-Type","application/json");
let url2:string;
let port:string;
port=window.location.port;
if(port==='4200'){
 url2= 'http://127.0.0.1:8181/restconf/operational/mef-legato-services:mef-services';
}
else{
  url2='http://'+window.location.hostname+':'+window.location.port+'/restconf/operational/mef-legato-services:mef-services';
}
//url2='http://'+window.location.hostname+':'+window.location.port+'/restconf/config/mef-legato-services:mef-services';

    console.log("In get User method" +url2);
// return this.http.get('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services',{headers});
  return this.http.get(url2,{headers});
  }
  createEvcServcie(data)
  {
    let body=JSON.stringify(data);
    console.log(JSON.stringify(data));
   console. log("----------In createEvcService Service  method");
    
   let url2:string;
   let port:string;
   port=window.location.port;
   if(port==='4200'){
    url2= 'http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services';
   }
   else{
     url2='http://'+window.location.hostname+':'+window.location.port+'/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services';
   }

return this.http.post(url2,body,{headers : new  HttpHeaders({
  'Authorization' : 'Basic YWRtaW46YWRtaW4=',
  'Content-Type' : 'application/json'
})});

}

deleteEvcServcie(rawData)
{
  let body=JSON.stringify(rawData);
    console.log(JSON.stringify(rawData));
   console. log("----------In createEvcService Service  method");
  //  const  headers = new  HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
                                 //   .set("Content-Type","application/json");
   // const httpOptions ={headers}

 let url2:string;
   let port:string;
   port=window.location.port;
   if(port==='4200'){
    url2= 'http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services/evc/'+rawData;
   }
   else{
     url2='http://'+window.location.hostname+':'+window.location.port+'/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services/evc/'+rawData;
   }
 console.log('*****delete url is************'+url2);
return this.http.delete(url2,{headers : new  HttpHeaders({
  'Authorization' : 'Basic YWRtaW46YWRtaW4=',
  'Content-Type' : 'application/json'
})});
}
}
