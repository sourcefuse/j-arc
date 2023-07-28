import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { InMemoryStorageService, LOCAL_STORAGE, SESSION_STORAGE, StorageServiceModule } from 'ngx-webstorage-service';
import { SharedModule } from '../shared/shared.module';
import { Adapters } from './adatpers';
import { Gaurds } from './auth';
import { APPLICATION_STORE, APP_SESSION_STORE } from './store/store.interface';
import { UserSessionStoreService } from './store/user-session-store.service';
import { HttpInterceptorProviders } from './interceptors';
import { ToastrModule } from 'ngx-toastr';
import { CoreServices } from './service';

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        FormsModule,
        HttpClientModule,
        RouterModule,
        StorageServiceModule,
        SharedModule,
        ToastrModule.forRoot()
    ],
    providers: [
        ...Gaurds,
        ...Adapters,
        ...HttpInterceptorProviders,
        ...CoreServices,
        UserSessionStoreService,
        {provide: APPLICATION_STORE, useExisting: LOCAL_STORAGE},
        {provide: APP_SESSION_STORE, useExisting: SESSION_STORAGE},
        InMemoryStorageService,
    ],
    exports: [
    ]
})
export class CoreModule { }
