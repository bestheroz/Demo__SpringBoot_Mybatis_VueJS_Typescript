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
/// <reference types="../support" />
import 'cypress-localstorage-commands';

const pbkdf2 = require('pbkdf2');
Cypress.Commands.add('login', (username: string, password: string) => {
  cy.request({
    method: 'POST',
    url: `${Cypress.env('apiHost')}/api/auth/login`,
    body: {
      id: username,
      password: pbkdf2.pbkdf2Sync(password, 'salt', 1, 32, 'sha512').toString(),
    },
  })
    .its('body')
    .then((body) => {
      cy.setLocalStorage('accessToken', body.data.accessToken);
      cy.setLocalStorage('refreshToken', body.data.refreshToken);
    })
    .then(() => {
      cy.visitHome();
    });
  return cy;
});
Cypress.Commands.add('visitHome', () => {
  cy.server();
  cy.route('GET', '**/api/auth/me').as('me');
  cy.route('GET', '**/api/menus/drawer').as('drawer');
  cy.route('GET', '**/api/menus').as('menus');
  cy.route('GET', '**/api/admin/members/memberList').as('memberList');
  cy.visit('/');
  cy.wait('@me');
  cy.wait('@drawer');
  cy.wait('@menus');
  cy.wait('@memberList');
  return cy;
});
Cypress.Commands.add('logout', () => {
  cy.server();
  cy.route('DELETE', '**/api/auth/logout').as('logout');
  cy.get('div.v-toolbar__content i.mdi-account').parent().trigger('mouseenter');
  cy.get('button').contains('Logout').click();
  cy.wait('@logout');
  cy.visit('/login');
  return cy;
});
Cypress.Commands.add('menu', (menuGroup: string, menu: string) => {
  cy.get('nav.v-navigation-drawer').within(() => {
    cy.get('div.v-list-item__title').contains(menuGroup).click();
    cy.get('div.v-list-item__title').contains(menu).click();
  });
  return cy;
});
Cypress.Commands.add(
  'chooseSelectValue',
  (label: string, value: string, within = false) => {
    cy.get('label')
      .contains(label)
      .next()
      .children('input')
      .then((element) => {
        cy.get(
          `div[aria-owns="${element.attr('id')!.split('input').join('list')}"]`,
        )
          .parent('div.v-input__control')
          .parent('div.v-select')
          .click();
        if (within) {
          cy.root()
            .parent('div.v-application')
            .within(() => {
              cy.get(
                `#${element
                  .attr('id')!
                  .split('input')
                  .join('list')} div.v-list-item__content`,
              )
                .contains(value)
                .click();
            });
        } else {
          cy.get(
            `#${element
              .attr('id')!
              .split('input')
              .join('list')} div.v-list-item__content`,
          )
            .contains(value)
            .click();
        }
      });
    return cy;
  },
);
