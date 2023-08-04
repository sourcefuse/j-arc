export class Count {
  count = 0;
  constructor(data?: Partial<Count>) {
    if (data) {
      this.count = data.count || 0;
    }
  }
}
