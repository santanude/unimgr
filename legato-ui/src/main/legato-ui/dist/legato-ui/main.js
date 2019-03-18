(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["main"],{

/***/ "./src/$$_lazy_route_resource lazy recursive":
/*!**********************************************************!*\
  !*** ./src/$$_lazy_route_resource lazy namespace object ***!
  \**********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncaught exception popping up in devtools
	return Promise.resolve().then(function() {
		var e = new Error("Cannot find module '" + req + "'");
		e.code = 'MODULE_NOT_FOUND';
		throw e;
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./src/$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./src/app/app-routing.module.ts":
/*!***************************************!*\
  !*** ./src/app/app-routing.module.ts ***!
  \***************************************/
/*! exports provided: AppRoutingModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppRoutingModule", function() { return AppRoutingModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _user_comp_user_comp_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./user-comp/user-comp.component */ "./src/app/user-comp/user-comp.component.ts");
/* harmony import */ var _post_comp_post_comp_component__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./post-comp/./post-comp.component */ "./src/app/post-comp/post-comp.component.ts");





var routes = [
    {
        path: "get",
        component: _user_comp_user_comp_component__WEBPACK_IMPORTED_MODULE_3__["UserCompComponent"]
    },
    {
        path: "post",
        component: _post_comp_post_comp_component__WEBPACK_IMPORTED_MODULE_4__["PostCompComponent"]
    }
];
var AppRoutingModule = /** @class */ (function () {
    function AppRoutingModule() {
    }
    AppRoutingModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            imports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"].forRoot(routes)],
            exports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"]]
        })
    ], AppRoutingModule);
    return AppRoutingModule;
}());



/***/ }),

/***/ "./src/app/app.component.css":
/*!***********************************!*\
  !*** ./src/app/app.component.css ***!
  \***********************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2FwcC5jb21wb25lbnQuY3NzIn0= */"

/***/ }),

/***/ "./src/app/app.component.html":
/*!************************************!*\
  !*** ./src/app/app.component.html ***!
  \************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<!--The content below is only a placeholder and can be replaced.\n<div style=\"text-align:center\">\n  <h1>\n    Welcome to {{ title }}!\n  </h1>\n  <img width=\"300\" alt=\"Angular Logo\" src=\"data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNTAgMjUwIj4KICAgIDxwYXRoIGZpbGw9IiNERDAwMzEiIGQ9Ik0xMjUgMzBMMzEuOSA2My4ybDE0LjIgMTIzLjFMMTI1IDIzMGw3OC45LTQzLjcgMTQuMi0xMjMuMXoiIC8+CiAgICA8cGF0aCBmaWxsPSIjQzMwMDJGIiBkPSJNMTI1IDMwdjIyLjItLjFWMjMwbDc4LjktNDMuNyAxNC4yLTEyMy4xTDEyNSAzMHoiIC8+CiAgICA8cGF0aCAgZmlsbD0iI0ZGRkZGRiIgZD0iTTEyNSA1Mi4xTDY2LjggMTgyLjZoMjEuN2wxMS43LTI5LjJoNDkuNGwxMS43IDI5LjJIMTgzTDEyNSA1Mi4xem0xNyA4My4zaC0zNGwxNy00MC45IDE3IDQwLjl6IiAvPgogIDwvc3ZnPg==\">\n</div>\n<h2>Here are some links to help you start: </h2>\n<ul>\n  <li>\n    <h2><a target=\"_blank\" rel=\"noopener\" href=\"https://angular.io/tutorial\">Tour of Heroes</a></h2>\n  </li>\n  <li>\n    <h2><a target=\"_blank\" rel=\"noopener\" href=\"https://angular.io/cli\">CLI Documentation</a></h2>\n  </li>\n  <li>\n    <h2><a target=\"_blank\" rel=\"noopener\" href=\"https://blog.angular.io/\">Angular blog</a></h2>\n  </li>\n</ul>-->\n<div id=\"container\">\n  <app-get-comp></app-get-comp>\n\n<div id=\"content\">\n<router-outlet></router-outlet>\n</div>\n</div>"

/***/ }),

/***/ "./src/app/app.component.ts":
/*!**********************************!*\
  !*** ./src/app/app.component.ts ***!
  \**********************************/
