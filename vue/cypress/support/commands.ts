// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
import 'cypress-localstorage-commands';
const pbkdf2 = require('pbkdf2');
Cypress.Commands.add('login', () => {
  const pbkdf2Password = pbkdf2
    .pbkdf2Sync(Cypress.env('password'), 'salt', 1, 32, 'sha512')
    .toString();
  cy.request({
    method: 'POST',
    url: `${Cypress.env('apiHost')}/api/auth/login`,
    body: {
      id: Cypress.env('username'),
      password: pbkdf2Password,
    },
  })
    .its('body')
    .then((body) => {
      cy.setLocalStorage('accessToken', body.data.accessToken);
      cy.setLocalStorage('refreshToken', body.data.refreshToken);
    })
    .then(() => {
      console.log(window.localStorage.getItem('accessToken'));
      cy.visit('/');
    });
});
