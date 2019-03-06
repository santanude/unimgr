import * as tslib_1 from "tslib";
import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { DataService } from '../data.service';
var PostCompComponent = /** @class */ (function () {
    // data :DataService;
    function PostCompComponent(data) {
        this.data = data;
    }
    PostCompComponent.prototype.ngOnInit = function () {
        this.createEvcForm = new FormGroup({
            node1: new FormControl(),
            node2: new FormControl()
        });
    };
    PostCompComponent.prototype.internalcreateEvcService = function () {
        console.log(this.createEvcForm.value['node1']);
        console.log(this.createEvcForm.value['node2']);
        var rawdata = {};
        var arr = [];
        var cosName = [];
        var cosNames = {};
        cosName.push({ "mef-legato-services:name": "EVPL" });
        cosNames["mef-legato-services:cos-name"] = cosName;
        var endPoints = {};
        var endPoint = [];
        var ceVlans = {};
        var ceVlan = [];
        ceVlan.push("301");
        ceVlans["mef-legato-services:ce-vlan"] = ceVlan;
        var bwpPerCos = {};
        var bwpFlowperCos = [];
        bwpFlowperCos.push({ "mef-legato-services:cos-name": "Neon" });
        bwpFlowperCos.push({ "envelope-id": "Rank 0" });
        bwpFlowperCos.push({ "rank": "0" });
        bwpPerCos["mef-legato-services:bwp-flow-per-cos"] = bwpFlowperCos;
        var bwpPerEcc = {};
        var bwpFlowPerEcc = [];
        bwpFlowPerEcc.push({ "mef-legato-services:eec-name": "EEC-Krypton" });
        bwpFlowPerEcc.push({ "envelope-id": "Rank 0" });
        bwpFlowPerEcc.push({ "rank": "0" });
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
        serviceEndPointPair.push({ serviceEndPointPair1: serviceEndPointPair1 });
        uniExclusion["mef-legato-services:end-point-pair"] = serviceEndPointPair;
        var endPointPairs = {};
        var endPointPair = [];
        var endPointPair1 = {
            "mlef-legato-services:index": "0",
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
            "mef-legato-services:set-of-end-point-pairs": endPointPairs,
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
        arr.push({ "mef-legato-services:evc-id": "{{evc_id}}" });
        arr.push({ "mef-legato-services:cos-names": cosNames });
        arr.push({ "mef-legato-services:end-points": endPoints });
        arr.push({ "mef-legato-services:carrier-ethernet-sls": carrierEthernetsls });
        arr.push({ "mef-legato-services:connection-type": "point-to-point" });
        arr.push({ "mef-legato-services:admin-state": "true" });
        arr.push({ "mef-legato-services:user-label": "U4" });
        arr.push({ "mef-legato-services:max-frame-size": "1522" });
        arr.push({ "mef-legato-services:max-num-of-evc-end-point": "2" });
        arr.push({ "mef-legato-services:ce-vlan-id-preservation": "false" });
        arr.push({ "mef-legato-services:ce-vlan-pcp-preservation": "false" });
        arr.push({ "mef-legato-services:ce-vlan-dei-preservation": "false" });
        arr.push({ "mef-legato-services:unicast-frame-delivery": "unconditional" });
        arr.push({ "mef-legato-services:multicast-frame-delivery": "unconditional" });
        arr.push({ "mef-legato-services:broadcast-frame-delivery": "unconditional" });
        arr.push({ "mef-legato-services:svc-type": "epl" });
        rawdata["mef-legato-services:evc"] = arr;
        console.log("Row data is ---->" + rawdata);
        this.data.createEvcServcie();
    };
    PostCompComponent = tslib_1.__decorate([
        Component({
            selector: 'app-post-comp',
            templateUrl: './post-comp.component.html',
            styleUrls: ['./post-comp.component.css']
        }),
        tslib_1.__metadata("design:paramtypes", [DataService])
    ], PostCompComponent);
    return PostCompComponent;
}());
export { PostCompComponent };
//# sourceMappingURL=post-comp.component.js.map