import { HttpHeaders } from '@angular/common/http';

import { maxBy, minBy } from 'lodash';
import { GetListAPICommand } from './get-list-api.command';
import * as moment from 'moment';
import { Observable, from, concatMap, map } from 'rxjs';
import { ApiService } from 'src/app/core/service';
import { environment } from 'src/environments/environment';
import { IAdapter } from '../adatpers';
import { Integers } from '../enums';
import { DexieService } from 'src/app/shared/service/dexie.service';
import { Id } from '../models';
import { CachedData } from '../types';

export abstract class GetCacheListAPICommand<
  T extends Id,
> extends GetListAPICommand<T> {
  constructor(
    protected readonly apiService: ApiService,
    protected readonly adapter: IAdapter<T>,
    protected readonly uri: string,
    protected readonly cacheType: string,
    protected readonly cacheIdentifier: string,
    protected readonly dexieService?: DexieService,
  ) {
    super(apiService, adapter, uri);
  }

  execute(): Observable<T[]> {
    if (!this.dexieService || environment.enableCache === 'false') {
      return super.execute();
    }

    const dbDataPromise = this.dexieService.keyData.get(this.cacheIdentifier);
    return from(dbDataPromise)
      .pipe(
        concatMap((dbData) => {
          let maxModifiedOn: Date;
          if (dbData?.data.length) {
            maxModifiedOn = this.setEtagUsingDBdata(dbData.data);
          }
          return super.execute().pipe(
            map((serverData) => {
              return { dbData, serverData, add: !!!maxModifiedOn };
            }),
          );
        }),
      )
      .pipe(
        map((data) => this.mapServerDataWithDB(data)),
        map(({ list, add }) => this.upsertAndValidateData({ list, add })),
      );
  }

  setEtagUsingDBdata(data: any[]) {
    const maxModifiedOn: Date = this.getMaxModifiedOnDate(data);
    if (maxModifiedOn) {
      const modifiedOn = moment(maxModifiedOn)
        .add(1, 'millisecond')
        .toDate()
        .toISOString();
      if (!this.parameters) {
        this.parameters = {};
      }

      if (this.parameters.headers) {
        this.parameters.headers.set('ETag', modifiedOn);
      } else {
        this.parameters.headers = new HttpHeaders().set('ETag', modifiedOn);
      }
    }
    return maxModifiedOn;
  }

  mapServerDataWithDB({ dbData, serverData, add }) {
    if (!dbData?.data?.length) {
      return { list: serverData, add };
    }
    const dbDataAdapted = dbData.data.map((d) => this.adapter.adaptToModel(d));
    const dbDataMap = new Map<string, any>();
    for (const entity of dbDataAdapted) {
      dbDataMap[entity.id] = entity;
    }
    for (const data of serverData) {
      dbDataMap[data.id] = data;
    }
    const list = Object.values(dbDataMap);
    return { list, add };
  }

  upsertAndValidateData({ list, add }) {
    this.dexieService.upsert(list, this.cacheIdentifier, add);
    this.validateStorage();
    return list;
  }

  validateStorage() {
    const id = this.cacheIdentifier;
    const maxBoardsToCache = Integers.Ten;
    const data = localStorage.getItem(this.cacheType);
    const cacheData: CachedData[] = data
      ? JSON.parse(data).map((d) => {
          return { id: d.id, date: new Date(d.date) };
        })
      : [];

    const boardFound = cacheData.find((x) => x.id === id);
    if (boardFound) {
      boardFound.date = new Date();
    } else {
      cacheData.push({ id, date: new Date() });
    }

    if (cacheData.length <= maxBoardsToCache) {
      localStorage.setItem(this.cacheType, JSON.stringify(cacheData));
      return;
    }
    const minDateObj = minBy(cacheData, 'date');
    const deletionIndex = cacheData.findIndex((x) => x.id === minDateObj.id);
    this.dexieService.keyData.delete(minDateObj.id);
    cacheData.splice(deletionIndex, 1);
    localStorage.setItem(this.cacheType, JSON.stringify(cacheData));
  }

  getMaxModifiedOnDate(list: any[]) {
    const maxObj = maxBy(list, 'modifiedOn');
    return maxObj['modifiedOn'];
  }
}
