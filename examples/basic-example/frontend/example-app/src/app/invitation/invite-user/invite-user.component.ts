import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-invite-user',
  templateUrl: './invite-user.component.html',
  styleUrls: ['./invite-user.component.scss']
})
export class InviteUserComponent implements OnInit {

  userDetails:any ={
    authProvider:"internal"
  }

  constructor() { }

  ngOnInit(): void {
  }

}
