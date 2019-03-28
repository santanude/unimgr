import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import  {UserCompComponent} from './user-comp/user-comp.component';
import  {PostCompComponent} from './post-comp/./post-comp.component';
import {PostSuccessCompComponent} from './post-success-comp/./post-success-comp.component';
const routes: Routes = [
  {
    path:"get",
    component:UserCompComponent
  },
  {
    path:"post",
    component:PostCompComponent
  },
  {
    path:"success",
    component:PostSuccessCompComponent
  } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
