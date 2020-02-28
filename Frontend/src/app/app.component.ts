import { Component, OnInit } from '@angular/core'
import { UserService } from './services/user.service'
import { Router } from '@angular/router'
import { saveAs } from 'file-saver'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  logged: boolean
  student: boolean
  dni: string

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.logged = false
    this.student = false
  }

  getUserManual() {
    this.userService.getUserManual().subscribe((response) => {
      saveAs(new Blob([response], { type: 'application/pdf' }), 'UserManual.pdf')
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  logout() {
    this.logged = false
    this.student = false
    this.userService.logout()
    setTimeout(() => { this.router.navigateByUrl("/login") }, 500)
  }

  public onRouterOutletActivate(event) {
    if (event.router.url === "/login") {
      this.logged = false
      this.student = false
    } else {
      this.logged = true
      if (event.router.url.substring(0, 16) === "/inscriptionList" || event.router.url.substring(0, 15) === "/inscriptionAdd") {
        this.dni = this.userService.dni
        this.student = true
      }
    }
  }
}