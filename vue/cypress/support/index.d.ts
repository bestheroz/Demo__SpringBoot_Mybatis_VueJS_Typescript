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
    login(username: string, password: string): Chainable<any>;
    logout(): Chainable<any>;
    visitHome(): Chainable<any>;
    menu(menuGroup: string, menu: string): Chainable<any>;
    clickFunction(dialIndex: number, buttonReverseIndex: number): Chainable<any>;
    setInputValue(label: string, value: string): Chainable<any>;
    setInputAutoValue(labels: string[], prefix?: string): Chainable<any>;
    clickSelection(label: string): Chainable<any>;
    setSelectValue(label: string, value: string): Chainable<any>;
    clickAlert(label: string): Chainable<any>;
  }
}
