export class SuccessResponse {
  success?: boolean;

  constructor(data: Partial<SuccessResponse>) {
    this.success = data.success;
  }
}
