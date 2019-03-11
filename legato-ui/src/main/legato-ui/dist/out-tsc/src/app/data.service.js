import * as tslib_1 from "tslib";
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from "@angular/common/http";
var DataService = /** @class */ (function () {
    function DataService(http) {
        this.http = http;
    }
    DataService.prototype.getUsers = function () {
        //var headers_object = new HttpHeaders();
        //headers_object.append('Content-Type', 'application/json');
        //headers_object.append('Authorization', 'Basic YWRtaW46YWRtaW4=');
        //headers_object.append('Access-Control-Allow-Origin',"*");
        var headers = new HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
            .set("Content-Type", "application/json");
        //const httpOptions = {
        //  headers: headers_object
        //}
        console.log("In get User method");
        // return this.http.get('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services/',{headers});
        return this.http.get('http://127.0.0.1:8181/restconf/operational/tapi-common:context/tapi-topology:topology/mef:presto-nrp-topology', { headers: headers });
    };
    DataService.prototype.createEvcServcie = function () {
        console.log("In createEvcService Service  method");
        // const mef-legato-services:evc  = "EVPL";
        var data = {};
        data["mef-legato-services:evc"] = "EVPL";
        console.log('data --->', data);
    };
    DataService = tslib_1.__decorate([
        Injectable({
            providedIn: 'root'
        }),
        tslib_1.__metadata("design:paramtypes", [HttpClient])
    ], DataService);
    return DataService;
}());
export { DataService };
//# sourceMappingURL=data.service.js.map