import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/core/models';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  displayedColumns: string[] = ['firstName', 'lastName', 'username', 'email'];
  dataSource: User[] = [];
  constructor() { }

  ngOnInit(): void {
  }

  getUsers(){
    
  }

}
