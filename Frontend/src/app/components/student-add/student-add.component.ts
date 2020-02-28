import { Component, OnInit } from '@angular/core'
import { Student } from 'src/app/models/student'
import { FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms'
import { StudentService } from 'src/app/services/student.service'
import { Router } from '@angular/router'
import { Observable, of } from 'rxjs'
import { map, catchError } from 'rxjs/operators'

@Component({
  selector: 'app-student-add',
  templateUrl: './student-add.component.html',
  styleUrls: ['./student-add.component.css']
})
export class StudentAddComponent implements OnInit {

  student = new Student()
  studentForm: FormGroup

  constructor(private studentService: StudentService, private router: Router) { }

  ngOnInit() {
    this.student.dni = ''
    this.student.lastName = ''
    this.student.firstName = ''
    this.student.email = ''
    this.student.cohort = new Date().getFullYear()
    this.student.status = 'Activo'
    this.student.gender = ''
    this.student.address = ''
    this.student.phone = ''

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
    document.getElementsByTagName('input')[0].focus()
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

  checkEmail(email: AbstractControl): Observable<ValidationErrors | null> {
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

  addStudent() {
    let student = new Student()
    student.dni = this.dni.value
    student.lastName = this.lastName.value
    student.firstName = this.firstName.value
    student.email = this.email.value
    student.cohort = this.cohort.value
    student.status = this.status.value
    student.gender = this.gender.value
    student.address = this.address.value
    student.phone = this.phone.value

    this.studentService.add(student).subscribe(() => {
      this.dni.setValue('')
      this.lastName.setValue('')
      this.firstName.setValue('')
      this.email.setValue('')
      this.cohort.setValue(new Date().getFullYear())
      this.status.setValue('Activo')
      this.gender.setValue('')
      this.address.setValue('')
      this.phone.setValue('')
      alert('Alta Exitosa!')
      document.getElementsByTagName("input")[0].focus()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
      document.getElementsByTagName('input')[0].focus()
    })
  }
}