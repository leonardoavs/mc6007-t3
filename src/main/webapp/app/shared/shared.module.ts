import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Mc6007T2SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [Mc6007T2SharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [Mc6007T2SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Mc6007T2SharedModule {
  static forRoot() {
    return {
      ngModule: Mc6007T2SharedModule
    };
  }
}
