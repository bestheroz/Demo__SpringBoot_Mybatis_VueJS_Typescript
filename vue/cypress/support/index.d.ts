// ***********************************************************
// This example support/index.d.ts is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************
// import './commands';
/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    login(username: string, password: string): void;
    logout(): void;
    visitHome(): void;
    menu(menuGroup: string, menu: string): Chainable<any>;
    chooseSelectValue(label: string, value: string, within?: boolean): Chainable<any>;
  }
}
