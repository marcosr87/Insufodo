import { Injectable } from '@angular/core'
import { Student } from '../models/student'
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
    providedIn: 'root'
})
export class StudentService {

    private url = 'http://localhost:8080/student'

    constructor(private http: HttpClient) { }

    add(student: Student): Observable<any> {
        return this.http.post(this.url, student)
    }

    getFromTo(page, size): Observable<any> {
        return this.http.get(this.url + '?page=' + page + '&size=' + size)
    }

    getTotal(): Observable<any> {
        return this.http.get(this.url + '/total')
    }

    getById(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id)
    }

    getByDni(dni: string): Observable<any> {
        return this.http.get(this.url + '/dni/' + dni)
    }

    getInscriptions(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/inscriptions')
    }

    edit(student: Student): Observable<any> {
        return this.http.post(this.url + '/' + student.id + '/update', student)
    }

    delete(id: number): Observable<any> {
        return this.http.post(this.url + '/' + id + '/delete', null)
    }

    checkDni(dni: string): Observable<HttpResponse<any>> {
        return this.http.get(this.url + '/identityDNI', {
            params: new HttpParams().set('dni', dni),
            observe: 'response'
        })
    }

    checkEmail(email: string): Observable<HttpResponse<any>> {
        return this.http.get(this.url + '/identityEmail', {
            params: new HttpParams().set('email', email),
            observe: 'response'
        })
    }

    getStudentManual(): Observable<ArrayBuffer> {
        const params = new HttpParams().set('fileName', 'StudentManual.mp4')
        const options = { params, responseType: 'arraybuffer' as 'arraybuffer' }
        return this.http.get(this.url + '/studentManual', options)
    }
}