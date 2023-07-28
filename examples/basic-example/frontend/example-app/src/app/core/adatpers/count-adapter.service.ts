import {Injectable} from '@angular/core';
import {IAdapter} from './i-adapter';
import { Count } from '../models';

@Injectable()
export class CountAdapter implements IAdapter<Count> {
  adaptToModel(resp: any): Count {
    return new Count(resp);
  }
  adaptFromModel(data: Count): any {
    return data;
  }
}
