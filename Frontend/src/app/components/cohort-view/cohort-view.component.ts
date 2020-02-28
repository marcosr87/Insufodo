import { Component, OnInit } from '@angular/core'
import { Cohort } from 'src/app/models/cohort'
import { CohortService } from 'src/app/services/cohort.service'
import { ActivatedRoute, Router } from '@angular/router'
import { InscriptionService } from 'src/app/services/inscription.service'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { Inscription } from 'src/app/models/inscription'
import { FormGroup, FormControl, Validators } from '@angular/forms'

@Component({
  selector: 'app-cohort-view',
  templateUrl: './cohort-view.component.html',
  styleUrls: ['./cohort-view.component.css']
})
export class CohortViewComponent implements OnInit {

  cohort = new Cohort()
  qual: number
  qualificationAux: number
  inscriptionForm: FormGroup

  constructor(private cohortService: CohortService, private inscriptionService: InscriptionService, private route: ActivatedRoute, private router: Router, private modalService: NgbModal) { }

  ngOnInit() {
    this.inscriptionForm = new FormGroup({
      'qualification': new FormControl(this.qual, Validators.pattern('^[0-9]*$'))
    })
    this.getCohort()
  }

  get qualification() { return this.inscriptionForm.get('qualification') }

  getCohort() {
    let id = Number(this.route.snapshot.paramMap.get('id'))

    this.cohortService.getById(id).subscribe(cohortResponse => {
      this.cohort = cohortResponse
      this.cohortService.getSubject(id).subscribe(subjectResponse => {
        this.cohort.subject = subjectResponse
        this.cohortService.getInscriptions(id).subscribe(inscriptionsResponse => {
          this.cohort.inscriptions = inscriptionsResponse
          this.cohort.inscriptions.forEach(i => {
            this.inscriptionService.getStudent(i.id).subscribe(studentResponse => {
              i.student = studentResponse
            })
          })
        })
      })
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }

  setQualification(qualify, inscriptionId: number) {
    this.inscriptionService.getById(inscriptionId).subscribe(response => {
      this.qualification.setValue(response.qualification)
      this.qualificationAux = response.qualification
      this.modalService.open(qualify).result.then(() => {
        if (this.qualification.value !== this.qualificationAux) {
          let inscription = new Inscription()
          inscription.id = inscriptionId
          inscription.qualification = this.qualification.value
          this.inscriptionService.edit(inscription).subscribe(() => {
            alert('Calificación Exitosa!')
            this.getCohort()
          }, error => {
            console.error(error)
            alert('Error: ' + error.error.message)
          })
        }
      }, reason => { })
    })
  }

  deleteInscription(inscriptionId: number) {
    this.inscriptionService.delete(inscriptionId).subscribe(() => {
      alert('Baja Exitosa!')
      this.getCohort()
    }, error => {
      console.error(error)
      alert('Error: ' + error.error.message)
    })
  }
}