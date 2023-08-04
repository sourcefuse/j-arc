/* eslint-disable @typescript-eslint/no-explicit-any */
import { Injectable } from '@angular/core';
import { IAdapter } from 'src/app/core/adatpers';
import { LoginModel } from 'src/app/core/models';

@Injectable()
export class LoginAdapter implements IAdapter<LoginModel> {
  adaptToModel(resp: any): any {
    return resp;
  }
  adaptFromModel(data: LoginModel): any {
    return {
      username: data.username,
      password: data.password,
      clientId: data.clientId,
      clientSecret: data.clientSecret,
    };
  }
}
