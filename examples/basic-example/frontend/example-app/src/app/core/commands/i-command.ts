/* eslint-disable @typescript-eslint/no-explicit-any */
import {Observable} from 'rxjs';

export interface ICommand {
  parameters?: any;
  execute(): Observable<any>;
}
