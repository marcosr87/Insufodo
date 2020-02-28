import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core'

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PaginatorComponent implements OnInit {

  pageSize: number = 5
  @Input() pageNumber: number
  @Input() totalResults: number
  @Output() pageChange = new EventEmitter()

  pages = []

  constructor() { }

  ngOnInit() {
    this.renderPaginator()
  }

  clickPage(page: number) {
    this.pageChange.emit(page)
  }

  renderPaginator() {
    const pages = Math.ceil(this.totalResults / this.pageSize)
    this.pages = new Array(pages)
  }
}