import { Component, OnInit } from '@angular/core'
import { User } from 'src/app/models/user'
import { UserService } from 'src/app/services/user.service'
import { FormControl, FormGroup, Validators, ValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms'
import { Router } from '@angular/router'
import { Observable, of } from 'rxjs'
import { map, catchError } from 'rxjs/operators'

@Component({
  selector: 'app-user-signup',
  templateUrl: './user-signup.component.html',
  styleUrls: ['./user-signup.component.css']
})
export class UserSignupComponent implements OnInit {

  user = new User()
  checkPassword: string
  signupForm: FormGroup

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.user.name = ''
    this.user.password = ''
    this.checkPassword = ''
    this.signupForm = new FormGroup({
      'name': new FormControl(this.user.name, { validators: [Validators.required, Validators.email], asyncValidators: this.checkEmail.bind(this), updateOn: 'blur' }),
      'password': new FormControl(this.user.password, Validators.required),
      'confirmPassword': new FormControl(this.checkPassword, Validators.required)
    }, { validators: [this.passwordsMatchValidator(), this.validatePassword()] })
    document.getElementsByTagName('input')[0].focus()
  }

  get name() { return this.signupForm.get('name') }
  get password() { return this.signupForm.get('password') }
  get confirmPassword() { return this.signupForm.get('confirmPassword') }

  passwordChange(event: any) {
    this.user.password = event.target.value
  }

  confirmChange(event: any) {
    this.checkPassword = event.target.value
  }

  passwordsMatchValidator(): ValidatorFn {
    return (control: FormGroup): ValidationErrors | null => {
      const password = control.get('password')
      const confirmPassword = control.get('confirmPassword')
      return password && confirmPassword && password.value === confirmPassword.value ? null : { passwordsDoNotMatch: true }
    }
  }

  passwordsDoNotMatch() {
    if (this.signupForm.errors) {
      return this.signupForm.errors.passwordsDoNotMatch
    }
    return false
  }

  checkEmail(email: AbstractControl): Observable<ValidationErrors | null> {
    return this.userService.checkEmail(email.value).pipe(
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

  validatePassword(): ValidatorFn {
    return (control: FormGroup): ValidationErrors | null => {
      const password = control.get('password')
        return /(?=.*[A-Za-z])/.test(password.value) ? null : { invalidPassword: true }
    }
  }

  invalidPassword() {
    if (this.signupForm.errors) {
      return this.signupForm.errors.invalidPassword
    }
    return false
  }

  signup() {
    let user = new User()
    user.name = this.name.value
    user.password = this.password.value

    this.userService.signup(user).subscribe(() => {
      alert('Alta Exitosa!')
      this.router.navigateByUrl('/login')
    }, error => {
      if (error.status === 409) {
        alert('Error: el usuario: ' + user.name + ' ya existe!')
      } else {
        console.error(error)
        alert('Error: ' + error.error.message)
      }
    })
  }
}