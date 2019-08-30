export interface IPerson {
  id?: string;
  name?: string;
}

export class Person implements IPerson {
  constructor(public id?: string, public name?: string) {}
}
