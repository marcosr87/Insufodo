import { Student } from './student'
import { Cohort } from './cohort'

export class Inscription {
    id: number
    student: Student
    cohort: Cohort
    qualificationDate: string
    qualification: number
}