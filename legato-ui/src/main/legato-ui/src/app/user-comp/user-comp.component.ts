import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';
import {Observable} from 'rxjs';
import { identifierModuleUrl } from '@angular/compiler';
import { browser } from 'protractor';
@Component({
  selector: 'app-user-comp',
  templateUrl: './user-comp.component.html',
  styleUrls: ['./user-comp.component.css']
})
export class UserCompComponent implements OnInit {

  users$: Object;
  evc$:Object;
  users1$: Object;
  data1$:Object;
  arrCase$:Object[];
  result$:Object;
  result1$:Object;
  result2$:Object;
  endPoint$:Object[];
  endPoints$:Object;
  ceVlans$:Object;
  ceVlan$:Object[];
  vlanId$:Object;
  vlan$:String;
  evcType$:Object;
  constructor(private data :DataService) {
    
        
//console.log(this.endPoint$);
   }

  ngOnInit() {
     // this.fetchData();
     this.data.getUsers().subscribe(
      data => { this.users$ = data;
      
      this.result1$ = data['mef-services'];
      this.result2$ = this.result1$['carrier-ethernet'];
       this.result$ = this.result2$['subscriber-services'];
       this.data1$=this.result$['evc'];
       
    //   this.users1$=this.result$['evc'];  
      /* this.evc$ =this.users1$[0]['evc-id'];
       this.evcType$=this.users1$[0]['svc-type'];
       this.endPoints$=this.users1$[0]['end-points'];
       this.endPoint$=this.endPoints$['end-point']; 
       this.ceVlans$=this.endPoint$[0]['ce-vlans'];
       this.ceVlan$=this.ceVlans$['ce-vlan'];
       this.vlanId$=this.ceVlan$[0];
       if(this.evcType$==='epl' || this.evcType$==='eplan')
       {
         this.vlan$='';
       }
       else{
         this.vlan$=this.vlanId$.toString();
       }
       this.evcType$=this.evcType$.toString().toUpperCase();*/
       });
  }
  
  deleteEvc(evcId)
  {
    console.log('Delete data for '+evcId);
    (this.data.deleteEvcServcie(evcId)).subscribe(
        
      (error: any)=> console.log(error)   
    );
   //  browser.sleep(100);
    location.reload();
  }
 /* fetchData(){
    this.data.getUsers().subscribe(
      data => { this.users$ = data;
      
      this.result1$ = data['mef-services'];
      this.result2$ = this.result1$['carrier-ethernet'];
      this.result$ = this.result2$['subscriber-services'];
      this.data1$=this.result$['evc'];
    });
  }*/
}
