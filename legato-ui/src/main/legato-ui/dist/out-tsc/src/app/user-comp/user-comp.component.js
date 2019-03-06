import * as tslib_1 from "tslib";
import { Component } from '@angular/core';
import { DataService } from '../data.service';
var UserCompComponent = /** @class */ (function () {
    function UserCompComponent(data) {
        var _this = this;
        this.data = data;
        this.data.getUsers().subscribe(function (data) {
            _this.users$ = data;
            //debugger
            // this.result$ = data['subscriber-services']['evc'];
            var result1 = data['tapi-topology:topology'];
            var uu1 = result1[0].uuid;
            console.log("hello" + uu1);
            var uu2 = result1[0]['node'];
            _this.result$ = uu2[0]['owned-node-edge-point'];
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
        console.log(this.users$);
    }
    UserCompComponent.prototype.ngOnInit = function () {
    };
    UserCompComponent = tslib_1.__decorate([
        Component({
            selector: 'app-user-comp',
            templateUrl: './user-comp.component.html',
            styleUrls: ['./user-comp.component.css']
        }),
        tslib_1.__metadata("design:paramtypes", [DataService])
    ], UserCompComponent);
    return UserCompComponent;
}());
export { UserCompComponent };
//# sourceMappingURL=user-comp.component.js.map