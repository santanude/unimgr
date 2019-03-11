import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';
import {Observable} from 'rxjs';
import { identifierModuleUrl } from '@angular/compiler';
@Component({
  selector: 'app-user-comp',
  templateUrl: './user-comp.component.html',
  styleUrls: ['./user-comp.component.css']
})
export class UserCompComponent implements OnInit {

  users$: Object;
  evc$:Object;
  users1$: Object;
  arrCase$:Object[];
  result$:Object;
  endPoint$:Object[];
  endPoints$:Object;
  constructor(private data :DataService) {
    this.data.getUsers().subscribe(
      data => { this.users$ = data;
       this.result$ = data['subscriber-services'];
       this.users1$=this.result$['evc'];
       this.evc$ =this.users1$[0]['evc-id'];
       this.endPoints$=this.users1$[0]['end-points'];
       this.endPoint$=this.endPoints$['end-point'];      
       });
        
console.log(this.endPoint$);
   }

  ngOnInit() {
  }

}