/*! exports provided: AppComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppComponent", function() { return AppComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");


var AppComponent = /** @class */ (function () {
    function AppComponent() {
        this.title = 'test-frontend';
    }
    AppComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-root',
            template: __webpack_require__(/*! ./app.component.html */ "./src/app/app.component.html"),
            styles: [__webpack_require__(/*! ./app.component.css */ "./src/app/app.component.css")]
        })
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "./src/app/app.module.ts":
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/*! exports provided: AppModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppModule", function() { return AppModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser */ "./node_modules/@angular/platform-browser/fesm5/platform-browser.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _app_routing_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./app-routing.module */ "./src/app/app-routing.module.ts");
/* harmony import */ var _app_component__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./app.component */ "./src/app/app.component.ts");
/* harmony import */ var _get_comp_get_comp_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./get-comp/get-comp.component */ "./src/app/get-comp/get-comp.component.ts");
/* harmony import */ var _user_comp_user_comp_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./user-comp/user-comp.component */ "./src/app/user-comp/user-comp.component.ts");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var _post_comp_post_comp_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./post-comp/post-comp.component */ "./src/app/post-comp/post-comp.component.ts");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm5/forms.js");










var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_2__["NgModule"])({
            declarations: [
                _app_component__WEBPACK_IMPORTED_MODULE_4__["AppComponent"],
                _get_comp_get_comp_component__WEBPACK_IMPORTED_MODULE_5__["GetCompComponent"],
                _user_comp_user_comp_component__WEBPACK_IMPORTED_MODULE_6__["UserCompComponent"],
                _post_comp_post_comp_component__WEBPACK_IMPORTED_MODULE_8__["PostCompComponent"]
            ],
            imports: [
                _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__["BrowserModule"],
                _app_routing_module__WEBPACK_IMPORTED_MODULE_3__["AppRoutingModule"],
                _angular_common_http__WEBPACK_IMPORTED_MODULE_7__["HttpClientModule"],
                _angular_forms__WEBPACK_IMPORTED_MODULE_9__["ReactiveFormsModule"]
            ],
            providers: [],
            bootstrap: [_app_component__WEBPACK_IMPORTED_MODULE_4__["AppComponent"]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "./src/app/data.service.ts":
/*!*********************************!*\
  !*** ./src/app/data.service.ts ***!
  \*********************************/
/*! exports provided: DataService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DataService", function() { return DataService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");




var DataService = /** @class */ (function () {
    function DataService(http) {
        this.http = http;
    }
    DataService.prototype.getUsers = function () {
        var headers = new _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpHeaders"]().set("Authorization", "Basic YWRtaW46YWRtaW4=")
            .set("Content-Type", "application/json");
        console.log("In get User method");
        return this.http.get('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services', { headers: headers });
        //return this.http.get('http://127.0.0.1:8181/restconf/operational/tapi-common:context/tapi-topology:topology/mef:presto-nrp-topology',{headers});
    };
    DataService.prototype.createEvcServcie = function (data) {
        var body = JSON.stringify(data);
        console.log(JSON.stringify(data));
        console.log("----------In createEvcService Service  method");
        //  const  headers = new  HttpHeaders().set("Authorization", "Basic YWRtaW46YWRtaW4=")
        //   .set("Content-Type","application/json");
        // const httpOptions ={headers};
        return this.http.post('http://127.0.0.1:8181/restconf/config/mef-legato-services:mef-services/carrier-ethernet/subscriber-services', body, { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpHeaders"]({
                'Authorization': 'Basic YWRtaW46YWRtaW4=',
                'Content-Type': 'application/json'
            }) });
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
    };
    DataService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
            providedIn: 'root'
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpClient"]])
    ], DataService);
    return DataService;
}());



/***/ }),

/***/ "./src/app/get-comp/get-comp.component.css":
/*!*************************************************!*\
  !*** ./src/app/get-comp/get-comp.component.css ***!
  \*************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2dldC1jb21wL2dldC1jb21wLmNvbXBvbmVudC5jc3MifQ== */"

/***/ }),

/***/ "./src/app/get-comp/get-comp.component.html":
/*!**************************************************!*\
  !*** ./src/app/get-comp/get-comp.component.html ***!
  \**************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<nav>\n  <ul>\n      <li>\n          <a routerLink=\"post\">Post\n          </a>\n        </li>\n    <li>\n      <a routerLink=\"get\">Get\n      </a>\n    </li>\n    \n  </ul>\n</nav>"

/***/ }),

