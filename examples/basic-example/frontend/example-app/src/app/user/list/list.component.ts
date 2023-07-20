import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  displayedColumns: string[] = ['firstName', 'lastName', 'username', 'email'];
  dataSource = [];
  constructor() { }

  ngOnInit(): void {
  }

}
