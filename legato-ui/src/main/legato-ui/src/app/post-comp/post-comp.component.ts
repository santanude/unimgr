import { Component, OnInit,NgModule } from '@angular/core';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { getDefaultService } from 'selenium-webdriver/opera';
import {DataService} from '../data.service';
import { Observable, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {Router} from '@angular/router';

@Component({
  selector: 'app-post-comp',
  templateUrl: './post-comp.component.html',
  styleUrls: ['./post-comp.component.css']
})
export class PostCompComponent implements OnInit {
  createEvcForm : FormGroup;
  // data :DataService;
  constructor(private data: DataService, private router: Router ) { }
  serviceType: String;
  ngOnInit() {
  this.createEvcForm = new FormGroup({
    node1: new FormControl(),
    node2: new FormControl(),
    evcId: new FormControl(),
    serviceType:new FormControl(),
    vlanId : new FormControl()
});
  }
 internalcreateEvcService()
 {
   console.log(this.createEvcForm.value['node1']);
   console.log(this.createEvcForm.value['node2']);
   let connectionType: String;
   let vlanId=this.createEvcForm.value['vlanId'];
   if(this.createEvcForm.value['serviceType']==='EPL' || this.createEvcForm.value['serviceType']==='EVPL')
   {
    connectionType="point-to-point";
   }
   else{
    connectionType="multipoint-to-multipoint";
   }
   if(this.createEvcForm.value['serviceType']==='EPL' || this.createEvcForm.value['serviceType']==='EPLAN')
   {
    vlanId='1';
   }
   let rawdata = {};

   let arr = [];
   let cosName =[];
   let cosNames={};
   cosName.push({"mef-legato-services:name":this.createEvcForm.value['serviceType']});
   cosNames["mef-legato-services:cos-name"]=cosName;
   let endPoints={};
   let endPoint=[];
   let ceVlans={};
  let ceVlan=[];
  ceVlan.push(vlanId);
  ceVlans["mef-legato-services:ce-vlan"]=ceVlan;
  let bwpPerCos ={};
  let bwpFlowperCos=[];
  const bwpFlower={
    "mef-legato-services:cos-name":"Neon",
    "envelope-id":"Rank 0",
    "rank":"0"
  }
  bwpFlowperCos.push(bwpFlower);
 
  bwpPerCos["mef-legato-services:bwp-flow-per-cos"]=bwpFlowperCos;
  let bwpPerEcc ={};
  let bwpFlowPerEcc=[];
  const bwpFlow ={
    "mef-legato-services:eec-name":"EEC-Krypton",
    "envelope-id":"Rank 0",
    "rank":"0"
  }
  bwpFlowPerEcc.push(bwpFlow);
  bwpPerEcc["mef-legato-services:bwp-flow-per-eec"]=bwpFlowPerEcc;
   let endpoint1={
    "mef-legato-services:uni-id":this.createEvcForm.value['node1'],
                 "mef-legato-services:role":"root",
                 "mef-legato-services:admin-state":"true",
                 "mef-legato-services:color-identifier":"COLID1",
                 "mef-legato-services:cos-identifier":"COSID1",
                 "mef-legato-services:eec-identifier":"EECID1",
                 "mef-legato-services:source-mac-address-limit":"1",
                 "mef-legato-services:source-mac-address-limit-time-interval":"1",
                 "mef-legato-services:test-meg-enabled":"false",
                 "mef-legato-services:user-label":"admin",
                 "mef-legato-services:subscriber-meg-mip-enabled":"false",
                 "mef-legato-services:ce-vlans":ceVlans,
                 "ingress-bwp-per-cos":bwpPerCos,
                 "egress-bwp-per-eec":bwpPerEcc
   }
   let endpoint2={
    "mef-legato-services:uni-id":this.createEvcForm.value['node2'],
                 "mef-legato-services:role":"root",
                 "mef-legato-services:admin-state":"true",
                 "mef-legato-services:color-identifier":"COLID1",
                 "mef-legato-services:cos-identifier":"COSID1",
                 "mef-legato-services:eec-identifier":"EECID1",
                 "mef-legato-services:source-mac-address-limit":"1",
                 "mef-legato-services:source-mac-address-limit-time-interval":"1",
                 "mef-legato-services:test-meg-enabled":"false",
                 "mef-legato-services:user-label":"admin",
                 "mef-legato-services:subscriber-meg-mip-enabled":"false",
                 "mef-legato-services:ce-vlans":ceVlans,
                 "ingress-bwp-per-cos":bwpPerCos,
                 "egress-bwp-per-eec":bwpPerEcc
   }
   endPoint.push(endpoint1);
   endPoint.push(endpoint2);
   endPoints['mef-legato-services:end-point'] = endPoint;


let uniExclusion ={};
let serviceEndPointPair =[];
let serviceEndPointPair1={
  "mef-legato-services:end-point1":"1",
  "mef-legato-services:end-point2":"2"
};
serviceEndPointPair.push(serviceEndPointPair1);
uniExclusion["mef-legato-services:end-point-pair"]=serviceEndPointPair;
let endPointPairs ={};
let endPointPair =[];
let endPointPair1={
  "mef-legato-services:index":"0",
  "sls-uni-exclusions":uniExclusion,

}
endPointPair.push(endPointPair1);
endPointPairs[ "mef-legato-services:set-of-end-point-pairs"]=endPointPair;

let legetoServicePointPairs={
  "sls-uni-exclusions":uniExclusion
};
let  cosEtntries ={};
let cosEntry=[];
let  pmEtntries ={};
let pmEntry=[];
let pmEntry1={
  "mef-legato-services:pm-entry-id":"68",
  "mef-legato-services:sets-of-end-point-pairs":endPointPairs,
  "mef-legato-services:end-point-pairs":legetoServicePointPairs

};
pmEntry.push(pmEntry1);
pmEtntries["mef-legato-services:pm-entry"]=pmEntry;

let cosEntry1={
  "mef-legato-services:cos-name":"Krypton",
  "mef-legato-services:pm-entries":pmEtntries
}
cosEntry.push(cosEntry1);
cosEtntries["mef-legato-services:cos-entry"]=cosEntry;


   let carrierEthernetsls = {
    "mef-legato-services:sls-id":"SLSID1",
    "mef-legato-services:start-time":"9372-84-27T31:46:58Z",
    "mef-legato-services:cos-entries":cosEtntries
   };

   const obj = {
    "mef-legato-services:evc-id":this.createEvcForm.value['evcId'],
    "mef-legato-services:cos-names":cosNames,
    "mef-legato-services:end-points":endPoints,
    "mef-legato-services:carrier-ethernet-sls":carrierEthernetsls,
    "mef-legato-services:connection-type":connectionType,
    "mef-legato-services:admin-state":"true",
    "mef-legato-services:user-label":"U4",
    "mef-legato-services:max-frame-size": "1522",
    "mef-legato-services:max-num-of-evc-end-point": "2",
    "mef-legato-services:ce-vlan-id-preservation": "false",
    "mef-legato-services:ce-vlan-pcp-preservation": "false",
    "mef-legato-services:ce-vlan-dei-preservation": "false",
    "mef-legato-services:unicast-frame-delivery": "unconditional",
    "mef-legato-services:multicast-frame-delivery": "unconditional",
    "mef-legato-services:broadcast-frame-delivery": "unconditional",
    "mef-legato-services:svc-type": this.createEvcForm.value['serviceType'].toLowerCase()
   }

   arr.push(obj);
   
   rawdata["mef-legato-services:evc"] = arr;
   console.log("Row data is ---->", rawdata);
  (this.data.createEvcServcie(rawdata)).subscribe(
    data1 => {
        this.router.navigateByUrl('/success');
    },
    data1=>
    {
    (error: any)=> console.log(error)
    alert(data1.error.error);
    }
  
  );
  
  /*pipe(
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
  );*/
  console. log("-------------In createEvcService Service  end method");

 } 
}
