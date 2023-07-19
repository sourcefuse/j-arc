import {Injectable} from '@angular/core';
import {Count} from '@rao/core/models/count.model';
import {IAdapter} from './i-adapter';

@Injectable()
export class CountAdapter implements IAdapter<Count> {
  adaptToModel(resp: any): Count {
    return new Count(resp);
  }
  adaptFromModel(data: Count): any {
    return data;
  }
}
