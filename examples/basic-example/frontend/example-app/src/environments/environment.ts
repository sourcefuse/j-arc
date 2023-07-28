// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  clientId:"jarc",
  publicKey:"HIfZOya6vPukf9BnMqyH4xrRKxLgGypE",
  homePath:"/main",
  enableCache:"",
  authApiUrl: "http://localhost:8081",
  userTenantApiUrl: "http://localhost:8083",
  auditApiUrl: "http://localhost:8083",
  notificationApiUrl: "http://localhost:8083",
  facadeApiUrl: "http://localhost:8083",
  messageTimeout:3000

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