/***/ "./src/app/get-comp/get-comp.component.ts":
/*!************************************************!*\
  !*** ./src/app/get-comp/get-comp.component.ts ***!
  \************************************************/
/*! exports provided: GetCompComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "GetCompComponent", function() { return GetCompComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");


var GetCompComponent = /** @class */ (function () {
    function GetCompComponent() {
    }
    GetCompComponent.prototype.ngOnInit = function () {
    };
    GetCompComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-get-comp',
            template: __webpack_require__(/*! ./get-comp.component.html */ "./src/app/get-comp/get-comp.component.html"),
            styles: [__webpack_require__(/*! ./get-comp.component.css */ "./src/app/get-comp/get-comp.component.css")]
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [])
    ], GetCompComponent);
    return GetCompComponent;
}());



/***/ }),

/***/ "./src/app/post-comp/post-comp.component.css":
/*!***************************************************!*\
  !*** ./src/app/post-comp/post-comp.component.css ***!
  \***************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = ".wrapper {\n    text-align: center;\n}\n\n.button {\n    position: absolute;\n    top: 50%;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInNyYy9hcHAvcG9zdC1jb21wL3Bvc3QtY29tcC5jb21wb25lbnQuY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0lBQ0ksa0JBQWtCO0FBQ3RCOztBQUVBO0lBQ0ksa0JBQWtCO0lBQ2xCLFFBQVE7QUFDWiIsImZpbGUiOiJzcmMvYXBwL3Bvc3QtY29tcC9wb3N0LWNvbXAuY29tcG9uZW50LmNzcyIsInNvdXJjZXNDb250ZW50IjpbIi53cmFwcGVyIHtcbiAgICB0ZXh0LWFsaWduOiBjZW50ZXI7XG59XG5cbi5idXR0b24ge1xuICAgIHBvc2l0aW9uOiBhYnNvbHV0ZTtcbiAgICB0b3A6IDUwJTtcbn0iXX0= */"

/***/ }),

/***/ "./src/app/post-comp/post-comp.component.html":
/*!****************************************************!*\
  !*** ./src/app/post-comp/post-comp.component.html ***!
  \****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\n  <br />\n<form  (ngSubmit)='internalcreateEvcService()' [formGroup]='createEvcForm' novalidate class=\"form\">\n  <table>\n  <tr>\n    <td>\n    <label>Service Name </label>\n      </td>\n    <td>\n        <input formControlName='evcId' type=\"text\" class=\"form-control\" placeholder=\"Enter Service Name\" />\n    </td>\n  </tr><br>\n  <tr>\n      <td>\n      <label>Service Type </label>\n        </td>\n      <td>\n          <input type=\"radio\" class=\"form-control\" [(ngModel)]=\"serviceType\"  name=\"serviceType\"  formControlName='serviceType' value=\"EPL\">EPL &nbsp;&nbsp;\n          <input type=\"radio\" class=\"form-control\" [(ngModel)]=\"serviceType\"  name=\"serviceType\" formControlName='serviceType' value=\"EVPL\">EVPL &nbsp;&nbsp;\n          <input type=\"radio\" class=\"form-control\" [(ngModel)]=\"serviceType\"  name=\"serviceType\" formControlName='serviceType' value=\"EPLAN\">EP-LAN &nbsp;&nbsp;\n          <input type=\"radio\" class=\"form-control\" [(ngModel)]=\"serviceType\" name=\"serviceType\" formControlName='serviceType' value=\"EVPLAN\">EVP-LAN &nbsp;&nbsp;  \n      </td>\n    </tr><br>\n      <tr *ngIf=\"serviceType === 'EVPL' || serviceType === 'EVPLAN'\">\n          <td>\n          <label>VLAN ID</label>\n            </td>\n          <td>\n          <input formControlName='vlanId' type=\"text\" class=\"form-control\" placeholder=\"Enter VLAN ID\"/> \n          </td>\n        </tr><br>\n      <tr>\n<td><label>End Point 1</label></td>\n<td><input formControlName='node1'\n  type=\"text\"\n  class=\"form-control\"\n  placeholder=\"Enter Node 1\"/></td>\n  </tr><br>\n  <tr>\n      <td>\n<label>End Point 2</label>\n        </td>\n<td>\n    <input formControlName='node2'\n  type=\"text\"\n  class=\"form-control\"\n  placeholder=\"Enter Node 2\" />\n</td>\n  </tr><br>\n  \n  <tr>\n      <div class=\"wrapper\">\n          <button class=\"button\">Create Service</button>\n      </div>\n  </tr>\n  </table>\n</form>\n</div>\n"

/***/ }),

