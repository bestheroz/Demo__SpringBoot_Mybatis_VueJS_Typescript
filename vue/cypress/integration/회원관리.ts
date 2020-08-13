before(() => {
  cy.login();
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>회원관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.route('**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
});
