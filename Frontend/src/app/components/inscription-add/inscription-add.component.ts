import { Component, OnInit } from '@angular/core'
import { InscriptionService } from 'src/app/services/inscription.service'
import { Router, ActivatedRoute } from '@angular/router'
import { Inscription } from 'src/app/models/inscription'
import { Cohort } from 'src/app/models/cohort'
import { CohortService } from 'src/app/services/cohort.service'
import { SubjectService } from 'src/app/services/subject.service'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { StudentService } from 'src/app/services/student.service'

@Component({
  selector: 'app-inscription-add',
  templateUrl: './inscription-add.component.html',
  styleUrls: ['./inscription-add.component.css']
})
export class InscriptionAddComponent implements OnInit {

  inscription = new Inscription()
  cohortList = new Array<Cohort>()
  inscriptionForm: FormGroup

  constructor(private inscriptionService: InscriptionService, private cohortService: CohortService, private subjectService: SubjectService, private studentService: StudentService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let dni = this.route.snapshot.paramMap.get('dni')

    this.inscription.cohort = new Cohort()
    this.inscription.cohort.type = 'Cursada'

    this.getStudent(dni)

    this.inscriptionForm = new FormGroup({
      'type': new FormControl(this.inscription.cohort.type, Validators.required),
      'cohorts': new FormControl(this.cohortList, Validators.required)
    })
  }

  get type() { return this.inscriptionForm.get('type') }
  get cohorts() { return this.inscriptionForm.get('cohorts') }

  getStudent(dni) {
    this.studentService.getByDni(dni).subscribe(studentResponse => {
      this.inscription.student = studentResponse
      this.studentService.getInscriptions(this.inscription.student.id).subscribe(studentInscriptionsResponse => {
        this.inscription.student.inscriptions = studentInscriptionsResponse
        this.inscription.student.inscriptions.forEach(i => {
          this.inscriptionService.getCohort(i.id).subscribe(cohortResponse => {
            i.cohort = cohortResponse
            this.cohortService.getSubject(i.cohort.id).subscribe(subjectResponse => {
              i.cohort.subject = subjectResponse
              this.subjectService.getCorrelatives(i.cohort.subject.id).subscribe(correlativesResponse => {
                i.cohort.subject.correlatives = correlativesResponse
              })
            })
          })
        })
        setTimeout(() => {
          this.getCohorts()
        }, 300)
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  getCohorts() {
    this.cohortList = []
    this.cohortService.getAll().subscribe(cohortsResponse => {  //traigo todas las cohortes
      cohortsResponse.forEach(c => {  //recorro las cohortes
        this.cohortService.getInscriptions(c.id).subscribe(inscriptionsResponse => {  //traigo todas las inscripciones de la cohorte
          c.inscriptions = inscriptionsResponse
          if (c.type === this.inscription.cohort.type && c.quote > 0) {  //si la cohorte es del tipo elegido por el usuario (cursada o final) y hay cupo
            this.cohortService.getSubject(c.id).subscribe(subjectResponse => {  //traigo la materia de la cohorte
              c.subject = subjectResponse
              this.subjectService.getCorrelatives(c.subject.id).subscribe(correlativesResponse => { //traigo las correlativas de la materia de la cohorte
                c.subject.correlatives = correlativesResponse
                let registered = false
                let disabled = false
                if (this.inscription.student.inscriptions === undefined || this.inscription.student.inscriptions.length === 0) { //si el alumno no esta inscripto a nada
                  if (parseInt(c.subject.year) === 1) { //si la materia de la cohorte es de primero
                    this.cohortList.push(c) //habilito la cohorte para inscripcion
                  }
                } else {  //si el alumno esta inscripto a algunas cohortes
                  this.inscription.student.inscriptions.forEach(i => {  //recorro las inscripciones del alumno
                    if (i.cohort.subject.id === c.subject.id && i.cohort.type === c.type && (i.qualification == null || i.qualification >= 7)) {
                      registered = true //el alumno ya esta inscripto o ya aprobo la materia de la cohorte
                    }
                  })
                  if (!registered) {  //si no esta inscripto o no la aprobo
                    c.subject.correlatives.forEach(c => { //recorro las correlativas de la materia de la cohorte
                      this.inscription.student.inscriptions.forEach(i => {  //recorro las incripciones del alumno
                        if (c.id === i.cohort.subject.id && (i.qualification == null || i.qualification < 7)) {
                          disabled = true //el alumno no aprobo alguna correlativa de la materia de la cohorte
                        }
                      })
                    })
                    if (!registered && !disabled) { //el alumno no esta registrado y aprobo todas las correlativas de la materia de la cohorte
                      this.cohortList.push(c) //habilito la cohorte para inscripcion
                    }
                  }
                }
              })
            })
          }
        })
        setTimeout(() => {
          if (this.cohortList.length > 0) {
            this.cohorts.setValue(this.cohortList[0].id)
            this.cohortChange(this.cohortList[0].id)
            document.getElementsByTagName('button')[0].disabled = false
          } else {
            document.getElementsByTagName('button')[0].disabled = true
          }
          document.getElementsByTagName('select')[0].focus()
        }, 300)
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  typeChange(event: any) {
    this.inscription.cohort.type = event.target.value
    this.getCohorts()
    setTimeout(() => {
      if (this.cohortList.length === 0) {
        document.getElementsByTagName('button')[0].disabled = true
      } else {
        document.getElementsByTagName('button')[0].disabled = false
      }
    }, 300)
  }

  cohortChange(cohortId: number) {
    this.cohortService.getById(cohortId).subscribe(cohortResponse => {
      this.inscription.cohort = cohortResponse
      this.cohortService.getSubject(cohortId).subscribe(subjectResponse => {
        this.inscription.cohort.subject = subjectResponse
      })
    })
  }

  addInscription() {
    let inscription = new Inscription()
    inscription.cohort = this.inscription.cohort
    inscription.student = this.inscription.student
    this.inscriptionService.add(inscription).subscribe(() => {
      alert('Inscripción Exitosa!')
      this.type.setValue('Cursada')
      this.getStudent(this.inscription.student.dni)
    }, error => {
      console.error(error)
      if (error.status === 412) {
        alert('Error: no hay cupo para esta cohorte.')
      }
    })
  }
}