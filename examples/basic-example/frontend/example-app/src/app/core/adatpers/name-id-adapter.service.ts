import {Injectable} from '@angular/core';
import {IAdapter} from './i-adapter';
import { NameId } from '../models/name-id.model';

@Injectable()
export class NameIdAdapter implements IAdapter<NameId> {
  adaptToModel(resp: any): NameId {
    return new NameId(resp);
  }
  adaptFromModel(data: NameId): any {
    return data;
  }
}
