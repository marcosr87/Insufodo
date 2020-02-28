import { Injectable } from '@angular/core'
import { Cohort } from '../models/cohort'
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
    providedIn: 'root'
})
export class CohortService {

    private url = 'http://localhost:8080/cohort'

    constructor(private http: HttpClient) { }

    add(cohort: Cohort): Observable<any> {
        return this.http.post(this.url, cohort)
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

    getSubject(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/subject')
    }

    getInscriptions(id: number): Observable<any> {
        return this.http.get(this.url + '/' + id + '/inscriptions')
    }

    edit(cohort: Cohort): Observable<any> {
        return this.http.post(this.url + '/' + cohort.id + '/update', cohort)
    }

    delete(id: number): Observable<any> {
        return this.http.post(this.url + '/' + id + '/delete', null)
    }
}