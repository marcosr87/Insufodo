import { BrowserModule } from '@angular/platform-browser'
import { NgModule } from '@angular/core'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module'
import { InterceptorService } from './services/interceptor.service'
import { DatePipe } from '@angular/common'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'

import { AppComponent } from './app.component'
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
import { PaginatorComponent } from './components/paginator/paginator.component'

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    UserSignupComponent,
    StudentAddComponent,
    StudentEditComponent,
    StudentListComponent,
    StudentViewComponent,
    SubjectAddComponent,
    SubjectEditComponent,
    SubjectListComponent,
    SubjectViewComponent,
    CohortAddComponent,
    CohortEditComponent,
    CohortListComponent,
    CohortViewComponent,
    InscriptionAddComponent,
    InscriptionListComponent,
    PaginatorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true
    },
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }