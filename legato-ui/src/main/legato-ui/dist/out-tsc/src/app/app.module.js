import * as tslib_1 from "tslib";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GetCompComponent } from './get-comp/get-comp.component';
import { UserCompComponent } from './user-comp/user-comp.component';
import { HttpClientModule } from '@angular/common/http';
import { PostCompComponent } from './post-comp/post-comp.component';
import { ReactiveFormsModule } from '@angular/forms';
var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = tslib_1.__decorate([
        NgModule({
            declarations: [
                AppComponent,
                GetCompComponent,
                UserCompComponent,
                PostCompComponent
            ],
            imports: [
                BrowserModule,
                AppRoutingModule,
                HttpClientModule,
                ReactiveFormsModule
            ],
            providers: [],
            bootstrap: [AppComponent]
        })
    ], AppModule);
    return AppModule;
}());
export { AppModule };
//# sourceMappingURL=app.module.js.map