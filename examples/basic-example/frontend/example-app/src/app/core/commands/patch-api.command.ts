import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {map} from 'rxjs/operators';
import { ApiService } from 'src/app/core/service';
import { IAdapter } from '../adatpers';
import { HttpObserve } from '../types';
import { ICommand } from './i-command';


export class PatchAPICommand<T> implements ICommand {
  constructor(
    protected readonly apiService: ApiService,
    protected readonly adapter: IAdapter<T>,
    protected readonly uri: string,
  ) {}

  parameters!: {
    data: Partial<T>;
    headers?: HttpHeaders;
    observe?: HttpObserve;
    query?: HttpParams;
  };

  execute(): Observable<T> {
    if (!this.parameters) {
      throwError(`Parameters missing for PATCH ${this.uri}`);
    }

    // sonarignore:start
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const options: any = {observe: this.parameters.observe || 'body'};
    if (this.parameters.headers) {
      options.headers = this.parameters.headers;
    }
    // sonarignore:end

    if (this.parameters.query) {
      options.params = this.parameters.query;
    }
    return this.apiService
      .patch(
        this.uri,
        this.adapter.adaptFromModel(this.parameters.data),
        options,
      )
      .pipe(map(resp => this.adapter.adaptToModel(resp)));
  }
}
