import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {map} from 'rxjs/operators';

import {HttpObserve} from '../../types';
import {IAdapter} from '../adapters/i-adapter';
import {ApiService} from '../api.service';
import {ICommand} from './i-command';

export class PostAPICommand<T, R = T> implements ICommand {
  constructor(
    protected readonly apiService: ApiService,
    protected readonly adapter: IAdapter<T, R>,
    protected readonly uri: string,
  ) {}

  parameters!: {
    data: T;
    headers?: HttpHeaders;
    observe?: HttpObserve;
    query?: HttpParams;
    reportProgress?: boolean;
  };

  execute(): Observable<T> {
    if (!this.parameters) {
      throwError(`Parameters missing for POST ${this.uri}`);
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const options: any = {};
    options.observe = this.parameters.observe || 'body';
    options.reportProgress = this.parameters.reportProgress;
    if (this.parameters.headers) {
      options.headers = this.parameters.headers;
    }
    return this.apiService
      .post(
        this.uri,
        this.adapter.adaptFromModel(this.parameters.data),
        options,
      )
      .pipe(
        map(resp => {
          if (!options.reportProgress) {
            return this.adapter.adaptToModel(resp);
          } else {
            return resp;
          }
        }),
      );
  }
}
