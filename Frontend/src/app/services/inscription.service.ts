import { Injectable } from '@angular/core'
import { Inscription } from '../models/inscription'
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
    providedIn: 'root'
})
export class InscriptionService {

    private url = 'http://localhost:8080/inscription'

    constructor(private http: HttpClient) { }

    add(inscription: Inscription): Observable<any> {
        return this.http.post(this.url, inscription)
    }

    getAll(): Observable<any> {
        return this.http.get(this.url)
    }

    getById(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id)
    }

    getCohort(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/cohort')
    }

    getStudent(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/student')
    }

    edit(inscription: Inscription): Observable<any> {
        return this.http.post(this.url + '/' + inscription.id + '/update', inscription)
    }

    delete(id: number): Observable<any> {
        return this.http.post(this.url + '/' + id + '/delete', null)
    }
}