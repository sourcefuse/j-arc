import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { concatMap } from 'rxjs';
import { AuthService, InvitationService } from 'src/app/shared/services';

@Component({
  selector: 'app-verify-invitation',
  templateUrl: './verify-invitation.component.html',
  styleUrls: ['./verify-invitation.component.scss']
})
export class VerifyInvitationComponent implements OnInit {
  invitationId: string;
  verifying: Boolean = true;
  constructor(
    private activatedRoute: ActivatedRoute,
    private readonly invitationService: InvitationService,
    private readonly authService : AuthService
  ) {

  }

  ngOnInit(): void {
    this.verifyInvitation();
  }

  verifyInvitation() {
    this.activatedRoute.paramMap.pipe(
      concatMap((params: any) => this.invitationService.isValidInvitation(params.params.id))
    ).subscribe(response => {
      console.log("Response");
      this.verifying = false;
      if (response.valid) {
        this.authService.loginViaSSO();
      }
    }, () => {
      this.verifying = false;
    });
  }

}
