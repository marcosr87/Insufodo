import { Injectable } from '@angular/core'
import { Subject } from '../models/subject'
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
    providedIn: 'root'
})
export class SubjectService {

    private url = 'http://localhost:8080/subject'

    constructor(private http: HttpClient) { }

    add(subject: Subject): Observable<any> {
        return this.http.post(this.url, subject)
    }

    getAll(): Observable<any> {
        return this.http.get(this.url + '/getAll')
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

    getCorrelatives(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/correlatives')
    }

    edit(subject: Subject): Observable<any> {
        return this.http.post(this.url + '/' + subject.id + '/update', subject)
    }

    delete(id: number): Observable<any> {
        return this.http.post(this.url + '/' + id + '/delete', null)
    }

    checkName(name: string): Observable<HttpResponse<any>> {
        return this.http.get(this.url + '/identity', {
            params: new HttpParams().set('name', name),
            observe: 'response'
        })
    }
}