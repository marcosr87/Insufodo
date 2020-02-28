import { Component, OnInit } from '@angular/core'
import { Subject } from 'src/app/models/subject'
import { SubjectService } from 'src/app/services/subject.service'
import { ActivatedRoute, Router } from '@angular/router'

@Component({
  selector: 'app-subject-view',
  templateUrl: './subject-view.component.html',
  styleUrls: ['./subject-view.component.css']
})
export class SubjectViewComponent implements OnInit {

  subject: Subject = new Subject()

  constructor(private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.subjectService.getById(id).subscribe(subjectResponse => {
      this.subject = subjectResponse
      this.subjectService.getCorrelatives(id).subscribe(correlativesResponse => {
        this.subject.correlatives = correlativesResponse
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }
}