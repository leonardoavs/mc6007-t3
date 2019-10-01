import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Mc6007T2SharedModule } from 'app/shared';
import {
  PersonComponent,
  PersonDetailComponent,
  PersonUpdateComponent,
  PersonDeletePopupComponent,
  PersonDeleteDialogComponent,
  personRoute,
  personPopupRoute
} from './';

const ENTITY_STATES = [...personRoute, ...personPopupRoute];

@NgModule({
  imports: [Mc6007T2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PersonComponent, PersonDetailComponent, PersonUpdateComponent, PersonDeleteDialogComponent, PersonDeletePopupComponent],
  entryComponents: [PersonComponent, PersonUpdateComponent, PersonDeleteDialogComponent, PersonDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Mc6007T2PersonModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
