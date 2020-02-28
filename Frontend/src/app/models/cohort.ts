import { Subject } from 'src/app/models/subject'
import { Inscription } from './inscription'

export class Cohort {
    id: number
    subject: Subject
    type: string
    quote: number
    year: number
    date: string
    weekday: string
    schedule: number
    inscriptions: Array<Inscription>
}