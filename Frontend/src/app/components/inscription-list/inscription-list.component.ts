import { Component, OnInit } from '@angular/core'
import { InscriptionService } from 'src/app/services/inscription.service'
import { StudentService } from 'src/app/services/student.service'
import { CohortService } from 'src/app/services/cohort.service'
import { ActivatedRoute, Router } from '@angular/router'
import { Student } from 'src/app/models/student'
import { AcademicStatus } from 'src/app/models/academicStatus'

@Component({
  selector: 'app-inscription-list',
  templateUrl: './inscription-list.component.html',
  styleUrls: ['./inscription-list.component.css']
})
export class InscriptionListComponent implements OnInit {

  student = new Student()
  academicStatus = new AcademicStatus()
  academicStatusList = new Array<AcademicStatus>()
  average: number
  total: number

  constructor(private inscriptionService: InscriptionService, private studentService: StudentService, private cohortService: CohortService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let dni = this.route.snapshot.paramMap.get('dni')

    this.getStudent(dni)
  }

  getStudent(dni) {
    this.studentService.getByDni(dni).subscribe(studentResponse => {
      this.student = studentResponse
      this.studentService.getInscriptions(this.student.id).subscribe(studentInscriptionsResponse => {
        this.student.inscriptions = studentInscriptionsResponse
        this.student.inscriptions.forEach(i => {
          this.inscriptionService.getCohort(i.id).subscribe(cohortResponse => {
            i.cohort = cohortResponse
            this.cohortService.getSubject(i.cohort.id).subscribe(subjectResponse => {
              i.cohort.subject = subjectResponse
            })
          })
        })
        setTimeout(() => {
          this.setAcademicStatus()
        }, 300)
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  setAcademicStatus() {
    this.academicStatusList = []
    this.average = 0
    this.total = 0
    this.student.inscriptions.forEach(c => {
      let as = new AcademicStatus()
      if (c.cohort.type === 'Cursada') {
        as.subject = c.cohort.subject
        as.course = c
        this.student.inscriptions.forEach(f => {
          if (f.cohort.subject.id === c.cohort.subject.id && f.cohort.type === 'Final') {
            as.final = f
            this.average = this.average + f.qualification
            this.total = this.total + 1
          }
        })
      }
      this.academicStatusList.push(as)
    })
    if (this.total !== 0) {
      this.average = this.average / this.total
    }
  }
}