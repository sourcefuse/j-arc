import { Injectable } from '@angular/core';
import Dexie, { Table } from 'dexie';

@Injectable()
export class DexieService extends Dexie {
  // sonarignore:start
  keyData: Table<{ data: any[]; key: string }, string>;
  // sonarignore:end
  constructor() {
    super('project-manager');
    this.version(1).stores({
      keyData: 'key',
    });
  }
  // sonarignore:start
  async upsert(values: any[], key: string, add: boolean) {
    const data = {
      data: values,
      key,
    };
    if (add) {
      await this.keyData.add(data, key);
    } else {
      await this.keyData.update(key, data);
    }
  }
  // sonarignore:end
}
