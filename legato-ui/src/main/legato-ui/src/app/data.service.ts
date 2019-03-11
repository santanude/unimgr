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

 
const  headers = new  HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
                                    .set("Content-Type","application/json");
    console.log("In get User method")
   return this.http.get('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services',{headers});
   //return this.http.get('http://127.0.0.1:8181/restconf/operational/tapi-common:context/tapi-topology:topology/mef:presto-nrp-topology',{headers});
  }
  createEvcServcie(data)
  {
    let body=JSON.stringify(data);
    console.log(JSON.stringify(data));
   console. log("----------In createEvcService Service  method");
  //  const  headers = new  HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
                                 //   .set("Content-Type","application/json");
   // const httpOptions ={headers};

return this.http.post('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services',body,{headers : new  HttpHeaders({
  'Authorization' : 'Basic YWRtaW46YWRtaW4=',
  'Content-Type' : 'application/json'
})});


/*.pipe(
  catchError((error : any)=>{
    console.log("error is "+error.status);
    if (error.status === 500) {
      return Observable.throw(new Error(error.status));
  }
  else if (error.status >= 400) {
      return Observable.throw(new Error(error.status));
  }
  else if (error.status === 409) {
      return Observable.throw(new Error(error.status));
  }
  else if (error.status === 406) {
      return Observable.throw(new Error(error.status));
  }
  })
);
console. log("-------------In createEvcService Service  end method");
  }*/
}
}