/***/ "./src/app/post-comp/post-comp.component.ts":
/*!**************************************************!*\
  !*** ./src/app/post-comp/post-comp.component.ts ***!
  \**************************************************/
/*! exports provided: PostCompComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "PostCompComponent", function() { return PostCompComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm5/forms.js");
/* harmony import */ var _data_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../data.service */ "./src/app/data.service.ts");




var PostCompComponent = /** @class */ (function () {
    // data :DataService;
    function PostCompComponent(data) {
        this.data = data;
    }
    PostCompComponent.prototype.ngOnInit = function () {
        this.createEvcForm = new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormGroup"]({
            node1: new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormControl"](),
            node2: new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormControl"](),
            evcId: new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormControl"](),
            serviceType: new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormControl"](),
            vlanId: new _angular_forms__WEBPACK_IMPORTED_MODULE_2__["FormControl"]()
        });
    };
    PostCompComponent.prototype.internalcreateEvcService = function () {
        console.log(this.createEvcForm.value['node1']);
        console.log(this.createEvcForm.value['node2']);
        var connectionType;
        var vlanId = this.createEvcForm.value['vlanId'];
        if (this.createEvcForm.value['serviceType'] === 'EPL' || this.createEvcForm.value['serviceType'] === 'EVPL') {
            connectionType = "point-to-point";
        }
        else {
            connectionType = "multipoint-to-multipoint";
        }
        if (this.createEvcForm.value['serviceType'] === 'EPL' || this.createEvcForm.value['serviceType'] === 'EPLAN') {
            vlanId = '1';
        }
        var rawdata = {};
        var arr = [];
        var cosName = [];
        var cosNames = {};
        cosName.push({ "mef-legato-services:name": this.createEvcForm.value['serviceType'] });
        cosNames["mef-legato-services:cos-name"] = cosName;
        var endPoints = {};
        var endPoint = [];
        var ceVlans = {};
        var ceVlan = [];
        ceVlan.push(vlanId);
        ceVlans["mef-legato-services:ce-vlan"] = ceVlan;
        var bwpPerCos = {};
        var bwpFlowperCos = [];
        var bwpFlower = {
            "mef-legato-services:cos-name": "Neon",
            "envelope-id": "Rank 0",
            "rank": "0"
        };
        bwpFlowperCos.push(bwpFlower);
        bwpPerCos["mef-legato-services:bwp-flow-per-cos"] = bwpFlowperCos;
        var bwpPerEcc = {};
        var bwpFlowPerEcc = [];
        var bwpFlow = {
            "mef-legato-services:eec-name": "EEC-Krypton",
            "envelope-id": "Rank 0",
            "rank": "0"
        };
        bwpFlowPerEcc.push(bwpFlow);
        bwpPerEcc["mef-legato-services:bwp-flow-per-eec"] = bwpFlowPerEcc;
        var endpoint1 = {
            "mef-legato-services:uni-id": this.createEvcForm.value['node1'],
            "mef-legato-services:role": "root",
            "mef-legato-services:admin-state": "true",
            "mef-legato-services:color-identifier": "COLID1",
            "mef-legato-services:cos-identifier": "COSID1",
            "mef-legato-services:eec-identifier": "EECID1",
            "mef-legato-services:source-mac-address-limit": "1",
            "mef-legato-services:source-mac-address-limit-time-interval": "1",
            "mef-legato-services:test-meg-enabled": "false",
            "mef-legato-services:user-label": "admin",
            "mef-legato-services:subscriber-meg-mip-enabled": "false",
            "mef-legato-services:ce-vlans": ceVlans,
            "ingress-bwp-per-cos": bwpPerCos,
            "egress-bwp-per-eec": bwpPerEcc
        };
        var endpoint2 = {
            "mef-legato-services:uni-id": this.createEvcForm.value['node2'],
            "mef-legato-services:role": "root",
            "mef-legato-services:admin-state": "true",
            "mef-legato-services:color-identifier": "COLID1",
            "mef-legato-services:cos-identifier": "COSID1",
            "mef-legato-services:eec-identifier": "EECID1",
            "mef-legato-services:source-mac-address-limit": "1",
            "mef-legato-services:source-mac-address-limit-time-interval": "1",
            "mef-legato-services:test-meg-enabled": "false",
            "mef-legato-services:user-label": "admin",
            "mef-legato-services:subscriber-meg-mip-enabled": "false",
            "mef-legato-services:ce-vlans": ceVlans,
            "ingress-bwp-per-cos": bwpPerCos,
            "egress-bwp-per-eec": bwpPerEcc
        };
        endPoint.push(endpoint1);
        endPoint.push(endpoint2);
        endPoints['mef-legato-services:end-point'] = endPoint;
        var uniExclusion = {};
        var serviceEndPointPair = [];
        var serviceEndPointPair1 = {
            "mef-legato-services:end-point1": "1",
            "mef-legato-services:end-point2": "2"
        };
        serviceEndPointPair.push(serviceEndPointPair1);
        uniExclusion["mef-legato-services:end-point-pair"] = serviceEndPointPair;
        var endPointPairs = {};
        var endPointPair = [];
        var endPointPair1 = {
            "mef-legato-services:index": "0",
            "sls-uni-exclusions": uniExclusion,
        };
        endPointPair.push(endPointPair1);
        endPointPairs["mef-legato-services:set-of-end-point-pairs"] = endPointPair;
        var legetoServicePointPairs = {
            "sls-uni-exclusions": uniExclusion
        };
        var cosEtntries = {};
        var cosEntry = [];
        var pmEtntries = {};
        var pmEntry = [];
        var pmEntry1 = {
            "mef-legato-services:pm-entry-id": "68",
            "mef-legato-services:sets-of-end-point-pairs": endPointPairs,
            "mef-legato-services:end-point-pairs": legetoServicePointPairs
        };
        pmEntry.push(pmEntry1);
        pmEtntries["mef-legato-services:pm-entry"] = pmEntry;
        var cosEntry1 = {
            "mef-legato-services:cos-name": "Krypton",
            "mef-legato-services:pm-entries": pmEtntries
        };
        cosEntry.push(cosEntry1);
        cosEtntries["mef-legato-services:cos-entry"] = cosEntry;
        var carrierEthernetsls = {
            "mef-legato-services:sls-id": "SLSID1",
            "mef-legato-services:start-time": "9372-84-27T31:46:58Z",
            "mef-legato-services:cos-entries": cosEtntries
        };
        var obj = {
            "mef-legato-services:evc-id": this.createEvcForm.value['evcId'],
            "mef-legato-services:cos-names": cosNames,
            "mef-legato-services:end-points": endPoints,
            "mef-legato-services:carrier-ethernet-sls": carrierEthernetsls,
            "mef-legato-services:connection-type": connectionType,
            "mef-legato-services:admin-state": "true",
            "mef-legato-services:user-label": "U4",
            "mef-legato-services:max-frame-size": "1522",
            "mef-legato-services:max-num-of-evc-end-point": "2",
            "mef-legato-services:ce-vlan-id-preservation": "false",
            "mef-legato-services:ce-vlan-pcp-preservation": "false",
            "mef-legato-services:ce-vlan-dei-preservation": "false",
            "mef-legato-services:unicast-frame-delivery": "unconditional",
            "mef-legato-services:multicast-frame-delivery": "unconditional",
            "mef-legato-services:broadcast-frame-delivery": "unconditional",
            "mef-legato-services:svc-type": this.createEvcForm.value['serviceType'].toLowerCase()
        };
        arr.push(obj);
        rawdata["mef-legato-services:evc"] = arr;
        console.log("Row data is ---->", rawdata);
        (this.data.createEvcServcie(rawdata)).subscribe(function (error) { return console.log(error); });
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
        console.log("-------------In createEvcService Service  end method");
    };
    PostCompComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-post-comp',
            template: __webpack_require__(/*! ./post-comp.component.html */ "./src/app/post-comp/post-comp.component.html"),
            styles: [__webpack_require__(/*! ./post-comp.component.css */ "./src/app/post-comp/post-comp.component.css")]
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_data_service__WEBPACK_IMPORTED_MODULE_3__["DataService"]])
    ], PostCompComponent);
    return PostCompComponent;
}());



