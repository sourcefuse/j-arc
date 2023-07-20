import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  displayedColumns: string[] = ['action', 'actedOn', 'actionKey', 'before', 'after'];
  dataSource = [];
  constructor() { }

  ngOnInit(): void {
  }

}
