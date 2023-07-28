import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { Adapters } from './adapters';
import { RouterModule } from '@angular/router';
import { SharedServices } from './services';
@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        RouterModule,
        HttpClientModule
    ],
    exports: [],
    providers: [
        ...SharedServices,
        ...Adapters
    ],
})
export class SharedModule { }
