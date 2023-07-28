import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { Role, User } from 'src/app/core/models';
import { RoleService, UserService } from 'src/app/shared/services';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  displayedColumns: string[] = ['firstName', 'lastName', 'username', 'email', 'roleId'];
  dataSource: User[] = [];
  roles: Role[] = [];
  constructor(
    private readonly userService: UserService,
    private readonly roleService: RoleService
  ) { }

  ngOnInit(): void {
    this.getRoles();
  }

  getUsers() {
    this.userService.getUsers()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.dataSource = data;
      });
  }

  getRoles() {
    this.roleService.getRoles()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.roles = data;
        this.getUsers();
      });
  }

  getRoleName(roleId: string) {
    return this.roles.find(ele=> ele.id == roleId)?.name || "--";
  }

}
