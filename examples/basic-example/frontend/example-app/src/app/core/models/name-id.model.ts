export class NameId {
  id: string | undefined;
  name: string | undefined;
  constructor(data?: Partial<NameId>) {
    this.id = data?.id;
    this.name = data?.name;
  }
}
