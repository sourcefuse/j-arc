import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {
  InMemoryStorageService,
  LOCAL_STORAGE,
  SESSION_STORAGE,
  StorageServiceModule,
} from 'ngx-webstorage-service';

import {APP_SESSION_STORE, APPLICATION_STORE} from './store.interface';
import {UserSessionStoreService} from './user-session-store.service';

@NgModule({
  declarations: [],
  imports: [CommonModule, StorageServiceModule],
  providers: [
    UserSessionStoreService,
    {provide: APPLICATION_STORE, useExisting: LOCAL_STORAGE},
    {provide: APP_SESSION_STORE, useExisting: SESSION_STORAGE},
    InMemoryStorageService,
  ],
})
export class StoreModule {}
