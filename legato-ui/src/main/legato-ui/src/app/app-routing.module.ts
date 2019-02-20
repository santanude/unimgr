import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import  {UserCompComponent} from './user-comp/user-comp.component';
import  {PostCompComponent} from './post-comp/./post-comp.component';
const routes: Routes = [
  {
    path:"",
    component:UserCompComponent
  },
  {
    path:"post",
    component:PostCompComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
