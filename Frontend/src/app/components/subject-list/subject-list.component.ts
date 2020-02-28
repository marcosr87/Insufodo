import { Component, OnInit } from '@angular/core'
import { Subject } from 'src/app/models/subject'
import { SubjectService } from 'src/app/services/subject.service'
import { Router } from '@angular/router'

@Component({
  selector: 'app-subject-list',
  templateUrl: './subject-list.component.html',
  styleUrls: ['./subject-list.component.css']
})
export class SubjectListComponent implements OnInit {

  subjectList = new Array<Subject>()
  totalSubjects: number
  page: number = 0
  size: number = 5

  constructor(private subjectService: SubjectService, private router: Router) { }

  ngOnInit() {
    this.subjectService.getTotal().subscribe(totalResponse => {
      this.totalSubjects = totalResponse
      this.getAll()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  getAll() {
    this.subjectService.getFromTo(this.page, this.size).subscribe(response => {
      this.subjectList = response
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  delete(id) {
    this.subjectService.delete(id).subscribe(() => {
      location.reload()
      alert('Baja Exitosa!')
    }, error => {
      console.error(error)
      if (error.status === 500) {
        alert('Error: la materia es correlativa de otras o tiene cohortes, eliminelas primero y luego vuelva a intentarlo.')
      }
    })
  }

  onPageChange(pageNumber) {
    this.page = pageNumber
    this.getAll()
  }
}