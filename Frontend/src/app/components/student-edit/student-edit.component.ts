import { Component, OnInit } from '@angular/core'
import { Student } from 'src/app/models/student'
import { FormControl, Validators, FormGroup, AbstractControl, ValidationErrors } from '@angular/forms'
import { StudentService } from 'src/app/services/student.service'
import { ActivatedRoute, Router } from '@angular/router'
import { of, Observable } from 'rxjs'
import { map, catchError } from 'rxjs/operators'

@Component({
  selector: 'app-student-edit',
  templateUrl: './student-edit.component.html',
  styleUrls: ['./student-edit.component.css']
})
export class StudentEditComponent implements OnInit {

  student = new Student()
  studentForm: FormGroup

  constructor(private studentService: StudentService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.studentForm = new FormGroup({
      'dni': new FormControl(this.student.dni, { validators: [Validators.required, Validators.pattern('^[0-9]*$'), Validators.minLength(8)], asyncValidators: this.checkDni.bind(this), updateOn: 'blur' }),
      'lastName': new FormControl(this.student.lastName, Validators.required),
      'firstName': new FormControl(this.student.firstName, Validators.required),
      'email': new FormControl(this.student.email, { validators: [Validators.required, Validators.email], asyncValidators: this.checkEmail.bind(this), updateOn: 'blur' }),
      'cohort': new FormControl(this.student.cohort, [Validators.required, Validators.pattern('^[0-9]*$'), Validators.minLength(4)]),
      'status': new FormControl(this.student.status),
      'gender': new FormControl(this.student.gender),
      'address': new FormControl(this.student.address),
      'phone': new FormControl(this.student.phone, Validators.pattern('^[0-9]*$'))
    })

    this.studentService.getById(id).subscribe(response => {
      this.student = response
      this.dni.setValue(this.student.dni)
      this.lastName.setValue(this.student.lastName)
      this.firstName.setValue(this.student.firstName)
      this.email.setValue(this.student.email)
      this.cohort.setValue(this.student.cohort)
      this.status.setValue(this.student.status)
      this.gender.setValue(this.student.gender)
      this.address.setValue(this.student.address)
      this.phone.setValue(this.student.phone)
      document.getElementsByTagName('input')[0].focus()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  get dni() { return this.studentForm.get('dni') }
  get lastName() { return this.studentForm.get('lastName') }
  get firstName() { return this.studentForm.get('firstName') }
  get email() { return this.studentForm.get('email') }
  get cohort() { return this.studentForm.get('cohort') }
  get status() { return this.studentForm.get('status') }
  get gender() { return this.studentForm.get('gender') }
  get address() { return this.studentForm.get('address') }
  get phone() { return this.studentForm.get('phone') }

  statusChange(event: any) {
    this.student.status = event.target.value
  }

  genderChange(event: any) {
    this.student.gender = event.target.value
  }

  checkDni(dni: AbstractControl): Observable<ValidationErrors | null> {
    if (dni.value === this.student.dni) {
      return of(null)
    } else {
      return this.studentService.checkDni(dni.value).pipe(
        map(() => null),
        catchError(err => {
          if (err.status === 409) {
            return of({
              dniTaken: true
            })
          }
          return of(null)
        })
      )
    }
  }

  checkEmail(email: AbstractControl): Observable<ValidationErrors | null> {
    if (email.value === this.student.email) {
      return of(null)
    } else {
      return this.studentService.checkEmail(email.value).pipe(
        map(() => null),
        catchError(err => {
          if (err.status === 409) {
            return of({
              emailTaken: true
            })
          }
          return of(null)
        })
      )
    }
  }

  editStudent() {
    let student = new Student();
    student.id = this.student.id
    student.dni = this.dni.value
    student.lastName = this.lastName.value
    student.firstName = this.firstName.value
    student.email = this.email.value
    student.cohort = this.cohort.value
    student.status = this.status.value
    student.gender = this.gender.value
    student.address = this.address.value
    student.phone = this.phone.value

    this.studentService.edit(student).subscribe(() => {
      alert('Modificación Exitosa!')
      this.router.navigateByUrl('/studentList')
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }
}