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
  cy.route('GET', '**/api/admin/members/lists/codes').as('memberList');
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
  console.log(
    document.querySelector('div.v-list-group__header.v-list-item--active'),
  );
  console.log(
    !document.querySelector('div.v-list-group__header.v-list-item--active'),
  );
  cy.get('nav.v-navigation-drawer').within(() => {
    console.log(
      document.querySelector('div.v-list-group__header.v-list-item--active'),
    );
    console.log(
      !document.querySelector('div.v-list-group__header.v-list-item--active'),
    );
    if (
      !document.querySelector('div.v-list-group__header.v-list-item--active')
    ) {
      cy.get('div.v-list-item__title').contains(menuGroup).click();
    }
    cy.get('div.v-list-item__title').contains(menu).parent().click();
  });
  cy.wait(20).get('div.v-alert__content').contains(menu);
  return cy;
});
Cypress.Commands.add('clickSelection', (label: string) => {
  return cy.get('label').contains(label).click();
});
Cypress.Commands.add('setInputValue', (label: string, value: string) => {
  value
    ? cy.get('label').contains(label).next().clear().type(value)
    : cy.get('label').contains(label).next().clear();
  return cy;
});
Cypress.Commands.add(
  'setInputAutoValue',
  (labels: string[], prefix = '(cypress)') => {
    labels.forEach((label) => {
      cy.get('label')
        .contains(label)
        .next()
        .clear()
        .type(prefix + label);
    });
    return cy;
  },
);
Cypress.Commands.add(
  'setSelectValue',
  (label: string, value: string, inDialog = true) => {
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
        if (inDialog) {
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
Cypress.Commands.add(
  'clickFunction',
  (dialIndex: number, buttonReverseIndex: number) => {
    cy.get(`div.v-speed-dial:eq(${dialIndex})`).trigger('mouseenter');
    return cy
      .get(
        `div.v-speed-dial:eq(${dialIndex}) div.v-speed-dial__list button:eq(${buttonReverseIndex})`,
      )
      .click();
  },
);
Cypress.Commands.add('clickAlert', (label: string) => {
  return cy.wait(20).get('div.swal2-actions button').contains(label).click();
});
