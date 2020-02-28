import { Component, OnInit } from '@angular/core'
import { Cohort } from 'src/app/models/cohort'
import { Subject } from 'src/app/models/subject'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { CohortService } from 'src/app/services/cohort.service'
import { SubjectService } from 'src/app/services/subject.service'
import { Router, ActivatedRoute } from '@angular/router'

@Component({
  selector: 'app-cohort-edit',
  templateUrl: './cohort-edit.component.html',
  styleUrls: ['./cohort-edit.component.css']
})
export class CohortEditComponent implements OnInit {

  cohort = new Cohort()
  subjectList = new Array<Subject>()
  cohortForm: FormGroup;

  constructor(private cohortService: CohortService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.cohortForm = new FormGroup({
      'subjects': new FormControl(this.subjectList),
      'type': new FormControl(this.cohort.type),
      'quote': new FormControl(this.cohort.quote, [Validators.required, Validators.pattern('^[0-9]*$')]),
      'year': new FormControl(this.cohort.year, [Validators.required, Validators.pattern('^[0-9]*$'), Validators.minLength(4)]),
      'date': new FormControl(this.cohort.date, Validators.required),
      'weekday': new FormControl(this.cohort.weekday),
      'schedule': new FormControl(this.cohort.schedule, [Validators.required, Validators.pattern('^[0-9]*$')]),
    })
    this.cohortService.getById(id).subscribe(cohortResponse => {
      this.cohort = cohortResponse
      this.cohortService.getSubject(id).subscribe(subjectResponse => {
        this.cohort.subject = subjectResponse
        this.type.setValue(this.cohort.type)
        this.quote.setValue(this.cohort.quote)
        this.year.setValue(this.cohort.year)
        this.date.setValue(this.cohort.date)
        this.weekday.setValue(this.cohort.weekday)
        this.schedule.setValue(this.cohort.schedule)
        this.subjectService.getAll().subscribe(subjectsResponse => {
          this.subjectList = subjectsResponse
          if (this.subjectList.length > 0) {
            this.subjects.setValue(this.cohort.subject.id)
          }
        })
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
    document.getElementsByTagName('select')[0].focus()
  }

  get subjects() { return this.cohortForm.get('subjects') }
  get type() { return this.cohortForm.get('type') }
  get quote() { return this.cohortForm.get('quote') }
  get year() { return this.cohortForm.get('year') }
  get date() { return this.cohortForm.get('date') }
  get weekday() { return this.cohortForm.get('weekday') }
  get schedule() { return this.cohortForm.get('schedule') }

  subjectChange(subjectId: number) {
    this.subjectService.getById(subjectId).subscribe(response => {
      this.cohort.subject = response
    })
  }

  typeChange(event: any) {
    this.cohort.type = event.target.value
  }

  weekdayChange(event: any) {
    this.cohort.weekday = event.target.value
  }

  editCohort() {
    let cohort = new Cohort()
    cohort.id = this.cohort.id
    cohort.subject = this.cohort.subject
    cohort.type = this.cohort.type
    cohort.quote = this.quote.value
    cohort.year = this.year.value
    cohort.date = this.date.value
    cohort.weekday = this.cohort.weekday
    cohort.schedule = this.schedule.value

    this.cohortService.edit(cohort).subscribe(() => {
      alert('Modificación Exitosa!')
      this.router.navigateByUrl('/cohortList')
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
      document.getElementsByTagName('select')[0].focus()
    })
  }
}