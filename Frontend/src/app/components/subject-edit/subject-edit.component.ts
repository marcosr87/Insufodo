import { Component, OnInit } from '@angular/core'
import { Subject } from 'src/app/models/subject'
import { FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms'
import { SubjectService } from 'src/app/services/subject.service'
import { ActivatedRoute, Router } from '@angular/router'
import { Observable, of } from 'rxjs'
import { map, catchError } from 'rxjs/operators'

@Component({
  selector: 'app-subject-edit',
  templateUrl: './subject-edit.component.html',
  styleUrls: ['./subject-edit.component.css']
})
export class SubjectEditComponent implements OnInit {

  subject = new Subject()
  subjectList = new Array<Subject>()
  subjectForm: FormGroup

  constructor(private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.subjectForm = new FormGroup({
      'name': new FormControl(this.subject.name, { validators: Validators.required, asyncValidators: this.checkName.bind(this), updateOn: 'blur' }),
      'year': new FormControl(this.subject.year, [Validators.required, Validators.pattern('^[1-5]*$')]),
      'subjects': new FormControl(this.subjectList)
    })

    this.subjectService.getById(id).subscribe(subjectResponse => {
      this.subject = subjectResponse
      this.subjectService.getCorrelatives(id).subscribe(correlativesResponse => {
        this.subject.correlatives = correlativesResponse
      })
      this.name.setValue(this.subject.name)
      this.year.setValue(this.subject.year)
      this.getSubjectsByYear(this.year.value)
      document.getElementsByTagName('input')[0].focus()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  get name() { return this.subjectForm.get('name') }
  get year() { return this.subjectForm.get('year') }
  get subjects() { return this.subjectForm.get('subjects') }

  getSubjectsByYear(year) {
    this.subjectList = []
    this.subjectService.getAll().subscribe(response => {
      response.forEach(s => {
        if (s.year < year && s.id !== this.subject.id) {
          this.subjectList.push(s)
        }
      })
      if (this.subjectList.length > 0) {
        this.subjects.setValue(this.subjectList[0].id)
      }
    }, error => {
      console.error(error)
    })
  }

  yearChange(event: any) {
    this.subjectList = []
    if (event.target.value >= 2 && event.target.value <= 5) {
      this.getSubjectsByYear(event.target.value)
    }
  }

  addCorrelative(event: any) {
    var exists = false
    this.subject.correlatives.forEach(c => {
      if (c.id === parseInt(event.target.value)) {
        exists = true
      }
    })
    if (!exists) {
      this.subjectService.getById(event.target.value).subscribe(response => {
        this.subject.correlatives.push(response)
      }, error => {
        console.log(error)
      })
    }
  }

  deleteCorrelative(correlative) {
    const index = this.subject.correlatives.indexOf(correlative, 0);
    if (index > -1) {
      this.subject.correlatives.splice(index, 1);
    }
  }

  checkName(name: AbstractControl): Observable<ValidationErrors | null> {
    if (name.value === this.subject.name) {
      return of(null)
    } else {
      return this.subjectService.checkName(name.value).pipe(
        map(() => null),
        catchError(err => {
          if (err.status === 409) {
            return of({
              nameTaken: true
            })
          }
          return of(null)
        })
      )
    }
  }

  editSubject() {
    var inconsistency = false
    this.subject.correlatives.forEach(c => {
      if (c.year >= this.year.value) {
        inconsistency = true
      }
    })
    if (!inconsistency) {
      let subject = new Subject();
      subject.id = this.subject.id
      subject.name = this.name.value
      subject.year = this.year.value
      subject.correlatives = this.subject.correlatives

      this.subjectService.edit(subject).subscribe(() => {
        alert('Modificación Exitosa!')
        this.router.navigateByUrl('/subjectList')
      }, error => {
        console.error(error)
        alert('Error: ' + error.error.message)
      })
    } else {
      alert('Error: una o más correlativas no corresponden con el año ingresado. Por favor revise los datos y vuelva a intentarlo.')
      document.getElementsByTagName('input')[0].focus()
    }
  }
}