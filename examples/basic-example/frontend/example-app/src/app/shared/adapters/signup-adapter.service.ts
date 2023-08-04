import { Injectable } from '@angular/core';
import { IAdapter } from 'src/app/core/adatpers';
import { SignUpModel } from '../models';

@Injectable()
export class SignUpAdapter implements IAdapter<SignUpModel> {
  adaptToModel(resp: any): any {
    return resp;
  }

  adaptFromModel(data: SignUpModel): any {
    return {
      email: data.email,
      password: data.password,
      userData: {
        firstName: data.userData?.firstName,
        lastName: data.userData?.lastName,
        username: data.userData?.username,
      },
    };
  }
}
