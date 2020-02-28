import { NgModule } from '@angular/core'
import { Routes, RouterModule } from '@angular/router'
import { AuthGuard } from './auth/auth.guard'
import { UserLoginComponent } from './components/user-login/user-login.component'
import { UserSignupComponent } from './components/user-signup/user-signup.component'
import { StudentAddComponent } from './components/student-add/student-add.component'
import { StudentEditComponent } from './components/student-edit/student-edit.component'
import { StudentListComponent } from './components/student-list/student-list.component'
import { StudentViewComponent } from './components/student-view/student-view.component'
import { SubjectAddComponent } from './components/subject-add/subject-add.component'
import { SubjectEditComponent } from './components/subject-edit/subject-edit.component'
import { SubjectListComponent } from './components/subject-list/subject-list.component'
import { SubjectViewComponent } from './components/subject-view/subject-view.component'
import { CohortAddComponent } from './components/cohort-add/cohort-add.component'
import { CohortEditComponent } from './components/cohort-edit/cohort-edit.component'
import { CohortListComponent } from './components/cohort-list/cohort-list.component'
import { CohortViewComponent } from './components/cohort-view/cohort-view.component'
import { InscriptionAddComponent } from './components/inscription-add/inscription-add.component'
import { InscriptionListComponent } from './components/inscription-list/inscription-list.component'

const routes: Routes = [
  { path: 'login', component: UserLoginComponent },
  { path: 'signup', component: UserSignupComponent, canActivate: [AuthGuard] },
  { path: 'studentAdd', component: StudentAddComponent, canActivate: [AuthGuard] },
  { path: 'studentEdit/:id', component: StudentEditComponent, canActivate: [AuthGuard] },
  { path: 'studentList', component: StudentListComponent, canActivate: [AuthGuard] },
  { path: 'studentView/:id', component: StudentViewComponent, canActivate: [AuthGuard] },
  { path: 'subjectAdd', component: SubjectAddComponent, canActivate: [AuthGuard] },
  { path: 'subjectEdit/:id', component: SubjectEditComponent, canActivate: [AuthGuard] },
  { path: 'subjectList', component: SubjectListComponent, canActivate: [AuthGuard] },
  { path: 'subjectView/:id', component: SubjectViewComponent, canActivate: [AuthGuard] },
  { path: 'cohortAdd', component: CohortAddComponent, canActivate: [AuthGuard] },
  { path: 'cohortEdit/:id', component: CohortEditComponent, canActivate: [AuthGuard] },
  { path: 'cohortList', component: CohortListComponent, canActivate: [AuthGuard] },
  { path: 'cohortView/:id', component: CohortViewComponent, canActivate: [AuthGuard] },
  { path: 'inscriptionAdd/:dni', component: InscriptionAddComponent, canActivate: [AuthGuard] },
  { path: 'inscriptionList/:dni', component: InscriptionListComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }