import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import { ApiService } from 'src/app/shared/service/api.service';
import { IAdapter } from '../adatpers';
import { HttpObserve } from '../types';
import { ICommand } from './i-command';
export abstract class DelAPICommand<T> implements ICommand {
  constructor(
    protected readonly apiService: ApiService,
    protected readonly adapter: IAdapter<T>,
    protected readonly uri: string,
  ) {}

  parameters?: {
    data?: object;
    query?: HttpParams;
    headers?: HttpHeaders;
    observe?: HttpObserve;
  };

  execute(): Observable<T> {
    let options: any;
    if (this.parameters) {
      options = {};
      options.observe = this.parameters.observe || 'body';
      if (this.parameters.headers) {
        options.headers = this.parameters.headers;
      }

      if (this.parameters.query) {
        options.params = this.parameters.query;
      }

      if (this.parameters.data) {
        options.body = this.parameters.data;
      }
    }
    return this.apiService
      .delete(this.uri, options)
      .pipe(map(resp => this.adapter.adaptToModel(resp)));
  }
}
