import { Component, OnInit } from '@angular/core'
import { Student } from 'src/app/models/student'
import { StudentService } from 'src/app/services/student.service'
import { ActivatedRoute, Router } from '@angular/router'

@Component({
  selector: 'app-student-view',
  templateUrl: './student-view.component.html',
  styleUrls: ['./student-view.component.css']
})
export class StudentViewComponent implements OnInit {

  student = new Student()

  constructor(private studentService: StudentService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.studentService.getById(id).subscribe(response => {
      this.student = response
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }
}