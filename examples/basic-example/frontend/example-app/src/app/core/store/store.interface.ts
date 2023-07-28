import {InjectionToken} from '@angular/core';
import {StorageService, InMemoryStorageService} from 'ngx-webstorage-service';

export const APPLICATION_STORE = new InjectionToken<StorageService>(
  'APPLICATION_STORE',
);

export const APP_SESSION_STORE = new InjectionToken<StorageService>(
  'APP_SESSION_STORE',
);

export const APP_IN_MEMORY_STORE = new InjectionToken<InMemoryStorageService>(
  'APP_IN_MEMORY_STORE',
);

export interface CurrentBoardFilter {
  boardId: string;
  filterCategory: unknown[];
  appliedSavedFilterId?: string;
}
