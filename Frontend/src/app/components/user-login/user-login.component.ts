import { Component, OnInit } from '@angular/core'
import { User } from 'src/app/models/user'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { UserService } from 'src/app/services/user.service'
import { StudentService } from 'src/app/services/student.service'
import { Router } from '@angular/router'
import { saveAs } from 'file-saver'

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  user = new User()
  loginForm: FormGroup

  constructor(private userService: UserService, private studentService: StudentService, private router: Router) { }

  ngOnInit() {
    this.user.name = ''
    this.user.password = ''
    this.loginForm = new FormGroup({
      'name': new FormControl(this.user.name, [Validators.required, Validators.email]),
      'password': new FormControl(this.user.password, Validators.required)
    });
    document.getElementsByTagName('input')[0].focus()
  }

  get name() { return this.loginForm.get('name') }
  get password() { return this.loginForm.get('password') }

  login() {
    let user = new User()
    user.name = this.name.value
    user.password = this.password.value

    this.userService.login(user).subscribe(() => {
      if (this.userService.getToken()) {
        let redirect = null
        if (/^\d+$/.test(user.password)) {
          redirect = '/inscriptionList/' + user.password
        } else {
          redirect = this.userService.redirectUrl ? this.router.parseUrl(this.userService.redirectUrl) : '/studentList'
        }
        this.router.navigateByUrl(redirect)
      }
    }, error => {
      console.error(error)
      if (error.status === 500) {
        alert('Error: usuario ' + user.name + ' no encontrado.')
      }
    })
  }

  getStudentManual() {
    this.studentService.getStudentManual().subscribe((response) => {
      saveAs(new Blob([response], { type: 'application/mp4' }), 'StudentManual.mp4')
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }
}