import { Inscription } from 'src/app/models/inscription'

export class Student {
    id: number
    dni: string
    lastName: string
    firstName: string
    email: string
    cohort: number
    status: string
    gender: string
    address: string
    phone: string
    inscriptions: Array<Inscription>
}