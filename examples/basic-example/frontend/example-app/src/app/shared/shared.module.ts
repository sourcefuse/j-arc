import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { Adapters } from './adapters';
import { ApiService } from './service/api.service';
import { AuthService } from './service/auth.service';
import { DexieService } from './service/dexie.service';
import { RouterModule } from '@angular/router';
@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        RouterModule,
        HttpClientModule
    ],
    exports: [],
    providers: [
        AuthService,
        ApiService,
        DexieService,
        ...Adapters
    ],
})
export class SharedModule { }
