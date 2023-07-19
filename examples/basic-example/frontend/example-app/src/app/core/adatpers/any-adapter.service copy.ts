/* eslint-disable @typescript-eslint/no-explicit-any */
import {Injectable} from '@angular/core';
import {IAdapter} from './i-adapter';

@Injectable()
export class AnyAdapter implements IAdapter<any> {
  adaptToModel(resp: any): any {
    return resp;
  }
  adaptFromModel(data: any): any {
    return data;
  }
}
