import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { take } from 'rxjs';
import { Role, User } from 'src/app/core/models';
import { UserDto } from 'src/app/shared/DTO/user.dto';
import { InvitationService } from 'src/app/shared/services';
import { RoleService } from 'src/app/shared/services/role.service';

@Component({
  selector: 'app-invite-user',
  templateUrl: './invite-user.component.html',
  styleUrls: ['./invite-user.component.scss'],
})
export class InviteUserComponent implements OnInit {
  roles: Role[] = [];

  inviteUserForm = new FormGroup({});

  constructor(
    private readonly roleService: RoleService,
    private invitationService: InvitationService,
    private toastr: ToastrService,
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.getRoles();
  }

  initForm() {
    this.inviteUserForm = new FormGroup({
      authProvider: new FormControl('keycloak', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      roleId: new FormControl('', [Validators.required]),
      firstName: new FormControl('', []),
      lastName: new FormControl('', []),
    });
  }
  getRoles() {
    this.roleService
      .getRoles()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.roles = data;
      });
  }
  submit() {
    this.inviteUserForm.markAllAsTouched();
    this.inviteUserForm.markAsDirty();
    if (!this.inviteUserForm.invalid) {
      const requestBody: UserDto = new UserDto({
        roleId: this.inviteUserForm.value.roleId,
        authId: this.inviteUserForm.value.email,
        userDetails: new User({
          firstName:
            this.inviteUserForm.value.firstName ||
            this.inviteUserForm.value.email,
          lastName:
            this.inviteUserForm.value.lastName ||
            this.inviteUserForm.value.email,
          email: this.inviteUserForm.value.email,
          username: this.inviteUserForm.value.email,
        }),
        authProvider: this.inviteUserForm.value.authProvider.toUpperCase(),
      });
      this.invitationService.inviteUser(requestBody).subscribe((data) => {
        this.initForm();
        this.toastr.success('User has been invited', 'Success');
      });
    }
  }
}
