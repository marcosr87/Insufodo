import { Injectable } from '@angular/core'
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http'
import { User } from '../models/user'
import { Observable } from 'rxjs'
import { map } from 'rxjs/operators'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = 'http://localhost:8080/user'

  redirectUrl: string
  dni: string

  constructor(private http: HttpClient) { }

  signup(user: User): Observable<any> {
    return this.http.post(this.url, user)
  }

  login(user: User): Observable<any> {
    this.dni = user.password
    return this.http.post(this.url + '/login', user, { responseType: 'text' }).pipe(
      map(response => {
        if (response !== 'ERROR') {
          this.setToken(response)
        } else {
          alert('Error: contraseña incorrecta.')
        }
      })
    )
  }

  logout() {
    this.setToken('')
  }

  checkEmail(email: string): Observable<HttpResponse<any>> {
    return this.http.get(this.url + '/identity', {
      params: new HttpParams().set('email', email),
      observe: 'response'
    })
  }

  getUserManual(): Observable<ArrayBuffer> {
    const params = new HttpParams().set('fileName', 'UserManual.pdf')
    const options = {params, responseType: 'arraybuffer' as 'arraybuffer'}
    return this.http.get(this.url + '/userManual', options)
  }

  setToken(response: any) { localStorage.setItem('token', response ? response : '') }
  getToken() { return localStorage.getItem('token') }
}