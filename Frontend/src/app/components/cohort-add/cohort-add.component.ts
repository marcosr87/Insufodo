import { Component, OnInit } from '@angular/core'
import { Cohort } from 'src/app/models/cohort'
import { Subject } from 'src/app/models/subject'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { CohortService } from 'src/app/services/cohort.service'
import { Router } from '@angular/router'
import { SubjectService } from 'src/app/services/subject.service'
import { DatePipe } from '@angular/common'

@Component({
  selector: 'app-cohort-add',
  templateUrl: './cohort-add.component.html',
  styleUrls: ['./cohort-add.component.css']
})
export class CohortAddComponent implements OnInit {

  cohort = new Cohort()
  subjectList = new Array<Subject>()
  cohortForm: FormGroup

  constructor(private cohortService: CohortService, private subjectService: SubjectService, private router: Router, private datePipe: DatePipe) { }

  ngOnInit() {
    this.cohort.subject = null
    this.cohort.type = 'Cursada'
    this.cohort.quote = 99
    this.cohort.year = new Date().getFullYear()
    this.cohort.date = this.datePipe.transform(new Date(new Date().getFullYear(), 11, 31), 'dd/MM/yyyy')
    this.cohort.weekday = 'Lunes'
    this.cohort.schedule = new Date(new Date().setHours(8, 0)).getHours()

    this.cohortForm = new FormGroup({
      'subjects': new FormControl(this.subjectList),
      'type': new FormControl(this.cohort.type),
      'quote': new FormControl(this.cohort.quote, [Validators.required, Validators.pattern('^[0-9]*$')]),
      'year': new FormControl(this.cohort.year, [Validators.required, Validators.pattern('^[0-9]*$'), Validators.minLength(4)]),
      'date': new FormControl(this.cohort.date, Validators.required),
      'weekday': new FormControl(this.cohort.weekday),
      'schedule': new FormControl(this.cohort.schedule, [Validators.required, Validators.pattern('^[0-9]*$')]),
    })
    this.subjectService.getAll().subscribe(response => {
      this.subjectList = response
      if (this.subjectList.length > 0) {
        this.subjects.setValue(this.subjectList[0].id)
        this.subjectChange(this.subjectList[0].id)
      }
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

  addCohort() {
    let exists = false
    this.cohortService.getAll().subscribe(cohortResponse => {
      cohortResponse.forEach(c => {
        this.cohortService.getSubject(c.id).subscribe(subjectResponse => {
          c.subject = subjectResponse
          if (c.subject.id === this.cohort.subject.id && c.type === this.cohort.type && c.year === this.cohort.year) {
            exists = true
          }
        })
      })
      setTimeout(() => {
        if (!exists) {
          let cohort = new Cohort()
          cohort.subject = this.cohort.subject
          cohort.type = this.cohort.type
          cohort.quote = this.quote.value
          cohort.year = this.year.value
          cohort.date = this.date.value
          cohort.weekday = this.cohort.weekday
          cohort.schedule = this.schedule.value

          this.cohortService.add(cohort).subscribe(() => {
            this.subjects.setValue(this.subjectList[0].id)
            this.type.setValue('Cursada')
            this.quote.setValue(99)
            this.year.setValue(new Date().getFullYear())
            this.date.setValue(this.datePipe.transform(new Date(new Date().getFullYear(), 11, 31), 'dd/MM/yyyy'))
            this.weekday.setValue('Lunes')
            this.schedule.setValue(new Date(new Date().setHours(8, 0)).getHours().toString())
            alert('Alta Exitosa!')
            document.getElementsByTagName('select')[0].focus()
          })
        } else {
          alert('Error: Cohorte ya existente!')
          document.getElementsByTagName('select')[0].focus()
        }
      }, 300)
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
      document.getElementsByTagName('select')[0].focus()
    })
  }
}