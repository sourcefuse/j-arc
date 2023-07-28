import { Component, OnInit } from '@angular/core';
import { AuthService } from '../shared/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService) { }

   ngOnInit(): void {
     this.redirectToKeyCloak();
  }
   redirectToKeyCloak() {
    this.authService.loginViaSSO();
  }
}
