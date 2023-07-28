import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { Role } from 'src/app/core/models';
import { RoleService } from 'src/app/shared/services/role.service';

@Component({
  selector: 'app-invite-user',
  templateUrl: './invite-user.component.html',
  styleUrls: ['./invite-user.component.scss']
})
export class InviteUserComponent implements OnInit {

  userDetails:any ={
    authProvider:"keycloak"
  }
  roles:Role[]=[];
  
  constructor(private readonly roleService: RoleService) { }

  ngOnInit(): void {
    this.getRoles();
  }
  getRoles(){
    this.roleService.getRoles()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.roles = data;
      });
  }
}
