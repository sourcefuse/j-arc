import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { SignupCommand } from '../commands/signup.command';
import { SignUpAdapter } from '../adapters';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private readonly apiService: ApiService,
    private readonly signUpAdapter: SignUpAdapter
  ) { }

  createExternalUser(user:any) {
    const command = new SignupCommand(
      this.apiService,
      this.signUpAdapter,
    );

    command.parameters = {
      data: user,
    };

    return command.execute();
  }

}
