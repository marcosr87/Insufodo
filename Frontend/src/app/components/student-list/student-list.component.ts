import { Component, OnInit } from '@angular/core'
import { StudentService } from 'src/app/services/student.service'
import { Router } from '@angular/router'
import { Student } from 'src/app/models/student'

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.css']
})
export class StudentListComponent implements OnInit {

  studentList = new Array<Student>()
  totalStudents: number
  page: number = 0
  size: number = 5

  constructor(private studentService: StudentService, private router: Router) { }

  ngOnInit() {
    this.studentService.getTotal().subscribe(totalResponse => {
      this.totalStudents = totalResponse
      this.getAll()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  getAll() {
    this.studentService.getFromTo(this.page, this.size).subscribe(response => {
      this.studentList = response
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  delete(id) {
    this.studentService.delete(id).subscribe(() => {
      location.reload()
      alert('Baja Exitosa!')
    }, error => {
      console.error(error)
      if (error.status === 500) {
        alert('Error: el alumno tiene inscripciones, eliminelas primero y luego vuelva a intentarlo.')
      }
    })
  }

  onPageChange(pageNumber) {
    this.page = pageNumber
    this.getAll()
  }
}