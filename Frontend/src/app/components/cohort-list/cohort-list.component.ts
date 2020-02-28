import { Component, OnInit } from '@angular/core'
import { Cohort } from 'src/app/models/cohort'
import { CohortService } from 'src/app/services/cohort.service'
import { Router } from '@angular/router'

@Component({
  selector: 'app-cohort-list',
  templateUrl: './cohort-list.component.html',
  styleUrls: ['./cohort-list.component.css']
})
export class CohortListComponent implements OnInit {

  cohortList = new Array<Cohort>()
  totalCohorts: number
  page: number = 0
  size: number = 5

  constructor(private cohortService: CohortService, private router: Router) { }

  ngOnInit() {
    this.cohortService.getTotal().subscribe(totalResponse => {
      this.totalCohorts = totalResponse
      this.getAll()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  getAll() {
    this.cohortService.getFromTo(this.page, this.size).subscribe(cohortResponse => {
      this.cohortList = cohortResponse
      this.cohortList.forEach(c => {
        this.cohortService.getSubject(c.id).subscribe(subjectResponse => {
          c.subject = subjectResponse
        })
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  delete(id) {
    this.cohortService.delete(id).subscribe(() => {
      location.reload()
      alert('Baja Exitosa!')
    }, error => {
      console.error(error)
      if (error.status === 500) {
        alert('Error: la cohorte tiene estudiantes inscriptos, eliminelos primero y luego vuelva a intentarlo.')
      }
    })
  }

  onPageChange(pageNumber) {
    this.page = pageNumber
    this.getAll()
  }
}