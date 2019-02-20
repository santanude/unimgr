import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';
import {Observable} from 'rxjs';
@Component({
  selector: 'app-user-comp',
  templateUrl: './user-comp.component.html',
  styleUrls: ['./user-comp.component.css']
})
export class UserCompComponent implements OnInit {

  users$: Object;
  users1$: Object [];
  arrCase$:Object[];
  result$:Object[];
  constructor(private data :DataService) {
    this.data.getUsers().subscribe(
      data => {this.users$ = data 
       // this.result$ = data['subscriber-services']['evc'];
       const result1=data['tapi-topology:topology'];
       const uu1=result1[0].uuid;
       console.log("hello"+uu1);
       const uu2=result1[0]['node'];
       this.result$=uu2[0]['owned-node-edge-point'];
      // const result2=result1['node'];
      // const uu=result2;
      // console.log(uu);
   //  this.result$.forEach(element => {
      //  console.log(element['uuid'])
       
       });
        
//this.result$=result2['owned-node-edge-point'];
       //this.result$=data['tapi-topology:topology']['node']['owned-node-edge-point'];
    //  this.result$.forEach(element => {
        //  console.log(element['uuid'])
         
//});
 //     }


  //  )
    console.log(this.users$)
   }

  ngOnInit() {
  }

}
