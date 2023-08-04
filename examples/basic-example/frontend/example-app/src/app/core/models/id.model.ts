export class Id {
  id?: string;
  constructor(data?: Partial<Id>) {
    this.id = data?.id;
  }
}