/***/ }),

/***/ "./src/app/user-comp/user-comp.component.css":
/*!***************************************************!*\
  !*** ./src/app/user-comp/user-comp.component.css ***!
  \***************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL3VzZXItY29tcC91c2VyLWNvbXAuY29tcG9uZW50LmNzcyJ9 */"

/***/ }),

/***/ "./src/app/user-comp/user-comp.component.html":
/*!****************************************************!*\
  !*** ./src/app/user-comp/user-comp.component.html ***!
  \****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "Evc Id : {{evc$}}\n<br/>\n<br/>\nService Type : {{evcType$}}\n<br/>\n<br/>\nVlan ID : {{vlan$}}\n<br/>\n<br/>\nEnd Point Uni-Id\n<ul>\n    <li *ngFor=\"let end of endPoint$\">\n     {{end['uni-id']}}   \n    </li>\n</ul>"

/***/ }),

/***/ "./src/app/user-comp/user-comp.component.ts":
/*!**************************************************!*\
  !*** ./src/app/user-comp/user-comp.component.ts ***!
  \**************************************************/
/*! exports provided: UserCompComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UserCompComponent", function() { return UserCompComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _data_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../data.service */ "./src/app/data.service.ts");



var UserCompComponent = /** @class */ (function () {
    function UserCompComponent(data) {
        var _this = this;
        this.data = data;
        this.data.getUsers().subscribe(function (data) {
            _this.users$ = data;
            _this.result$ = data['subscriber-services'];
            _this.users1$ = _this.result$['evc'];
            _this.evc$ = _this.users1$[0]['evc-id'];
            _this.evcType$ = _this.users1$[0]['svc-type'];
            _this.endPoints$ = _this.users1$[0]['end-points'];
            _this.endPoint$ = _this.endPoints$['end-point'];
            _this.ceVlans$ = _this.endPoint$[0]['ce-vlans'];
            _this.ceVlan$ = _this.ceVlans$['ce-vlan'];
            _this.vlanId$ = _this.ceVlan$[0];
            if (_this.evcType$ === 'epl' || _this.evcType$ === 'eplan') {
                _this.vlan$ = '';
            }
            else {
                _this.vlan$ = _this.vlanId$.toString();
            }
            _this.evcType$ = _this.evcType$.toString().toUpperCase();
        });
        console.log(this.endPoint$);
    }
    UserCompComponent.prototype.ngOnInit = function () {
    };
    UserCompComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-user-comp',
            template: __webpack_require__(/*! ./user-comp.component.html */ "./src/app/user-comp/user-comp.component.html"),
            styles: [__webpack_require__(/*! ./user-comp.component.css */ "./src/app/user-comp/user-comp.component.css")]
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_data_service__WEBPACK_IMPORTED_MODULE_2__["DataService"]])
    ], UserCompComponent);
    return UserCompComponent;
}());



/***/ }),

/***/ "./src/environments/environment.ts":
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/*! exports provided: environment */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "environment", function() { return environment; });
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
var environment = {
    production: false
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.


/***/ }),

/***/ "./src/main.ts":
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser-dynamic */ "./node_modules/@angular/platform-browser-dynamic/fesm5/platform-browser-dynamic.js");
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./app/app.module */ "./src/app/app.module.ts");
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./environments/environment */ "./src/environments/environment.ts");




if (_environments_environment__WEBPACK_IMPORTED_MODULE_3__["environment"].production) {
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_0__["enableProdMode"])();
}
Object(_angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__["platformBrowserDynamic"])().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_2__["AppModule"])
    .catch(function (err) { return console.error(err); });


/***/ }),

/***/ 0:
/*!***************************!*\
  !*** multi ./src/main.ts ***!
  \***************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /home/mef-dev/unimgr/legato-ui/src/main/legato-ui/src/main.ts */"./src/main.ts");


/***/ })

},[[0,"runtime","vendor"]]]);
//# sourceMappingURL=main.js.map